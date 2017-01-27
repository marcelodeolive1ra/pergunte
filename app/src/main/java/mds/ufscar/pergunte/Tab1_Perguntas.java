package mds.ufscar.pergunte;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import mds.ufscar.pergunte.model.Alternativa;
import mds.ufscar.pergunte.model.Materia;
import mds.ufscar.pergunte.model.Pergunta;

/**
 * Created by Danilo on 24/12/2016.
 */

public class Tab1_Perguntas extends Fragment {

    private ListView mListView;
    private boolean mProfessor;
    private ArrayList<ListItem> mListItems;
    private ArrayList<Materia> mMaterias;
    private PerguntaAdapter adapter;

    public static final String visualizarPergunta = "Visualizar pergunta";
    public static final String visualizarGrafico = "Visualizar gráfico";
    public static final String disponibilizarPergunta = "Disponibilizar pergunta";
    public static final String editarPergunta = "Editar pergunta";
    public static final String excluirPergunta = "Excluir pergunta";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1_perguntas, container, false);

        mListView = (ListView) rootView.findViewById(R.id.pergunta_list_view);
        mProfessor = ((MainScreen)this.getActivity()).isProfessor();
        mListItems = new ArrayList<>();
        mMaterias = new ArrayList<>();
        adapter = new PerguntaAdapter(getActivity(), mListItems);
        mListView.setAdapter(adapter);

        if (mProfessor) {
            buscaPerguntasServidor();

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
                    final int posicao = pos;
                    if (mProfessor) {
                        final CharSequence opcoes[] = new CharSequence[]{
                                visualizarPergunta,
                                visualizarGrafico,
                                disponibilizarPergunta,
                                editarPergunta,
                                excluirPergunta
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(Tab1_Perguntas.this.getActivity());
                        builder.setTitle("Selecione uma opção");
                        builder.setItems(opcoes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (opcoes[which].toString().equals(visualizarGrafico)) {
                                    Intent perguntaGrafico = new Intent(Tab1_Perguntas.this.getActivity(), PerguntaGrafico.class);
                                    // vai precisar passar a materia tambem? Espero que não. haha
                                    perguntaGrafico.putExtra("pergunta", (Pergunta) mListItems.get(posicao));
                                    ArrayList<Alternativa> alternativas = ((Pergunta) mListItems.get(posicao)).getAlternativas();
                                    perguntaGrafico.putParcelableArrayListExtra("alternativas", alternativas);
                                    getActivity().startActivity(perguntaGrafico);
                                } else if (opcoes[which].toString().equals(disponibilizarPergunta)) {
                                    Intent perguntaDisponivel = new Intent(Tab1_Perguntas.this.getActivity(), PerguntaDisponivel.class);
                                    perguntaDisponivel.putExtra("pergunta", (Pergunta) mListItems.get(posicao));
                                    ArrayList<Alternativa> alternativas = ((Pergunta) mListItems.get(posicao)).getAlternativas();
                                    perguntaDisponivel.putParcelableArrayListExtra("alternativas", alternativas);
                                    getActivity().startActivity(perguntaDisponivel);
                                }
                            }
                        });
                        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //the user clicked on Cancel
                            }
                        });
                        builder.show();
                    }
                }
            });
        } else {
            // TODO: Implementar Tab1 para o perfil Aluno
        }

        return rootView;
    }

    public void buscaPerguntasServidor(){
        mListItems.clear();
        mMaterias.clear();

        RequisicaoAssincrona requisicao = new RequisicaoAssincrona();

        try {
            JSONObject resultado_requisicao = requisicao.execute(RequisicaoAssincrona.BUSCAR_PERGUNTAS_POR_PROFESSOR,
                    MainScreen.getEmailDoUsuarioAtual()).get();

            if (resultado_requisicao != null) {
                if (resultado_requisicao.getString("status").equals("ok")) {

                    JSONArray materias_json = resultado_requisicao.getJSONArray("materias");

                    if (materias_json.length() > 0) {

                        for (int i = 0; i < materias_json.length(); i++) {
                            Materia materia = new Materia(materias_json.getJSONObject(i));

                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(materia.getNomeDisciplina()).append("   -   ");
                            stringBuilder.append(materia.getAno()).append("/");
                            stringBuilder.append(materia.getSemestre()).append("   -   ");
                            stringBuilder.append(" Turma: ").append(materia.getTurma());
                            mListItems.add(new Section(stringBuilder.toString()));

                            JSONArray perguntas_json = materias_json.getJSONObject(i).getJSONArray("perguntas");

                            boolean temPergunta = false;
                            ArrayList<Pergunta> perguntas = new ArrayList<>();
                            for (int j = 0; j < perguntas_json.length(); j++) {
                                mListItems.add(new Pergunta(perguntas_json.getJSONObject(j)));
                                temPergunta = true;
                                perguntas.add((Pergunta) mListItems.get(mListItems.size() - 1));
                            }
                            if (!temPergunta) {
                                mListItems.remove(mListItems.size() - 1); // remove ultima seção (materia)
                            }
                            materia.setPerguntas(perguntas);
                            mMaterias.add(materia);
                        }
                    } else {
                        Toast.makeText(Tab1_Perguntas.this.getActivity(),
                                "Você ainda não cadastrou nenhuma pergunta.", Toast.LENGTH_LONG).show();
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
