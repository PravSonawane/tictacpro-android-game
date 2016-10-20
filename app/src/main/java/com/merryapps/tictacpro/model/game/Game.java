package com.merryapps.tictacpro.model.game;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import static com.merryapps.tictacpro.model.game.GameState.COMPLETED;
import static com.merryapps.tictacpro.model.game.GameState.UNLOCKED_DIFFICULTY_B;
import static com.merryapps.tictacpro.model.game.GameState.UNLOCKED_DIFFICULTY_E;
import static com.merryapps.tictacpro.model.game.GameState.UNLOCKED_DIFFICULTY_M;

/**
 * Manages the game.
 */
public class Game {

    private static final String TAG = "Game";

    private LevelManager levelManager;
    private GlobalStatsManager globalStatsMgr;
    private Board board;
    private int score;

    private AIPlayerMinimax ai;
    private boolean isBestMove;

    public Game(LevelManager levelManager, GlobalStatsManager globalStatsMgr) {
        this.levelManager = levelManager;
        this.globalStatsMgr = globalStatsMgr;
        this.board = levelManager.getNextLevel(globalStatsMgr.getGlobalStats().getSelectedDifficulty()).getBoard();
        ai = new AIPlayerMinimax(board);
    }

    public void newGame() {
        loadNextLevel(globalStatsMgr.getGlobalStats().getSelectedDifficulty());
        score = 0;
    }

    public void loadNextLevel(Difficulty difficulty) {
        this.board = levelManager.getNextLevel(difficulty).getBoard();
        ai.setBoard(board);
    }

    public Difficulty getSelectedDifficulty() {
        return globalStatsMgr.getGlobalStats().getSelectedDifficulty();
    }

    public SoundPref getSoundPref() {
        return globalStatsMgr.getGlobalStats().getSoundPref();
    }

    public void setSoundPref(SoundPref soundPref) {
        globalStatsMgr.setSoundPref(soundPref);
    }

    @NonNull
    public List<Difficulty> getDifficulties() {
        return globalStatsMgr.getDifficulties();
    }

    public void updateGameState() {
        Difficulty beginnerDifficulty = globalStatsMgr.getDifficulty(Difficulty.TYPE_BEGINNER);
        Difficulty easyDifficulty = globalStatsMgr.getDifficulty(Difficulty.TYPE_EASY);
        Difficulty mediumDifficulty = globalStatsMgr.getDifficulty(Difficulty.TYPE_MEDIUM);
        //Difficulty hardDifficulty = globalStatsMgr.getDifficulty(Difficulty.TYPE_HARD);

        if (score >= beginnerDifficulty.getMinimumScoreToUnlockNextDifficulty()
                && globalStatsMgr.getGlobalStats().getGameState().equals(UNLOCKED_DIFFICULTY_B)) {
            moveToNextGameState();
        } else if (score >= easyDifficulty.getMinimumScoreToUnlockNextDifficulty()
                && globalStatsMgr.getGlobalStats().getGameState().equals(UNLOCKED_DIFFICULTY_E)) {
            moveToNextGameState();
        } else if (score >= mediumDifficulty.getMinimumScoreToUnlockNextDifficulty()
            && globalStatsMgr.getGlobalStats().getGameState().equals(UNLOCKED_DIFFICULTY_M)) {
            moveToNextGameState();
        }/* else if (score >= hardDifficulty.getMinimumScoreToUnlockNextDifficulty()
                && globalStatsMgr.getGlobalStats().getGameState().equals(UNLOCKED_DIFFICULTY_M)) {
            moveToNextGameState();
        } else if (score >= hardDifficulty.getMinimumScoreToUnlockNextDifficulty()
                && globalStatsMgr.getGlobalStats().getGameState().equals(UNLOCKED_DIFFICULTY_H)) {
            moveToNextGameState();
        }*/
    }

