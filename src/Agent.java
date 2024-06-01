import Cells.Cell;
import Cells.Tile;
import Cells.Colors;

public class Agent extends Cell implements Runnable {
    private final Colors color;
    private Tile carriedTile;
    private final String symbol = "A";
    private Environment env;

    public Agent(Colors color, int startX, int startY, Environment env) {
        super(startX, startY);
        this.color = color;
        this.env = env;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println(carriedTile);
        }
    }
    /* Agent methods
       Movement methods */
    public void move(String direction) {
        env.moveAgent(this, direction);
    }
    public void setPosition(int newX, int newY) {
        /* We call this method inside from environment
           because we need first to check if the method
           is valid. */
        this.positionX = newX;
        this.positionY = newY;
    }

    public void pickATile(Tile carriedTile) {
        // Carry the tile
        this.carriedTile = carriedTile;
    }

    public boolean isHoldingTile () {
        // Return true if is holding a tile
        return carriedTile != null;
    }
    @Override
    public int getPositionX() {
        return this.positionX;
    }
    @Override
    public int getPositionY() {
        return this.positionY;
    }
    public Colors getColor() {
        return this.color;
    }
    public String getSymbol() {
        return this.symbol;
    }
    public String getCoordonates() {
        return "(X:" + this.positionX + ", Y:" + this.positionY + ")";
    }
    public int[] getFormatedCoordonates() {
        return new int[]{this.positionX, this.positionY};
    }
}