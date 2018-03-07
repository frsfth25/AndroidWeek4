package com.asus.gl.androidweek4;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mp=null;
    private EditText edt_word;
    private TextView txt_mean;
    private TextToSpeech tts;
    private boolean isTTSReady =false;

    private final int REQ_CODE_PIC  = 123;

    private HashMap<String, String> dict=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edt_word = findViewById(R.id.edt_theWord);
        txt_mean =findViewById(R.id.txtMean);
        loadData();
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                isTTSReady = true;
            }
        });

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
        if(dict.containsKey(the_user_word.toLowerCase())){
            txt_mean.setText(dict.get(the_user_word));

        }else {
            txt_mean.setText("The word not exist in the dictionary");
        }

    }

    private void loadData()  {
        dict = new HashMap<>();
        try {
            URL uri = new URL("https://raw.githubusercontent.com/SmtCO/AndroidWeek4/master/app/src/main/res/raw/my_dict.txt");
            Scanner scan = new Scanner(uri.openStream());
            if(uri ==null)
                Log.d("File","Uri is null");
           while(scan.hasNext()){
               Log.d("File", scan.nextLine());
           }
        }
        catch (Exception ex){
            Log.e("File", "Error:" +ex.getMessage());

        }
        Scanner theFile = new Scanner(getResources().openRawResource(R.raw.my_dict));
        while(theFile.hasNext()){
            String line = theFile.nextLine();
            String[] splittedWords = line.split("-");
            String word  =splittedWords[0];
            String mean = splittedWords[1];
            dict.put(word.toLowerCase(), mean);

        }
        theFile.close();
    }

    public void speak(View view) {
        Button btn = (Button)view;
        String text = btn.getText().toString();
        if(isTTSReady)
            tts.speak(text,TextToSpeech.QUEUE_FLUSH,null);

        Intent picIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(picIntent,REQ_CODE_PIC);

    }
    @Override
    protected void onActivityResult(int rqCode, int resCode, Intent picIntent){
        if(rqCode == REQ_CODE_PIC && resCode == RESULT_OK){
            Bitmap bmp = (Bitmap) picIntent.getExtras().get("data");
            ImageView img = findViewById(R.id.photo);
            img.setImageBitmap(bmp);
        }
    }
}
