import Cells.Colors;
import Cells.Hole;
import Cells.Obstacle;
import Cells.Tile;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class ReadFile {
    private final File file;
    public ReadFile(File file) {
        this.file = file;
    }
    public Environment createEnvironment() {
        // Initializes the environment with the actual input data
        Environment env = null;
        try {
            Scanner myReader = new Scanner(this.file);
            // Parse the first row and create the ENV
            String[] firstLine = myReader.nextLine().split("\\s+");
            // Number of agents
            int N = Integer.parseInt(firstLine[0]);
            // Times
            int t = Integer.parseInt(firstLine[1]);
            int T = Integer.parseInt(firstLine[2]);
            // WIDH and HEIGHT for the env
            int W = Integer.parseInt(firstLine[3]);
            int H = Integer.parseInt(firstLine[4]);
            env = new Environment(H, W);

            // Read colors
            // Read initial positions and create agents
            String[] colors = myReader.nextLine().split("\\s+");
            String[] positions = myReader.nextLine().split("\\s+");
            for (int i = 0; i < N; i++) {
                int x = Integer.parseInt(positions[2 * i]);
                int y = Integer.parseInt(positions[2 * i + 1]);
                Colors color = Colors.valueOf(colors[i]);
                Agent agent = new Agent(color, x, y, env);
                env.addCell(agent);
                env.addAgent(agent);
            }

            // Read OBSTACLES section
            String line = myReader.nextLine().trim();
            if (line.equals("OBSTACLES")) {
                String[] obstaclePositions = myReader.nextLine().split("\\s+");
                for (int i = 0; i < obstaclePositions.length; i += 2) {
                    int x = Integer.parseInt(obstaclePositions[i]);
                    int y = Integer.parseInt(obstaclePositions[i + 1]);
                    env.addCell(new Obstacle(x, y));
                }
            }

            // Read TILES section
            line = myReader.nextLine().trim();
            if (line.equals("TILES")) {
                while (myReader.hasNext()) {
                    line = myReader.nextLine().trim();
                    if (line.equals("HOLES")) break;

                    String[] tileData = line.split("\\s+");
                    int numberOfTiles = Integer.parseInt(tileData[0]);
                    Colors color = Colors.valueOf(tileData[1]);
                    int xt = Integer.parseInt(tileData[2]);
                    int yt = Integer.parseInt(tileData[3]);
                    env.addCell(new Tile(numberOfTiles, color, xt, yt));
                }
            }

            // Read HOLES section
            if (line.equals("HOLES")) {
                while (myReader.hasNext()) {
                    line = myReader.nextLine().trim();
                    String[] holeData = line.split("\\s+");
                    int deep = Integer.parseInt(holeData[0]);
                    Colors color = Colors.valueOf(holeData[1]);
                    int xh = Integer.parseInt(holeData[2]);
                    int yh = Integer.parseInt(holeData[3]);
                    env.addCell(new Hole(xh, yh, color, deep));
                }
            }

            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return env;
    }
}