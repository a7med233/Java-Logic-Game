package swapstones.model;

import javafx.beans.property.ReadOnlyObjectWrapper;
import puzzle.TwoPhaseMoveState;
import puzzle.solver.BreadthFirstSearch;

import java.util.*;

public class PuzzleState implements TwoPhaseMoveState<Integer> {
    public static final int BOARD_SIZE = 16;

    private ReadOnlyObjectWrapper<Stone>[] board = new ReadOnlyObjectWrapper[BOARD_SIZE];


    public PuzzleState() {
        this(new Position( 0, Stone.TAIL),
                new Position( 1, Stone.HEAD),
                new Position( 2, Stone.TAIL),
                new Position( 3, Stone.HEAD),
                new Position( 4, Stone.TAIL),
                new Position( 5, Stone.HEAD));
    }

    public PuzzleState(Position... positions) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            board[i] = new ReadOnlyObjectWrapper<>(Stone.EMPTY);
        }

        for (Position position : positions) {
            setStoneAtPosition(position);
        }
    }

    private void setStoneAtPosition(Position position) {
        int index = position.getCol();
        if (index >= 0 && index < BOARD_SIZE) {
            board[index] = new ReadOnlyObjectWrapper<>(position.getStone());
        }
    }


    @Override
    public String toString() {
        return "PuzzleState{" +
                "board=" + Arrays.toString(board) +
                '}';
    }

    @Override
    public boolean isLegalMove(TwoPhaseMove<Integer> directionTwoPhaseMove) {
        int fromIndex = directionTwoPhaseMove.from();
        int toIndex = directionTwoPhaseMove.to();
        return isLegalToMoveFrom(fromIndex) && isLegalToMoveTo(toIndex);
    }

    @Override
    public boolean isLegalToMoveFrom(Integer fromIndex) {
        return fromIndex >= 0 && fromIndex < BOARD_SIZE - 1
                && board[fromIndex].get() != Stone.EMPTY
                && board[fromIndex + 1].get() != Stone.EMPTY;
    }

    public boolean isLegalToMoveTo(Integer toIndex) {
        return toIndex >= 0 && toIndex < BOARD_SIZE - 1
                && board[toIndex].get() == Stone.EMPTY
                && board[toIndex + 1].get() == Stone.EMPTY;
    }

    @Override
    public boolean isSolved() {
        for (int i = 0; i <= BOARD_SIZE - 6; i++) { // Ensure we have enough space for 6 stones
            if (board[i].get() == Stone.TAIL && board[i + 1].get() == Stone.TAIL && board[i + 2].get() == Stone.TAIL
                    && board[i + 3].get() == Stone.HEAD && board[i + 4].get() == Stone.HEAD && board[i + 5].get() == Stone.HEAD) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void makeMove(TwoPhaseMove<Integer> directionTwoPhaseMove) {
        if (!isLegalMove(directionTwoPhaseMove)) {
            System.out.println("Illegal move detected: " + directionTwoPhaseMove);
            throw new IllegalArgumentException("Illegal move: " + directionTwoPhaseMove);
        }
        Stone[] source = new Stone[2];
        source[0] = board[directionTwoPhaseMove.from()].get();
        source[1] = board[directionTwoPhaseMove.from() + 1].get();
        board[directionTwoPhaseMove.from()].set(Stone.EMPTY);
        board[directionTwoPhaseMove.from() + 1].set(Stone.EMPTY);
        board[directionTwoPhaseMove.to()].set(source[0]);
        board[directionTwoPhaseMove.to() + 1].set(source[1]);
    }


    @Override
    public Set<TwoPhaseMove<Integer>> getLegalMoves() {
        Set<TwoPhaseMove<Integer>> legalMoves = new HashSet<>();

        for (int fromIndex = 0; fromIndex < BOARD_SIZE - 1; fromIndex++) {
            if (isLegalToMoveFrom(fromIndex)) {
                for (int toIndex = 0; toIndex < BOARD_SIZE - 1; toIndex++) {
                    if (Math.abs(fromIndex - toIndex) > 1 && isLegalToMoveTo(toIndex)) {
                        TwoPhaseMove<Integer> move = new TwoPhaseMove<>(fromIndex, toIndex);
                        if (isLegalMove(move)) {
                            legalMoves.add(move);
                        }
                    }
                }
            }
        }
        return legalMoves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PuzzleState that = (PuzzleState) o;
        return Arrays.equals(getBoard(), that.getBoard());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(getBoard());
    }

    // Helper method to get the board as a Stone array
    private Stone[] getBoard() {
        Stone[] boardState = new Stone[BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            boardState[i] = board[i].get();
        }
        return boardState;
    }

    public Stone getStone(int col) {
        return board[col].get();
    }

    @Override
    public TwoPhaseMoveState<Integer> clone() {
        PuzzleState copy;
        try {
            copy = (PuzzleState) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
        copy.board = new ReadOnlyObjectWrapper[BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            copy.board[i] = new ReadOnlyObjectWrapper<>(this.board[i].get());
        }
        return copy;
    }

}