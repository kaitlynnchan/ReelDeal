package project.game.reeldeal.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import project.game.reeldeal.R;

/**
 * Help Screen
 * Includes about authors, how to play, and citations
 */
public class HelpActivity extends AppCompatActivity {

    public static Intent makeLaunchIntent(Context context){
        return new Intent(context, HelpActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        setupTexts();
        setupBackButton();
    }

    private void setupTexts() {
        TextView textAboutContent = findViewById(R.id.text_about);
        textAboutContent.setMovementMethod(LinkMovementMethod.getInstance());

        TextView textCitationsContent = findViewById(R.id.text_citations);
        textCitationsContent.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void setupBackButton() {
        Button buttonBack = findViewById(R.id.button_back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
