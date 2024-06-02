import Cells.*;
import java.util.*;

public class PathfindingHelper {

    private final Environment environment;

    public PathfindingHelper(Environment environment) {
        this.environment = environment;

    }

    public List<String> findPath(int startX, int startY, int targetX, int targetY) {
        // BFS pathfinding algorithm
        Queue<Location> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        Location startLocation = new Location(startX, startY, null, "Start");
        queue.add(startLocation);
        visited.add(startLocation.getKey());

        while (!queue.isEmpty()) {
            Location currentLocation = queue.poll();

            if (currentLocation.x == targetX && currentLocation.y == targetY) {
                System.out.println("Path found from (" + startX + ", " + startY + ") to (" + targetX + ", " + targetY + ")");
                return reconstructPath(currentLocation);
            }

            for (String direction : Arrays.asList("N", "S", "E", "W")) {
                Location newLocation = exploreInDirection(currentLocation, direction, targetX, targetY);
                if (newLocation != null && !visited.contains(newLocation.getKey())) {
                    queue.add(newLocation);
                    visited.add(newLocation.getKey());
                }
            }
        }

        System.out.println("No path found from (" + startX + ", " + startY + ") to (" + targetX + ", " + targetY + ")");
        return Collections.emptyList(); // No path found
    }

    private Location exploreInDirection(Location currentLocation, String direction, int targetX, int targetY) {
        int newX = currentLocation.x;
        int newY = currentLocation.y;

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
        }

        if (newX < 0 || newX >= environment.getWidth() || newY < 0 || newY >= environment.getHeight()) {
            return null;
        }

        Cell cell = environment.getCell(newX, newY);
        if (cell instanceof Obstacle) {
            return null;
        }

        Location newLocation = new Location(newX, newY, currentLocation, "Valid");
        if (newX == targetX && newY == targetY) {
            newLocation.status = "Goal";
        }

        return newLocation;
    }

    private List<String> reconstructPath(Location node) {
        List<String> path = new LinkedList<>();
        Location current = node;
        while (current.parent != null) {
            if (current.x == current.parent.x) {
                if (current.y > current.parent.y) {
                    path.add(0, "E");
                } else {
                    path.add(0, "W");
                }
            } else {
                if (current.x > current.parent.x) {
                    path.add(0, "S");
                } else {
                    path.add(0, "N");
                }
            }
            current = current.parent;
        }
        System.out.println("Reconstructed path: " + path);
        return path;
    }

    private static class Location {
        int x, y;
        Location parent;
        String status;

        Location(int x, int y, Location parent, String status) {
            this.x = x;
            this.y = y;
            this.parent = parent;
            this.status = status;
        }

        String getKey() {
            return x + "," + y;
        }
    }
}
