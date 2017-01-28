package mds.ufscar.pergunte;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import mds.ufscar.pergunte.model.Alternativa;
import mds.ufscar.pergunte.model.Pergunta;

/**
 * Created by Danilo on 24/01/2017.
 */

public class PerguntaDisponivel extends AppCompatActivity {

    private TextView mNRespostas;
    private TextView mPergunta;
    private TextView mTimerUI;
    private CountDownTimer mTimerCountUp;
    private Button mEncerrar;
    Pergunta pergunta;
    int total_de_respostas = 0;
    final Handler handler = new Handler();
    // Define the code block to be executed
    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            Intent intent = getIntent();
            Pergunta pergunta = intent.getParcelableExtra("pergunta");
            ArrayList<Alternativa> alternativas = intent.getParcelableArrayListExtra("alternativas");
            pergunta.setAlternativas(alternativas);

            RequisicaoAssincrona requisicao = new RequisicaoAssincrona();

            try {
                JSONObject resultado_requisicao = requisicao.execute(RequisicaoAssincrona.BUSCAR_QUANTIDADE_DE_RESPOSTAS_TOTAIS_POR_PERGUNTA,
                        Integer.toString(pergunta.getCodigo())).get();

                // Apenas para testar se o update está funcionando:
                total_de_respostas++;
                mNRespostas.setText(Integer.toString(total_de_respostas));

                // Código real:
                String quantidade_respostas_total = resultado_requisicao.getString("quantidade_respostas_total");
//                mNRespostas.setText(quantidade_respostas_total);

            } catch (InterruptedException | ExecutionException | JSONException e) {
                e.printStackTrace();
            }

            Log.d("Handlers", "Called on main thread");

            // Repeat this the same runnable code block again another 2 seconds
            handler.postDelayed(runnableCode, 2000);
        }
    };


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
        this.pergunta = intent.getParcelableExtra("pergunta");
        ArrayList<Alternativa> alternativas = intent.getParcelableArrayListExtra("alternativas");
        this.pergunta.setAlternativas(alternativas);

        mPergunta.setText(this.pergunta.getTextoPergunta());

        // setting timer count up
        mTimerCountUp = setTimer();
        mTimerCountUp.start();

        // TODO: send notification to students

        // Inicia a execução do código em background (atualizar a quantidade de respostas obtidas)
        handler.post(runnableCode);

        mEncerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: go where from here?
                handler.removeCallbacks(runnableCode);

                // Terminou o tempo de respostas da pergunta, atualiza a quantidade de respostas obtidas em cada alternativa
                RequisicaoAssincrona requisicao = new RequisicaoAssincrona();

                // TODO: Tratar falta de conexão à Internet
                try {
                    JSONObject resultado_requisicao = requisicao.execute(
                            RequisicaoAssincrona.BUSCAR_QUANTIDADE_DE_RESPOSTAS_POR_ALTERNATIVA_POR_PERGUNTA,
                            Integer.toString(pergunta.getCodigo())).get();

                    JSONArray alternativas_com_respostas_json = resultado_requisicao.getJSONArray("quantidade_respostas");

                    for (int i = 0; i < pergunta.getAlternativas().size(); i++) {
                        pergunta.getAlternativas().get(i).setnRespostas(
                                alternativas_com_respostas_json.getJSONObject(i).getInt("quantidade_respostas"));
                    }

                } catch (InterruptedException | ExecutionException | JSONException e) {
                    e.printStackTrace();
                }

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
