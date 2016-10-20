package com.merryapps.tictacpro.model.game;

import android.support.annotation.NonNull;

import com.merryapps.tictacpro.model.db.DifficultyEntity;
import com.merryapps.tictacpro.model.db.DifficultyEntityDao;
import com.merryapps.tictacpro.model.db.GlobalStatsEntity;
import com.merryapps.tictacpro.model.db.GlobalStatsEntityDao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.merryapps.framework.EntityState.LOCAL;
import static com.merryapps.tictacpro.model.game.GameState.UNLOCKED_DIFFICULTY_B;
import static com.merryapps.tictacpro.model.game.SoundPref.ON;

/**
 * Created by mephisto on 10/10/16.
 */

public class GlobalStatsManager {

    private static final String TAG = "GlobalStatsManager";

    private GlobalStatsEntityDao globalStatsEntityDao;
    private DifficultyEntityDao difficultyEntityDao;
    private GlobalStats globalStats;
    private List<Difficulty> difficulties;

    public GlobalStatsManager(@NonNull GlobalStatsEntityDao globalStatsEntityDao,
                              @NonNull DifficultyEntityDao difficultyEntityDao) {
        this.globalStatsEntityDao = globalStatsEntityDao;
        this.difficultyEntityDao = difficultyEntityDao;
    }

    public void loadSeedData() {
        //This table will always every have just one record
        if(difficultyEntityDao.count() == 0) {
            difficultyEntityDao.insert(new DifficultyEntity(1L, 1, "Child's Play", "", 5, 0, 30, false, LOCAL));
            difficultyEntityDao.insert(new DifficultyEntity(2L, 2, "I can do this", "", 4, 0, 50, true, LOCAL));
            difficultyEntityDao.insert(new DifficultyEntity(3L, 3, "Are you kidding?", "", 3, -1, 70, true, LOCAL));
            //difficultyEntityDao.insert(new DifficultyEntity(4L, 4, "Insane!", "", 5, -1, 5, true, LOCAL));
        }

        //This table will always every have just one record
        if(globalStatsEntityDao.load(1L) == null) {
            globalStatsEntityDao.insert(new GlobalStatsEntity(1L, 1L, ON, UNLOCKED_DIFFICULTY_B, LOCAL));
        }
    }

    @NonNull
    GlobalStats getGlobalStats() {
        if(this.globalStats == null) {
            this.globalStats = new GlobalStats(globalStatsEntityDao.load(1L));
        }
        return this.globalStats;
    }

    @NonNull
    List<Difficulty> getDifficulties() {
        if(this.difficulties == null) {
            List<DifficultyEntity> difficultyEntities = difficultyEntityDao.loadAll();
            difficulties = new ArrayList<>(difficultyEntities.size());
            for (DifficultyEntity e : difficultyEntities) {
                difficulties.add(new Difficulty(e));
            }

        }
        return Collections.unmodifiableList(difficulties);
    }

    @NonNull
    Difficulty getDifficulty(int type) {
        for (Difficulty d : getDifficulties()) {
            if (d.getType() == type) {
                return d;
            }
        }
        throw new IllegalArgumentException("difficulty not found for type:" + type);
    }

    void save(GlobalStats globalStats) {
        if (globalStats.getSelectedDifficulty().isLocked()) {
            throw new IllegalStateException("Cannot have a locked difficulty as a preffered difficulty selection");
        }
        globalStatsEntityDao.update(globalStats.getEntity());
    }

    void save(Difficulty difficulty) {
        difficultyEntityDao.update(difficulty.getEntity());
    }

    /**
     * Changes to specified difficulty.
     * @param difficulty cannot be null
     */
    void changeDifficulty(@NonNull Difficulty difficulty) {
        globalStats.getEntity().setDifficultyEntity(difficulty.getEntity());
        globalStatsEntityDao.update(globalStats.getEntity());
    }

    /**
     * Changes sound preferences.
     * @param soundPref cannot be null
     */
    void setSoundPref(@NonNull SoundPref soundPref) {
        globalStats.getEntity().setSoundPref(soundPref);
        globalStatsEntityDao.update(globalStats.getEntity());
    }

    List<Difficulty> getUnlockedDifficulties() {
        List<Difficulty> allDifficulties = getDifficulties();
        List<Difficulty> difficulties = new ArrayList<>(allDifficulties.size());
        for (Difficulty entity : allDifficulties) {
            if (!entity.isLocked()) {
                difficulties.add(entity);
            }
        }

        return Collections.unmodifiableList(difficulties);
    }
}
