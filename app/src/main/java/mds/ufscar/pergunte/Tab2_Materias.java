package mds.ufscar.pergunte;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
    private boolean mProfessor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2_materia, container, false);

        mListView = (ListView) rootView.findViewById(R.id.materia_list_view);
        mProfessor = ((MainScreen)this.getActivity()).isProfessor();

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

        final MateriaAdapter adapter = new MateriaAdapter(getActivity(), materias);
        mListView.setAdapter(adapter);

        // setting one click on an item of the list view
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3)
            {
                if (mProfessor) {
                    Intent cadastroPergunta = new Intent(Tab2_Materias.this.getActivity(), CadastroPergunta.class);
                    startActivity(cadastroPergunta);
                }
            }
        });

        // setting option to remove an item for a long press
        mListView.setLongClickable(true);
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                // removing from the interface:
                AlertDialog.Builder adb = new AlertDialog.Builder(Tab2_Materias.this.getActivity());
                adb.setTitle("Remover?");
                if (mProfessor)
                    adb.setMessage("Tem certeza que deseja apagar " + materias.get(pos).getNomeDisciplina());
                else
                    adb.setMessage("Tem certeza que deseja sair de " + materias.get(pos).getNomeDisciplina());
                final int positionToRemove = pos;
                adb.setNegativeButton("Cancelar", null);
                adb.setPositiveButton("Sim", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Materia materiaASerRemovida = materias.remove(positionToRemove);
                        adapter.notifyDataSetChanged();
                        if (mProfessor) {
                            // TODO: Caso Professor - Marcelo desabilite ou remova a matéria (materiaASerRemovida) do BD por favore
                        } else {
                            // TODO: Caso Aluno - Marcelo descadastre o aluno do BD por favore
                        }
                    }});
                adb.show();
                return true;    // true means it won't call another click listener
            }
        });

        return rootView;
    }
}