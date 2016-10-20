package com.merryapps.tictacpro.model.game;

/**
 * Created by mephisto on 9/17/16.
 */
public class Move {

    private Cell cell;
    private int moveEvaluation;

    public Move(Cell cell) {
        this.cell = cell;
    }

    public Cell getCell() {
        return cell;
    }

    public int getMoveEvaluation() {
        return moveEvaluation;
    }

    public static Move findMinMove(Move m1, Move m2) {
        if (Math.min(m1.moveEvaluation, m2.moveEvaluation) == m1.moveEvaluation) {
            return m1;
        } else {
            return m2;
        }
    }

    public static Move findMaxMove(Move m1, Move m2) {
        if (Math.max(m1.moveEvaluation, m2.moveEvaluation) == m1.moveEvaluation) {
            return m1;
        } else {
            return m2;
        }
    }

    public void setMoveEvaluation(int moveEvaluation) {
        this.moveEvaluation = moveEvaluation;
    }

    @Override
    public String toString() {
        return "Move{" +
                "cell=" + cell +
                ", moveEvaluation=" + moveEvaluation +
                '}';
    }
}
