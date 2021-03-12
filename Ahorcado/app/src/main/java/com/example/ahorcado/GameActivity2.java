package com.example.ahorcado;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

public class GameActivity2 extends AppCompatActivity {
    public MediaPlayer ring;
    public MediaPlayer ring1;
    public MediaPlayer ring2;

    private String[] words;
    private Random random;
    private String currWord;
    private TextView[] charViews;
    private LinearLayout wordLayout;
    private LetterAdapter adapter;
    private GridView gridView;
    private int numCorr;
    private int numChars;
    private ImageView[]parts;
    private int sizeParts=6;
    private int currPart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2);
        words=getResources().getStringArray(R.array.words);
        wordLayout=findViewById(R.id.words);
        gridView=findViewById(R.id.letters);
        random=new Random();

        parts=new ImageView[sizeParts];
        parts[0]=findViewById(R.id.head);
        parts[1]=findViewById(R.id.body);
        parts[2]=findViewById(R.id.armLeft);
        parts[3]=findViewById(R.id.armRight);
        parts[4]=findViewById(R.id.legLeft);
        parts[5]=findViewById(R.id.legRight);

        playGame();
    }

    private void playGame(){
        ring=MediaPlayer.create(GameActivity2.this,R.raw.metal);
        ring.start();

        String newWord=words[random.nextInt(words.length)];
        while(newWord.equals(currWord)) newWord=words[random.nextInt(words.length)];

        currWord=newWord;
        charViews=new TextView[currWord.length()];
        wordLayout.removeAllViews();
        for(int i=0; i<currWord.length(); i++){
            charViews[i]=new TextView(this);
            charViews[i].setText(""+currWord.charAt(i));
            charViews[i].setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            charViews[i].setGravity(Gravity.CENTER);
            charViews[i].setTextColor(Color.WHITE);
            charViews[i].setBackgroundResource(R.drawable.letter_bg);
            wordLayout.addView(charViews[i]);
        }
        adapter=new LetterAdapter(this);
        gridView.setAdapter(adapter);
        numCorr=0;
        currPart=0;
        numChars=currWord.length();

        for(int i=0; i<sizeParts; i++){
            parts[i].setVisibility(View.INVISIBLE);
        }
    }

    public void letterPressed(View view){
        String letter=((TextView)view).getText().toString();
        char letterChar=letter.charAt(0);
        view.setEnabled(false);
        boolean correct=false;

        for(int i=0; i<currWord.length();i++){
            if(currWord.charAt(i)==letterChar){
                correct=true;
                numCorr++;
                charViews[i].setTextColor(Color.BLACK);
            }
        }
        if(correct){
            if(numCorr==numChars){
                ring.stop();
                ring1=MediaPlayer.create(GameActivity2.this,R.raw.stageclear);
                ring1.start();
                disableButtons();
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle("GANASTE");
                builder.setMessage("FELICIDADES!\n\n LA RESPUESTA ERA \n\n" +currWord);
                builder.setPositiveButton("JUGAR OTRA VEZ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GameActivity2.this.playGame();
                    }
                });

                builder.setNegativeButton("SALIR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GameActivity2.this.finish();
                    }
                });
                builder.show();
            }
        }else if(currPart<sizeParts){
            parts[currPart].setVisibility(View.VISIBLE);
            currPart++;
        }else{
            disableButtons();
            ring.stop();
            ring2=MediaPlayer.create(GameActivity2.this,R.raw.gameover);
            ring2.start();
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("PERDISTE");
            builder.setMessage("TU PERDISTE!\n\n LA RESPUESTA ERA \n\n" +currWord);
            builder.setPositiveButton("JUGAR OTRA VEZ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    GameActivity2.this.playGame();
                }
            });

            builder.setNegativeButton("SALIR", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    GameActivity2.this.finish();
                }
            });
            builder.show();
        }
    }
    public void disableButtons(){
        for(int i=0;i<gridView.getChildCount();i++){
            gridView.getChildAt(i).setEnabled(false);
        }
    }


}