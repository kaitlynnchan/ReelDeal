package project.game.reeldeal.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import project.game.reeldeal.R;

/**
 * PAUSE DIALOG
 * Shows blank game and stop/resume buttons
 */
public class PauseDialog extends AppCompatDialogFragment {

    private View view;
    public InterfaceCommunicator interfaceCommunicator;
    public interface InterfaceCommunicator {
        void sendRequestCode(int code);
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_pause, null);

        setupButtons();

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }

    private void setupButtons() {
        Button buttonResume = view.findViewById(R.id.buttonResume);
        buttonResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaceCommunicator.sendRequestCode(GameActivity.REQUEST_CODE_RESUME);
                dismiss();
            }
        });

        Button buttonStop = view.findViewById(R.id.buttonStop);
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaceCommunicator.sendRequestCode(GameActivity.REQUEST_CODE_STOP);
                dismiss();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        interfaceCommunicator.sendRequestCode(GameActivity.REQUEST_CODE_RESUME);
    }
}