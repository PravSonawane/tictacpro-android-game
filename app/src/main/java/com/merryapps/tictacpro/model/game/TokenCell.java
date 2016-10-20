package com.merryapps.tictacpro.model.game;

import android.support.annotation.NonNull;

/**
 * Created by mephisto on 8/30/16.
 */
public class TokenCell extends Cell {
    
    private Token token;

    /** Creates a new cell with the given token */
    TokenCell(int row, int column, @NonNull Token token) {
        super(row, column);
        this.token = token;
    }

    TokenCell(@NonNull TokenCell tokenCell) {
        super(tokenCell);
        this.token = tokenCell.token;
    }

    public Token getToken() {
        return token;
    }

    void setToken(@NonNull Token token) {
        this.token = token;
    }

    /** Toggles this cell's token from ON to OFF or vice versa */
    void toggle() {
        switch (token) {
            case X:
                this.token = Token.X;
                break;
            case O:
                this.token = Token.O;
                break;
            case E:
                this.token = Token.E;
                break;
        }
    }

    /** Returns true if this cell has its token as marked. False otherwise. */
    boolean isMarked() {
        switch (token) {
            case O:
            case X:
                return true;
            case E:
                return false;
        }

        throw new AssertionError("Unknown token:" + token);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        TokenCell tokenCell = (TokenCell) o;

        return token == tokenCell.token;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (token != null ? token.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "T(r,c,t):"+getRowIndex()+","+getColumnIndex()+","+getToken();
    }

    public boolean isEmpty() {
        return this.token.equals(Token.E);
    }
}
