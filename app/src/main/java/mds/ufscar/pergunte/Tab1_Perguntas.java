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
import mds.ufscar.pergunte.model.Pergunta;
import mds.ufscar.pergunte.model.Professor;

/**
 * Created by Danilo on 24/12/2016.
 */

public class Tab1_Perguntas extends Fragment {

    private ListView mListView;
    private boolean mProfessor;
    private ArrayList<ListItem> mListItems;
    private PerguntaAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1_perguntas, container, false);

        mListView = (ListView) rootView.findViewById(R.id.pergunta_list_view);
        mProfessor = ((MainScreen)this.getActivity()).isProfessor();
        String emailUsuarioAtual = MainScreen.getEmailDoUsuarioAtual();
        mListItems =  new ArrayList<>();

        RequisicaoAssincrona requisicao = new RequisicaoAssincrona();

        try {
            JSONObject resultado_requisicao = requisicao.execute(RequisicaoAssincrona.BUSCAR_PERGUNTAS_POR_PROFESSOR,
                    MainScreen.getEmailDoUsuarioAtual()).get();

            JSONArray materias_json = resultado_requisicao.getJSONArray("materias");

            for (int i = 0; i < materias_json.length(); i++) {
                Materia materia = new Materia();
                materia.construirObjetoComJSONSemProfessor(materias_json.getJSONObject(i).getJSONObject("materia"));

                JSONArray perguntas_json = materias_json.getJSONObject(i).getJSONObject("materia").getJSONArray("perguntas");

                for (int j = 0; j < perguntas_json.length(); j++) {
                    Pergunta pergunta = new Pergunta();
                    if (pergunta.construirObjetoComJSON(perguntas_json.getJSONObject(j))) {
                        mListItems.add(pergunta);
                    }
                }
            }
        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }

        adapter = new PerguntaAdapter(getActivity(), mListItems);
        mListView.setAdapter(adapter);

        return rootView;
    }
}
