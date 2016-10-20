package com.merryapps.tictacpro.model.game;

import com.merryapps.tictacpro.model.db.DifficultyEntity;

/**
 * Created by mephisto on 10/9/16.
 */
public class Difficulty {

    private DifficultyEntity entity;

//    public static final Difficulty B = new Difficulty("1", "B", "Child's Play",  5, 0, 15);
//    public static final Difficulty E = new Difficulty("2", "E", "I can do this", 3, 0, 20);
//    public static final Difficulty M = new Difficulty("3", "M", "Are you kidding me?", 2, -1, 25);
//    public static final Difficulty H = new Difficulty("4", "H", "Insane!", 1, -1, 30);

    static final int TYPE_BEGINNER  = 1;
    static final int TYPE_EASY      = 2;
    static final int TYPE_MEDIUM    = 3;
    static final int TYPE_HARD      = 4;



    /*
        B("1", "Child's Play", "Next to play is always X. " +
            "Plus 5 seconds on correct move. " +
            "Minus 0 seconds on incorrect move. " +
            "Score at least 15 to unlock next difficulty.", 5, 0, 15),
    E("2", "I can do this", "Next to play is always O. " +
            "Plus 3 seconds on correct move. " +
            "Minus 0 seconds on incorrect move. " +
            "Score at least 20 to unlock next difficulty.", 3, 0, 20),
    M("3", "Are you kidding me?", "Next to play is X or O. " +
            "Plus 2 seconds on correct move. " +
            "Minus 1 second on incorrect move. " +
            "Score at least 25 to unlock next difficulty.", 2, -1, 25),
    H("4", "Insane!", "Next to play is X or O, or triangle/square. " +
            "Plus 1 second on correct move. " +
            "Minus 1 second on incorrect move. " +
            "Score at least 30 to complete this difficulty.", 1, -1, 30);
     */

    Difficulty(DifficultyEntity entity) {
        this.entity = entity;
    }

    public int getType() {
        return entity.getType();
    }

    public String getName() {
        return entity.getName();
    }

    public String getDifficultyDescription() {
        return entity.getDescription();
    }

    public int getSecondsAddedOnCorrectMove() {
        return entity.getSecondsAddedOnCorrectMove();
    }

    public int getSecondsSubtractedOnIncorrectMove() {
        return entity.getSecondsSubtractedOnCorrectMove();
    }

    public int getMinimumScoreToUnlockNextDifficulty() {
        return entity.getMinimumScoreToUnlockNextDifficulty();
    }

    public boolean isLocked() {
        return entity.getIsLocked();
    }

    DifficultyEntity getEntity() {
        return entity;
    }

    public void unlock() {
        entity.setIsLocked(false);
    }

    public void lock() {
        entity.setIsLocked(true);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Difficulty that = (Difficulty) o;

        return entity != null ? entity.equals(that.entity) : that.entity == null;

    }

    @Override
    public int hashCode() {
        return entity != null ? entity.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Difficulty{" +
                "entity=" + entity +
                '}';
    }
}
