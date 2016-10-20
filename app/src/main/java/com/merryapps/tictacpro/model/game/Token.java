package com.merryapps.tictacpro.model.game;

/**
 * Created by mephisto on 8/30/16.
 */
public enum Token {
    X("T01"),
    O("T02"),
    E("T03");

    private String value;

    Token(String value) {
        this.value = value;
    }

    public String get() {
        return this.value;
    }

    public static Token convert(String value) {
        if(value == null || value.isEmpty()) {
            throw new IllegalArgumentException("value cannot be null or empty");
        }

        switch (value) {
            case "T01":
                return X;
            case "T02":
                return O;
            case "T03":
                return E;
        }

        throw new IllegalArgumentException("no type defined for value:" + value);
    }
}
