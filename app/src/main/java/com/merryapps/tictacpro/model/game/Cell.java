package com.merryapps.tictacpro.model.game;

/**
 * Created by mephisto on 9/5/16.
 */
public class Cell {

    private final int rowIndex;
    private final int columnIndex;

    public Cell(int rowIndex, int columnIndex) {
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
    }

    Cell(Cell cell) {
        this.rowIndex = cell.rowIndex;
        this.columnIndex = cell.columnIndex;
    }

    int getRowIndex() {
        return rowIndex;
    }

    int getColumnIndex() {
        return columnIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cell cell = (Cell) o;

        if (rowIndex != cell.rowIndex) return false;
        return columnIndex == cell.columnIndex;

    }

    @Override
    public int hashCode() {
        int result = rowIndex;
        result = 31 * result + columnIndex;
        return result;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "rowIndex=" + rowIndex +
                ", columnIndex=" + columnIndex +
                '}';
    }
}
