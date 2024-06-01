package Cells;


public class Cell {
    protected int positionX;
    protected int positionY;
    private String symbol = "=";
    private Colors color;

    public Cell(int positionX, int positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.color = Colors.BLACK;
    }
    public int getPositionX() {
        return this.positionX;
    }
    public int getPositionY() {
        return this.positionY;
    }
    public String getSymbol() {
        return this.symbol;
    }
    public Colors getColor() {
        return this.color;
    }
}
