import Cells.Cell;
import Cells.Colors;
import Cells.Obstacle;

import java.util.*;

public class Environment {
    private final int width, height;
    private final Cell[][] grid;
    private final List<Agent> agents;
    public static Environment INSTACE;

    public Environment(int width, int height) {
        this.width = width;
        this.height = height;
        this.agents = new ArrayList<>();
        // Populate grid by default with empty cells
        this.grid = new Cell[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y] = new Cell(x, y);
            }
        }
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
        if(isValidMove(newX, newY)) {
            grid[oldX][oldY] = new Cell(oldX, oldY);
            grid[newX][newY] = agent;
            agent.setPosition(newX, newY);
            return true;
        }
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
    private boolean isValidMove(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            System.out.println("Invalid move!!!");
            return false;
        }
        Cell cell = grid[x][y];
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
}
