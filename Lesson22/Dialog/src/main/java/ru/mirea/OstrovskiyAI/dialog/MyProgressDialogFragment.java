package ru.mirea.OstrovskiyAI.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class MyProgressDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        ProgressBar progressBar = new ProgressBar(getActivity());
        progressBar.setPadding(50, 50, 50, 50);

        builder.setTitle("Loading...")
                .setView(progressBar)
                .setCancelable(false);

        return builder.create();
    }
}