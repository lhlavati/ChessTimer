package luka.ferit.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

public class Increment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_increment);

        final NumberPicker numberPicker = findViewById(R.id.numberPicker);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(10);

        Button buttonSet = findViewById(R.id.buttonSet);
        buttonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("result", numberPicker.getValue());
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
