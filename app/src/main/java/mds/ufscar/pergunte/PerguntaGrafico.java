package mds.ufscar.pergunte;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import org.json.JSONObject;

import java.util.ArrayList;

import mds.ufscar.pergunte.model.Alternativa;
import mds.ufscar.pergunte.model.Pergunta;

/**
 * Created by Danilo on 21/01/2017.
 */

public class PerguntaGrafico extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pergunta_grafico);

        // pegando dados
        Intent intent = getIntent();
        Pergunta pergunta = intent.getParcelableExtra("pergunta");

        ArrayList<Alternativa> alternativas = intent.getParcelableArrayListExtra("alternativas");
        pergunta.setAlternativas(alternativas);
        System.out.println(pergunta.getCodigo());
        System.out.println(pergunta.getTextoPergunta());
        DataPoint[] dataPoint = new DataPoint[alternativas.size()];

        for(int i =0;i<alternativas.size();i++){
            System.out.println(alternativas.get(i).getnRespostas());
            dataPoint[i] = new DataPoint(i,alternativas.get(i).getnRespostas());

        }



         /*  GRAFICO */
        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.setTitle(pergunta.getTextoPergunta());
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(dataPoint);
        graph.addSeries(series);
        graph.getViewport().setMinX(0);
        //graph.getViewport().setMaxX(series.getHighestValueX()+1);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(series.getHighestValueY()+0.5);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        //coloquei esses espaços no começo e no fim para ele n ficar "colado" nas bordas
        String[] respostas =  new String[alternativas.size()];
        for(int i =0;i<alternativas.size();i++){
            respostas[i] = new String(alternativas.get(i).getLetra());
        }
        //staticLabelsFormatter.setHorizontalLabels(new String[] {" ","A", "B", "C","D","E"," "});
        staticLabelsFormatter.setHorizontalLabels(respostas);
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        // styling
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((50+(int) data.getX()*25)%255, 100, 100);
            }
        });

        series.setSpacing(20);

        // draw values on top
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);

        /*  GRAFICO */

    }
}
