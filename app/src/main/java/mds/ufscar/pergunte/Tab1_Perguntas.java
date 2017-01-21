package mds.ufscar.pergunte;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

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

        // TODO: Marcelo, sua mágica aqui
//        try {
//            String perfil_usuario = (mProfessor) ?
//                    RequisicaoAssincrona.Parametros.PERFIL_PROFESSOR : RequisicaoAssincrona.Parametros.PERFIL_ALUNO;
//
//              JSONObject resultado_requisicao = requisicao.execute(RequisicaoAssincrona.BUSCAR_MATERIAS,
//                    emailUsuarioAtual, perfil_usuario,
//                    RequisicaoAssincrona.Parametros.STATUS_MATERIA_ATIVA).get();
//
//            if (resultado_requisicao != null) {
//                if (resultado_requisicao.getString("status").equals("ok")) {
//                    JSONArray materias_json = resultado_requisicao.getJSONArray("materias");
//
//                    ArrayList<Materia> materias = new ArrayList<>();
//                    for (int i = 0; i < materias_json.length(); i++) {
//                        Materia materia = new Materia();
//                        if (materia.construirObjetoComJSON(materias_json.getJSONObject(i))) {
//                            materias.add(materia);
//                        } else {
//                            Toast.makeText(Tab2_Materias.this.getActivity(),
//                                    "Erro ao carregar matéria.", Toast.LENGTH_LONG).show();
//                        }
//                    }
//                    mListItems = addSections(materias);
//                } else {
//                    Log.w("REQUISICAO", resultado_requisicao.toString());
//                    Toast.makeText(Tab2_Materias.this.getActivity(),
//                            resultado_requisicao.getString("descricao"), Toast.LENGTH_LONG).show();
//                }
//            } else {
//                AlertDialog.Builder adb = new AlertDialog.Builder(Tab1_Perguntas.this.getActivity());
//                adb.setTitle("Erro");
//                adb.setMessage("Não foi possível conectar à Internet.\n\nVerifique sua conexão e tente novamente.");
//                adb.setPositiveButton("Tentar novamente", new AlertDialog.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        android.os.Process.killProcess(android.os.Process.myPid());
//                        System.exit(0);
//                    }
//                });
//                adb.show();
//            }
//
//        } catch (InterruptedException | ExecutionException | JSONException e) {
//            e.printStackTrace();
//        }

        adapter = new PerguntaAdapter(getActivity(), mListItems);
        mListView.setAdapter(adapter);

        return rootView;
    }
}
