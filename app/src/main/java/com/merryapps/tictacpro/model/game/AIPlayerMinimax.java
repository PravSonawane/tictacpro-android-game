package com.merryapps.tictacpro.model.game;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/** AIPlayer using Minimax algorithm */
public class AIPlayerMinimax {

    private Board board;

    private Token myToken;    // computer's seed
    private Token oppToken;   // opponent's seed

    private static final String TAG = "AIPlayerMinimax";
    private List<TokenCell> allmoves;
    private List<Integer> allScores;
    private TokenCell bestMove;
    private int bestScoreIndex;
    private int score;

    /** Constructor with the given game board */
    public AIPlayerMinimax(Board board) {
        this.board = board;
    }

    /** Set/change the seed used by computer and opponent */
    public void setToken(Token seed) {
        this.myToken = seed;
        oppToken = (myToken == Token.X) ? Token.O : Token.X;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    boolean isBestMove(int row, int col) {
        for (int i = 0; i < allmoves.size(); i++) {
            if (allScores.get(i).equals(allScores.get(max(allScores))) &&
                    allmoves.get(i).equals(new TokenCell(row, col, board.computeNextTurn()))) {
                return true;
            }
        }

        return false;
    }


    void compute() {
        score = minimax(board, 0);
    }

    /** Recursive minimax at level of depth for either maximizing or minimizing token.
     Return int[3] of {score, row, col}  */
    private int minimax(Board board, int depth) {
        if(board.isBoardAtEndState()) {
            return score(board, depth);
        }

        depth++;

        List<Integer> scores = new ArrayList<>();
        List<TokenCell> moves = new ArrayList<>();

        for (TokenCell empty : board.getEmptyCells()) {
            TokenCell markedCell = new TokenCell(empty.getRowIndex(), empty.getColumnIndex(), board.computeNextTurn());
            Board newBoard = board.computeBoardAfterMove(markedCell);
            scores.add(minimax(newBoard, depth));
            moves.add(markedCell);
        }

        if (board.computeNextTurn().equals(myToken)) {

            if(depth == 1) {
                incrementScoreBasedOnMoveType(board, scores, moves);
            }

            int maxScoreIndex = max(scores);

            if (depth == 1) {
                Log.d(TAG, "minimax: myToken:" + myToken);
                Log.d(TAG, "minimax: myToken:moves:" + moves);
                Log.d(TAG, "minimax: myToken:scores:" + scores);

                allmoves = moves;
                allScores = scores;
            }
            bestMove = moves.get(maxScoreIndex);
            this.bestScoreIndex = maxScoreIndex;
            return scores.get(maxScoreIndex);
        } else {
            if (depth == 1) {
                decrementScoreBasedOnMoveType(board, scores, moves);
            }

            int minScoreIndex = min(scores);

            if (depth == 1) {
                Log.d(TAG, "minimax: oppToken:" + oppToken);
                Log.d(TAG, "minimax: oppToken:moves:" + moves);
                Log.d(TAG, "minimax: oppToken:scores:" + scores);

                allmoves = moves;
                allScores = scores;
            }
            bestMove = moves.get(minScoreIndex);
            return scores.get(minScoreIndex);
        }
    }

    private void decrementScoreBasedOnMoveType(Board board, List<Integer> scores, List<TokenCell> moves) {
        for (int i = 0; i < moves.size(); i++) {
            if (board.isWinningMove(moves.get(i))) {

                Log.d(TAG, "minimax: winning compute found:" + moves.get(i));

                scores.set(i, scores.get(i) - 3);
            }
            if (board.isBlockingMove(moves.get(i))) {
                Log.d(TAG, "minimax: forking compute found:" + moves.get(i));
                scores.set(i, scores.get(i) - 2);
            }

            if (board.isForkingMove(moves.get(i))) {
                    Log.d(TAG, "minimax: forking compute found:" + moves.get(i));
                scores.set(i, scores.get(i) - 1);
            }
        }
    }

    private void incrementScoreBasedOnMoveType(Board board, List<Integer> scores, List<TokenCell> moves) {
        for (int i = 0; i < moves.size(); i++) {
            if (board.isWinningMove(moves.get(i))) {

                Log.d(TAG, "minimax: winning compute found:" + moves.get(i));

                scores.set(i, scores.get(i) + 3);
            }
            if (board.isBlockingMove(moves.get(i))) {

                Log.d(TAG, "minimax: blocking compute found:" + moves.get(i));

                scores.set(i, scores.get(i) + 2);
            }

            if (board.isForkingMove(moves.get(i))) {

                Log.d(TAG, "minimax: forking compute found:" + moves.get(i));

                scores.set(i, scores.get(i) + 1);
            }
        }
    }

    private int score(Board board, int depth) {
        if (board.isWonByToken(myToken)) {
            return 100 - depth*3;
        } else if (board.isWonByToken(oppToken)) {
            return depth*3 - 100;
        } else {
            return 0;
        }
    }

    private int max(List<Integer> scores) {
        int index = -1;
        int max = Integer.MIN_VALUE;
        for(int i = 0; i < scores.size(); i++) {
            if(scores.get(i) >= max) {
                max = scores.get(i);
                index = i;
            }
        }

        return index;
    }

    private int min(List<Integer> scores) {
        int index = -1;
        int min = Integer.MAX_VALUE;
        for(int i = 0; i < scores.size(); i++) {
            if(scores.get(i) <= min) {
                min = scores.get(i);
                index = i;
            }
        }

        return index;
    }


}