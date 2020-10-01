package luka.ferit.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shawnlin.numberpicker.NumberPicker;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private long START_TIME_IN_MILLIS = 300000;
    private long toMillis;
    private long mIncrementValue = 0;

    private TextView mTextViewWhite;
    private TextView mTextViewBlack;
    private Button mButtonStartPause;
    private Button mButtonReset;
    private Button mButtonTime;
    private Button mButtonIncrement;
    private Button mButtonSound;
    private LinearLayout mWhiteLayout;
    private LinearLayout mBlackLayout;

    private CountDownTimer mCountDownTimerWhite;
    private CountDownTimer mCountDownTimerBlack;
    private boolean mTimerRunningWhite = false;
    private boolean mTimerRunningBlack = false;
    private boolean mWhiteLastToPlay = false;
    private boolean mSoundOn = true;
    private boolean alreadyExecutedWhite = false;
    private boolean alreadyExecutedBlack = false;
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
        final MediaPlayer mediaPlayerTick = MediaPlayer.create(this, R.raw.tick);
        mWhiteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSoundOn && mTimerRunningWhite) mediaPlayerTick.start();
                startBlackTimer();

                updateWhiteTimer();
            }
        });
        mBlackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSoundOn && mTimerRunningBlack) mediaPlayerTick.start();
                startWhiteTimer();

                updateBlackTimer();
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
                    startWhiteTimer();
                } else if (mButtonStartPause.getBackground().getConstantState() == getResources().getDrawable(R.drawable.pause).getConstantState()) {
                    pauseTimers();
                } else if (mButtonStartPause.getBackground().getConstantState() == getResources().getDrawable(R.drawable.play).getConstantState()) {
                    resumeTimers();
                }
            }
        });
        mButtonTime = findViewById(R.id.buttonTime);
        mButtonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime();
            }
        });

        mButtonIncrement = findViewById(R.id.buttonIncrement);
        mButtonIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIncrement();
            }
        });

        mButtonSound = findViewById(R.id.buttonSound);
        mButtonSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSoundOn = !mSoundOn;
                if(mSoundOn){
                    mButtonSound.setBackgroundResource(R.drawable.sound);
                } else {
                    mButtonSound.setBackgroundResource(R.drawable.mute);
                };
            }
        });
    }

    private void startWhiteTimer(){
        mButtonStartPause.setBackgroundResource(R.drawable.pause);

        if(mTimerRunningWhite) return;

        mTimeLeftInMillisBlack += mIncrementValue;
        mTimerRunningWhite = true;
        mWhiteLastToPlay = true;

        if(mTimerRunningBlack){
            mCountDownTimerBlack.cancel();
            mTimerRunningBlack = false;
        }

        final Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mCountDownTimerWhite = new CountDownTimer(mTimeLeftInMillisWhite, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillisWhite = millisUntilFinished;
                updateWhiteTimer();
                if(millisUntilFinished < 10000){
                    mTextViewWhite.setTextColor(getResources().getColor(R.color.colorRed));
                    if(!alreadyExecutedWhite && mSoundOn){
                        v.vibrate(400);
                        alreadyExecutedWhite = true;
                    }
                } else {
                    mTextViewWhite.setTextColor(getResources().getColor(R.color.colorBlack));
                }
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

        if(START_TIME_IN_MILLIS != mTimeLeftInMillisWhite) {
            mTimeLeftInMillisWhite += mIncrementValue;
        }

        mTimerRunningBlack = true;
        mWhiteLastToPlay = false;

        if(mTimerRunningWhite){
            mCountDownTimerWhite.cancel();
            mTimerRunningWhite = false;
        }

        final Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mCountDownTimerBlack = new CountDownTimer(mTimeLeftInMillisBlack, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillisBlack = millisUntilFinished;
                updateBlackTimer();
                if(millisUntilFinished < 10000){
                    mTextViewBlack.setTextColor(getResources().getColor(R.color.colorRed));
                    if(!alreadyExecutedBlack && mSoundOn){
                        v.vibrate(400);
                        alreadyExecutedBlack = true;
                    }
                } else {
                    mTextViewBlack.setTextColor(getResources().getColor(R.color.colorWhite));
                }
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
        pauseTimers();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reset timer?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mButtonStartPause.setBackgroundResource(R.drawable.play);
                        mTimeLeftInMillisWhite = START_TIME_IN_MILLIS;
                        mTimeLeftInMillisBlack = START_TIME_IN_MILLIS;
                        if(mTimerRunningWhite || mTimerRunningBlack){
                            mCountDownTimerWhite.cancel();
                            mCountDownTimerBlack.cancel();
                        }
                        mTextViewWhite.setTextColor(getResources().getColor(R.color.colorBlack));
                        mTextViewBlack.setTextColor(getResources().getColor(R.color.colorWhite));
                        mTimerRunningWhite = false;
                        mTimerRunningBlack = false;
                        alreadyExecutedWhite = false;
                        alreadyExecutedBlack = false;
                        updateBlackTimer();
                        updateWhiteTimer();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        resumeTimers();
                    }
                });
                builder.create().show();
    }

    private void setTime(){
        pauseTimers();

        final NumberPicker numberPicker1 = new NumberPicker(this);
        numberPicker1.setMaxValue(60);
        numberPicker1.setMinValue(1);
        numberPicker1.setValue(5);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(numberPicker1)
                .setTitle("Set the time:")
                .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toMillis = TimeUnit.MINUTES.toMillis(numberPicker1.getValue());
                        START_TIME_IN_MILLIS = toMillis;
                        mButtonStartPause.setBackgroundResource(R.drawable.play);
                        mTimeLeftInMillisWhite = START_TIME_IN_MILLIS;
                        mTimeLeftInMillisBlack = START_TIME_IN_MILLIS;
                        if(mTimerRunningWhite || mTimerRunningBlack){
                            mCountDownTimerWhite.cancel();
                            mCountDownTimerBlack.cancel();
                        }
                        mTextViewWhite.setTextColor(getResources().getColor(R.color.colorBlack));
                        mTextViewBlack.setTextColor(getResources().getColor(R.color.colorWhite));
                        mTimerRunningWhite = false;
                        mTimerRunningBlack = false;
                        updateBlackTimer();
                        updateWhiteTimer();
                    }
                });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                resumeTimers();
            }
        });
        builder.create().show();
    }

    private void setIncrement(){
        pauseTimers();

        final NumberPicker numberPicker2 = new NumberPicker(this);
        numberPicker2.setMaxValue(10);
        numberPicker2.setMinValue(0);
        numberPicker2.setValue(0);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(numberPicker2)
                .setTitle("Set the increment:")
                .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mIncrementValue = numberPicker2.getValue() * 1000;
                        mButtonStartPause.setBackgroundResource(R.drawable.play);
                        if(mTimerRunningWhite || mTimerRunningBlack){
                            mCountDownTimerWhite.cancel();
                            mCountDownTimerBlack.cancel();
                        }
                        mTimerRunningWhite = false;
                        mTimerRunningBlack = false;
                        updateBlackTimer();
                        updateWhiteTimer();
                    }
                });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                resumeTimers();
            }
        });
        builder.create().show();
    }

    private void pauseTimers(){
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
    }

    private void resumeTimers(){
        if (mWhiteLastToPlay) {
            startWhiteTimer();
        } else {
            startBlackTimer();
        }
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
