package mds.ufscar.pergunte;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.integration.android.IntentIntegrator;

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
    private ArrayList<Materia> mMaterias;
    private MateriaAdapter adapter;
    private String emailUsuarioAtual;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2_materia, container, false);

        mListView = (ListView) rootView.findViewById(R.id.materia_list_view);
        mProfessor = ((MainScreen)this.getActivity()).isProfessor();
        emailUsuarioAtual = ((MainScreen)this.getActivity()).getEmailDoUsuarioAtual();
        mMaterias =  new ArrayList<>();

        RequisicaoAssincrona requisicao = new RequisicaoAssincrona();

        try {
            JSONObject resultado_requisicao = requisicao.execute("buscarmaterias", emailUsuarioAtual, "aluno").get();
            JSONArray materias_json = resultado_requisicao.getJSONArray("mMaterias");

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
                        materia_json.getInt("codigo"),
                        materia_json.getString("turma"),
                        materia_json.getInt("ano"),
                        materia_json.getInt("semestre"),
                        materia_json.getString("nome_materia"),
                        professor,
                        materia_json.getString("codigo_inscricao")
                );

                mMaterias.add(materia);
            }

        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }

        adapter = new MateriaAdapter(getActivity(), mMaterias);
        mListView.setAdapter(adapter);

        // fab button
        final Activity activity = this.getActivity();
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mProfessor) {
                    Intent cadastroMateria = new Intent(Tab2_Materias.this.getActivity(), CadastroMateria.class);
                    startActivity(cadastroMateria);
                } else {
                    IntentIntegrator integrator = new IntentIntegrator(activity);
                    integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                    integrator.setPrompt("Scan");
                    integrator.setCameraId(0);
                    integrator.setOrientationLocked(false);
                    integrator.setBeepEnabled(false);
                    integrator.setBarcodeImageEnabled(false);
                    integrator.initiateScan();
                }
            }
        });

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

                AlertDialog.Builder adb = new AlertDialog.Builder(Tab2_Materias.this.getActivity());
                adb.setTitle("Remover?");
                if (mProfessor) {
                    adb.setMessage("Tem certeza que deseja apagar a disciplina \"" +
                            mMaterias.get(pos).getNomeDisciplina() + "\"?");
                } else {
                    adb.setMessage("Tem certeza que deseja sair da disciplina \"" +
                            mMaterias.get(pos).getNomeDisciplina() + "\"?");
                }
                final int positionToRemove = pos;
                adb.setNegativeButton("Cancelar", null);
                adb.setPositiveButton("Sim", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        RequisicaoAssincrona requisicao = new RequisicaoAssincrona();

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String email = (user != null) ? user.getEmail() : "";

                        if (mProfessor) {
                            // TODO: Caso Professor - Marcelo desabilite ou remova a matéria (materiaASerRemovida) do BD por favore
                        } else {
                            try {
                                JSONObject resultado_requisicao = requisicao.execute("cancelarinscricaoemmateria",
                                        email, Integer.toString(mMaterias.get(positionToRemove).getCodigo())).get();

                                if (resultado_requisicao.getString("status").equals("ok")) {
                                    mMaterias.remove(positionToRemove); // removing from the interface
                                    Toast.makeText(Tab2_Materias.this.getActivity(),
                                            "Matéria cancelada - você não receberá mais notificações dela",
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(Tab2_Materias.this.getActivity(),
                                            "Ocorreu um erro na operação com status: " +
                                                    resultado_requisicao.getString("descricao"),
                                            Toast.LENGTH_LONG).show();
                                }

                            } catch (InterruptedException | ExecutionException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }});
                adb.show();
                return true;    // true means it won't call another click listener
            }
        });

        return rootView;
    }

    public void addMateria(Materia materiaAdicionada){
        int index = 0;
        boolean adicionada = false;
        for (Materia materia : mMaterias) {
            // verify year
            if (materia.getAno() < materiaAdicionada.getAno()){
                mMaterias.add(index, materiaAdicionada);
                adicionada = true;
                break;
            }
            else if (materia.getAno() == materiaAdicionada.getAno()){
                //verify semester
                if (materia.getSemestre() < materiaAdicionada.getSemestre()) {
                    mMaterias.add(index, materiaAdicionada);
                    adicionada = true;
                    break;
                }
                else if (materia.getSemestre() == materiaAdicionada.getSemestre()) {
                    // verify alphabet
                    if (materia.getNomeDisciplina().compareToIgnoreCase(materiaAdicionada.getNomeDisciplina()) > 0){
                        mMaterias.add(index, materiaAdicionada);
                        adicionada = true;
                        break;
                    }
                    else {
                        mMaterias.add(index+1, materiaAdicionada);
                        adicionada = true;
                        break;
                    }
                }
            }
            index++;
        }
        if (!adicionada)
            mMaterias.add(materiaAdicionada);
        adapter.notifyDataSetChanged();
    }
}