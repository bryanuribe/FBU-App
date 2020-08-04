package com.example.pickup.dialogues;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.pickup.R;

public class FilterDialogue extends AppCompatDialogFragment {

    private static final String TAG = "FilterDialogue";

    private SeekBar seekBar;
    private FilterDialogueListener listener;
    private TextView tvProgress;
    private Integer currentProgress;

    public interface FilterDialogueListener {
        void onDialogPositiveClick(FilterDialogue dialog, int distanceFromUser);
        void onDialogNegativeClick(FilterDialogue dialog);
    }

    public FilterDialogue(Integer progress) {
        currentProgress = progress;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogue_filter, null);

        builder.setView(view).setTitle("Filters").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onDialogNegativeClick(FilterDialogue.this);
            }
        }).setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int distance = seekBar.getProgress();
                listener.onDialogPositiveClick(FilterDialogue.this, distance);
            }
        });

        tvProgress = view.findViewById(R.id.tvProgress);
        seekBar = view.findViewById(R.id.seekBar);
        seekBar.setProgress(currentProgress);
        currentProgress = seekBar.getProgress();
        tvProgress.setText( currentProgress.toString() + " miles");
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                currentProgress = seekBar.getProgress();
                tvProgress.setText(currentProgress.toString() + " miles");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (FilterDialogueListener) getTargetFragment();
        }
        catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ", e);
        }
    }
}
