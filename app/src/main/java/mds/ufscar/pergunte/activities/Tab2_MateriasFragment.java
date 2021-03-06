package mds.ufscar.pergunte.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.zxing.integration.android.IntentIntegrator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import mds.ufscar.pergunte.helpers.ListItem;
import mds.ufscar.pergunte.R;
import mds.ufscar.pergunte.helpers.RequisicaoAssincrona;
import mds.ufscar.pergunte.helpers.Section;
import mds.ufscar.pergunte.adapters.MateriaAdapter;
import mds.ufscar.pergunte.models.Materia;

/**
 * Created by Danilo on 24/12/2016.
 */

public class Tab2_MateriasFragment extends Fragment {

    private ListView mListView;
    private boolean mProfessor;
    private ArrayList<ListItem> mListItems;
    private MateriaAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab2_materia_fragment, container, false);

        mListView = (ListView) rootView.findViewById(R.id.materia_list_view);
        final MainScreenActivity mainScreen = (MainScreenActivity)this.getActivity();
        mProfessor = mainScreen.isProfessor();
        String emailUsuarioAtual = MainScreenActivity.getEmailDoUsuarioAtual();
        mListItems =  new ArrayList<>();

        RequisicaoAssincrona requisicao = new RequisicaoAssincrona();

        try {
            String perfil_usuario = (mProfessor) ?
                    RequisicaoAssincrona.Parametros.PERFIL_PROFESSOR : RequisicaoAssincrona.Parametros.PERFIL_ALUNO;

            JSONObject resultado_requisicao = requisicao.execute(RequisicaoAssincrona.BUSCAR_MATERIAS,
                    emailUsuarioAtual, perfil_usuario,
                    RequisicaoAssincrona.Parametros.STATUS_MATERIA_ATIVA).get();

            if (resultado_requisicao != null) {
                if (resultado_requisicao.getString("status").equals("ok")) {
                    JSONArray materias_json = resultado_requisicao.getJSONArray("materias");

                    if (materias_json.length() > 0) {
                        ArrayList<Materia> materias = new ArrayList<>();
                        for (int i = 0; i < materias_json.length(); i++) {
                            materias.add(new Materia(materias_json.getJSONObject(i)));
                        }
                        mListItems = addSections(materias);
                    } else {
                        String mensagem_de_erro = mProfessor ?
                                "Você ainda não cadastrou nenhuma matéria." :
                                "Você ainda não está inscrito(a) em nenhuma matéria.";

                        Toast.makeText(Tab2_MateriasFragment.this.getActivity(),
                                mensagem_de_erro, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.w("REQUISICAO", resultado_requisicao.toString());
                    Toast.makeText(Tab2_MateriasFragment.this.getActivity(),
                            resultado_requisicao.getString("descricao"), Toast.LENGTH_LONG).show();
                }
            } else {
                AlertDialog.Builder adb = new AlertDialog.Builder(Tab2_MateriasFragment.this.getActivity());
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

        adapter = new MateriaAdapter(getActivity(), mListItems);
        mListView.setAdapter(adapter);

        // fab button
        final Activity activity = this.getActivity();

        final com.getbase.floatingactionbutton.FloatingActionsMenu fabMain =
                (com.getbase.floatingactionbutton.FloatingActionsMenu) rootView.findViewById(R.id.multiple_actions);

        final com.getbase.floatingactionbutton.FloatingActionButton fabScan =
                (com.getbase.floatingactionbutton.FloatingActionButton) rootView.findViewById(R.id.action_a);
        fabScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabMain.collapse();

                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setOrientationLocked(false);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();

            }
        });

        final com.getbase.floatingactionbutton.FloatingActionButton fabType =
                (com.getbase.floatingactionbutton.FloatingActionButton) rootView.findViewById(R.id.action_b);
        fabType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabMain.collapse();
                if (mProfessor) {
                    Intent cadastroMateria = new Intent(Tab2_MateriasFragment.this.getActivity(), CadastrarMateriaActivity.class);
                    getActivity().startActivityForResult(cadastroMateria, MainScreenActivity.cadastroMateriaCode);
                } else {

                    LayoutInflater factory = LayoutInflater.from(getContext());
                    final View dialogView = factory.inflate(R.layout.digitar_codigo_inscricao_dialog, null);

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
                    AlertDialog dialog;
                    alertDialog.setView(dialogView);
                    alertDialog.setTitle("Inscrição em matéria");

                    final EditText input = (EditText)dialogView.findViewById(R.id.input_codigo_inscricao_materia);

                    alertDialog.setPositiveButton("Inscrever", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("scan", input.getText().toString());
                            mainScreen.onActivityResult(MainScreenActivity.scannerCode, Activity.RESULT_OK, returnIntent);
                        }
                    });
                    alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    dialog = alertDialog.create();
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                    dialog.show();
                }
            }
        });

        if (mProfessor) {
            fabScan.setVisibility(View.GONE);
            fabType.setTitle("Nova matéria");
        }

        // setting one click on an item of the list view
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3)
            {
                Intent materiaDetalhes = new Intent(Tab2_MateriasFragment.this.getActivity(), MateriaDetalhesActivity.class);
                materiaDetalhes.putExtra("materia", (Materia) mListItems.get(pos));
                materiaDetalhes.putExtra("nome", ((Materia) mListItems.get(pos)).getProfessor().getNome());
                materiaDetalhes.putExtra("sobrenome", ((Materia) mListItems.get(pos)).getProfessor().getSobrenome());
                materiaDetalhes.putExtra("email", ((Materia) mListItems.get(pos)).getProfessor().getEmail());
                materiaDetalhes.putExtra("universidade", ((Materia) mListItems.get(pos)).getProfessor().getUniversidade());
                materiaDetalhes.putExtra("isProfessor", mProfessor);
//                    getActivity().findViewById(R.id.progress_overlay).setVisibility(View.VISIBLE);
                getActivity().startActivityForResult(materiaDetalhes, MainScreenActivity.materiaDetalhesCode);

            }
        });

        // setting option to remove an item for a long press
        mListView.setLongClickable(true);
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {

                AlertDialog.Builder adb = new AlertDialog.Builder(Tab2_MateriasFragment.this.getActivity());

                if (mProfessor) {
                    adb.setTitle("Desativar a matéria");
                    adb.setMessage("Tem certeza que deseja desativar a matéria \"" +
                            ((Materia) mListItems.get(pos)).getNomeDisciplina() + "\"?\n\n" +
                            "Os alunos cadastrados não poderão mais acessar os dados da matéria.");
                } else {
                    adb.setTitle("Cancelar inscrição");
                    adb.setMessage("Tem certeza que deseja cancelar sua inscrição na disciplina \"" +
                            ((Materia) mListItems.get(pos)).getNomeDisciplina() + "\"?");
                }
                final int positionToRemove = pos;
                adb.setNegativeButton("Voltar", null);
                adb.setPositiveButton(mProfessor ? "Desativar" : "Cancelar inscrição", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        RequisicaoAssincrona requisicao = new RequisicaoAssincrona();

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String email = (user != null) ? user.getEmail() : "";

                        // TODO: Tratar falta de conexão à Internet aqui

                        try {
                            String tipo_requisicao = (mProfessor) ?
                                    RequisicaoAssincrona.DESATIVAR_MATERIA : RequisicaoAssincrona.CANCELAR_INSCRICAO_EM_MATERIA;

                            JSONObject resultado_requisicao = requisicao.execute(tipo_requisicao,
                                    email, Integer.toString(((Materia) mListItems.get(positionToRemove)).getCodigo())).get();

                            if (resultado_requisicao.getString("status").equals("ok")) {

                                if (!mProfessor) {
                                    FirebaseMessaging.getInstance().unsubscribeFromTopic(((Materia) mListItems.get(positionToRemove)).getCodigoInscricao());
                                }

                                Log.w("REQUISICAO", resultado_requisicao.toString());



                                String mensagemDeFeedback = (mProfessor) ?
                                        "Matéria desativada com sucesso!" :
                                        "Inscrição cancelada com sucesso! A partir de agora, você não receberá mais notificações desta matéria.";

                                Toast.makeText(Tab2_MateriasFragment.this.getActivity(),
                                        mensagemDeFeedback,
                                        Toast.LENGTH_LONG).show();

                                // TODO: make function here
                                removerMateriaDaLista(positionToRemove);

                            } else {
                                Log.w("REQUISICAO", resultado_requisicao.toString());
                                Toast.makeText(Tab2_MateriasFragment.this.getActivity(),
                                        resultado_requisicao.getString("descricao"),
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (InterruptedException | ExecutionException | JSONException e) {
                            e.printStackTrace();
                        }
                    }});
                adb.show();
                return true;    // true means it won't call another click listener
            }
        });

        return rootView;
    }

    public ArrayList<Materia> extrairMaterias(ArrayList<ListItem> listItems) {
        ArrayList<Materia> materias = new ArrayList<>();
        for (ListItem listItem : listItems) {
            if (!listItem.isSection()) {
                materias.add((Materia) listItem);
            }
        }
        return materias;
    }

    public ArrayList<ListItem> addSections(ArrayList<Materia> materias) {
        ArrayList<ListItem> listItems = new ArrayList<>();
        if (materias.size() > 0) {
            String lastYearSemesterAdded;
            lastYearSemesterAdded = getSectionTitle(materias.get(0));
            listItems.add(new Section(lastYearSemesterAdded));
            for (Materia materia : materias) {
                if (!lastYearSemesterAdded.equals(getSectionTitle(materia))) {
                    lastYearSemesterAdded = getSectionTitle(materia);
                    listItems.add(new Section(lastYearSemesterAdded));
                }
                listItems.add(materia);
            }
        }
        return listItems;
    }

    public String getSectionTitle(Materia materia) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(materia.getAno());
        stringBuilder.append("/").append(materia.getSemestre());
        return stringBuilder.toString();
    }

    public void addMateria(Materia materiaAdicionada){
        int index = 0;
        boolean adicionada = false;
        for (ListItem listItem : mListItems) {
            if (listItem.isSection()) {
                index++;
                continue;
            } else {
                Materia materia = (Materia) listItem;
                // verify year
                if (materia.getAno() < materiaAdicionada.getAno()) {
                    mListItems.add(index-1, new Section(getSectionTitle(materiaAdicionada)));
                    mListItems.add(index, materiaAdicionada);
                    adicionada = true;
                    break;
                } else if (materia.getAno() == materiaAdicionada.getAno()) {
                    //verify semester
                    if (materia.getSemestre() < materiaAdicionada.getSemestre()) {
                        mListItems.add(index-1, new Section(getSectionTitle(materiaAdicionada)));
                        mListItems.add(index, materiaAdicionada);
                        adicionada = true;
                        break;
                    } else if (materia.getSemestre() == materiaAdicionada.getSemestre()) {
                        // verify alphabet
                        if (materia.getNomeDisciplina().compareToIgnoreCase(materiaAdicionada.getNomeDisciplina()) > 0) {
                            mListItems.add(index, materiaAdicionada);
                            adicionada = true;
                            break;
                        } else if (index >= (mListItems.size()-1) || mListItems.get(index+1).isSection()){ // acabou a seção
                            mListItems.add(index + 1, materiaAdicionada);
                            adicionada = true;
                            break;
                        }
                    }
                }
            }
            index++;
        }
        if (!adicionada) {
            mListItems.add(new Section(getSectionTitle(materiaAdicionada)));
            mListItems.add(materiaAdicionada);
        }
        adapter.notifyDataSetChanged();
    }

    public boolean removerMateriaDaLista(int positionToRemove) {
        mListItems.remove(positionToRemove); // removing from the interface
        ArrayList<Materia> materias = extrairMaterias(mListItems);
        // has to keep the same object
        mListItems.clear();
        mListItems.addAll(addSections(materias));
        adapter.notifyDataSetChanged();
        return true;
    }

    public int getPositionMateria(String codigoInscr) {
        int pos = 0;
        boolean encontrada = false;
        while (pos < mListItems.size() && !encontrada) {
            if (mListItems.get(pos) instanceof Materia) {
                Materia materia = (Materia)mListItems.get(pos);
                if (materia.getCodigoInscricao().equals(codigoInscr)) {
                    encontrada = true;
                }
            }
            pos++;
        }
        return encontrada ? (pos-1) : -1;   // retorna -1 caso nao encontrar materia
    }
}