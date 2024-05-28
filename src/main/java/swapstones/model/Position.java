package swapstones.model;



public record Position(int col) {


    public Position move(Direction direction) {
        return new Position(col + direction.getColChange());
    }


    public Position moveLeft() {
        return move(Direction.LEFT);
    }


    public Position moveRight() {
        return move(Direction.RIGHT);
    }

    @Override
    public String toString() {
        return String.format("(%d)", col);
    }
}
