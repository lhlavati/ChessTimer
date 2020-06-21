package luka.ferit.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final long START_TIME_IN_MILLIS = 300000;

    private TextView mTextViewWhite;
    private TextView mTextViewBlack;
    private Button mButtonStartPause;
    private Button mButtonReset;
    private LinearLayout mWhiteLayout;
    private LinearLayout mBlackLayout;

    private CountDownTimer mCountDownTimerWhite;
    private CountDownTimer mCountDownTimerBlack;
    private boolean mTimerRunningWhite = false;
    private boolean mTimerRunningBlack = false;
    private boolean mWhiteLastToPlay = false;
    private long mTimeLeftInMillisWhite = START_TIME_IN_MILLIS;
    private long mTimeLeftInMillisBlack = START_TIME_IN_MILLIS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUI();
    }

    private void setUI(){
        mTextViewWhite = findViewById(R.id.textViewWhite);
        mTextViewBlack = findViewById(R.id.textViewBlack);
        mWhiteLayout = findViewById(R.id.layoutWhite);
        mBlackLayout = findViewById(R.id.layoutBlack);
        mWhiteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBlackTimer();
            }
        });
        mBlackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startWhiteTimer();
            }
        });
        mButtonReset = findViewById(R.id.buttonRestart);
        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });
        mButtonStartPause = findViewById(R.id.buttonStartPause);
        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mTimerRunningWhite && !mTimerRunningBlack && mTimeLeftInMillisWhite == START_TIME_IN_MILLIS && mTimeLeftInMillisBlack == START_TIME_IN_MILLIS){
                    startBlackTimer();
                } else if (mButtonStartPause.getBackground().equals(R.drawable.pause)) {
                    if(mTimerRunningWhite){
                        mButtonStartPause.setBackgroundResource(R.drawable.play);
                        mCountDownTimerWhite.cancel();
                        mTimerRunningWhite = false;
                        mWhiteLastToPlay = true;
                    } else if (mTimerRunningBlack) {
                        mButtonStartPause.setBackgroundResource(R.drawable.play);
                        mCountDownTimerBlack.cancel();
                        mTimerRunningBlack = false;
                        mWhiteLastToPlay = false;
                    }
                } else if (mButtonStartPause.getBackground().equals(R.drawable.play)){
                    if(mWhiteLastToPlay){
                        startWhiteTimer();
                    } else {
                        startBlackTimer();
                    }
                }
            }
        });
    }

    private void startWhiteTimer(){
        mButtonStartPause.setBackgroundResource(R.drawable.pause);
        if(mTimerRunningWhite) return;

        mTimerRunningWhite = true;

        if(mTimerRunningBlack){
            mCountDownTimerBlack.cancel();
            mTimerRunningBlack = false;
        }
        mCountDownTimerWhite = new CountDownTimer(mTimeLeftInMillisWhite, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillisWhite = millisUntilFinished;
                updateWhiteTimer();
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    private void updateWhiteTimer(){
        int minutes = (int) mTimeLeftInMillisWhite / 60000;
        int seconds = (int) mTimeLeftInMillisWhite % 60000 / 1000;

        String timeLeftText;
        if(minutes < 10){
            timeLeftText = "0" + minutes;
        }else{
            timeLeftText = "" + minutes;
        }
        timeLeftText += ":";
        if (seconds < 10) timeLeftText += "0";
        timeLeftText += seconds;

        mTextViewWhite.setText(timeLeftText);
    }

    private void startBlackTimer(){
        mButtonStartPause.setBackgroundResource(R.drawable.pause);

        if(mTimerRunningBlack) return;

        mTimerRunningBlack = true;

        if(mTimerRunningWhite){
            mCountDownTimerWhite.cancel();
            mTimerRunningWhite = false;
        }
        mCountDownTimerBlack = new CountDownTimer(mTimeLeftInMillisBlack, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillisBlack = millisUntilFinished;
                updateBlackTimer();
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    private void updateBlackTimer(){
        int minutes = (int) mTimeLeftInMillisBlack / 60000;
        int seconds = (int) mTimeLeftInMillisBlack % 60000 / 1000;

        String timeLeftText;
        if(minutes < 10){
            timeLeftText = "0" + minutes;
        }else{
            timeLeftText = "" + minutes;
        }
        timeLeftText += ":";
        if (seconds < 10) timeLeftText += "0";
        timeLeftText += seconds;

        mTextViewBlack.setText(timeLeftText);
    }

    private void resetTimer(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reset timer?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mButtonStartPause.setBackgroundResource(R.drawable.play);
                        mTimeLeftInMillisWhite = START_TIME_IN_MILLIS;
                        mTimeLeftInMillisBlack = START_TIME_IN_MILLIS;
                        mCountDownTimerWhite.cancel();
                        mCountDownTimerBlack.cancel();
                        mTimerRunningWhite = false;
                        mTimerRunningBlack = false;
                        updateBlackTimer();
                        updateWhiteTimer();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                builder.create().show();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}
