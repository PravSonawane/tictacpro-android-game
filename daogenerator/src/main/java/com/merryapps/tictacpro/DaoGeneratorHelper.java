package com.merryapps.tictacpro;


import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

/**
 * Generates Daos and Dto objects. Also generates supporting classes for database interaction
 */
public class DaoGeneratorHelper {

    private static final int DATABASE_VERSION = 1;

    //the package where the entity itself will be generated
    private static final String ENTITY_PACKAGE_NAME = "com.merryapps.tictacpro.model.db";
    //the package where the dao will be generated
    private static final String DAO_PACKAGE_NAME = "com.merryapps.tictacpro.model.db";
    //the package where the test classes will be generated
    private static final String DAO_TEST_PACKAGE_NAME = "com.merryapps.tictacpro.model.db";

    private static final String ENTITY_STATE_FQDN = "com.merryapps.framework.EntityState";
    private static final String ENTITY_STATE_CONVERTER_FQDN = "com.merryapps.framework.EntityStateConverter";
    private static final String ENTITY_INTERFACE_FQDN = "com.merryapps.framework.Entity";

    //table names
    private static final String LEVEL_TBL_NAME = "LEVEL";
    private static final String USER_STATS_TBL_NAME = "USER_STATS";
    private static final String DIFFICULTY_TBL_NAME = "DIFFICULTY";

    public static void main(String... args) throws Exception {

        Schema tictacproDbSchema = new Schema(DATABASE_VERSION, ENTITY_PACKAGE_NAME);

        //setting the package where the DAO code will be generated
        tictacproDbSchema.setDefaultJavaPackageDao(DAO_PACKAGE_NAME);

        tictacproDbSchema.setDefaultJavaPackageTest(DAO_TEST_PACKAGE_NAME);

        //enabling keep sections
        tictacproDbSchema.enableKeepSectionsByDefault();

        //LevelEntity
        Entity levelEntity = tictacproDbSchema.addEntity("LevelEntity");
        describeLevelEntityTable(levelEntity);

        //DifficultyEntityDao
        Entity difficultyEntity = tictacproDbSchema.addEntity("DifficultyEntity");
        describeDifficultyEntityTable(difficultyEntity);

        //GlobalUserStatsEntity
        Entity globalStatsEntity = tictacproDbSchema.addEntity("GlobalStatsEntity");
        describeGlobalStatsEntityTable(globalStatsEntity, difficultyEntity);

        new DaoGenerator().generateAll(tictacproDbSchema, "app/src/main/java");

    }

    private static void describeLevelEntityTable(Entity levelEntity) {

        final String TOKEN_CONVERTER_FQDN = DAO_PACKAGE_NAME + ".TokenConverter";
        final String TOKEN_FQDN = "com.merryapps.tictacpro.model.game.Token";

        levelEntity.addIdProperty();
        levelEntity.setTableName(LEVEL_TBL_NAME);
        levelEntity.implementsInterface(ENTITY_INTERFACE_FQDN);
        levelEntity.addIntProperty("levelNumber").unique().notNull();
        levelEntity.addStringProperty("board").unique().notNull();
        levelEntity.addStringProperty("firstMoveToken")
                .customType(TOKEN_FQDN, TOKEN_CONVERTER_FQDN).notNull();
        levelEntity.addStringProperty("entityState")
                .customType(ENTITY_STATE_FQDN, ENTITY_STATE_CONVERTER_FQDN).notNull();
    }

    private static void describeGlobalStatsEntityTable(Entity globalStatsEntity, Entity difficultyEntity) {

        final String GAME_STATE_CONVERTER_FQDN = DAO_PACKAGE_NAME + ".GameStateConverter";
        final String GAME_STATE_FQDN = "com.merryapps.tictacpro.model.game.GameState";
        final String DIFFICULTY_CONVERTER_FQDN = DAO_PACKAGE_NAME + ".DifficultyConverter";
        final String DIFFICULTY_FQDN = "com.merryapps.tictacpro.model.game.Difficulty";
        final String SOUND_PREF_CONVERTER_FQDN = DAO_PACKAGE_NAME + ".SoundPrefConverter";
        final String SOUND_PREF_FQDN = "com.merryapps.tictacpro.model.game.SoundPref";

        globalStatsEntity.addIdProperty();
        globalStatsEntity.setTableName(USER_STATS_TBL_NAME);
        globalStatsEntity.implementsInterface(ENTITY_INTERFACE_FQDN);
//        globalStatsEntity.addIntProperty("totalGamesPlayed").notNull();
//        globalStatsEntity.addIntProperty("averageScorePerGame").notNull();
//        globalStatsEntity.addIntProperty("averageTimePerMove").notNull();
//        globalStatsEntity.addIntProperty("fastestMove").notNull();
//        globalStatsEntity.addIntProperty("slowestMove").notNull();
//        globalStatsEntity.addIntProperty("highestScore").notNull();
//        globalStatsEntity.addIntProperty("lowestScore").notNull();
//        globalStatsEntity.addIntProperty("totalBestMoves").notNull();
//        globalStatsEntity.addIntProperty("totalIncorrectMoves").notNull();
//        globalStatsEntity.addIntProperty("winRatio").notNull();
        Property difficultyProperty = globalStatsEntity.addLongProperty("difficultyId").getProperty();
        globalStatsEntity.addToOne(difficultyEntity, difficultyProperty);
        globalStatsEntity.addStringProperty("soundPref")
                .customType(SOUND_PREF_FQDN, SOUND_PREF_CONVERTER_FQDN).notNull();
        globalStatsEntity.addStringProperty("gameState")
                .customType(GAME_STATE_FQDN, GAME_STATE_CONVERTER_FQDN).notNull();
        globalStatsEntity.addStringProperty("entityState")
                .customType(ENTITY_STATE_FQDN, ENTITY_STATE_CONVERTER_FQDN).notNull();
    }

    private static void describeDifficultyEntityTable(Entity difficultyEntity) {

        difficultyEntity.addIdProperty();
        difficultyEntity.setTableName(DIFFICULTY_TBL_NAME);
        difficultyEntity.implementsInterface(ENTITY_INTERFACE_FQDN);
        difficultyEntity.addIntProperty("type").unique().notNull();
        difficultyEntity.addStringProperty("name").notNull();
        difficultyEntity.addStringProperty("description").notNull();
        difficultyEntity.addIntProperty("secondsAddedOnCorrectMove").notNull();
        difficultyEntity.addIntProperty("secondsSubtractedOnCorrectMove").notNull();
        difficultyEntity.addIntProperty("minimumScoreToUnlockNextDifficulty").notNull();
        difficultyEntity.addBooleanProperty("isLocked").notNull();
        difficultyEntity.addStringProperty("entityState")
                .customType(ENTITY_STATE_FQDN, ENTITY_STATE_CONVERTER_FQDN).notNull();
    }
}

