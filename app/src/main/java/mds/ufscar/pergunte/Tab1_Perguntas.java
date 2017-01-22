package mds.ufscar.pergunte;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import mds.ufscar.pergunte.model.Materia;
import mds.ufscar.pergunte.model.Pergunta;

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
        adapter = new PerguntaAdapter(getActivity(), mListItems);
        mListView.setAdapter(adapter);

        buscaPerguntasServidor();

        return rootView;
    }

    public void buscaPerguntasServidor(){
        mListItems.clear();

        RequisicaoAssincrona requisicao = new RequisicaoAssincrona();

        try {
            JSONObject resultado_requisicao = requisicao.execute(RequisicaoAssincrona.BUSCAR_PERGUNTAS_POR_PROFESSOR,
                    MainScreen.getEmailDoUsuarioAtual()).get();

            if (resultado_requisicao != null) {
                if (resultado_requisicao.getString("status").equals("ok")) {
                    JSONArray materias_json = resultado_requisicao.getJSONArray("materias");

                    for (int i = 0; i < materias_json.length(); i++) {
                        Materia materia = new Materia();
                        materia.construirObjetoComJSONSemProfessor(materias_json.getJSONObject(i).getJSONObject("materia"));

                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(materia.getNomeDisciplina()).append("   -   ");
                        stringBuilder.append(materia.getAno()).append("/");
                        stringBuilder.append(materia.getSemestre()).append("   -   ");
                        stringBuilder.append(" Turma: ").append(materia.getTurma());
                        mListItems.add(new Section(stringBuilder.toString()));

                        JSONArray perguntas_json = materias_json.getJSONObject(i).getJSONObject("materia").getJSONArray("perguntas");

                        for (int j = 0; j < perguntas_json.length(); j++) {
                            Pergunta pergunta = new Pergunta();
                            if (pergunta.construirObjetoComJSON(perguntas_json.getJSONObject(j))) {
                                mListItems.add(pergunta);
                            }
                        }
                    }
                } else {
                    Log.w("REQUISICAO", resultado_requisicao.toString());
                    Toast.makeText(Tab1_Perguntas.this.getActivity(),
                            resultado_requisicao.getString("descricao"), Toast.LENGTH_LONG).show();
                }
            } else {
                AlertDialog.Builder adb = new AlertDialog.Builder(Tab1_Perguntas.this.getActivity());
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

        adapter.notifyDataSetChanged();
    }
}
