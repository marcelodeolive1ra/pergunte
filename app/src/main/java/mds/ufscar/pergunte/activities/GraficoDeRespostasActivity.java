package mds.ufscar.pergunte.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import mds.ufscar.pergunte.R;
import mds.ufscar.pergunte.helpers.RequisicaoAssincrona;
import mds.ufscar.pergunte.models.Pergunta;

/**
 * Created by Danilo on 21/01/2017.
 */

public class GraficoDeRespostasActivity extends AppCompatActivity {

    private Toolbar mPerguntaGraficoToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.grafico_de_respostas_activity);

        mPerguntaGraficoToolbar = (Toolbar)findViewById(R.id.pergunta_grafico_toolbar);
        mPerguntaGraficoToolbar.setTitle("Respostas obtidas");
        mPerguntaGraficoToolbar.setTitleTextColor(getResources().getColor(R.color.white));

        setSupportActionBar(mPerguntaGraficoToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPerguntaGraficoToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // pegando dados
        Intent intent = getIntent();
        Pergunta pergunta = intent.getParcelableExtra("pergunta");

        DataPoint[] dataPoint = null;

        RequisicaoAssincrona requisicao = new RequisicaoAssincrona();

        int quantidade_alternativas = 0;
        String letras[] = {"A", "B", "C", "D", "E"};

        try {

            JSONObject resultado_requisicao = requisicao.execute(RequisicaoAssincrona.BUSCAR_RESPOSTAS_POR_PERGUNTA,
                    Integer.toString(pergunta.getCodigo())).get();

            if (resultado_requisicao != null) {
                if (resultado_requisicao.getString("status").equals("ok")) {
                    quantidade_alternativas = resultado_requisicao.getInt("quantidade_alternativas");
                    dataPoint = new DataPoint[quantidade_alternativas];

                    for (int i = 0; i < quantidade_alternativas; i++) {
                        dataPoint[i] = new DataPoint(i+1, resultado_requisicao.getInt(letras[i]));
                    }
                } else {
                    Log.w("REQUISICAO", resultado_requisicao.toString());
                    Toast.makeText(GraficoDeRespostasActivity.this,
                            resultado_requisicao.getString("descricao"), Toast.LENGTH_LONG).show();
                }
            } else {
                AlertDialog.Builder adb = new AlertDialog.Builder(GraficoDeRespostasActivity.this);
                adb.setTitle("Erro");
                adb.setMessage("Não foi possível conectar à Internet.\n\nVerifique sua conexão e tente novamente.");
                adb.setPositiveButton("Tentar novamente", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(0);
                    }
                });
                adb.show();
            }

        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }

        if (dataPoint != null) {

         /*  GRAFICO */
            GraphView graph = (GraphView) findViewById(R.id.graph);
            graph.setTitle(pergunta.getTextoPergunta());
            BarGraphSeries<DataPoint> series = new BarGraphSeries<>(dataPoint);
            graph.addSeries(series);
            graph.getViewport().setMinX(0);
            graph.getViewport().setMaxX(series.getHighestValueX()+1);
            graph.getViewport().setMinY(0);
            graph.getViewport().setMaxY(series.getHighestValueY() + 0.5);
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setXAxisBoundsManual(true);

            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
            //coloquei esses espaços no começo e no fim para ele n ficar "colado" nas bordas
            System.out.println("*********************************"+quantidade_alternativas);
            String[] respostas = new String[quantidade_alternativas+2];
            respostas[0] = " ";


            for (int i = 0; i < quantidade_alternativas; i++) {
                respostas[i+1] = letras[i];
            }
            respostas[quantidade_alternativas+1] = " ";

            for (int i = 0; i <= quantidade_alternativas+1; i++) {
                System.out.println("*********************************"+ i +" "+ respostas[i] );
            }

            //staticLabelsFormatter.setHorizontalLabels(new String[] {" ","A", "B", "C","D","E"," "});
            staticLabelsFormatter.setHorizontalLabels(respostas);
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

            // styling
            series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
                @Override
                public int get(DataPoint data) {
                    return Color.rgb((50 + (int) data.getX() * 25) % 255, 100, 100);
                }
            });

            series.setSpacing(10);

            // draw values on top
            series.setDrawValuesOnTop(true);
            series.setValuesOnTopColor(Color.RED);

        /*  GRAFICO */
        } else {
            Toast.makeText(this, "Ainda não exite resposta para essa pergunta", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
