package Cells;

public class Tile extends Cell {
    private final Colors color;
    private int numberOfTiles;
    private final String symbol = "T";

    public Tile(int numberOfTiles, Colors color, int positionX, int positionY) {
        super(positionX, positionY);
        this.color = color;
        this.numberOfTiles = numberOfTiles;
    }

    public int getNumberOfTiles() {
        return numberOfTiles;
    }

    public Colors getColor() {
        return color;
    }

    public void decrementNumberOfTiles() {
        if (numberOfTiles > 0) {
            numberOfTiles--;
        }
    }

    public String getSymbol() {
        return symbol;
    }
}
