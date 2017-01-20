package mds.ufscar.pergunte;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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

/**
 * Created by Danilo on 24/12/2016.
 */

public class Tab2_Materias extends Fragment {

    private ListView mListView;
    private boolean mProfessor;
    private ArrayList<MateriaItem> mMateriaItems;
    private MateriaAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2_materia, container, false);

        mListView = (ListView) rootView.findViewById(R.id.materia_list_view);
        mProfessor = ((MainScreen)this.getActivity()).isProfessor();
        String emailUsuarioAtual = MainScreen.getEmailDoUsuarioAtual();
        mMateriaItems =  new ArrayList<>();

        RequisicaoAssincrona requisicao = new RequisicaoAssincrona();

        try {
            String perfil_usuario = (mProfessor) ?
                    RequisicaoAssincrona.Parametros.PERFIL_PROFESSOR : RequisicaoAssincrona.Parametros.PERFIL_ALUNO;

            JSONObject resultado_requisicao = requisicao.execute(RequisicaoAssincrona.BUSCAR_MATERIAS,
                    emailUsuarioAtual, perfil_usuario,
                    RequisicaoAssincrona.Parametros.STATUS_MATERIA_ATIVA).get();

            if (resultado_requisicao.getString("status").equals("ok")) {
                JSONArray materias_json = resultado_requisicao.getJSONArray("materias");

                ArrayList<Materia> materias = new ArrayList<>();
                for (int i = 0; i < materias_json.length(); i++) {
                    Materia materia = new Materia();
                    if (materia.construirObjetoComJSON(materias_json.getJSONObject(i))) {
                        materias.add(materia);
                    } else {
                        Toast.makeText(Tab2_Materias.this.getActivity(),
                                "Erro ao carregar matéria.", Toast.LENGTH_LONG).show();
                    }
                }
                mMateriaItems = addSections(materias);
            } else {
                Log.w("REQUISICAO", resultado_requisicao.toString());
                Toast.makeText(Tab2_Materias.this.getActivity(),
                        resultado_requisicao.getString("descricao"), Toast.LENGTH_LONG).show();
            }

        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }

        adapter = new MateriaAdapter(getActivity(), mMateriaItems);
        mListView.setAdapter(adapter);

        // fab button
        final Activity activity = this.getActivity();
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mProfessor) {
                    Intent cadastroMateria = new Intent(Tab2_Materias.this.getActivity(), CadastroMateria.class);
                    getActivity().startActivityForResult(cadastroMateria, MainScreen.cadastroMateriaCode);
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
        // TODO: Danilo corrigir remover seção caso remova todo semestre
        mListView.setLongClickable(true);
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {

                AlertDialog.Builder adb = new AlertDialog.Builder(Tab2_Materias.this.getActivity());

                if (mProfessor) {
                    adb.setTitle("Desativar a matéria?");
                    adb.setMessage("Tem certeza que deseja desativar a matéria \"" +
                            ((Materia) mMateriaItems.get(pos)).getNomeDisciplina() + "\"?\n\n" +
                            "Os alunos cadastrados não poderão mais acessar os dados da matéria.");
                } else {
                    adb.setTitle("Cancelar inscrição");
                    adb.setMessage("Tem certeza que deseja sair da disciplina \"" +
                            ((Materia) mMateriaItems.get(pos)).getNomeDisciplina() + "\"?");
                }
                final int positionToRemove = pos;
                adb.setNegativeButton("Cancelar", null);
                adb.setPositiveButton("Sim", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        RequisicaoAssincrona requisicao = new RequisicaoAssincrona();

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String email = (user != null) ? user.getEmail() : "";

                        try {

                            String tipo_requisicao = (mProfessor) ?
                                    RequisicaoAssincrona.DESATIVAR_MATERIA : RequisicaoAssincrona.CANCELAR_INSCRICAO_EM_MATERIA;

                            JSONObject resultado_requisicao = requisicao.execute(tipo_requisicao,
                                    email, Integer.toString(((Materia) mMateriaItems.get(positionToRemove)).getCodigo())).get();

                            if (resultado_requisicao.getString("status").equals("ok")) {
                                Log.w("REQUISICAO", resultado_requisicao.toString());
                                mMateriaItems.remove(positionToRemove); // removing from the interface

                                String mensagemDeFeedback = (mProfessor) ?
                                        "Matéria desativada com sucesso!" :
                                        "Matéria cancelada com sucesso! A partir de agora, você não receberá mais notificações desta matéria.";

                                Toast.makeText(Tab2_Materias.this.getActivity(),
                                        mensagemDeFeedback,
                                        Toast.LENGTH_LONG).show();

                                mMateriaItems = addSections(extrairMaterias(mMateriaItems));
                            } else {
                                Log.w("REQUISICAO", resultado_requisicao.toString());
                                Toast.makeText(Tab2_Materias.this.getActivity(),
                                        resultado_requisicao.getString("descricao"),
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (InterruptedException | ExecutionException | JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                    }});
                adb.show();
                return true;    // true means it won't call another click listener
            }
        });

        return rootView;
    }

    public ArrayList<Materia> extrairMaterias(ArrayList<MateriaItem> materiaItems) {
        ArrayList<Materia> materias = new ArrayList<>();
        for (MateriaItem materiaItem : materiaItems) {
            if (!materiaItem.isSection()) {
                materias.add((Materia)materiaItem);
            }
        }
        return materias;
    }

    public ArrayList<MateriaItem> addSections(ArrayList<Materia> materias) {
        ArrayList<MateriaItem> materiaItems = new ArrayList<>();
        String lastYearSemesterAdded;
        lastYearSemesterAdded = getSectionTitle(materias.get(0));
        materiaItems.add(new MateriaSection(lastYearSemesterAdded));
        for (Materia materia : materias) {
            if (!lastYearSemesterAdded.equals(getSectionTitle(materia))) {
                lastYearSemesterAdded = getSectionTitle(materia);
                materiaItems.add(new MateriaSection(lastYearSemesterAdded));
            }
            materiaItems.add(materia);
        }
        return materiaItems;
    }

    public String getSectionTitle(Materia materia) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(materia.getAno());
        stringBuilder.append(" - ").append(materia.getSemestre());
        return stringBuilder.toString();
    }

    public void addMateria(Materia materiaAdicionada){
        int index = 0;
        boolean adicionada = false;
        for (MateriaItem materiaItem : mMateriaItems) {
            if (materiaItem.isSection()) {
                continue;
            } else {
                Materia materia = (Materia) materiaItem;
                // verify year
                if (materia.getAno() < materiaAdicionada.getAno()) {
                    mMateriaItems.add(index, new MateriaSection(getSectionTitle(materiaAdicionada)));
                    mMateriaItems.add(index+1, materiaAdicionada);
                    adicionada = true;
                    break;
                } else if (materia.getAno() == materiaAdicionada.getAno()) {
                    //verify semester
                    if (materia.getSemestre() < materiaAdicionada.getSemestre()) {
                        mMateriaItems.add(index, new MateriaSection(getSectionTitle(materiaAdicionada)));
                        mMateriaItems.add(index+1, materiaAdicionada);
                        adicionada = true;
                        break;
                    } else if (materia.getSemestre() == materiaAdicionada.getSemestre()) {
                        adicionada = true;
                        // verify alphabet
                        if (materia.getNomeDisciplina().compareToIgnoreCase(materiaAdicionada.getNomeDisciplina()) > 0) {
                            mMateriaItems.add(index, materiaAdicionada);
                            break;
                        } else {
                            mMateriaItems.add(index + 1, materiaAdicionada);
                            break;
                        }
                    }
                }
            }
            index++;
        }
        if (!adicionada) {
            mMateriaItems.add(new MateriaSection(getSectionTitle(materiaAdicionada)));
            mMateriaItems.add(materiaAdicionada);
        }
        adapter.notifyDataSetChanged();
    }
}