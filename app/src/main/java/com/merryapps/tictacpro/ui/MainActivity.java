package com.merryapps.tictacpro.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.merryapps.TicTacProApp;
import com.merryapps.TicTacProManagerFactory;
import com.merryapps.tictacpro.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private FrameLayout placeHolder;
    private GameController gameController;
    private MenuFragment menuFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //remove the status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        initViews();

        //load seed data
        managerFactory().globalStatsManager().loadSeedData();

        //init gameController
        gameController = new GameController(this);

        //replacing placeholder with menu fragment
        if (savedInstanceState == null) {
            menuFragment = new MenuFragment();
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_main_frmLyt_placeHolder_id, menuFragment, MenuFragment.FRAGMENT_NAME_TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
        } else {
            menuFragment = (MenuFragment) getSupportFragmentManager().findFragmentByTag(MenuFragment.FRAGMENT_NAME_TAG);
        }


    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed() called");

        Log.d(TAG, "onBackPressed: getSupportFragmentManager().getBackStackEntryCount():"
                + getSupportFragmentManager().getBackStackEntryCount());

        if(getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStackImmediate();
        } else {
            super.onBackPressed();
        }
    }

    private void initViews() {
        placeHolder = (FrameLayout) findViewById(R.id.activity_main_frmLyt_placeHolder_id);
    }

    public TicTacProManagerFactory managerFactory() {
        return (TicTacProManagerFactory) ((TicTacProApp) getApplication()).getManagerFactory();
    }

    GameController getGameController() {
        return this.gameController;
    }
}
