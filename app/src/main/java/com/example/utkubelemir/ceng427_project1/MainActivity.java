package com.example.utkubelemir.ceng427_project1;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Button leftNumberBtn;
    Button rightNumberBtn;
    Button startButton;
    int currentLevel = 1;
    int leftNumber;
    int rightNumber;
    int streak = 0;
    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPref  = getPreferences(Context.MODE_PRIVATE);
        leftNumberBtn = (Button) findViewById(R.id.leftNumberBtn);
        rightNumberBtn = (Button) findViewById(R.id.rightNumberBtn);
        startButton = (Button) findViewById(R.id.startBtn);
        leftNumberBtn.setOnClickListener(buttonListeners("LEFT"));
        rightNumberBtn.setOnClickListener(buttonListeners("RIGHT"));
        startButton.setOnClickListener(buttonListeners("START"));
        initializeHighScores();
    }

    public View.OnClickListener buttonListeners(final String buttonType) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                switch (buttonType) {
                    case "START":
                        initializeGame();
                        break;
                    case "LEFT":
                    case "RIGHT":
                        checkAnswer(buttonType);
                        break;
                    default:
                        break;
                }
                setCurrentScores();
            }
        };
    }
    public void setCurrentScores(){
        TextView currentLevelText = (TextView) findViewById(R.id.currentLeveText);
        TextView curentPointText = (TextView) findViewById(R.id.currentPointText);
        currentLevelText.setText(Integer.toString(currentLevel));
        curentPointText.setText(Integer.toString(streak));
    }

    public void initializeHighScores(){
        TextView highestLevel = (TextView) findViewById(R.id.highLevelText);
        TextView highestPoint = (TextView) findViewById(R.id.highPointText);
        highestLevel.setText(Integer.toString(sharedPref.getInt("highestLevel",0)));
        highestPoint.setText(Integer.toString(sharedPref.getInt("highestPoint",0)));
    }
    public void setHighScores(){
        if(currentLevel > sharedPref.getInt("highestLevel",0)){
            sharedPref.edit().putInt("highestLevel",currentLevel).apply();
        }
        if(streak > sharedPref.getInt("highestPoint",0)){
            sharedPref.edit().putInt("highestPoint",streak).apply();
        }
    }

    public void setRandomNumbers() {
        switch (currentLevel) {
            case 1:
            case 2:
            case 3:
            case 4:
                leftNumber = generateRandomNumber(10 * currentLevel, false);
                rightNumber = generateRandomNumber(10 * currentLevel, false);
                while (leftNumber == rightNumber) {
                    rightNumber = generateRandomNumber(10 * currentLevel, false);
                }
                break;
            case 5:
                leftNumber = generateRandomNumber(40, true);
                rightNumber = generateRandomNumber(40, true);
                while (leftNumber == rightNumber) {
                    rightNumber = generateRandomNumber(40, true);
                }
                break;
            case 6:
                leftNumber = generateRandomNumber(60, true);
                rightNumber = generateRandomNumber(60, true);
                while (leftNumber == rightNumber) {
                    rightNumber = generateRandomNumber(60, true);
                }
                break;
            case 7:
            case 8:
            case 9:
            case 10:
                leftNumber = generateRandomNumber(100, true);
                rightNumber = generateRandomNumber(100, true);
                while (leftNumber == rightNumber) {
                    rightNumber = generateRandomNumber(100, true);
                }
                break;
            default:
                break;
        }
    }

    public void initializeGame() {
        currentLevel = 1;
        streak = 0;
        initializeHighScores();
        LinearLayout numberButtonCnt = (LinearLayout) findViewById(R.id.numberButtonCnt);
        numberButtonCnt.setVisibility(View.VISIBLE);
        LinearLayout startButtonCnt = (LinearLayout) findViewById(R.id.startButtonCnt);
        startButtonCnt.setVisibility(View.GONE);
        setButtonTexts();
    }

    public void gameOver() {
        setHighScores();
        initializeHighScores();
        TextView gameStatusText = (TextView) findViewById(R.id.gameStatusText);
        if(currentLevel == 11 && streak%5 == 0){
            gameStatusText.setText("Congratulations... You won");
        }else{
            gameStatusText.setText("You lost the game");
        }
        LinearLayout numberButtonCnt = (LinearLayout) findViewById(R.id.numberButtonCnt);
        numberButtonCnt.setVisibility(View.GONE);
        LinearLayout startButtonCnt = (LinearLayout) findViewById(R.id.startButtonCnt);
        startButtonCnt.setVisibility(View.VISIBLE);
    }

    public void checkAnswer(String buttonType) {
        Log.i("CHECKK", "Button Typeee" + buttonType);
        boolean isCorrect = false;
        switch (buttonType) {
            case "RIGHT":
                if (rightNumber > leftNumber) {
                    isCorrect = true;
                }
                break;
            case "LEFT":
                if (leftNumber > rightNumber) {
                    isCorrect = true;
                }
                break;
            default:
                break;
        }
        if (isCorrect) {
            streak += 1;
            if (streak%5 == 0) {
                currentLevel += 1;
                Log.i("CORRECT", "Streak : " + streak + " Current Level" + currentLevel);
            }
            if (currentLevel == 11 && streak%5 == 0) {
                gameOver();
            } else {
                setButtonTexts();
            }
        } else {
            gameOver();
        }
    }

    public void setButtonTexts() {
        setRandomNumbers();
        leftNumberBtn.setText(Integer.toString(leftNumber));
        rightNumberBtn.setText(Integer.toString(rightNumber));
    }

    public int generateRandomNumber(int bound, boolean includeNegatives) {
        Random rnd = new Random();
        int tempRandom = rnd.nextInt(bound + 1);
        if (!includeNegatives) {
            return tempRandom;
        }
        if (rnd.nextInt(2) == 1) {
            tempRandom = tempRandom * -1;
        }
        return tempRandom;
    }
}
