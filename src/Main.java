import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Main {
//    public static void main(String[] args) {
//        // Get the file and pass for the initialization
//        File myInputFile = new File("./src/input.txt");
//        ReadFile file = new ReadFile(myInputFile);
//        Environment environment = file.createEnvironment();
//        environment.printGrid();
//
//        List<Agent> angets = environment.getAgents();
//        Agent a1 = angets.get(0);
//        Agent a2 = angets.get(1);
////         Start the thread
//        Thread t1 = new Thread(a1);
//        Thread t2 = new Thread(a2);
//        t1.start();
//        t2.start();
//
//        System.out.println(Arrays.toString(a1.getFormatedCoordonates()));
//        a1.move("N");
//        System.out.println(Arrays.toString(a1.getFormatedCoordonates()));
//        environment.printGrid();
//        a1.move("N");
//        System.out.println(Arrays.toString(a1.getFormatedCoordonates()));
//        environment.printGrid();
//        a1.move("N");
//        System.out.println(Arrays.toString(a1.getFormatedCoordonates()));
//        environment.printGrid();
//    }
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
