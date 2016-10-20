package com.merryapps.tictacpro.model.game;

import android.support.annotation.NonNull;

import com.merryapps.tictacpro.model.db.LevelEntity;

/**
 * Created by mephisto on 9/5/16.
 */
public class Level {

    private static final String COLON = ":";
    private static final String SEMICOLON = ";";
    private static final String COMMA = ",";

    private final LevelEntity entity;

    public Level(int levelNum, @NonNull Board board) {

        if (levelNum <= 0) {
            throw new IllegalArgumentException("levelNum cannot <= 0 and/or solution cannot be empty");
        }

        this.entity = new LevelEntity();
        this.entity.setLevelNumber(levelNum);
        this.entity.setBoard(createBoardString(board));
        this.entity.setFirstMoveToken(board.getFirstMove());
    }

    Level(LevelEntity entity) {
        this.entity = entity;
    }

    public int getLevelNum() {
        return this.entity.getLevelNumber();
    }

    @NonNull
    public Board getBoard() {
        Board board = Level.createBoard(
                this.entity.getBoard(), 3, 3
        );
        board.setFirstMove(entity.getFirstMoveToken());
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Level level = (Level) o;

        return entity != null ? entity.equals(level.entity) : level.entity == null;

    }

    @Override
    public int hashCode() {
        return entity != null ? entity.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Level{" +
                "entity=" + entity +
                '}';
    }

    /**
     * Creates and returns the board's layout as a string.
     * Each cell of the board is represented in the format [x,y,token].
     * Each column in a row is separated by a ';' symbol. Each row is separated by a ':'.
     * The string is of the format x1,y1,t1:x1,y1,t1;...]
     */
    private static String createBoardString(Board board) {
        StringBuilder boardString = new StringBuilder();
        for (int row = 0; row < board.getRows(); row++) {
            for (int column = 0; column < board.getColumns(); column++) {
                boardString.append(row).append(",").append(column)
                        .append(",").append(board.getCell(row, column).getToken().get());
                if (column != board.getColumns() - 1) {
                    boardString.append(";");
                }
            }
            if (row != board.getRows() - 1) {
                boardString.append(":");
            }
        }

        return boardString.toString();
    }

    /**
     * Creates a Board object from the given string. The string must be in the format returned by
     * {@code Level#createBoardString(Board)} method of this class.
     * @param boardString the string to be converted
     * @param rows the number of rows in the board
     * @param columns the number of columns
     * @return a Board
     */
    private static Board createBoard(String boardString, int rows, int columns) {
        TokenCell[][] boardState = new TokenCell[rows][columns];

        String[] rowStrings = boardString.split(COLON);
        for (String rowString : rowStrings) {
            String[] columnStrings = rowString.split(SEMICOLON);
            for (String tuples : columnStrings) {
                String[] tuple = tuples.split(COMMA);
                boardState[Integer.parseInt(tuple[0])][Integer.parseInt(tuple[1])] =
                        new TokenCell(Integer.parseInt(tuple[0]), Integer.parseInt(tuple[1]), Token.convert(tuple[2]));
            }
        }

        return new Board(boardState);
    }

    LevelEntity getLevelEntity() {
        return entity;
    }
}
