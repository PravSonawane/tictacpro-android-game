package com.merryapps.tictacpro.model.game;

import com.merryapps.tictacpro.model.db.LevelEntityDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.merryapps.tictacpro.model.game.Token.E;
import static com.merryapps.tictacpro.model.game.Token.O;
import static com.merryapps.tictacpro.model.game.Token.X;

/**
 * <p>
 * Manages game levels.
 * </p>
 * <p>
 * This manager is used to create levels, load levels, etc.
 * </p>
 */
public class LevelManager {

    private LevelEntityDao dao;

    private List<Level> levels = new ArrayList<>();

    public LevelManager(LevelEntityDao dao) {
        this.dao = dao;
    }

    public Level createLevel1() {
        Board board = new Board(3, 3);

        board.mark(0, 0, E);
        board.mark(0, 1, E);
        board.mark(0, 2, X);
        board.mark(1, 0, O);
        board.mark(1, 1, X);
        board.mark(1, 2, E);
        board.mark(2, 0, O);
        board.mark(2, 1, E);
        board.mark(2, 2, E);
        return new Level(1, board);
    }

    public void save(Level level) {
        dao.insert(level.getLevelEntity());
    }

    public Level getLevel(int levelNumber) {
        return new Level(dao.queryBuilder().where(LevelEntityDao.Properties.LevelNumber
                .eq(levelNumber)).unique());
    }

    public Level getNextLevel(Difficulty difficulty) {
        return generateRandomLevel(4, difficulty);
    }

    public Level generateRandomLevel(int tokens, Difficulty difficulty) {
        Board board = new Board(3, 3);
        Random random = new Random();
        switch (difficulty.getType()) {
            case 1: {
                board.setFirstMove(X);
                break;
            }
            case 2: {
                board.setFirstMove(O);
                break;
            }
            case 3: {
                board.setFirstMove(random.nextFloat() > 0.5f ? X : O);
                break;
            }
        }

        for(int i = 0; i < tokens; i++) {
            List<TokenCell> emptyCells = board.getEmptyCells();
            TokenCell randomCell = emptyCells.get(random.nextInt(emptyCells.size()));
            board.mark(randomCell.getRowIndex(), randomCell.getColumnIndex(), board.computeNextTurn());
        }

        return new Level(1, board);
    }
}
