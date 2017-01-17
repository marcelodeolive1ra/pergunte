package mds.ufscar.pergunte;

import android.graphics.Color;
import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

/**
 * Created by Danilo on 24/12/2016.
 */

public class Tab1_Respondidas extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1_respondidas, container, false);



        /*  GRAFICO */
        GraphView graph = (GraphView) rootView.findViewById(R.id.graph);
        graph.setTitle("Respostas");
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                //new DataPoint(X, Y) -> X: 1=A, 2=B, 3=C, 4=D, 5=E
                //new DataPoint(X, Y) -> Y: value
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

        return rootView;


    }
}
