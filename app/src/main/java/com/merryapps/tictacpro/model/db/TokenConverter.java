package com.merryapps.tictacpro.model.db;

import com.merryapps.tictacpro.model.game.Token;

import de.greenrobot.dao.converter.PropertyConverter;

/**
 * Created by mephisto on 9/24/16.
 */

public class TokenConverter implements PropertyConverter<Token, String> {
    @Override
    public Token convertToEntityProperty(String databaseValue) {
        return Token.convert(databaseValue);
    }

    @Override
    public String convertToDatabaseValue(Token entityProperty) {
        return entityProperty.get();
    }
}
