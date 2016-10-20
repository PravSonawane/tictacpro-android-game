package com.merryapps.tictacpro.ui;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.merryapps.tictacpro.R;
import com.merryapps.tictacpro.model.game.Game;
import com.merryapps.tictacpro.model.game.SoundPref;
import com.merryapps.tictacpro.model.game.Token;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import static android.os.Build.VERSION.SDK_INT;
import static com.merryapps.tictacpro.model.game.SoundPref.OFF;
import static com.merryapps.tictacpro.model.game.SoundPref.ON;

/**
 * Created by mephisto on 9/19/16.
 */
class GameController {

    private static final String REMAINING_TIME_FORMAT = "%01d:%02d:%01d";
    private static final String SCORE_TEXT_FORMAT = "%01d";
    private static final int DEFAULT_GAMEPLAY_TIME = 60000;
    private static final int COUNT_DOWN_INTERVAL = 100;

    private Game game;
    private TokenView[][] tokenViews;
    private ImageButton soundImgBtn;
    private ImageButton starImgBtn;
    private ImageView nextToPlayImgVw;
    private TextView scoreTxtVw;
    private ImageButton playImgBtn;
    private int[] timesArray = new int[3];
    private CountDownTimer countDownTimer;
    private TextView timeRemainingTxtVw;
    private SoundPool sp;
    private int[] cMajScale;
    private int[] miscNotes;
    private SoundPref soundPref;

    private int currentMoveCount = 0;
    private long timeRemaining;

    private FragmentActivity activity;
    private GameFragment gameFragment;
    private MenuFragment menuFragment;
    private TextView difficultyTxtVw;

    GameController(MainActivity activity) {
        this.game = new Game(activity.managerFactory().levelManager(),
                activity.managerFactory().globalStatsManager());
        //this.countDownTimer = createCountDownTimer(DEFAULT_GAMEPLAY_TIME, COUNT_DOWN_INTERVAL);
        this.soundPref = getGame().getSoundPref();
    }

