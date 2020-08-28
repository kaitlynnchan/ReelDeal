package cmpt276.assign3.assign3game.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cmpt276.assign3.assign3game.R;

/**
 * Help Screen
 * Includes about authors, how to play, and citations
 */
public class HelpActivity extends AppCompatActivity {

    public static Intent makeLaunchIntent(Context context){
        Intent intent = new Intent(context, HelpActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        setText();
        setupBackButton();
    }

    private void setupBackButton() {
        Button buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setText() {
        TextView aboutContent = findViewById(R.id.textAboutContent);
        aboutContent.setMovementMethod(LinkMovementMethod.getInstance());

        TextView citationContent = findViewById(R.id.textCitationContent);
        citationContent.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
