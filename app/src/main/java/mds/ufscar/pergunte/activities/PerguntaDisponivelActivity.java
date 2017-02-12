package mds.ufscar.pergunte.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import mds.ufscar.pergunte.R;
import mds.ufscar.pergunte.helpers.RequisicaoAssincrona;
import mds.ufscar.pergunte.models.Alternativa;
import mds.ufscar.pergunte.models.Pergunta;

/**
 * Created by Danilo on 24/01/2017.
 */

public class PerguntaDisponivelActivity extends AppCompatActivity {

    private TextView mNRespostas;
    private TextView mPergunta;
    private TextView mTimerUI;
    private CountDownTimer mTimerCountUp;
    private Button mEncerrar;
    private Pergunta pergunta;
    private Toolbar mToolbar;


    int total_de_respostas = 0;

    static final int TEMPO_DE_ATUALIZACAO_EM_BACKGROUND = 5000;

    final Handler handler = new Handler();
    // Define the code block to be executed
    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {

            // TODO: Tratar a clássica falta de conexão à Internet aqui
            Intent intent = getIntent();
            Pergunta pergunta = intent.getParcelableExtra("pergunta");
            ArrayList<Alternativa> alternativas = intent.getParcelableArrayListExtra("alternativas");
            pergunta.setAlternativas(alternativas);

            RequisicaoAssincrona requisicao = new RequisicaoAssincrona();

            try {
                JSONObject resultado_requisicao = requisicao.execute(RequisicaoAssincrona.BUSCAR_QUANTIDADE_DE_RESPOSTAS_TOTAIS_POR_PERGUNTA,
                        Integer.toString(pergunta.getCodigo())).get();

                System.out.println(pergunta.getCodigo());

                // Apenas para testar se o update está funcionando:
//                total_de_respostas++;
//                mNRespostas.setText(Integer.toString(total_de_respostas));

                // Código real:
                String quantidade_respostas_total = resultado_requisicao.getString("quantidade_respostas_total");
                mNRespostas.setText(quantidade_respostas_total);

            } catch (InterruptedException | ExecutionException | JSONException e) {
                e.printStackTrace();
            }

            Log.d("Handlers", "Called on main thread");

            // Repeat this the same runnable code block again another 2 seconds
            handler.postDelayed(runnableCode, TEMPO_DE_ATUALIZACAO_EM_BACKGROUND);
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pergunta_disponivel_activity);

        // bounding the interface components
        mPergunta = (TextView) findViewById(R.id.pergunta_content);
        mTimerUI = (TextView) findViewById(R.id.timer_content);
        mNRespostas = (TextView) findViewById(R.id.respostas_numero);
        mEncerrar = (Button) findViewById(R.id.btn_encerrar);

        mToolbar = (Toolbar)findViewById(R.id.pergunta_disponivel_toolbar);
        mToolbar.setTitle("");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));

        // pegando dados
        Intent intent = getIntent();
        this.pergunta = intent.getParcelableExtra("pergunta");
        ArrayList<Alternativa> alternativas = intent.getParcelableArrayListExtra("alternativas");
        this.pergunta.setAlternativas(alternativas);

        mPergunta.setText(this.pergunta.getTextoPergunta());

        RequisicaoAssincrona requisicao = new RequisicaoAssincrona();
        try {
            JSONObject resultado_requisicao = requisicao.execute(
                    RequisicaoAssincrona.DISPONIBILIZAR_PERGUNTA,
                    Integer.toString(pergunta.getCodigo())).get();

            if (resultado_requisicao != null) {
                if (resultado_requisicao.getString("status").equals("ok")) {
                    Toast.makeText(PerguntaDisponivelActivity.this,
                            "Pergunta disponibilizada.", Toast.LENGTH_LONG).show();

                    // setting timer count up
                    mTimerCountUp = setTimer();
                    mTimerCountUp.start();

                } else {
                    Toast.makeText(PerguntaDisponivelActivity.this,
                            resultado_requisicao.getString("error"), Toast.LENGTH_LONG).show();
                    // TODO: tratar melhor esse caso, quando deu erro na disponibilização da pergunta
                }
            } else {
                // TODO: tratar falta de conexão à Internet aqui
            }

        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }

        // TODO: send notification to students

        // Inicia a execução do código em background (atualizar a quantidade de respostas obtidas)
        handler.post(runnableCode);

        mEncerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: go where from here?

                AlertDialog.Builder adb = new AlertDialog.Builder(PerguntaDisponivelActivity.this);
//                adb.setTitle("Encerrar pergunta?");
                adb.setMessage("Encerrar pergunta?");
                adb.setPositiveButton("Encerrar", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        handler.removeCallbacks(runnableCode);

                        RequisicaoAssincrona requisicao = new RequisicaoAssincrona();

                        try {
                            JSONObject resultado_requisicao = requisicao.execute(
                                    RequisicaoAssincrona.FINALIZAR_PERGUNTA, Integer.toString(pergunta.getCodigo())).get();

                            if (resultado_requisicao != null) {
                                if (resultado_requisicao.getString("status").equals("ok")) {

                                    Toast.makeText(PerguntaDisponivelActivity.this,
                                            "Pergunta encerrada.", Toast.LENGTH_LONG).show();

                                    // Terminou o tempo de respostas da pergunta, atualiza a quantidade de respostas obtidas em cada cadastrar_alternativa_dialog
                                    RequisicaoAssincrona requisicao2 = new RequisicaoAssincrona();

                                    try {
                                        JSONObject resultado_requisicao2 = requisicao2.execute(
                                                RequisicaoAssincrona.BUSCAR_QUANTIDADE_DE_RESPOSTAS_POR_ALTERNATIVA_POR_PERGUNTA,
                                                Integer.toString(pergunta.getCodigo())).get();

                                        if (resultado_requisicao2 != null) {

                                            JSONArray alternativas_com_respostas_json = resultado_requisicao2.getJSONArray("quantidade_respostas");

                                            for (int i = 0; i < pergunta.getAlternativas().size(); i++) {
                                                pergunta.getAlternativas().get(i).setnRespostas(
                                                        alternativas_com_respostas_json.getJSONObject(i).getInt("quantidade_respostas"));
                                            }
                                        } else {
                                            // TODO: tratar falta de conexão à Internet aqui também
                                        }

                                    } catch (InterruptedException | ExecutionException | JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    // TODO Tratar erro: EVERYBODY CRIES
                                }

                            } else {
                                // TODO: Tratar falta de conexão à Internet
                            }

                        } catch (InterruptedException | ExecutionException | JSONException e) {
                            e.printStackTrace();
                        }

                        finish();
                    }
                });
                adb.setNegativeButton("Voltar", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                adb.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
         // Do nothing or:
        mEncerrar.callOnClick();
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
