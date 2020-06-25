package cmpt276.assign3.assign3game.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import cmpt276.assign3.assign3game.R;

/**
 * Win Dialog
 * Displays congratulations message
 */
public class WinFragment extends AppCompatDialogFragment {

    private int score;
    private int highScore;

    public WinFragment(int scans, int highScore) {
        this.score = scans;
        this.highScore = highScore;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_win, null);

        TextView txtScore = v.findViewById(R.id.textViewScore);
        String strScore = getString(R.string.score);
        strScore += "  " + score;
        txtScore.setText(strScore);

        if(score < highScore || highScore == -1){
            TextView txtBest = v.findViewById(R.id.textViewBest);
            txtBest.setVisibility(View.VISIBLE);
        }

        Button btnOk = v.findViewById(R.id.buttonOK);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .create();
    }

}
