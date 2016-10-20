package com.merryapps.tictacpro.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.merryapps.tictacpro.R;

import java.util.Locale;

/**
 * Created by mephisto on 10/10/16.
 */
public class GameOverDialog extends DialogFragment {

    private static final String SCORE_FORMAT = "%1d";
    private static final String DIFFICULTY_TYPE_FORMAT = "%d";

    private TextView scoreTxtVw;
    private TextView difficultyTxtVw;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_dlg_gameover, null);

        scoreTxtVw = (TextView) view.findViewById(R.id.fragment_dlg_gameover_txtVw_score_id);
        difficultyTxtVw = (TextView) view.findViewById(R.id.fragment_dlg_gameover_txtVw_difficulty_id);
        final GameFragment gameFragment = (GameFragment) getTargetFragment();
        initViews(gameFragment);

        View customTitleView = getActivity().getLayoutInflater().inflate(R.layout.custom_title_text_view, null);
        TextView titleTxtVw = (TextView)customTitleView.findViewById(R.id.custom_title_txtVw_title_id);
        titleTxtVw.setText(getActivity().getResources().getString(R.string.fragment_dlg_gameOver_title_text));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setCustomTitle(customTitleView)
                .setPositiveButton(getActivity().getResources().getString(R.string.fragment_dlg_gameOver_positiveBtn_text),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                                gameFragment.returnToMainMenu();
                            }
                        }
                )
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        // Prevent dialog close on back press button
                        return keyCode == KeyEvent.KEYCODE_BACK;
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        // Make some UI changes for AlertDialog
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {

            int textColor;

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                textColor = getContext().getResources().getColor(R.color.activity_main_label_textColor, null);
            } else {
                textColor = getContext().getResources().getColor(R.color.activity_main_label_textColor);
            }


            // Add or create your own background drawable for AlertDialog window
            Window view = ((AlertDialog)dialog).getWindow();
            assert view != null;
            view.setBackgroundDrawableResource(R.drawable.board_background);

            // Customize POSITIVE, NEGATIVE and NEUTRAL buttons.
            Button positiveButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_POSITIVE);
            positiveButton.setTextColor(textColor);
            positiveButton.setTextColor(textColor);
            positiveButton.invalidate();

            Button negativeButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
            negativeButton.setTextColor(textColor);
            negativeButton.setTypeface(Typeface.DEFAULT_BOLD);
            negativeButton.invalidate();
            }
        });

        return dialog;
    }

    private void initViews(GameFragment gameFragment) {
        scoreTxtVw.setText(String.format(Locale.getDefault(), SCORE_FORMAT, gameFragment.getScore()));
        difficultyTxtVw.setText(String.format(Locale.getDefault(), DIFFICULTY_TYPE_FORMAT, gameFragment.getSelectedDifficulty().getType()));
    }
}
