package com.merryapps.tictacpro.model.game;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by mephisto on 8/30/16.
 */
public class Board {

    final private TokenCell[][] tokenCells;
    private Token firstMove;

    /**
     * Creates a new board with the token of every {@link TokenCell} as {@link Token#E}.
     * This is same as {@link Board#Board(int, int, Token)} with firstMove as {@link Token#X}
     */
    Board(int rows, int columns) {
        this(rows, columns, Token.X);
    }

    /**
     * Creates a new board with the token of every {@link TokenCell} as {@link Token#E}.
     * Arguments cannot be <= 0 and must be equal.
     * @param rows The number of rows
     * @param columns The number of columns
     * @param firstMove the token for the first compute to be made
     * @throws IllegalArgumentException if rows,columns <= 0 or not equal to each other.
     */
    Board(int rows, int columns, @NonNull Token firstMove) {
        if(rows <= 0 || columns <= 0) {
            throw new IllegalArgumentException("rows and columns must be > 0. " +
                    "Found to be rows:" + rows + " and columns:" + columns);
        }

        if(rows != columns) {
            throw new IllegalArgumentException(("rows and columns must be equal. " +
                    "Found to be rows:" + rows + " and columns:" + columns));
        }

        if (firstMove.equals(Token.E)) {
            throw new IllegalArgumentException("firstMove cannot have the value:" + Token.E);
        }

        tokenCells = new TokenCell[rows][columns];
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                tokenCells[i][j] = new TokenCell(i, j, Token.E);
            }
        }

        this.firstMove = firstMove;
    }

    /**
     * Creates a new Board with the given cell 2D array.
     * This is same as calling {@link Board#Board(TokenCell[][], Token)} with firstMove as
     * {@link Token#X}
     */
    Board(@NonNull TokenCell[][] tokenCells) {
        this(tokenCells, Token.X);
    }

    /**
     * Creates a new Board with the given cell 2D array.
     * The rows and columns must be equal and the grid must have at least one row.
     * @param tokenCells the grid
     * @param firstMove the firstMove that was made. Cannot be {@link Token#E}
     * @throws IllegalArgumentException if rows and columns not equal
     * @throws IllegalStateException if board position is not legal.
     * @throws NullPointerException if any cell or array is null
     */
    Board(@NonNull TokenCell[][] tokenCells, @NonNull Token firstMove) {

        this.tokenCells = new TokenCell[tokenCells.length][tokenCells.length];

        if(tokenCells.length != tokenCells[0].length) {
            throw new IllegalArgumentException(("rows and columns must be equal. " +
                    "Found to be rows:" + tokenCells.length + " and columns:" + tokenCells[0].length));
        }

        for(int i = 0; i < tokenCells.length; i++) {
            for(int j = 0; j < tokenCells[i].length; j++) {
                if (tokenCells[i][j] == null) {
                    throw new NullPointerException("Cell cannot be null. Found null at:" + i + ", " + j);
                }
                this.tokenCells[i][j] = tokenCells[i][j];
            }
        }

        if (!isBoardPositionLegal()) {
            throw new IllegalStateException("The difference in number of X and O tokens on the board " +
                    "is greater than 1. Board is incorrect.");
        }

        this.firstMove = firstMove;
    }

    /** Copy constructor */
    Board(@NonNull Board board) {
        this.tokenCells = new TokenCell[board.getRows()][board.getColumns()];
        for(int i = 0; i < board.getRows(); i++) {
            for(int j = 0; j < board.getColumns(); j++) {
                this.tokenCells[i][j] = new TokenCell(board.getCell(i, j));
            }
        }

        this.firstMove = board.firstMove;
    }

    /**
     * Clears the board. The first cell marked after this compute will have the token
     * returned by {@link Board#getFirstMove()}.
     *
     * <p>
     *     To change the first compute, call {@link Board#setFirstMove(Token)}
     * </p>
     */
    public void clear() {
        for (TokenCell[] aBoardState : tokenCells) {
            for (TokenCell anABoardState : aBoardState) {
                anABoardState.setToken(Token.E);
            }
        }
    }

    public Token getFirstMove() {
        return firstMove;
    }

    /**
     * Sets the firstMove.
     * <p>
     *     Checks the state of the board before setting this value.
     *     If the state of the board is not consistent with the given firstMove,
     *     throws an exception.
     * </p>
     * @param firstMove the first compute to be set. Cannot be {@link Token#E}
     * @throws IllegalStateException if the argument is not consistent with the board.
     */
    void setFirstMove(@NonNull Token firstMove) {

        if (firstMove.equals(Token.E)) {
            throw new IllegalArgumentException("Cannot be:" + Token.E);
        }

        if (getTokenCount(Token.X) > getTokenCount(Token.O)
                && firstMove.equals(Token.O)) {
            throw new IllegalStateException("First compute cannot be:" + firstMove
                    + " at this stage of the game.");
        } else if (getTokenCount(Token.X) < getTokenCount(Token.O)
                && firstMove.equals(Token.X)) {
            throw new IllegalStateException("First compute cannot be:" + firstMove
                    + " at this stage of the game.");
        }

        this.firstMove = firstMove;
    }

    boolean isMarked(int row, int column) {
        return tokenCells[row][column].isMarked();
    }

    public int getRows() {
        return tokenCells.length;
    }

    public int getColumns() {
        return tokenCells[0].length;
    }

    public TokenCell getCell(int row, int column) {
        return tokenCells[row][column];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Board board = (Board) o;

        return Arrays.deepEquals(this.tokenCells, board.tokenCells);

    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(tokenCells);
    }

    @Override
    public String toString() {
        StringBuilder board = new StringBuilder();
        for (TokenCell[] rows : this.tokenCells) {
            board.append(Arrays.toString(rows));
        }
        return "Board{" +
                "tokenCells=" + board.toString() +
                '}';
    }

    /**
     * Returns true if the tokenCells is empty.
     */
    boolean isEmpty() {
        for (TokenCell[] aBoardState : tokenCells) {
            for (TokenCell anABoardState : aBoardState) {
                if (!anABoardState.isEmpty()) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Returns true if no tokenCell is empty.
     */
    boolean isFull() {
        for (TokenCell[] aBoardState : tokenCells) {
            for (TokenCell anABoardState : aBoardState) {
                if (anABoardState.isEmpty()) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Gets the total number of cells having the given token.
     * @return a count
     */
    int getTokenCount(Token token) {
        int count = 0;
        for (TokenCell[] aBoardState : tokenCells) {
            for (TokenCell anABoardState : aBoardState) {
                if (anABoardState.getToken().equals(token)) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Returns an unmodifiable list of empty cells from this board.
     */
    @NonNull
    List<TokenCell> getEmptyCells() {
        List<TokenCell> cells = new ArrayList<>(this.getRows() * this.getColumns());
        for(int i = 0; i < this.getRows(); i++) {
            for(int j = 0; j < this.getColumns(); j++){
                if(this.getCell(i,j).isEmpty()) {
                    cells.add(this.getCell(i,j));
                }
            }
        }

        return Collections.unmodifiableList(cells);
    }

    /**
     * Returns the token who's turn it is to play next.
     * Returns {@link Board#getFirstMove()} if the board is empty
     * or has equals number of player and opponent tokens.
     * Returns {@link Token#E} if the board is full;
     */
    @NonNull
    Token computeNextTurn() {
        if(this.isEmpty()) {
            return firstMove;
        }

        if (this.isFull()) {
            return Token.E;
        }

        if (!isBoardPositionLegal()) {
            throw new IllegalStateException("The difference in number of X and O tokens on the board" +
                    "is greater than 1. Board is incorrect.");
        }

        if (this.getTokenCount(Token.X) > this.getTokenCount(Token.O)) {
            return Token.O;
        } else if (this.getTokenCount(Token.O) > this.getTokenCount(Token.X)) {
            return Token.X;
        }

        return firstMove;
    }

    /**
     * Computes what this Board would look like after the given compute is played.
     * @param move the compute to play (cannot be null)
     * @return a new Board with the given compute played.
     */
    @NonNull
    Board computeBoardAfterMove(@NonNull TokenCell move) {
        Board boardAfterMove = new Board(this);
        boardAfterMove.getCell(move.getRowIndex(), move.getColumnIndex()).setToken(move.getToken());
        return boardAfterMove;
    }

    /**
     * Returns the state of this board.
     * Refer {@link BoardState} for more info on the states.
     */
    @NonNull
    BoardState getBoardState() {
        if (this.isEmpty()) {
            return BoardState.NEW;
        }

        if (isWonByToken(Token.X)) {
            return BoardState.WON_BY_X;
        } else if (isWonByToken(Token.O)) {
            return BoardState.WON_BY_O;
        } else if(isFull()) {
            return BoardState.DRAW;
        } else {
            return BoardState.IN_PROGRESS;
        }
    }

    /**
     * Returns true if the tokenCells is either won by X or O or there are no more moves
     * that can be played. Returns false otherwise.
     */
    boolean isBoardAtEndState() {

        return getBoardState().equals(BoardState.WON_BY_X)
                || getBoardState().equals(BoardState.WON_BY_O)
                || getBoardState().equals(BoardState.DRAW);

    }

    /**
     * Checks if the given token won.
     * @param token the token. (cannot be {@link Token#E} )
     * @return true if won.
     */
    boolean isWonByToken(Token token) {

        if (token.equals(Token.E)) {
            throw new IllegalArgumentException("Token cannot be Token.E");
        }

        for(int i = 0; i < getRows(); i++) {
            if (isRowSame(i, token)) {
                return true;
            }
        }

        for(int i = 0; i < getColumns(); i++) {
            if (isColumnSame(i,token)) {
                return true;
            }
        }
        if (isDiagonal1Same(token)) {
            return true;
        }

        if (isDiagonal2Same(token)) {
            return true;
        }


        return false;

    }

    private boolean isDiagonal2Same(Token token) {
        //check if diagonal 2 is same
        for(int i = 2; i >= 0; i--) {
            if(!getCell(i,getRows() - 1 - i).getToken().equals(token)) {
                return false;
            }
        }
        return true;
    }

    private boolean isDiagonal1Same(Token token) {
        //check if diagonal 1 is same
        boolean wonOnDiagonal1 = true;
        for(int i = 0; i < getRows(); i++) {
            if(!getCell(i,i).getToken().equals(token)) {
                wonOnDiagonal1 = false;
            }
        }

        return wonOnDiagonal1;
    }

    private boolean isRowSame(int row, Token token) {
        for(int j = 0; j < tokenCells[row].length; j++) {
            if (!getCell(row, j).getToken().equals(token)) {
                return false;
            }
        }
        return true;
    }

    private boolean isColumnSame(int column, Token token) {
        for(int j = 0; j < tokenCells.length; j++) {
            if (!getCell(j, column).getToken().equals(token)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Marks the given position with the given token. Token cannot be {@link Token#E}
     * and position cannot be out of board bounds. An existing board cell cannot be marked again.
     * The same token type cannot be used for two consecutive calls to this method.
     * @param row the row of the position
     * @param column the column of the position
     * @param token the token to be marked with
     * @throws IllegalArgumentException for illegal values.
     * @throws IllegalArgumentException if an existing cell is attempted to be marked again.
     */
    void mark(int row, int column, Token token) {
        if (row < 0 || column < 0 || row > tokenCells.length
                || column > tokenCells[row].length || token == null || token.equals(Token.E)) {
            throw new IllegalArgumentException("Invalid values or row/column out of bounds:"
                    + row + ", " + column + ", " + token);
        }

        if (this.isMarked(row, column)) {
            throw new IllegalArgumentException("Attempted to mark a cell that is already marked:"
                    + row + ", " + column + ", " + token);
        }

        if (!computeBoardAfterMove(new TokenCell(row,column,token)).isBoardPositionLegal()) {
            throw new IllegalArgumentException("Cannot use the same token for two consecutive " +
                    "calls to this method");
        }

        if (isEmpty() && !token.equals(firstMove)) {
            throw new IllegalArgumentException("First compute cannot be " + token + " for this board.");
        }

        tokenCells[row][column].setToken(token);
    }

    /**
     * Returns true if the board position is legal.
     * At any given time, the difference in the number of {@link Token#X} or {@link Token#O} tokens cannot
     * be greater than 1.
     */
    private boolean isBoardPositionLegal() {
        return Math.abs(getTokenCount(Token.X) - getTokenCount(Token.O)) <= 1;
    }

    /**
     * Returns true if the compute is a compute that stops a win.
     * @param cell the compute
     * @return true if the compute is a blocking compute
     */
    boolean isBlockingMove(@NonNull TokenCell cell) {
        Token opponentToken = cell.getToken() == Token.X ? Token.O : Token.X;

        //do the opponent compute in the same cell and check if opponent wins
        Board boardAfterMove = this.computeBoardAfterMove(new TokenCell(cell.getRowIndex(),
                cell.getColumnIndex(), opponentToken));

        for(int i = 0; i < boardAfterMove.getRows(); i++) {
            if (boardAfterMove.isRowSame(i, opponentToken)) {
                return true;
            }
            if (boardAfterMove.isColumnSame(i, opponentToken)) {
                return true;
            }
        }

        if (boardAfterMove.isDiagonal1Same(opponentToken)) {
            return true;
        }

        if (boardAfterMove.isDiagonal2Same(opponentToken)) {
            return true;
        }

        return false;
    }

    /**
     * Returns true if the given compute is a forking compute for this board.
     * @param cell the nove
     * @return true if this is a forking compute.
     */
    boolean isForkingMove(@NonNull TokenCell cell) {
        boolean isRowFork = false;
        boolean isColumnFork = false;
        boolean isDiagonal1Fork = false;
        boolean isDiagonal2Fork = false;

        Board newBoard = computeBoardAfterMove(cell);
        if(newBoard.getTokenCountInRow(cell.getRowIndex(), cell.getToken()) > 1
                && newBoard.getTokenCountInRow(cell.getRowIndex(), Token.E) >= 1) {
            isRowFork = true;
        }
        if(newBoard.getTokenCountInColumn(cell.getColumnIndex(), cell.getToken()) > 1
                && newBoard.getTokenCountInColumn(cell.getColumnIndex(), Token.E) >= 1) {
            isColumnFork = true;
        }
        if(newBoard.getTokenCountInDiagonal1(cell.getToken()) > 1
                && newBoard.getTokenCountInDiagonal1(Token.E) >= 1) {
            isDiagonal1Fork = true;
        }
        if(newBoard.getTokenCountInDiagonal2(cell.getToken()) > 1
                && newBoard.getTokenCountInDiagonal2(Token.E) >= 1) {
            isDiagonal2Fork = true;
        }

        int forkCount = 0;
        forkCount = isRowFork ? forkCount + 1 : forkCount;
        forkCount = isColumnFork ? forkCount + 1 : forkCount;
        forkCount = isDiagonal1Fork ? forkCount + 1 : forkCount;
        forkCount = isDiagonal2Fork ? forkCount + 1 : forkCount;

        return forkCount >= 2;

    }

    boolean isWinningMove(TokenCell cell) {

        Board newBoard = computeBoardAfterMove(cell);
        if(newBoard.getTokenCountInRow(cell.getRowIndex(), cell.getToken()) == 3
                && newBoard.getTokenCountInRow(cell.getRowIndex(), Token.E) == 0) {
            return true;
        }
        if(newBoard.getTokenCountInColumn(cell.getColumnIndex(), cell.getToken()) == 3
                && newBoard.getTokenCountInColumn(cell.getColumnIndex(), Token.E) == 0) {
            return true;
        }
        if(newBoard.getTokenCountInDiagonal1(cell.getToken()) == 3
                && newBoard.getTokenCountInDiagonal1(Token.E) == 0) {
            return true;
        }
        if(newBoard.getTokenCountInDiagonal2(cell.getToken()) == 3
                && newBoard.getTokenCountInDiagonal2(Token.E) == 0) {
            return true;
        }

        return false;
    }

    private int getTokenCountInRow(int row, Token token) {
        int count  = 0;
        for(int i = 0; i < getRows(); i++) {
            if (tokenCells[row][i].getToken().equals(token)) {
                count++;
            }
        }

        return count;
    }

    private int getTokenCountInColumn(int column, Token token) {
        int count  = 0;
        for(int i = 0; i < getRows(); i++) {
            if (tokenCells[i][column].getToken().equals(token)) {
                count++;
            }
        }

        return count;
    }

    private int getTokenCountInDiagonal1(Token token) {
        int count  = 0;
        for(int i = 0; i < getRows(); i++) {
            if(tokenCells[i][i].getToken().equals(token)) {
                count++;
            }
        }

        return count;
    }

    private int getTokenCountInDiagonal2(Token token) {
        int count  = 0;
        for(int i = 2; i >= 0; i--) {
            if(getCell(i,getRows() - 1 - i).getToken().equals(token)) {
                count++;
            }
        }

        return count;
    }
}