    void setupSoundPool() {

        if (cMajScale != null && miscNotes != null) {
            return; //sounds already loaded
        }

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        } else {
            AudioAttributes attrs = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            sp = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .setAudioAttributes(attrs)
                    .build();
        }

        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        cMajScale = new int[8];
        cMajScale[0] = sp.load(activity, R.raw.c3_sharp_maj, 1);
        cMajScale[1] = sp.load(activity, R.raw.d3_sharp_maj, 1);
        cMajScale[2] = sp.load(activity, R.raw.f3_maj, 1);
        cMajScale[3] = sp.load(activity, R.raw.f3_sharp_maj, 1);
        cMajScale[4] = sp.load(activity, R.raw.g3_sharp_maj, 1);
        cMajScale[5] = sp.load(activity, R.raw.a3_sharp_maj, 1);
        cMajScale[6] = sp.load(activity, R.raw.b3_sharp_maj, 1);
        cMajScale[7] = sp.load(activity, R.raw.c4_sharp_maj, 1);
        miscNotes = new int[2];
        miscNotes[0] = sp.load(activity, R.raw.c1_sharp_maj_long, 1);
        miscNotes[1] = sp.load(activity, R.raw.c3cs3d3_bad_move, 1);
    }

    Game getGame() {
        return game;
    }

    private void clearBoard() {
        game.getBoard().clear();
        updateAllTokenVws(Token.E);
    }

    boolean isMarked(TokenView tokenView) {
        int row = tokenView.getRowIndex();
        int column = tokenView.getColumnIndex();
        return !game.getBoard().getCell(row, column).isEmpty();
    }

    void mark(TokenView tokenView) {
        int row = tokenView.getRowIndex();
        int column = tokenView.getColumnIndex();

        if (isMarked(tokenView)) {
            return;
        }

        game.tap(row, column);

        if (game.isBestMove()) {
            playCorrectMoveSound();
            incrementTimer();

            currentMoveCount++;
        } else {
            playBadMoveSound();
            decrementTimer();

            //reset
            currentMoveCount = 0;
        }

        refresh();
    }

    private void decrementTimer() {
        countDownTimer.cancel();
        countDownTimer = createCountDownTimer(timeRemaining + computeTimeSubstraction(), COUNT_DOWN_INTERVAL);
        countDownTimer.start();
    }

    private void incrementTimer() {
        if ((currentMoveCount + 1) % 8 == 0) {
            countDownTimer.cancel();
            countDownTimer = createCountDownTimer(timeRemaining + computeTimeAddition(), COUNT_DOWN_INTERVAL);
            countDownTimer.start();
        }
    }

    void startTimer() {
        countDownTimer.start();
    }

    private void updateTokenVws() {
        for(int i = 0; i < game.getBoard().getRows(); i++) {
            for(int j = 0; j < game.getBoard().getColumns(); j++) {
                if (!tokenViews[i][j].getToken().equals(game.getBoard().getCell(i, j).getToken())) {
                    tokenViews[i][j].setToken(game.getBoard().getCell(i,j).getToken());
                }
            }
        }
    }

    private void updateAllTokenVws(Token token) {
        for(int i = 0; i < game.getBoard().getRows(); i++) {
            for(int j = 0; j < game.getBoard().getColumns(); j++) {
                tokenViews[i][j].setToken(token);
            }
        }
    }

    void refresh() {
        clearBoard();
        game.loadNextLevel(getGame().getSelectedDifficulty());
        updateTokenVws();
        updateNextToPlayImgVw();
        updateScoreTxtVw();
        updateDifficultyTxtVw();
    }

    void newGame() {
        game.newGame();
        countDownTimer = createCountDownTimer(DEFAULT_GAMEPLAY_TIME, COUNT_DOWN_INTERVAL);
        refresh();
        //reset
        currentMoveCount = 0;

        if (soundPref.equals(ON)) {
            setupSoundPool();
        }
    }

    private void updateScoreTxtVw() {
        scoreTxtVw.setText(String.format(Locale.getDefault(), SCORE_TEXT_FORMAT,game.getScore()));
    }

    private void updateDifficultyTxtVw() {
        difficultyTxtVw.setText(String.format(Locale.getDefault(), "%1d", game.getSelectedDifficulty().getType()));
    }

    private void updateNextToPlayImgVw() {
        if (game.getBoard().getFirstMove().equals(Token.X)) {
            Picasso.with(activity)
                    .load(R.drawable.x_to_play)
                    .into(nextToPlayImgVw);
            if (SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                nextToPlayImgVw.setImageDrawable(activity.getResources().getDrawable(R.drawable.x_to_play, null));
            } else {
                nextToPlayImgVw.setImageDrawable(activity.getResources().getDrawable(R.drawable.x_to_play));
            }

        } else {
            if (SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                nextToPlayImgVw.setImageDrawable(activity.getResources().getDrawable(R.drawable.o_to_play, null));
            } else {
                nextToPlayImgVw.setImageDrawable(activity.getResources().getDrawable(R.drawable.o_to_play));
            }
        }
    }

    private CountDownTimer createCountDownTimer(long timeLeft, long timeInterval) {

        return new CountDownTimer(timeLeft, timeInterval) {

            @Override
            public void onTick(long millisUntilFinished) {
                int[] times = computeTimes(millisUntilFinished, timesArray);
                String time = String.format(Locale.getDefault(), REMAINING_TIME_FORMAT,
                        times[0], times[1], times[2]);
                timeRemainingTxtVw.setText(time);
                timeRemaining = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                timeRemainingTxtVw.setText(String.format(Locale.getDefault(), REMAINING_TIME_FORMAT,
                        0,0,0));
                timeRemaining = 0;
                showGameOverDialog();
                game.updateGameState();
            }
        };
    }

    void showGameOverDialog() {
        GameOverDialog gameOverDialog = new GameOverDialog();
        gameOverDialog.setTargetFragment(gameFragment, 1);
        gameOverDialog.show(activity.getSupportFragmentManager(), "GameOverDialogTag");
    }

    void showDifficultyDialog() {
        DifficultyDialog difficultyDialog = new DifficultyDialog();
        difficultyDialog.setTargetFragment(menuFragment,1);
        difficultyDialog.show(activity.getSupportFragmentManager(), "DifficultyDialogTag");
    }

    private int[] computeTimes(long millisUntilFinished, int[] times) {
        times[0] = (int)(millisUntilFinished / 60000);
        times[1] = ((int)(millisUntilFinished/1000)) % 60;
        times[2] = (int)((millisUntilFinished/100)%10);
        return times;
    }

    private long computeTimeAddition() {
        return getGame().getSelectedDifficulty().getSecondsAddedOnCorrectMove() * 1000;
    }

    private long computeTimeSubstraction() {
        return getGame().getSelectedDifficulty().getSecondsSubtractedOnIncorrectMove() * 1000;
    }

    private void playCorrectMoveSound() {
        if (!soundPref.equals(ON)) {
            return;
        }

        sp.play(cMajScale[currentMoveCount % 8], 1, 1, 1, 0, 1.0f);
        if ((currentMoveCount +1) % 8 == 0) {
            sp.play(miscNotes[0], 1, 1, 1, 0, 1.0f);
        }
    }

    private void playBadMoveSound() {
        if (!soundPref.equals(ON)) {
            return;
        }

        sp.play(miscNotes[1], 1, 1, 1, 0, 1.0f);
    }

    void toggleSound() {
        soundPref = soundPref.equals(ON) ? OFF : ON;
        game.setSoundPref(soundPref);
        setSoundIcon();

    }

    void setSelectedDifficulty(int position) {
        game.setPreferredDifficulty(game.getDifficulties().get(position));
    }

    private void setSoundIcon() {
        if (soundPref.equals(ON)) {
            if(SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                soundImgBtn.setImageDrawable(activity.getResources().getDrawable(R.drawable.volume_high, null));
            } else {
                soundImgBtn.setImageDrawable(activity.getResources().getDrawable(R.drawable.volume_high));
            }

        } else {
            if(SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                soundImgBtn.setImageDrawable(activity.getResources().getDrawable(R.drawable.volume_off, null));
            } else {
                soundImgBtn.setImageDrawable(activity.getResources().getDrawable(R.drawable.volume_off));
            }
        }
    }



    void setupMenuFragment() {
        setSoundIcon();
    }

    void cancelCountDownTimer() {
        this.countDownTimer.cancel();
    }

    void setTokenViews(TokenView[][] tokenViews) {
        this.tokenViews = tokenViews;
    }

    void setSoundImgBtn(ImageButton soundImgBtn) {
        this.soundImgBtn = soundImgBtn;
    }

    void setStarImgBtn(ImageButton starImgBtn) {
        this.starImgBtn = starImgBtn;
    }

    void setNextToPlayImgVw(ImageView nextToPlayImgVw) {
        this.nextToPlayImgVw = nextToPlayImgVw;
    }

    void setActivity(FragmentActivity activity) {
        this.activity = activity;
    }

    void setScoreTxtVw(TextView scoreTxtVw) {
        this.scoreTxtVw = scoreTxtVw;
    }

    void setDifficultyTxtVw(TextView difficultyTxtVw) {
        this.difficultyTxtVw = difficultyTxtVw;
    }

    void setPlayImgBtn(ImageButton playImgBtn) {
        this.playImgBtn = playImgBtn;
    }

    void setTimeRemainingTxtVw(TextView timeRemainingTxtVw) {
        this.timeRemainingTxtVw = timeRemainingTxtVw;
    }

    void setGameFragment(GameFragment gameFragment) {
        this.gameFragment = gameFragment;
    }

    void setMenuFragment(MenuFragment menuFragment) {
        this.menuFragment = menuFragment;
    }
}
