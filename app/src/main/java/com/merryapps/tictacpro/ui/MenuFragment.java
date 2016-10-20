package com.merryapps.tictacpro.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.merryapps.TicTacProApp;
import com.merryapps.tictacpro.R;
import com.merryapps.tictacpro.model.game.Difficulty;

import java.util.List;

/**
 * Created by mephisto on 10/9/16.
 */
public class MenuFragment extends Fragment {

    static final String FRAGMENT_NAME_TAG = "menuFragment";
    private static final String TAG = "MenuFragment";
    private ImageButton playImgBtn;
    private ImageButton soundImgBtn;
    private ImageButton difficultyImgBtn;
    private ImageButton starImgBtn;
    private ImageButton helpImgBtn;

    private GameController gameController;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView() called with: inflater = [" + inflater + "], container = [" + container + "], savedInstanceState = [" + savedInstanceState + "]");
        Log.d(TAG, "onCreateView: hash:" + this.hashCode());

        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        //initialize
        initViews(view);

        //init gameController
        gameController = ((MainActivity)getActivity()).getGameController();

        //bind gameController to UI objects
        gameController.setActivity(this.getActivity());
        gameController.setStarImgBtn(starImgBtn);
        gameController.setSoundImgBtn(soundImgBtn);
        gameController.setPlayImgBtn(playImgBtn);
        gameController.setMenuFragment(this);

        gameController.setupMenuFragment();

        return view;
    }

    private void initViews(View view) {

        playImgBtn = (ImageButton) view.findViewById(R.id.activity_main_imgBtn_play_id);
        soundImgBtn = (ImageButton) view.findViewById(R.id.activity_main_imgBtn_sound_id);
        difficultyImgBtn = (ImageButton) view.findViewById(R.id.activity_main_imgBtn_difficulty_id);
        starImgBtn = (ImageButton) view.findViewById(R.id.activity_main_imgBtn_star_id);
        helpImgBtn = (ImageButton) view.findViewById(R.id.activity_main_imgBtn_help_id);

        playImgBtn.setOnClickListener(newPlayImgBtnOnClickListener());
        soundImgBtn.setOnClickListener(newSoundImgBtnOnClickListener());
        starImgBtn.setOnClickListener(newStarImgBtnOnClickListener());
        difficultyImgBtn.setOnClickListener(newDifficultyImgBtnOnClickListener());
        helpImgBtn.setOnClickListener(newHelpImgBtnOnClickListener());

    }

    private View.OnClickListener newSoundImgBtnOnClickListener() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                gameController.toggleSound();
            }
        };
    }

    private View.OnClickListener newDifficultyImgBtnOnClickListener() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                gameController.showDifficultyDialog();
            }
        };
    }

    void setSelectedDifficulty(int position) {
        gameController.setSelectedDifficulty(position);
    }

    List<Difficulty> getDifficulties() {
        return gameController.getGame().getDifficulties();
    }

    Difficulty getSelectedDifficulty() {
        return gameController.getGame().getSelectedDifficulty();
    }

    private View.OnClickListener newStarImgBtnOnClickListener() {
        final String packageName = ((TicTacProApp) getActivity().getApplication())
                .getProperty("app.packageName", "project");
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id="+packageName));
                startActivity(intent);
            }
        };
    }

    private View.OnClickListener newPlayImgBtnOnClickListener() {
        //clearing board temporarily
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.activity_main_frmLyt_placeHolder_id, new GameFragment(),  GameFragment.FRAGMENT_NAME_TAG)
                        .addToBackStack("BackStack1")
                        .commit();
            }
        };
    }

    private View.OnClickListener newHelpImgBtnOnClickListener() {
        //clearing board temporarily
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.activity_main_frmLyt_placeHolder_id, new HelpFragment(),  HelpFragment.FRAGMENT_NAME_TAG)
                        .addToBackStack("BackStack2")
                        .commit();
            }
        };
    }

}
