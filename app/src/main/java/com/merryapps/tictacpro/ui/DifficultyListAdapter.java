package com.merryapps.tictacpro.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.merryapps.tictacpro.R;
import com.merryapps.tictacpro.model.game.Difficulty;

import java.util.List;
import java.util.Locale;

/**
 * Created by mephisto on 10/9/16.
 */
public class DifficultyListAdapter extends BaseAdapter {

    private List<Difficulty> difficulties;
    private LayoutInflater layoutInflater;
    private int selectedPosition = -1;

    public DifficultyListAdapter(Context context, List<Difficulty> difficulties, Difficulty selectedDifficulty) {
        this.difficulties = difficulties;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //finding the selected item
        for (int i = 0; i < difficulties.size(); i++) {
            if (difficulties.get(i).equals(selectedDifficulty)) {
                selectedPosition = i;
                break;
            }
        }
    }

    @Override
    public int getCount() {
        return difficulties.size();
    }

    @Override
    public Object getItem(int position) {
        return difficulties.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;

        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_difficulty, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)view.getTag();
        }

        viewHolder.difficultySelectedChkBx.setTag(position);

        viewHolder.difficultyTypeTxtVw.setText(String.format(Locale.getDefault(),
                "%1d", difficulties.get(position).getType()));
        viewHolder.difficultyNameTxtVw.setText(difficulties.get(position).getName());

        if (position == selectedPosition) {
            viewHolder.difficultySelectedChkBx.setChecked(true);
        } else {
            viewHolder.difficultySelectedChkBx.setChecked(false);
        }

        if (difficulties.get(position).isLocked()) {
            viewHolder.difficultySelectedChkBx.setVisibility(View.GONE);
            viewHolder.difficultyLockedImgVw.setVisibility(View.VISIBLE);
        } else {
            viewHolder.difficultySelectedChkBx.setVisibility(View.VISIBLE);
            viewHolder.difficultyLockedImgVw.setVisibility(View.GONE);
        }

        viewHolder.difficultySelectedChkBx.setOnClickListener(
                newOnClickListener(viewHolder.difficultySelectedChkBx, position));

        return view;
    }

    private View.OnClickListener newOnClickListener(final CheckBox checkBox, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    selectedPosition = position;
                } else {
                    selectedPosition = -1;
                }
                notifyDataSetChanged();
            }
        };
    }

    private static class ViewHolder {

        TextView difficultyTypeTxtVw;
        TextView difficultyNameTxtVw;
        CheckBox difficultySelectedChkBx;
        ImageView difficultyLockedImgVw;

        ViewHolder(View view) {
            difficultyTypeTxtVw = (TextView)view.findViewById(R.id.item_difficulty_txtVw_difficulty_type_id);
            difficultyNameTxtVw = (TextView)view.findViewById(R.id.item_difficulty_txtVw_difficulty_desc_id);
            difficultySelectedChkBx = (CheckBox) view.findViewById(R.id.item_difficulty_chkBx_selected_id);
            difficultyLockedImgVw = (ImageView) view.findViewById(R.id.item_difficulty_imgVw_locked_id);
        }
    }

    int getSelectedPosition() {
        return selectedPosition;
    }
}
