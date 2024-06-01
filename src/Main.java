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

        List<Agent> angets = environment.getAgents();
        Agent a1 = angets.get(0);

//        System.out.println(Arrays.toString(a1.getFormatedCoordonates()));
//        a1.move("E");
//        System.out.println(Arrays.toString(a1.getFormatedCoordonates()));
//        environment.printGrid();
//        a1.move("E");
//        System.out.println(Arrays.toString(a1.getFormatedCoordonates()));
//        environment.printGrid();
//        a1.move("N");
//        System.out.println(Arrays.toString(a1.getFormatedCoordonates()));
//        environment.printGrid();
    }
}
