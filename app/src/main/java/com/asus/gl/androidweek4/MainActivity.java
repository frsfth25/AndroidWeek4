package com.asus.gl.androidweek4;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mp=null;
    private EditText edt_word;
    private TextView txt_mean;

    private HashMap<String, String> dict=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edt_word = findViewById(R.id.edt_theWord);
        txt_mean =findViewById(R.id.txtMean);

    }

    private void stopSong(){
        if(mp!=null){
            mp.stop();
            mp.release();
            mp=null;
        }
    }

    public void playSound(View view) {

        int id = view.getId();
        stopSong();

        if(id == R.id.sound_horn){
            mp = MediaPlayer.create(this,R.raw.horn);
            mp.start();
        }
        else if(id == R.id.sound_start){
            mp = MediaPlayer.create(this,R.raw.startvehicle);
            mp.start();
        }
    }

    public void searchForWord(View view) {

        String the_user_word = edt_word.getText().toString();
        if(the_user_word.equals("")){
            Toast.makeText(this, "You have enter a word inside edittext!", Toast.LENGTH_SHORT).show();
            return;
        }

        Scanner theFile = new Scanner(getResources().openRawResource(R.raw.my_dict));
        while(theFile.hasNext()){
            String line = theFile.nextLine();
            String[] splittedWords = line.split("-");
            String word  =splittedWords[0];
            String mean = splittedWords[1];

            if(word.toLowerCase().equals(the_user_word.toLowerCase())){
                txt_mean.setText(mean);
                theFile.close();
                return;
            }

        }
        theFile.close();
        txt_mean.setText("The word not exist in the dictionary");

    }
}
