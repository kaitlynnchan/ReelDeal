package project.game.reeldeal.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import project.game.reeldeal.R;

/**
 * Win Dialog
 * Displays congratulations message
 */
public class WinDialog extends AppCompatDialogFragment {

    private int score;
    private int highScore;

    public WinDialog(int scans, int highScore) {
        this.score = scans;
        this.highScore = highScore;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_win, null);

        TextView txtScore = view.findViewById(R.id.textViewScore);
        String strScore = getString(R.string.score);
        strScore += "  " + score;
        txtScore.setText(strScore);

        if(score < highScore || highScore == -1){
            TextView txtBest = view.findViewById(R.id.textViewBest);
            txtBest.setVisibility(View.VISIBLE);
        }

        Button btnOk = view.findViewById(R.id.buttonOK);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }
}