    private void moveToNextGameState() {
        GlobalStats globalStats = globalStatsMgr.getGlobalStats();
        switch (globalStats.getGameState()) {
            case UNLOCKED_DIFFICULTY_B: {
                globalStats.setGameState(UNLOCKED_DIFFICULTY_E);
                Difficulty difficulty = globalStatsMgr.getDifficulty(Difficulty.TYPE_EASY);
                difficulty.unlock();
                globalStatsMgr.save(difficulty);
                globalStatsMgr.save(globalStats);
                return;
            }
            case UNLOCKED_DIFFICULTY_E: {
                globalStats.setGameState(UNLOCKED_DIFFICULTY_M);
                Difficulty difficulty = globalStatsMgr.getDifficulty(Difficulty.TYPE_MEDIUM);
                difficulty.unlock();
                globalStatsMgr.save(difficulty);
                globalStatsMgr.save(globalStats);
                return;
            }
            case UNLOCKED_DIFFICULTY_M:
            case COMPLETED:
            default: {
                globalStats.setGameState(COMPLETED);
                //Difficulty difficulty = globalStatsMgr.getDifficulty(Difficulty.TYPE_HARD);
                //difficulty.unlock();
                //globalStatsMgr.save(difficulty);
                globalStatsMgr.save(globalStats);
            }
            /*case UNLOCKED_DIFFICULTY_H:
            case COMPLETED:
            default: {
                globalStats.setGameState(UNLOCKED_DIFFICULTY_H);
                Difficulty difficulty = globalStatsMgr.getDifficulty(Difficulty.TYPE_HARD);
                difficulty.unlock();
                globalStatsMgr.save(difficulty);
                globalStats.setSelectedDifficulty(difficulty);
                globalStatsMgr.save(globalStats);
            }*/
        }
    }

    /**
     * Returns true if specified difficulty is available in the list returned by
     * {@link GlobalStatsManager#getUnlockedDifficulties()}
     */
    boolean isDifficultyUnlocked(@NonNull Difficulty difficulty) {
        List<Difficulty> difficulties = globalStatsMgr.getUnlockedDifficulties();
        return difficulties.contains(difficulty);
    }

    /**
     * Checks if the difficulty is unlocked by calling {@link Game#isDifficultyUnlocked(Difficulty)}
     * and sets it. If difficulty is locked, does nothing.
     */
    public void setPreferredDifficulty(@NonNull Difficulty difficulty) {
        if (isDifficultyUnlocked(difficulty)) {
            globalStatsMgr.changeDifficulty(difficulty);
        }
    }

    public boolean isBestMove() {
        return isBestMove;
    }

    /**
     * Specify the row and column number which was clicked.
     * Values should be 0 based.
     * @param rowNum the row of the cell which was clicked.
     * @param colNum the column of the cell which was clicked.
     *
     * @throws IllegalArgumentException if rowNum or colNum are < 0 or > the size of the Board.
     */
    public void tap(int rowNum, int colNum) {
        Log.d(TAG, "tap() called with: " + "rowNum = [" + rowNum + "], colNum = [" + colNum + "]");

        if(rowNum < 0 || colNum < 0 || rowNum > board.getRows() - 1 || colNum > board.getColumns() - 1) {
            throw new IllegalArgumentException("rowNum and/or colNum should be >= 0 and < board dimensions." +
                    "Found to be rowNum:" + rowNum + " and colNum" + colNum);
        }

        if (board.isFull()) {
            return;
        }

        //AI compute
        ai.setToken(board.computeNextTurn());
        ai.compute();

        isBestMove = ai.isBestMove(rowNum, colNum);
        if (isBestMove) {
            score++;
        }
        Log.d(TAG, "tap: isBestMove:" + isBestMove);

        board.mark(rowNum, colNum, board.computeNextTurn());
    }

    public Board getBoard() {
        return this.board;
    }

    public int getScore() {
        return score;
    }
}
