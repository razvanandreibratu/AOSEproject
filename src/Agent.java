import Cells.Cell;
import Cells.Hole;
import Cells.Tile;
import Cells.Colors;

import java.util.List;

public class Agent extends Cell implements Runnable {
    private final Colors color;
    private Tile carriedTile;
    private final String symbol = "A";
    private Environment env;
    private boolean running = true;
    private PathfindingHelper pathfindingHelper;

    public Agent(Colors color, int startX, int startY, Environment env) {
        super(startX, startY);
        this.color = color;
        this.env = env;
        this.pathfindingHelper = new PathfindingHelper(env);
    }

    @Override
    public void run() {
        while (running) {
            // Example behavior: Move to a tile, pick it up, move to a hole, and use it
            if (carriedTile == null) {
                // Logic to move to a tile position and pick it up
                if (!tryPickNearbyTile()) {
                    // If no tile is nearby, move to the nearest tile
                    moveToTileAndPick();
                }
            } else {
                // Logic to move to a hole position and use the tile
                if(!tryUseTileNearby()){
                    moveToHoleAndUse();
                }
            }

            try {
                Thread.sleep(1000); // Simulate some delay in actions
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    private boolean tryPickNearbyTile() {
        int x = this.positionX;
        int y = this.positionY;

        // Check adjacent cells
        int[][] adjacentPositions = {
                {x - 1, y}, {x + 1, y}, {x, y - 1}, {x, y + 1}
        };

        for (int[] pos : adjacentPositions) {
            int adjX = pos[0];
            int adjY = pos[1];
            if (adjX >= 0 && adjX < env.getWidth() && adjY >= 0 && adjY < env.getHeight()) {
                Cell cell = env.getCell(adjX, adjY);
                if (cell instanceof Tile) {
                    Tile tile = (Tile) cell;
                    if (tile.getColor().equals(this.color) && tile.getNumberOfTiles() > 0) {
                        pickTile(tile.getColor());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void moveToTileAndPick() {
        // Try to pick a nearby tile first
        if (!tryPickNearbyTile()) {
            // If no tile is nearby, move to the nearest tile
            Tile targetTile = findNearestTile();
            if (targetTile != null) {
                List<String> path = pathfindingHelper.findPath(this.positionX, this.positionY, targetTile.getPositionX(), targetTile.getPositionY());
                followPath(path);
            }
        }
    }
    private void moveToHoleAndUse() {
        Hole targetHole = findNearestHole();
        if (targetHole != null) {
            if(isAdjacent(targetHole))
            {

            }
            List<String> path = pathfindingHelper.findPath(this.positionX, this.positionY, targetHole.getPositionX(), targetHole.getPositionY());
            if (!path.isEmpty()) {
                followPath(path);
            } else {
                System.out.println("No path found from (" + this.positionX + ", " + this.positionY + ") to (" + targetHole.getPositionX() + ", " + targetHole.getPositionY() + ")");
            }
        } else {
            System.out.println("No hole found for agent at (" + this.positionX + ", " + this.positionY + ")");
        }
    }
    private boolean isAdjacent(Cell targetCell) {
        return Math.abs(targetCell.getPositionX() - this.positionX) + Math.abs(targetCell.getPositionY() - this.positionY) == 1;
    }
    private void followPath(List<String> path) {
        for (String direction : path) {
            move(direction);
            try {
                Thread.sleep(100); // Simulate movement time
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Check for nearby tile if not carrying any tile
            if (!isHoldingTile() && tryPickNearbyTile()) {
                return;
            }

            // Check for nearby hole if carrying a tile
            if (isHoldingTile() && tryUseTileNearby()) {
                return;
            }
        }
    }
    private boolean tryUseTileNearby() {
        int x = this.positionX;
        int y = this.positionY;

        // Check adjacent cells
        int[][] adjacentPositions = {
                {x - 1, y}, {x + 1, y}, {x, y - 1}, {x, y + 1}
        };

        for (int[] pos : adjacentPositions) {
            int adjX = pos[0];
            int adjY = pos[1];
            if (adjX >= 0 && adjX < env.getWidth() && adjY >= 0 && adjY < env.getHeight()) {
                Cell cell = env.getCell(adjX, adjY);
                if (cell instanceof Hole) {
                    Hole hole = (Hole) cell;
                    if (hole.getDepth() > 0) {
                        useTile(determineDirection(hole));
                        return true;
                    }
                }
            }
        }
        return false;
    }
    private String determineDirection(Hole targetHole) {
        if (targetHole.getPositionX() < this.positionX) return "N";
        if (targetHole.getPositionX() > this.positionX) return "S";
        if (targetHole.getPositionY() < this.positionY) return "W";
        return "E";
    }
    private Tile findNearestTile() {
        int minDistance = Integer.MAX_VALUE;
        Tile nearestTile = null;

        for (int x = 0; x < env.getWidth(); x++) {
            for (int y = 0; y < env.getHeight(); y++) {
                Cell cell = env.getCell(x, y);
                if (cell instanceof Tile && ((Tile) cell).getColor() == this.color) {
                    int distance = Math.abs(x - this.positionX) + Math.abs(y - this.positionY);
                    if (distance < minDistance) {
                        minDistance = distance;
                        nearestTile = (Tile) cell;
                    }
                }
            }
        }
        return nearestTile;
    }
    private Hole findNearestHole() {
        int minDistance = Integer.MAX_VALUE;
        Hole nearestHole = null;

        for (int x = 0; x < env.getWidth(); x++) {
            for (int y = 0; y < env.getHeight(); y++) {
                Cell cell = env.getCell(x, y);
                if (cell instanceof Hole && ((Hole) cell).getColor() == this.color && ((Hole) cell).getDepth() > 0) {
                    int distance = Math.abs(x - this.positionX) + Math.abs(y - this.positionY);
                    if (distance < minDistance) {
                        minDistance = distance;
                        nearestHole = (Hole) cell;
                    }
                }
            }
        }
        return nearestHole;
    }

    public void stop() {
        running = false;
    }
    /* Agent methods
       Movement methods */
    public void move(String direction) {
        env.postOperation(new Operation(this, "Move", direction, null));
    }
    public void setPosition(int newX, int newY) {
        /* We call this method from inside environment
           because we need first to check if the move
           is valid. */
        this.positionX = newX;
        this.positionY = newY;
    }
    /* Tile methods */
    public void pickTile(Colors tileColor) {
        env.postOperation(new Operation(this, "Pick", null, tileColor));
    }
    public void setCarriedTile(Tile carriedTile) {
        this.carriedTile = carriedTile;
    }
    public void useTile(String direction) {
        env.postOperation(new Operation(this, "Use", direction, null));
    }
    public boolean isHoldingTile () {
        return carriedTile != null;
    }
    public Tile getCarriedTile() {
        return carriedTile;
    }

    public void notifyOperationResult(Operation operation, boolean success) {
        System.out.println("Operation " + operation.getType() + " " + (success ? "succeeded" : "failed"));
    }
    // Inherited Methods
    @Override
    public int getPositionX() {
        return this.positionX;
    }
    @Override
    public int getPositionY() {
        return this.positionY;
    }
    @Override
    public Colors getColor() {
        return this.color;
    }
    @Override
    public String getSymbol() {
        return this.symbol;
    }
}