package swapstones.model;

import lombok.Getter;

@Getter
public enum Direction {
    RIGHT(1),
    LEFT(-1);
    static int fromIndex;
    static int toIndex;
    private final int colChange;
    Direction(int colChange) {
        this.colChange = colChange;
    }
}
