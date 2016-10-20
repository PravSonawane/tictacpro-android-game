package com.merryapps.tictacpro.model.db;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.merryapps.tictacpro.model.db.LevelEntity;
import com.merryapps.tictacpro.model.db.DifficultyEntity;
import com.merryapps.tictacpro.model.db.GlobalStatsEntity;

import com.merryapps.tictacpro.model.db.LevelEntityDao;
import com.merryapps.tictacpro.model.db.DifficultyEntityDao;
import com.merryapps.tictacpro.model.db.GlobalStatsEntityDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig levelEntityDaoConfig;
    private final DaoConfig difficultyEntityDaoConfig;
    private final DaoConfig globalStatsEntityDaoConfig;

    private final LevelEntityDao levelEntityDao;
    private final DifficultyEntityDao difficultyEntityDao;
    private final GlobalStatsEntityDao globalStatsEntityDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        levelEntityDaoConfig = daoConfigMap.get(LevelEntityDao.class).clone();
        levelEntityDaoConfig.initIdentityScope(type);

        difficultyEntityDaoConfig = daoConfigMap.get(DifficultyEntityDao.class).clone();
        difficultyEntityDaoConfig.initIdentityScope(type);

        globalStatsEntityDaoConfig = daoConfigMap.get(GlobalStatsEntityDao.class).clone();
        globalStatsEntityDaoConfig.initIdentityScope(type);

        levelEntityDao = new LevelEntityDao(levelEntityDaoConfig, this);
        difficultyEntityDao = new DifficultyEntityDao(difficultyEntityDaoConfig, this);
        globalStatsEntityDao = new GlobalStatsEntityDao(globalStatsEntityDaoConfig, this);

        registerDao(LevelEntity.class, levelEntityDao);
        registerDao(DifficultyEntity.class, difficultyEntityDao);
        registerDao(GlobalStatsEntity.class, globalStatsEntityDao);
    }
    
    public void clear() {
        levelEntityDaoConfig.getIdentityScope().clear();
        difficultyEntityDaoConfig.getIdentityScope().clear();
        globalStatsEntityDaoConfig.getIdentityScope().clear();
    }

    public LevelEntityDao getLevelEntityDao() {
        return levelEntityDao;
    }

    public DifficultyEntityDao getDifficultyEntityDao() {
        return difficultyEntityDao;
    }

    public GlobalStatsEntityDao getGlobalStatsEntityDao() {
        return globalStatsEntityDao;
    }

}
