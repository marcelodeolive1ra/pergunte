package mds.ufscar.pergunte;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import mds.ufscar.pergunte.model.Materia;
import mds.ufscar.pergunte.model.Professor;

/**
 * Created by Danilo on 24/12/2016.
 */

public class Tab2_Materias extends Fragment {

    private ListView mListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2_Materias, container, false);

        mListView = (ListView) rootView.findViewById(R.id.materia_list_view);

        // Depois pegar as materias de um Json ou algo do tipo
        final ArrayList<Materia> materias = new ArrayList<>();
        // Criando mock data
        Professor helio = new Professor("Helio", "Guardia", "helio@dc.ufscar.br", "UFSCar Sao Carlos");
        Professor marilde = new Professor("Marilde", "Dos Santos", "marilde@dc.ufscar.br", "UFSCar Sao Carlos");
        Materia so2 = new Materia('A', 2016, 1, "Sistemas Operacionais 2", helio);
        materias.add(so2);
        Materia sistemasDistribuidos = new Materia('B', 2016, 2, "Sistemas Distribuídos", helio);
        materias.add(sistemasDistribuidos);
        Materia labbd = new Materia('C', 2015, 2, "Laboratório de Banco de Dados", marilde);
        materias.add(labbd);
        // Populando lista com adapter customizado
        MateriaAdapter adapter = new MateriaAdapter(getActivity(), materias);
        mListView.setAdapter(adapter);
        return rootView;
    }
}
