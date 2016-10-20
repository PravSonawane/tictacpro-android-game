package com.merryapps.tictacpro.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.merryapps.tictacpro.R;

/**
 * Created by mephisto on 10/9/16.
 */
public class HelpFragment extends Fragment {

    static final String FRAGMENT_NAME_TAG = "helpFragment";
    private static final String TAG = "HelpFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_help, container, false);
    }

}
