package mds.ufscar.pergunte;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import mds.ufscar.pergunte.model.Alternativa;
import mds.ufscar.pergunte.model.Pergunta;

/**
 * Created by Danilo on 24/01/2017.
 */

public class PerguntaDisponivel extends AppCompatActivity {

    private TextView mPergunta;
    private TextView mTimerUI;
    private CountDownTimer mTimerCountUp;
    private TextView mNRespostas;
    private Button mEncerrar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pergunta_disponivel);

        // bounding the interface components
        mPergunta = (TextView) findViewById(R.id.pergunta_content);
        mTimerUI = (TextView) findViewById(R.id.timer_content);
        mNRespostas = (TextView) findViewById(R.id.respostas_numero);
        mEncerrar = (Button) findViewById(R.id.btn_encerrar);

        // pegando dados
        Intent intent = getIntent();
        Pergunta pergunta = intent.getParcelableExtra("pergunta");
        ArrayList<Alternativa> alternativas = intent.getParcelableArrayListExtra("alternativas");
        pergunta.setAlternativas(alternativas);

        mPergunta.setText(pergunta.getTextoPergunta());

        // setting timer count up
        mTimerCountUp = setTimer();
        mTimerCountUp.start();

        // TODO: sent notification to students


        // TODO: receive number of answers in real time

        mEncerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: go where from here?
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
         // Do nothing or:
//        this will put app in background
//        moveTaskToBack(true);
    }

    private CountDownTimer setTimer() {
        final long totalSeconds = 14400; // 4h
        final long intervalSeconds = 1;
        CountDownTimer timer = new CountDownTimer(totalSeconds * 1000, intervalSeconds * 1000) {

            public void onTick(long millisUntilFinished) {
                long time = (totalSeconds * 1000 - millisUntilFinished) / 1000;
                long seconds = time % 60;
                long minutes = (time / 60) % 60;
                long hours = (time / 3600) % 24;
                String timePassed =
                        (""+(100+hours)).substring(1) + ":" +
                                (""+(100+minutes)).substring(1) + ":" +
                                (""+(100+seconds)).substring(1);
                mTimerUI.setText(timePassed);
            }

            public void onFinish() {
                Log.d( "done!", "Time's up!");
            }

        };
        return timer;
    }
}
