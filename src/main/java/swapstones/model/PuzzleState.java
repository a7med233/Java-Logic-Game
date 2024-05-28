package swapstones.model;

import puzzle.TwoPhaseMoveState;

import java.util.Set;

public class PuzzleState implements TwoPhaseMoveState<Integer> {
    @Override
    public boolean isLegalToMoveFrom(Integer integer) {
        return false;
    }

    @Override
    public boolean isSolved() {
        return false;
    }

    @Override
    public boolean isLegalMove(TwoPhaseMove<Integer> integerTwoPhaseMove) {
        return false;
    }

    @Override
    public void makeMove(TwoPhaseMove<Integer> integerTwoPhaseMove) {

    }

    @Override
    public Set<TwoPhaseMove<Integer>> getLegalMoves() {
        return Set.of();
    }

    @Override
    public TwoPhaseMoveState<Integer> clone() {
        return null;
    }
}
