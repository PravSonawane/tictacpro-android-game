package com.merryapps.tictacpro.model.db;

import com.merryapps.tictacpro.model.game.GameState;

import de.greenrobot.dao.converter.PropertyConverter;

/**
 * Created by mephisto on 10/10/16.
 */
public class GameStateConverter implements PropertyConverter<GameState, String> {

    @Override
    public GameState convertToEntityProperty(String databaseValue) {
        return GameState.convert(databaseValue);
    }

    @Override
    public String convertToDatabaseValue(GameState entityProperty) {
        return entityProperty.getCode();
    }
}
