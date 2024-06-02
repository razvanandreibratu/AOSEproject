import Cells.Colors;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Operation {
    private final Agent agent;
    private final String type;
    private final String direction;
    private final Colors tileColor;

    // Constructor and getters
    public Operation(Agent agent, String type, String direction, Colors tileColor) {
        this.agent = agent;
        this.type = type;
        this.direction = direction;
        this.tileColor = tileColor;
    }

    public Agent getAgent() {
        return agent;
    }

    public String getType() {
        return type;
    }

    public String getDirection() {
        return direction;
    }

    public Colors getTileColor() {
        return tileColor;
    }
}
