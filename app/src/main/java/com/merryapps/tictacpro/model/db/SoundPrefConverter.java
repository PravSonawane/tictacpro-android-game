package com.merryapps.tictacpro.model.db;

import com.merryapps.tictacpro.model.game.SoundPref;

import de.greenrobot.dao.converter.PropertyConverter;

/**
 * Created by mephisto on 10/10/16.
 */
public class SoundPrefConverter implements PropertyConverter<SoundPref, String> {

    @Override
    public SoundPref convertToEntityProperty(String databaseValue) {
        return SoundPref.convert(databaseValue);
    }

    @Override
    public String convertToDatabaseValue(SoundPref entityProperty) {
        return entityProperty.get();
    }
}
