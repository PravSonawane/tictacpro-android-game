package com.merryapps.tictacpro.model.game;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.merryapps.tictacpro.model.game.Token.O;
import static com.merryapps.tictacpro.model.game.Token.X;
import static junit.framework.Assert.assertEquals;

/**
 * Created by mephisto on 9/18/16.
 */
public class BoardTest {

    @Test
    public void testConstructor() {
        Board board = new Board(3,3);
        assertEquals(3, board.getRows());
        assertEquals(3, board.getColumns());
        for(int i = 0; i < board.getRows(); i++) {
            for(int j = 0; j < board.getRows(); j++) {
                assertEquals(new TokenCell(i, j, Token.E), board.getCell(i,j));
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWhenInvalidRow() {
        Board board = new Board(-3,3);
        assertEquals(3, board.getRows());
        assertEquals(3, board.getColumns());
        for(int i = 0; i < board.getRows(); i++) {
            for(int j = 0; j < board.getRows(); j++) {
                assertEquals(new TokenCell(i, j, Token.E), board.getCell(i,j));
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWhenInvalidColumn() {
        Board board = new Board(3,-3);
        assertEquals(3, board.getRows());
        assertEquals(3, board.getColumns());
        for(int i = 0; i < board.getRows(); i++) {
            for(int j = 0; j < board.getRows(); j++) {
                assertEquals(new TokenCell(i, j, Token.E), board.getCell(i,j));
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWhenArgumentsNotEqual() {
        Board board = new Board(3,4);
        assertEquals(3, board.getRows());
        assertEquals(3, board.getColumns());
        for(int i = 0; i < board.getRows(); i++) {
            for(int j = 0; j < board.getRows(); j++) {
                assertEquals(new TokenCell(i, j, Token.E), board.getCell(i,j));
            }
        }
    }

    @Test
    public void testCopyConstructor() {
        Board board = new Board(3, 3);
        board.mark(0, 2, Token.X);
        board.mark(2, 1, Token.O);

        Board anotherBoard = new Board(board);
        for(int i = 0; i < board.getRows(); i++) {
            for(int j = 0; j < board.getRows(); j++) {
                if(i == 0 && j == 2) {
                    assertEquals(new TokenCell(i, j, Token.X), anotherBoard.getCell(i,j));
                } else if(i == 2 && j == 1) {
                    assertEquals(new TokenCell(i, j, Token.O), anotherBoard.getCell(i,j));
                } else {
                    assertEquals(new TokenCell(i, j, Token.E), anotherBoard.getCell(i,j));
                }
            }
        }
    }

    @Test
    public void testConstructorWithCellArg() {
        TokenCell[][] tokenCells = new TokenCell[3][3];
        for (int i = 0; i < tokenCells.length; i++) {
            for (int j = 0; j < tokenCells[i].length; j++) {
                tokenCells[i][j] = new TokenCell(i, j, Token.E);
            }
        }

        assertEquals(new Board(3, 3), new Board(tokenCells));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithCellArgUnequal() {
        TokenCell[][] tokenCells = new TokenCell[3][4];
        for (int i = 0; i < tokenCells.length; i++) {
            for (int j = 0; j < tokenCells[i].length; j++) {
                tokenCells[i][j] = new TokenCell(i, j, Token.E);
            }
        }

        assertEquals(new Board(3, 3), new Board(tokenCells));
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructorWithCellArgResultingInInvalidBoardState() {
        TokenCell[][] tokenCells = new TokenCell[3][3];

        for (int i = 0; i < tokenCells.length; i++) {
            for (int j = 0; j < tokenCells[i].length; j++) {
                tokenCells[i][j] = new TokenCell(i, j, Token.E);
            }
        }

        tokenCells[0][0].setToken(Token.X);
        tokenCells[0][1].setToken(Token.X);
        tokenCells[0][2].setToken(Token.O);
        tokenCells[1][0].setToken(Token.X);

        new Board(tokenCells);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithCellArgNull() {
        TokenCell[][] tokenCells = null;
        new Board(tokenCells);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithCellArgNullCells() {
        TokenCell[][] tokenCells = new TokenCell[3][3];
        assertEquals(new Board(3, 3), new Board(tokenCells));
    }

    @Test
    public void testMark() {
        Board board = new Board(3, 3);
        board.mark(0, 0, Token.X);
        assertEquals(Token.X, board.getCell(0,0).getToken());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMarkWithInvalidValuesForRow() {
        Board board = new Board(-3, 3);
        board.mark(0, 0, Token.X);
        assertEquals(Token.X, board.getCell(0,0).getToken());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMarkWithInvalidValuesForColumn() {
        Board board = new Board(3, -3);
        board.mark(0, 0, Token.X);
        assertEquals(Token.X, board.getCell(0,0).getToken());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMarkWithInvalidValuesForToken() {
        Board board = new Board(3, 3);
        board.mark(0, 0, Token.E);
        assertEquals(Token.X, board.getCell(0,0).getToken());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMarkWhenExistingCellIsAttemptedToBeMarkedAgain() {
        Board board = new Board(3, 3);
        board.mark(0, 0, Token.X);
        board.mark(2, 1, Token.O);
        board.mark(0, 2, Token.X);

        board.mark(0, 0, Token.O);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMarkWhenSameTokenIsMarkedTwice() {
        Board board = new Board(3, 3);
        board.mark(0, 0, Token.X);
        board.mark(2, 1, Token.O);
        board.mark(0, 2, Token.X);

        board.mark(2, 0, Token.X);
    }

    @Test
    public void testClear() {
        Board board = new Board(3, 3);
        board.mark(0, 0, Token.X);
        board.mark(2, 1, Token.O);
        board.mark(0, 2, Token.X);


        board.clear();

        assertEquals(new Board(3, 3), board);
    }

    @Test
    public void testIsMarked() {
        Board board = new Board(3, 3);
        board.mark(0, 0, Token.X);
        board.mark(2, 1, Token.O);
        board.mark(0, 2, Token.X);


        assertEquals(true, board.isMarked(0, 0));
        assertEquals(true, board.isMarked(0, 2));
        assertEquals(true, board.isMarked(2, 1));
    }

    @Test
    public void testGetRowsAndColumns() {
        Board board = new Board(3, 3);
        assertEquals(3, board.getRows());
        assertEquals(3, board.getColumns());
    }

    @Test
    public void testGetCell() {
        Board board = new Board(3, 3);
        board.mark(0, 0, Token.X);
        board.mark(2, 1, Token.O);
        board.mark(0, 2, Token.X);

        assertEquals(new TokenCell(0, 0, Token.X), board.getCell(0, 0));
    }

    @Test
    public void testEqualsAndHashcode() {
        Board board = new Board(3, 3);
        board.mark(0, 0, Token.X);
        board.mark(2, 1, Token.O);
        board.mark(0, 2, Token.X);

        Board board2 = new Board(3, 3);

        board2.mark(0, 0, Token.X);
        board2.mark(2, 1, Token.O);
        board2.mark(0, 2, Token.X);

        assertEquals(true, board.equals(board2));
        assertEquals(board.hashCode(), board2.hashCode());
    }

    @Test
    public void testIsEmptyWhenEmpty() {
        Board board = new Board(3, 3);
        assertEquals(true, board.isEmpty());
    }

    @Test
    public void testIsEmptyWhenNotEmpty() {
        Board board = new Board(3, 3);
        board.mark(0, 0, Token.X);
        board.mark(2, 1, Token.O);
        board.mark(0, 2, Token.X);

        assertEquals(false, board.isEmpty());
    }

    @Test
    public void testIsFullWhenNotFull() {
        Board board = new Board(3, 3);
        assertEquals(false, board.isFull());
    }

    @Test
    public void testIsFullWhenFull() {
        Board board = new Board(3, 3);
        board.mark(0, 0, Token.X);
        board.mark(0, 1, Token.O);
        board.mark(0, 2, Token.X);
        board.mark(1, 0, Token.O);
        board.mark(1, 1, Token.X);
        board.mark(1, 2, Token.O);
        board.mark(2, 0, Token.X);
        board.mark(2, 1, Token.O);
        board.mark(2, 2, Token.X);

        assertEquals(true, board.isFull());
    }

    @Test
    public void testGetTokenCount() {
        Board board = new Board(3, 3);
        board.mark(0, 0, Token.X);
        board.mark(2, 1, Token.O);
        board.mark(0, 2, Token.X);

        assertEquals(2, board.getTokenCount(Token.X));
        assertEquals(1, board.getTokenCount(Token.O));
        assertEquals(6, board.getTokenCount(Token.E));
    }

    @Test
    public void testFindEmptyCells() {
        Board board = new Board(3, 3);
        board.mark(0, 0, Token.X);
        board.mark(2, 1, Token.O);
        board.mark(0, 2, Token.X);

        List<TokenCell> emptyCells = new ArrayList<>();
        emptyCells.add(new TokenCell(0, 1, Token.E));
        emptyCells.add(new TokenCell(1, 0, Token.E));
        emptyCells.add(new TokenCell(1, 1, Token.E));
        emptyCells.add(new TokenCell(1, 2, Token.E));
        emptyCells.add(new TokenCell(2, 0, Token.E));
        emptyCells.add(new TokenCell(2, 2, Token.E));

        assertEquals(emptyCells, board.getEmptyCells());
    }

    @Test
    public void testFindEmptyCellsWhenLastCellIsEmpty() {
        Board board = new Board(3, 3);
        board.mark(0, 0, Token.X);
        board.mark(1, 1, Token.O);
        board.mark(2, 0, Token.X);
        board.mark(1, 0, Token.O);
        board.mark(1, 2, Token.X);
        board.mark(2, 1, Token.O);
        board.mark(0, 1, Token.X);
        board.mark(0, 2, Token.O);
        //board.mark(0, 0, Token.X);

        List<TokenCell> emptyCells = new ArrayList<>();
        emptyCells.add(new TokenCell(2, 2, Token.E));

        assertEquals(emptyCells, board.getEmptyCells());
    }

    @Test
    public void testComputeNextTurn() {
        TokenCell[][] cells = new TokenCell[3][3];

        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                cells[i][j] = new TokenCell(i, j, Token.E);
            }
        }
        cells[0][0].setToken(X);
        cells[2][1].setToken(O);
        cells[0][2].setToken(X);
        cells[1][0].setToken(O);
        Board board = new Board(cells);
        board.setFirstMove(O);

        assertEquals(Token.O, board.computeNextTurn());

        board.mark(2, 2, Token.O);

        assertEquals(Token.X, board.computeNextTurn());
    }

    @Test
    public void testComputeNextTurnWhenBoardIsEmpty() {
        Board board = new Board(3, 3);

        assertEquals(Token.X, board.computeNextTurn());
    }

    @Test
    public void testComputeNextTurnWhenBoardIsFull() {
        Board board = new Board(3, 3);
        board.mark(0, 1, Token.X);
        board.mark(0, 2, Token.O);
        board.mark(1, 0, Token.X);
        board.mark(1, 1, Token.O);
        board.mark(1, 2, Token.X);
        board.mark(2, 0, Token.O);
        board.mark(2, 1, Token.X);
        board.mark(2, 2, Token.O);
        board.mark(0, 0, Token.O);

        assertEquals(true, board.isFull());

        assertEquals(Token.E, board.computeNextTurn());
    }

    @Test
    public void testComputeBoardAfterMove() {
        Board board = new Board(3, 3);

        board.mark(0, 0, Token.X);
        board.mark(0, 2, Token.O);
        board.mark(0, 1, Token.X);


        Board newBoard = new Board(3, 3);
        newBoard.mark(0, 0, Token.X);
        newBoard.mark(0, 2, Token.O);
        newBoard.mark(0, 1, Token.X);
        newBoard.mark(2, 0, Token.O);

        assertEquals(newBoard, newBoard.computeBoardAfterMove(new TokenCell(2, 0, Token.O)));
    }

    @Test
    public void testIsWonByTokenWhenXWinsOnRow1() {
        Board board = new Board(3, 3);
        board.mark(0, 0, Token.X);
        board.mark(1, 0, Token.O);
        board.mark(0, 1, Token.X);
        board.mark(1, 1, Token.O);
        board.mark(0, 2, Token.X);

        assertEquals(true, board.isWonByToken(Token.X));
    }

    @Test
    public void testIsWonByTokenWhenOWinsOnRow1() {
        Board board = new Board(3, 3);
        board.mark(1, 0, Token.X);
        board.mark(0, 1, Token.O);
        board.mark(1, 1, Token.X);
        board.mark(0, 2, Token.O);
        board.mark(0, 0, Token.O);

        assertEquals(true, board.isWonByToken(Token.O));
    }

    @Test
    public void testIsWonByTokenWhenXWinsOnColumn1() {
        Board board = new Board(3, 3);
        board.mark(0, 0, Token.X);
        board.mark(1, 1, Token.O);
        board.mark(1, 0, Token.X);
        board.mark(2, 1, Token.O);
        board.mark(2, 0, Token.X);

        assertEquals(true, board.isWonByToken(Token.X));
    }

    @Test
    public void testIsWonByTokenWhenOWinsOnColumn1() {
        Board board = new Board(3, 3);
        board.mark(1, 1, Token.X);
        board.mark(1, 0, Token.O);
        board.mark(2, 1, Token.X);
        board.mark(2, 0, Token.O);
        board.mark(0, 0, Token.O);

        assertEquals(true, board.isWonByToken(Token.O));
    }

    @Test
    public void testIsWonByTokenWhenXWinsOnRow2() {
        Board board = new Board(3, 3);
        board.mark(1, 0, Token.X);
        board.mark(0, 0, Token.O);
        board.mark(1, 1, Token.X);
        board.mark(0, 1, Token.O);
        board.mark(1, 2, Token.X);

        assertEquals(true, board.isWonByToken(Token.X));
    }

    @Test
    public void testIsWonByTokenWhenOWinsOnRow2() {
        Board board = new Board(3, 3);
        board.mark(0, 0, Token.X);
        board.mark(1, 1, Token.O);
        board.mark(0, 1, Token.X);
        board.mark(1, 2, Token.O);
        board.mark(1, 0, Token.O);

        assertEquals(true, board.isWonByToken(Token.O));
    }

    @Test
    public void testIsWonByTokenWhenXWinsOnColumn2() {
        Board board = new Board(3, 3);
        board.mark(0, 1, Token.X);
        board.mark(1, 0, Token.O);
        board.mark(1, 1, Token.X);
        board.mark(2, 0, Token.O);
        board.mark(2, 1, Token.X);

        assertEquals(true, board.isWonByToken(Token.X));
    }

    @Test
    public void testIsWonByTokenWhenOWinsOnColumn2() {
        Board board = new Board(3, 3);
        board.mark(1, 0, Token.X);
        board.mark(1, 1, Token.O);
        board.mark(2, 0, Token.X);
        board.mark(2, 1, Token.O);
        board.mark(0, 1, Token.O);

        assertEquals(true, board.isWonByToken(Token.O));
    }

    @Test
    public void testIsWonByTokenWhenXWinsOnRow3() {
        Board board = new Board(3, 3);
        board.mark(2, 0, Token.X);
        board.mark(1, 0, Token.O);
        board.mark(2, 2, Token.X);
        board.mark(1, 1, Token.O);
        board.mark(2, 1, Token.X);

        assertEquals(true, board.isWonByToken(Token.X));
    }

    @Test
    public void testIsWonByTokenWhenOWinsOnRow3() {
        Board board = new Board(3, 3);
        board.mark(1, 0, Token.X);
        board.mark(2, 1, Token.O);
        board.mark(1, 1, Token.X);
        board.mark(2, 2, Token.O);
        board.mark(2, 0, Token.O);

        assertEquals(true, board.isWonByToken(Token.O));
    }

    @Test
    public void testIsWonByTokenWhenXWinsOnColumn3() {
        Board board = new Board(3, 3);
        board.mark(0, 2, Token.X);
        board.mark(1, 1, Token.O);
        board.mark(1, 2, Token.X);
        board.mark(2, 1, Token.O);
        board.mark(2, 2, Token.X);

        assertEquals(true, board.isWonByToken(Token.X));
    }

    @Test
    public void testIsWonByTokenWhenOWinsOnColumn3() {
        Board board = new Board(3, 3);
        board.mark(1, 1, Token.X);
        board.mark(1, 2, Token.O);
        board.mark(2, 1, Token.X);
        board.mark(2, 2, Token.O);
        board.mark(0, 2, Token.O);

        assertEquals(true, board.isWonByToken(Token.O));
    }

    @Test
    public void testIsWonByTokenWhenXDoesNotWinOnRow1() {
        Board board = new Board(3, 3);
        board.mark(0, 0, Token.X);
        board.mark(1, 0, Token.O);
        board.mark(0, 1, Token.X);
        board.mark(1, 1, Token.O);

        assertEquals(false, board.isWonByToken(Token.X));
    }

    @Test
    public void testIsWonByTokenWhenODoesNotWinOnRow1() {
        Board board = new Board(3, 3);
        board.mark(1, 0, Token.X);
        board.mark(0, 1, Token.O);
        board.mark(1, 1, Token.X);
        board.mark(0, 0, Token.O);

        assertEquals(false, board.isWonByToken(Token.O));
    }

    @Test
    public void testIsWonByTokenWhenXDoesNotWinOnRow2() {
        Board board = new Board(3, 3);
        board.mark(1, 0, Token.X);
        board.mark(0, 0, Token.O);
        board.mark(1, 1, Token.X);
        board.mark(0, 1, Token.O);

        assertEquals(false, board.isWonByToken(Token.X));
    }

    @Test
    public void testIsWonByTokenWhenODoesNotWinOnRow2() {
        Board board = new Board(3, 3);
        board.mark(0, 0, Token.X);
        board.mark(1, 1, Token.O);
        board.mark(0, 1, Token.X);
        board.mark(1, 0, Token.O);

        assertEquals(false, board.isWonByToken(Token.O));
    }

    @Test
    public void testIsWonByTokenWhenXDoesNotWinOnRow3() {
        Board board = new Board(3, 3);
        board.mark(2, 0, Token.X);
        board.mark(0, 0, Token.O);
        board.mark(2, 1, Token.X);
        board.mark(0, 1, Token.O);

        assertEquals(false, board.isWonByToken(Token.X));
    }

    @Test
    public void testIsWonByTokenWhenODoesNotWinOnRow3() {
        Board board = new Board(3, 3);
        board.mark(0, 0, Token.X);
        board.mark(2, 1, Token.O);
        board.mark(0, 1, Token.X);
        board.mark(2, 0, Token.O);

        assertEquals(false, board.isWonByToken(Token.O));
    }

    @Test
    public void testIsWonByTokenWhenXWinsOnDiagonal1() {
        Board board = new Board(3, 3);
        board.mark(1, 1, Token.X);
        board.mark(2, 0, Token.O);
        board.mark(0, 0, Token.X);
        board.mark(2, 1, Token.O);
        board.mark(2, 2, Token.X);

        assertEquals(true, board.isWonByToken(Token.X));
    }

    @Test
    public void testIsWonByTokenWhenXWinsOnDiagonal2() {
        Board board = new Board(3, 3);
        board.mark(2, 0, Token.X);
        board.mark(0, 0, Token.O);
        board.mark(0, 2, Token.X);
        board.mark(2, 1, Token.O);
        board.mark(1, 1, Token.X);

        assertEquals(true, board.isWonByToken(Token.X));
    }

    @Test
    public void testGetBoardStateWhenBoardIsEmpty() {
        Board board = new Board(3, 3);
        assertEquals(BoardState.NEW, board.getBoardState());
    }

    @Test
    public void testGetBoardStateWhenGameIsInProgress() {
        Board board = new Board(3, 3);

        board.mark(0, 0, Token.X);
        board.mark(0, 2, Token.O);
        board.mark(0, 1, Token.X);

        assertEquals(BoardState.IN_PROGRESS, board.getBoardState());
    }

    @Test
    public void testGetBoardStateWhenGameIsDraw() {
        Board board = new Board(3, 3);
        board.mark(0, 0, Token.X);
        board.mark(1, 0, Token.O);
        board.mark(0, 1, Token.X);
        board.mark(0, 2, Token.O);
        board.mark(1, 2, Token.X);
        board.mark(1, 1, Token.O);
        board.mark(2, 0, Token.X);
        board.mark(2, 1, Token.O);
        board.mark(2, 2, Token.X);

        assertEquals(BoardState.DRAW, board.getBoardState());
    }

    @Test
    public void testGetBoardStateWhenGameIsWonByX() {
        Board board = new Board(3, 3);
        board.mark(0, 1, Token.X);
        board.mark(1, 0, Token.O);
        board.mark(0, 2, Token.X);
        board.mark(1, 1, Token.O);
        board.mark(1, 2, Token.X);
        board.mark(2, 0, Token.O);
        board.mark(2, 1, Token.X);
        board.mark(2, 2, Token.O);
        board.mark(0, 0, Token.X);

        assertEquals(BoardState.WON_BY_X, board.getBoardState());
    }

    @Test
    public void testGetBoardStateWhenGameIsWonByO() {
        Board board = new Board(3, 3);
        board.mark(1, 2, Token.X);
        board.mark(1, 1, Token.O);
        board.mark(1, 0, Token.X);
        board.mark(2, 2, Token.O);
        board.mark(0, 0, Token.O);

        assertEquals(BoardState.WON_BY_O, board.getBoardState());
    }

    @Test
    public void testIsBlockingMoveWhenBlockOnRow1() {
        Board board = new Board(3, 3);
        board.mark(0, 0, Token.X);
        board.mark(1, 1, Token.O);
        board.mark(0, 2, Token.X);

        assertEquals(true, board.isBlockingMove(new TokenCell(0, 1, Token.O)));
        assertEquals(false, board.isBlockingMove(new TokenCell(0, 2, Token.O)));
    }

    @Test
    public void testIsBlockingMoveWhenBlockOnRow2() {
        Board board = new Board(3, 3);
        board.mark(1, 0, Token.X);
        board.mark(2, 1, Token.O);
        board.mark(1, 2, Token.X);

        assertEquals(true, board.isBlockingMove(new TokenCell(1, 1, Token.O)));
        assertEquals(false, board.isBlockingMove(new TokenCell(2, 2, Token.O)));
    }

    @Test
    public void testIsBlockingMoveWhenBlockOnRow3() {
        Board board = new Board(3, 3);
        board.mark(2, 0, Token.X);
        board.mark(1, 1, Token.O);
        board.mark(2, 2, Token.X);

        assertEquals(true, board.isBlockingMove(new TokenCell(2, 1, Token.O)));
        assertEquals(false, board.isBlockingMove(new TokenCell(1, 2, Token.O)));
    }

    @Test
    public void testIsBlockingMoveWhenBlockOnCol1() {
        Board board = new Board(3, 3);
        board.mark(0, 0, Token.X);
        board.mark(1, 1, Token.O);
        board.mark(2, 0, Token.X);

        assertEquals(true, board.isBlockingMove(new TokenCell(1, 0, Token.O)));
        assertEquals(false, board.isBlockingMove(new TokenCell(1, 2, Token.O)));
    }

    @Test
    public void testIsBlockingMoveWhenBlockOnCol2() {
        Board board = new Board(3, 3);
        board.mark(0, 1, Token.X);
        board.mark(0, 0, Token.O);
        board.mark(2, 1, Token.X);

        assertEquals(true, board.isBlockingMove(new TokenCell(1, 1, Token.O)));
        assertEquals(false, board.isBlockingMove(new TokenCell(1, 0, Token.O)));
    }

    @Test
    public void testIsBlockingMoveWhenBlockOnCol3() {
        Board board = new Board(3, 3);
        board.mark(0, 2, Token.X);
        board.mark(1, 1, Token.O);
        board.mark(2, 2, Token.X);

        assertEquals(true, board.isBlockingMove(new TokenCell(1, 2, Token.O)));
        assertEquals(false, board.isBlockingMove(new TokenCell(1, 0, Token.O)));
    }

    @Test
    public void testIsBlockingMoveWhenBlockOnDiag1() {
        Board board = new Board(3, 3);
        board.mark(0, 0, Token.X);
        board.mark(1, 0, Token.O);
        board.mark(2, 2, Token.X);

        assertEquals(true, board.isBlockingMove(new TokenCell(1, 1, Token.O)));
        assertEquals(false, board.isBlockingMove(new TokenCell(1, 2, Token.O)));
    }

    @Test
    public void testIsBlockingMoveWhenBlockOnDiag2() {
        Board board = new Board(3, 3);
        board.mark(0, 2, Token.X);
        board.mark(1, 0, Token.O);
        board.mark(2, 0, Token.X);

        assertEquals(true, board.isBlockingMove(new TokenCell(1, 1, Token.O)));
        assertEquals(false, board.isBlockingMove(new TokenCell(1, 2, Token.O)));
    }

    @Test
    public void testIsForkingMoveWhenForking() {
        Board board = new Board(3, 3);

        board.mark(0, 1, X);
        board.mark(1, 0, O);
        board.mark(2, 0, X);
        board.mark(0, 0, O);

        assertEquals(true, board.isForkingMove(new TokenCell(1, 1, X)));
        assertEquals(true, board.isForkingMove(new TokenCell(2, 1, X)));
        assertEquals(false, board.isForkingMove(new TokenCell(0, 2, X)));
        assertEquals(false, board.isForkingMove(new TokenCell(1, 2, X)));
        assertEquals(false, board.isForkingMove(new TokenCell(2, 2, X)));

    }

    @Test
    public void testIsWinningMoveWhenWinning() {
        Board board = new Board(3, 3);
        board.mark(0, 1, X);
        board.mark(2, 0, O);
        board.mark(0, 2, X);
        board.mark(2, 2, O);

        assertEquals(true, board.isWinningMove(new TokenCell(0, 0, X)));
        assertEquals(false, board.isWinningMove(new TokenCell(1, 0, X)));
        assertEquals(false, board.isWinningMove(new TokenCell(1, 1, X)));
        assertEquals(false, board.isWinningMove(new TokenCell(1, 2, X)));
        assertEquals(false, board.isWinningMove(new TokenCell(2, 1, X)));

    }

    @Test
    public void testSetFirstMoveWhenBoardIsEmpty() {
        Board board = new Board(3, 3);
        assertEquals(Token.X, board.getFirstMove());

        board.setFirstMove(Token.O);
        assertEquals(Token.O, board.getFirstMove());
    }

    @Test
    public void testGetFirstMoveWhenBoardIsPartiallyFull() {
        Board board = new Board(3, 3);

        board.mark(0, 1, X);
        board.mark(0, 2, O);

        assertEquals(X, board.getFirstMove());

    }


}
