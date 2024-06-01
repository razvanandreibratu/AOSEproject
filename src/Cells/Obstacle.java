package Cells;

public class Obstacle extends Cell {
    private final String symbol = "O";
    private final Colors color = Colors.WHITE;
    public Obstacle(int coordonateX, int coordonateY) {
        super(coordonateX, coordonateY);
    }
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
