package com.merryapps.tictacpro.model.game;

import com.merryapps.tictacpro.model.db.GlobalStatsEntity;

/**
 * Created by mephisto on 10/10/16.
 */

public class GlobalStats {

    private GlobalStatsEntity entity;

    public GlobalStats(GlobalStatsEntity entity) {
        this.entity = entity;
    }

    public GameState getGameState() {
        return entity.getGameState();
    }

    public Difficulty getSelectedDifficulty() {
        return new Difficulty(entity.getDifficultyEntity());
    }

    public SoundPref getSoundPref() {
        return entity.getSoundPref();
    }

    void setGameState(GameState state) {
        entity.setGameState(state);
    }

    void setSoundPref(SoundPref soundPref) {
        entity.setSoundPref(soundPref);
    }

    void setSelectedDifficulty(Difficulty difficulty) {
        entity.setDifficultyEntity(difficulty.getEntity());
    }

    GlobalStatsEntity getEntity() {
        return entity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GlobalStats that = (GlobalStats) o;

        return entity != null ? entity.equals(that.entity) : that.entity == null;

    }

    @Override
    public int hashCode() {
        return entity != null ? entity.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "GlobalStats{" +
                "entity=" + entity +
                '}';
    }
}
