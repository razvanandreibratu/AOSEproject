package Cells;

public class Hole extends Cell {
    private final Colors color;
    private int depth;
    private final String symbol = "H";

    public Hole(int positionX, int positionY, Colors color, int depth) {
        super(positionX, positionY);
        this.color = color;
        this.depth = depth;
    }
    // Inherited methods
    @Override
    public int getPositionX() {
        return this.positionX;
    }

    @Override
    public int getPositionY() {
        return super.positionY;
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    // Class related methods
    public int getDepth() {
        return this.depth;
    }

    public Colors getColor() {
        return this.color;
    }

    public void decrementDepth() {
        if(this.depth > 0) {
            this.depth--;
        }
    }
}
