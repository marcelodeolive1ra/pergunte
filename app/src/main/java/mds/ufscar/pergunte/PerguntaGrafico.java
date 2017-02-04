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

         /*  GRAFICO */
        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.setTitle("Respostas");
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                //new DataPoint(X, Y) -> X: 1=A, 2=B, 3=C, 4=D, 5=E
                //new DataPoint(X, Y) -> Y: value
                //TODO colocar valores certos das respostas
                new DataPoint(1, 1),
                new DataPoint(2, 5),
                new DataPoint(3, 3),
                new DataPoint(4, 2),
                new DataPoint(5, 6)
        });
        graph.addSeries(series);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(series.getHighestValueX()+1);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(series.getHighestValueY()+0.5);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        //coloquei esses espaços no começo e no fim para ele n ficar "colado" nas bordas
        staticLabelsFormatter.setHorizontalLabels(new String[] {" ","A", "B", "C","D","E"," "});
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
