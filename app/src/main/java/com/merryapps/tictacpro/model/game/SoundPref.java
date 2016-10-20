package com.merryapps.tictacpro.model.game;

/**
 * Created by mephisto on 10/14/16.
 */

public enum SoundPref {

    ON("ON"),
    OFF("OFF");

    private String code;

    SoundPref(String code) {
        this.code = code;
    }

    public String get() {
        return code;
    }

    public static SoundPref convert(String code) {
        switch (code) {
            case "ON":
                return ON;
            case "OFF":
                return OFF;
        }

        throw new AssertionError("Unknown code:" + code);
    }
}
