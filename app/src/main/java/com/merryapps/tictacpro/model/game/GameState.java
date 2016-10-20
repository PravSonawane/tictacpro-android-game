package com.merryapps.tictacpro.model.game;

/**
 * Created by mephisto on 10/10/16.
 */
public enum GameState {

    UNLOCKED_DIFFICULTY_B("1"),
    UNLOCKED_DIFFICULTY_E("2"),
    UNLOCKED_DIFFICULTY_M("3"),
    UNLOCKED_DIFFICULTY_H("4"),
    COMPLETED("5");

    private String code;

    public String getCode() {
        return code;
    }

    GameState(String code) {
        this.code = code;
    }

    public static GameState convert(String code) {
        switch (code) {
            case "1":
                return UNLOCKED_DIFFICULTY_B;
            case "2":
                return UNLOCKED_DIFFICULTY_E;
            case "3":
                return UNLOCKED_DIFFICULTY_M;
            case "4":
                return UNLOCKED_DIFFICULTY_H;
            case "5":
                return COMPLETED;
        }

        throw new AssertionError("Unknown code:" + code);
    }

}
