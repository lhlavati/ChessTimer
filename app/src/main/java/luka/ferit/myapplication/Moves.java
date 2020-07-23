package luka.ferit.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

public class Moves extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moves);

        Intent intent = getIntent();
        int whiteMoves = intent.getIntExtra(MainActivity.WHITE, 0);
        int blackMoves = intent.getIntExtra(MainActivity.BLACK, 0);
        TextView textViewWhite = findViewById(R.id.textViewWhite);
        TextView textViewBlack = findViewById(R.id.textViewBlack);

        textViewWhite.setText("" + whiteMoves);
        textViewBlack.setText("" + blackMoves);

    }
}
