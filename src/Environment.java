import Cells.*;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Environment implements Runnable {
    private final int width, height;
    private final Cell[][] grid;
    private final List<Agent> agents;
    private final BlockingQueue<Operation> operationsQueue;
    private final long startTime;
    private final Map<Agent, Cell> agentPreviousCells = new HashMap<>();

    public Environment(int width, int height) {
        this.width = width;
        this.height = height;
        this.agents = new ArrayList<>();
        this.operationsQueue = new LinkedBlockingQueue<>();
        this.startTime = System.currentTimeMillis();
        // Populate grid by default with empty cells
        this.grid = new Cell[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y] = new Cell(x, y);
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Operation operation = operationsQueue.take();
                processOperation(operation);
                logOperation(operation, true);
                printGrid();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private void logOperation(Operation operation, boolean success) {
        long currentTime = System.currentTimeMillis();
        String log = String.format("[%6.3f][ENV][%s] %s %s - %s",
                (currentTime - startTime) / 1000.0,
                operation.getAgent().getColor(),
                operation.getType(),
                operation.getDirection() != null ? "Direction: " + operation.getDirection() : "",
                success ? "Succeeded" : "Failed");
        System.out.println(log);
    }

    public void postOperation(Operation operation) {
        try{
            operationsQueue.add(operation);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    private void processOperation(Operation operation) {
        boolean success = false;
        switch (operation.getType()) {
            case "Move":
                success = moveAgent(operation.getAgent(), operation.getDirection());
                break;
            case "Pick":
                success = pickTile(operation.getAgent(), operation.getTileColor());
                break;
            case "Use":
                success = useTile(operation.getAgent(), operation.getDirection());
                break;
        }
        operation.getAgent().notifyOperationResult(operation, success);
    }


    public synchronized boolean moveAgent(Agent agent, String direction) {
        int oldX = agent.getPositionX();
        int oldY = agent.getPositionY();

        int newX = oldX;
        int newY = oldY;
        switch (direction) {
            case "N":
                newX -= 1;
                break;
            case "S":
                newX += 1;
                break;
            case "E":
                newY += 1;
                break;
            case "W":
                newY -= 1;
                break;
            default:
                System.out.println("Invalid direction!");
                return false;
        }
        if (isValidMove(newX, newY)) {
            // Restore the previous cell that the agent moved from
            Cell previousCell = agentPreviousCells.getOrDefault(agent, new Cell(oldX, oldY));
            grid[oldX][oldY] = previousCell;

            // Store the current cell before moving the agent
            agentPreviousCells.put(agent, grid[newX][newY]);

            grid[newX][newY] = agent;
            agent.setPosition(newX, newY);
            System.out.println("Agent moved to (" + newX + ", " + newY + ")");
            return true;
        }
        System.out.println("Invalid move to (" + newX + ", " + newY + ")");
        return false;
    }
    private boolean pickTile(Agent agent, Colors tileColor) {
        int x = agent.getPositionX();
        int y = agent.getPositionY();

        // Check adjacent cells
        int[][] adjacentPositions = {
                {x - 1, y}, {x + 1, y}, {x, y - 1}, {x, y + 1}
        };

        for (int[] pos : adjacentPositions) {
            int adjX = pos[0];
            int adjY = pos[1];
            if (adjX >= 0 && adjX < width && adjY >= 0 && adjY < height) {
                Cell cell = grid[adjX][adjY];
                if (cell instanceof Tile) {
                    Tile tile = (Tile) cell;
                    if (tile.getColor().equals(tileColor) && tile.getNumberOfTiles() > 0) {
                        agent.setCarriedTile(new Tile(1, tileColor, adjX, adjY)); // Ensure agent picks only one tile
                        tile.decrementNumberOfTiles();
                        if (tile.getNumberOfTiles() == 0) {
                            grid[adjX][adjY] = new Cell(adjX, adjY); // Remove tile if no tiles left
                        }
                        System.out.println("Tile picked at (" + adjX + ", " + adjY + ")");
                        return true;
                    }
                }
            }
        }
        System.out.println("No tile to pick or wrong color near (" + x + ", " + y + ")");
        return false;
    }
    private boolean useTile(Agent agent, String direction) {
        if (!agent.isHoldingTile()) {
            System.out.println("Agent not holding any tile!");
            return false;
        }

        int x = agent.getPositionX();
        int y = agent.getPositionY();
        int newX = x;
        int newY = y;

        switch (direction) {
            case "N":
                newX -= 1;
                break;
            case "S":
                newX += 1;
                break;
            case "E":
                newY += 1;
                break;
            case "W":
                newY -= 1;
                break;
            default:
                System.out.println("Invalid direction!");
                return false;
        }

        if (newX < 0 || newX >= width || newY < 0 || newY >= height) {
            System.out.println("Invalid target position!");
            return false;
        }

        Cell cell = grid[newX][newY];
        if (cell instanceof Hole) {
            Hole hole = (Hole) cell;
            Tile tile = agent.getCarriedTile();
            hole.decrementDepth();
            agent.setCarriedTile(null);

            if (hole.getDepth() == 0) {
                grid[newX][newY] = new Cell(newX, newY); // Replace the hole with an empty cell if depth is 0
            }

            System.out.println("Tile used on hole at (" + newX + ", " + newY + ")");
            return true;
        }

        System.out.println("No hole in target position at (" + newX + ", " + newY + ")");
        return false;
    }

    public void addAgent(Agent agent) {
        // Append the agent in the agent list
        agents.add(agent);
    }
    public List<Agent> getAgents() {
        return agents;
    }
    public void addCell(Cell cell) {
        // Overwrite the cells with holes/obstacles/tiles
        int x = cell.getPositionX();
        int y = cell.getPositionY();
        grid[x][y] = cell;
    }
    public boolean isValidMove(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return false;
        }
        Cell cell = grid[x][y];
        // Check if the hole is close
        // OtherWise is an invalid move
        if (cell instanceof Hole) return ((Hole) cell).isClosed();
        return !(cell instanceof Obstacle) && !(cell instanceof Agent);
    }
    public void printGrid() {
        for (Cell[] row : grid) {
            for (Cell cell : row) {
                System.out.print(cell.getColor().getCode() + cell.getSymbol() + Colors.RESET.getCode());
            }
            System.out.println();
        }
    }
    //Getters//

    public int getHeight() {
        return height;
    }
    public int getWidth() {
        return width;
    }
    public Cell getCell(int x, int y) {
        return grid[x][y];
    }
}
