package swapstones.model;

import javafx.beans.property.ReadOnlyObjectWrapper;
import puzzle.TwoPhaseMoveState;

import java.util.*;

public class PuzzleState implements TwoPhaseMoveState<Integer> {
    public static final int BOARD_SIZE = 16;

    private ReadOnlyObjectWrapper<Stone>[] board = new ReadOnlyObjectWrapper[BOARD_SIZE];


    public PuzzleState() {
        int redCount = 0;
        int blackCount = 0;
        for (var i = 0; i < BOARD_SIZE; i++) {
            if (redCount < 3 && (i % 2 == 0)) {
                board[i] = new ReadOnlyObjectWrapper<>(Stone.TAIL);
                redCount++;
            } else if (blackCount < 3 && (i % 2 != 0)) {
                board[i] = new ReadOnlyObjectWrapper<>(Stone.HEAD);
                blackCount++;
            } else {
                board[i] = new ReadOnlyObjectWrapper<>(Stone.EMPTY);
            }
        }
    }

    @Override
    public String toString() {
        return "PuzzleState{" +
                "board=" + Arrays.toString(board) +
                '}';
    }

    @Override
    public boolean isLegalToMoveFrom(Integer fromIndex) {
        return fromIndex >= 0 && fromIndex <= BOARD_SIZE - 2
                && board[fromIndex].get() != Stone.EMPTY
                && board[fromIndex + 1].get() != Stone.EMPTY;
    }

    public boolean isLegalToMoveTo(Integer toIndex) {
        return toIndex >= 0 && toIndex <= BOARD_SIZE - 2
                && board[toIndex].get() == Stone.EMPTY
                && board[toIndex + 1].get() == Stone.EMPTY;
    }


    @Override
    public boolean isSolved() {
        for (var i = 0; i < 11; i++) {
            if ((board[i].get() == Stone.TAIL && board[i + 1].get() == Stone.TAIL && board[i + 2].get() == Stone.TAIL)
                    && (board[i + 3].get() == Stone.HEAD && board[i + 4].get() == Stone.HEAD && board[i + 5].get() == Stone.HEAD)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isLegalMove(TwoPhaseMove<Integer> directionTwoPhaseMove) {
        int fromIndex = directionTwoPhaseMove.from();
        int toIndex = directionTwoPhaseMove.to();

        return isLegalToMoveFrom(fromIndex)
                && isLegalToMoveTo(toIndex);
    }

    @Override
    public void makeMove(TwoPhaseMove<Integer> directionTwoPhaseMove) {
        if (!isLegalMove(directionTwoPhaseMove)) {
            throw new IllegalArgumentException("Illegal move: " + directionTwoPhaseMove);
        }
        Stone source[] = new Stone[2];
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

        for (int i = 0; i < BOARD_SIZE - 1; i++) {
            for (int j = BOARD_SIZE - 1; j > 0; j--) {
                if (board[i].get() != Stone.EMPTY && board[i + 1].get() != Stone.EMPTY) {
                    if (board[j].get() == Stone.EMPTY && board[j-1].get() == Stone.EMPTY) {
                        TwoPhaseMove<Integer> move = new TwoPhaseMove<>(i,j);
                        legalMoves.add(move);
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
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(board);
    }

    @Override
    public TwoPhaseMoveState<Integer> clone() {
        return null;
    }

    public Stone getStone(int col) {
        return board[col].get();
    }
}