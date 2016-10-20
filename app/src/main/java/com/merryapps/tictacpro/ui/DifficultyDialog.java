package com.merryapps.tictacpro.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.merryapps.tictacpro.R;
import com.merryapps.tictacpro.model.game.Difficulty;

import java.util.List;

/**
 * Created by mephisto on 10/10/16.
 */
public class DifficultyDialog extends DialogFragment {

    private static final String TAG = "DifficultyDialog";

    private ListView difficultyLstVw;
    private List<Difficulty> difficulties;
    private Difficulty selectedDifficulty;
    private DifficultyListAdapter adapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View customTitleView = getActivity().getLayoutInflater().inflate(R.layout.custom_title_text_view, null);
        TextView titleTxtVw = (TextView) customTitleView.findViewById(R.id.custom_title_txtVw_title_id);
        titleTxtVw.setText(getActivity().getResources().getString(R.string.fragment_dlg_difficulty_title_text));

        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_dlg_difficulty, null);
        difficultyLstVw = (ListView) view.findViewById(R.id.fragment_dlg_lstVw_difficulty_id);
        
        MenuFragment menuFragment = (MenuFragment)getTargetFragment();
        difficulties = menuFragment.getDifficulties();
        selectedDifficulty = menuFragment.getSelectedDifficulty();


        adapter = new DifficultyListAdapter(getActivity(), difficulties, selectedDifficulty);
        difficultyLstVw.setAdapter(adapter);
        difficultyLstVw.setDivider(null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setCustomTitle(customTitleView)
                .setPositiveButton(getActivity().getResources().getString(R.string.fragment_dlg_difficulty_positiveBtn_text),
                        newPositiveBtnListener()
                )
                .setNegativeButton(getActivity().getResources().getString(R.string.fragment_dlg_difficulty_negativeBtn_text),
                        newNegativeBtnListener()
                );

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        // Make some UI changes for AlertDialog
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {

                newOnShowListener((AlertDialog) dialog);
            }
        });

        return dialog;
    }

    private void newOnShowListener(AlertDialog dialog) {
        int textColor;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textColor = getContext().getResources().getColor(R.color.activity_main_label_textColor, null);
        } else {
            textColor = getContext().getResources().getColor(R.color.activity_main_label_textColor);
        }

        Window view = dialog.getWindow();
        assert view != null;
        view.setBackgroundDrawableResource(R.drawable.board_background);

        Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setTextColor(textColor);
        positiveButton.setTypeface(Typeface.DEFAULT_BOLD);
        positiveButton.invalidate();

        Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negativeButton.setTextColor(textColor);
        negativeButton.setTypeface(Typeface.DEFAULT_BOLD);
        negativeButton.invalidate();
    }

    @NonNull
    private DialogInterface.OnClickListener newNegativeBtnListener() {
        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        };
    }

    @NonNull
    private DialogInterface.OnClickListener newPositiveBtnListener() {
        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Log.d(TAG, "onClick: difficultyLstVw.getSelectedItemPosition():" + adapter.getSelectedPosition());
                ((MenuFragment)DifficultyDialog.this.getTargetFragment()).setSelectedDifficulty(adapter.getSelectedPosition());
                dialog.dismiss();
            }
        };
    }
}
