package swapstones.model;

/**
 * Represents the position of a stone on the board.
 */
public class Position {
    private int col;
    private Stone stone;

    public Position(int col, Stone stone) {
        this.col = col;
        this.stone = stone;
    }


    public int getCol() {
        return col;
    }

    public Stone getStone() {
        return stone;
    }

    @Override
    public String toString() {
        return "Position{" +
                "col=" + col +
                ", stone=" + stone +
                '}';
    }
}
