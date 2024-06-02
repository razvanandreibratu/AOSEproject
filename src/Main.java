import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Get the file and pass for the initialization
        File myInputFile = new File("./src/input.txt");
        ReadFile file = new ReadFile(myInputFile);
        Environment environment = file.createEnvironment();
        environment.printGrid();

        List<Agent> agents = environment.getAgents();

        // Start environment thread
        Thread envThread = new Thread(environment);
        envThread.start();

        // Start agent threads
        for (Agent agent : agents) {
            Thread agentThread = new Thread(agent);
            agentThread.start();
        }
    }
}
