package com.merryapps;

import com.merryapps.framework.AbstractManagerFactory;
import com.merryapps.tictacpro.model.db.DaoMaster;
import com.merryapps.tictacpro.model.game.GlobalStatsManager;
import com.merryapps.tictacpro.model.game.LevelManager;

/**
 * Created by mephisto on 9/7/16.
 */
public class TicTacProManagerFactory extends AbstractManagerFactory {

    public TicTacProManagerFactory(DaoMaster daoMaster) {
        super(daoMaster);
    }

    public LevelManager levelManager() {
        return new LevelManager(daoMaster.newSession().getLevelEntityDao());
    }

    public GlobalStatsManager globalStatsManager() {
        return new GlobalStatsManager(daoMaster.newSession().getGlobalStatsEntityDao(),
                daoMaster.newSession().getDifficultyEntityDao());
    }

}
