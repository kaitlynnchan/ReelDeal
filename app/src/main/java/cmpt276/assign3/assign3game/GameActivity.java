package cmpt276.assign3.assign3game;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    public static Intent makeIntent(Context context){
        Intent intent = new Intent(context, GameActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(GameActivity.RESULT_CANCELED, intent);
        finish();
        super.onBackPressed();
    }
}
