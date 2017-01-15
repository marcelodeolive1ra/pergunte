package mds.ufscar.pergunte;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

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
        View rootView = inflater.inflate(R.layout.tab2_materia, container, false);

        mListView = (ListView) rootView.findViewById(R.id.materia_list_view);

        final ArrayList<Materia> materias = new ArrayList<>();
        RequisicaoAssincrona requisicao = new RequisicaoAssincrona();

        try {
            String retorno_requisicao = requisicao.execute("buscarmaterias", "marcelodeoliveira@outlook.com", "aluno").get();
            JSONObject retorno_requisicao_json = new JSONObject(retorno_requisicao);
            JSONArray materias_json = retorno_requisicao_json.getJSONArray("materias");
            System.out.println(retorno_requisicao_json);

            for (int i = 0; i < materias_json.length(); i++) {
                JSONObject materia_json = materias_json.getJSONObject(i);
                JSONObject professor_json = materia_json.getJSONObject("professor");

                Professor professor = new Professor(
                        professor_json.getString("nome"),
                        professor_json.getString("sobrenome"),
                        professor_json.getString("email"),
                        professor_json.getString("universidade")
                );

                Materia materia = new Materia(
                        materia_json.getString("turma"),
                        materia_json.getInt("ano"),
                        materia_json.getInt("semestre"),
                        materia_json.getString("nome_materia"),
                        professor,
                        materia_json.getString("codigo_inscricao")
                );

                materias.add(materia);
            }

        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }

        // Criando mock data
        Professor helio = new Professor("Helio", "Guardia", "helio@dc.ufscar.br", "UFSCar Sao Carlos");
        Professor marilde = new Professor("Marilde", "Dos Santos", "marilde@dc.ufscar.br", "UFSCar Sao Carlos");
        Materia so2 = new Materia("A", 2016, 1, "Sistemas Operacionais 2", helio, "SO220161");
        materias.add(so2);
        Materia sistemasDistribuidos = new Materia("B", 2016, 2, "Sistemas Distribuídos", helio, "SD20162");
        materias.add(sistemasDistribuidos);
        Materia labbd = new Materia("C", 2015, 2, "Laboratório de Banco de Dados", marilde, "LBD20152");
        materias.add(labbd);

        // Populando lista com adapter customizado
        MateriaAdapter adapter = new MateriaAdapter(getActivity(), materias);
        mListView.setAdapter(adapter);
        return rootView;
    }
}