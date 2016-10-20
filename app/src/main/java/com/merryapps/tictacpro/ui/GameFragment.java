package com.merryapps.tictacpro.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.merryapps.tictacpro.R;
import com.merryapps.tictacpro.model.game.Board;
import com.merryapps.tictacpro.model.game.Difficulty;
import com.squareup.picasso.Picasso;

/**
 * Created by mephisto on 10/9/16.
 */

public class GameFragment extends Fragment {

    private static final String TAG = "GameFragment";
    static final String FRAGMENT_NAME_TAG = "GameFragment";

    private ImageView nextToPlayImgVw;
    private TextView scoreTxtVw;
    private TextView difficultyTxtVw;
    private TextView timeRemainingTxtVw;

    private GameController gameController;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_game, container, false);

        //initialize
        initViews(view);

        //init gameController
        gameController = ((MainActivity)getActivity()).getGameController();

        //bind gameController to UI objects
        gameController.setNextToPlayImgVw(nextToPlayImgVw);
        gameController.setScoreTxtVw(scoreTxtVw);
        gameController.setDifficultyTxtVw(difficultyTxtVw);
        gameController.setTimeRemainingTxtVw(timeRemainingTxtVw);
        gameController.setActivity(this.getActivity());
        //gameController.setupSoundPool();
        gameController.setGameFragment(this);
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated() called with: view = [" + view + "], savedInstanceState = [" + savedInstanceState + "]");
        super.onViewCreated(view, savedInstanceState);
        initGridLayout(gameController.getGame().getBoard(), this.getActivity());

        //start timer
        gameController.newGame();
        gameController.startTimer();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop() called");
        gameController.cancelCountDownTimer();
        super.onStop();
    }

    private void initViews(View view) {
        timeRemainingTxtVw = (TextView) view.findViewById(R.id.activity_main_txtVw_remaining_time_id);
        scoreTxtVw = (TextView) view.findViewById(R.id.activity_main_txtVw_score_id);
        difficultyTxtVw = (TextView) view.findViewById(R.id.activity_main_txtVw_difficulty_id);

        nextToPlayImgVw = (ImageView) view.findViewById(R.id.activity_main_imgVw_nextToPlay_id);
        Picasso.with(this.getActivity())
                .load(R.drawable.o_to_play)
                .into(nextToPlayImgVw);
    }

    private void initGridLayout(Board board, Activity activity) {
        View.OnClickListener l = newTokenViewOnClickListener();
        GridLayout gridLayout = (GridLayout) activity.findViewById(R.id.activity_main_grdVw_puzzle_id);
        int rowCount = board.getRows();
        int columnCount = board.getColumns();
        int columnIndex = 0;
        int rowIndex = 0;
        int children = rowCount * columnCount;
        TokenView[][] tokenViews = new TokenView[rowCount][columnCount];

        for(int i = 0; i < children; i++, columnIndex++) {

            if(columnIndex > columnCount - 1) {
                columnIndex = 0; //reset column
                rowIndex++; //increment row
            }

            TokenView tokenView = new TokenView(activity, rowIndex, columnIndex);
            tokenView.setToken(board.getCell(rowIndex,columnIndex).getToken());
            tokenView.setId(5000 + i);
            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.columnSpec = GridLayout.spec(columnIndex,1.0f);
            layoutParams.rowSpec = GridLayout.spec(rowIndex,1.0f);

            tokenView.setLayoutParams(layoutParams);
            gridLayout.addView(tokenView);

            tokenView.setOnClickListener(l);
            tokenViews[rowIndex][columnIndex] = tokenView;
            gameController.setTokenViews(tokenViews);
        }

        gridLayout.setRowCount(rowCount);
        gridLayout.setColumnCount(columnCount);
    }

    private View.OnClickListener newTokenViewOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v instanceof TokenView) {
                    gameController.mark((TokenView)v);
                }
            }
        };
    }

    void returnToMainMenu() {
        getActivity().getSupportFragmentManager().popBackStackImmediate();
    }

    int getScore() {
        return gameController.getGame().getScore();
    }

    Difficulty getSelectedDifficulty() {
        return gameController.getGame().getSelectedDifficulty();
    }
}
