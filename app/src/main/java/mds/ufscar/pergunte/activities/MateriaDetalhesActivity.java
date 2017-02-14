package mds.ufscar.pergunte.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import mds.ufscar.pergunte.R;
import mds.ufscar.pergunte.adapters.ExpandableListAdapter;
import mds.ufscar.pergunte.helpers.RequisicaoAssincrona;
import mds.ufscar.pergunte.models.Alternativa;
import mds.ufscar.pergunte.models.Materia;
import mds.ufscar.pergunte.models.Pergunta;
import mds.ufscar.pergunte.models.Professor;

import static mds.ufscar.pergunte.activities.MainScreenActivity.cadastroPerguntaCode;
import static mds.ufscar.pergunte.activities.MainScreenActivity.getEmailDoUsuarioAtual;

/**
 * Created by Danilo on 28/01/2017.
 */

public class MateriaDetalhesActivity extends AppCompatActivity {

    private TextView mMateriaTitulo;
    private TextView mMateriaInfo;
    private TextView mMateriaInfo2;
    private TextView mMateriaCodigo;
    private ImageView mMateriaImagem;
    private Materia mMateriaEmQuestao;
    private boolean mProfessor;
    private Toolbar mToolbar;
    // para a lista expandível
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, ArrayList<Pergunta>> listDataChild;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.materia_detalhes_activity);
        mMateriaTitulo = (TextView) findViewById(R.id.materia_list_title);
        mMateriaInfo = (TextView) findViewById(R.id.materia_list_subtitle);
        mMateriaInfo2 = (TextView)findViewById(R.id.materia_list_subtitle2);
        mMateriaCodigo = (TextView) findViewById(R.id.materia_list_code);
        mMateriaImagem = (ImageView) findViewById(R.id.materia_list_thumbnail);

        mToolbar = (Toolbar)findViewById(R.id.materia_detalhes_toolbar);
        mToolbar.setTitle("Detalhes da matéria");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        mMateriaEmQuestao = intent.getParcelableExtra("materia");
        Professor professor = new Professor();
        professor.setNome(intent.getStringExtra("nome"));
        professor.setSobrenome(intent.getStringExtra("sobrenome"));
        professor.setEmail(intent.getStringExtra("email"));
        professor.setUniversidade(intent.getStringExtra("universidade"));
        mMateriaEmQuestao.setProfessor(professor);
        mProfessor = intent.getBooleanExtra("isProfessor", false);

        mMateriaTitulo.setText(mMateriaEmQuestao.getNomeDisciplina());
        mMateriaInfo.setText(mMateriaEmQuestao.getProfessor().toString());
        String turma_e_semestre = "Turma " + mMateriaEmQuestao.getTurma() + " - " +
                mMateriaEmQuestao.getAno() + "/" + mMateriaEmQuestao.getSemestre();
        mMateriaInfo2.setText(turma_e_semestre);
        String codigo = "Código de inscrição: " + mMateriaEmQuestao.getCodigoInscricao();
        mMateriaCodigo.setText(codigo);
        Picasso.with(this).load(mMateriaEmQuestao.getImageUrl()).placeholder(R.drawable.ic_materia).into(mMateriaImagem);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // opening groups with something in it
        int count = listAdapter.getGroupCount();
        for (int position = 0; position < count; position++) {
            if (listAdapter.getChildrenCount(position) > 0) {
                expListView.expandGroup(position);
            }
        }

        // when child is clicked
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                // TODO: tratar o caso quando usuário clicar em uma pergunta da seção "Respondidas"
                // Neste caso, poderia abrir a questão respondida, com as alternativas do usuário selecionadas

                System.out.println(listDataHeader.get(groupPosition));

                if (listDataHeader.get(groupPosition).equals("Ativas")) {

                    Intent respostaTela = new Intent(v.getContext(), ResponderPerguntaActivity.class);
                    Pergunta pergunta = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
                    respostaTela.putExtra("pergunta", pergunta);
                    ArrayList<Alternativa> alternativas = pergunta.getAlternativas();
                    respostaTela.putParcelableArrayListExtra("alternativas", alternativas);

                    respostaTela.putExtra("nome_materia", mMateriaEmQuestao.getNomeDisciplina());

                    startActivity(respostaTela);
                }
                return false;
            }
        });

        // fab
        final com.getbase.floatingactionbutton.FloatingActionsMenu fabMain =
                (com.getbase.floatingactionbutton.FloatingActionsMenu) findViewById(R.id.multiple_actions);

        final com.getbase.floatingactionbutton.FloatingActionButton fabPergunta =
                (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.action_a);

        // case professor - show fab
        if (!mProfessor) {
            fabMain.setVisibility(View.GONE);
        }

        // on click
        fabPergunta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabMain.collapse();
                Intent cadastroPergunta = new Intent(view.getContext(), CadastrarPerguntaActivity.class);
                cadastroPergunta.putExtra("materiaID", mMateriaEmQuestao.getCodigo());
                startActivityForResult(cadastroPergunta, cadastroPerguntaCode);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        Log.v("resultCode", String.valueOf(resultCode));
        if (requestCode == cadastroPerguntaCode) {

            carregarExpandableList();

            Intent returnIntent = new Intent();
            setResult(resultCode, returnIntent);
        }

    }

    /*
         * Preparing the list data
         */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, ArrayList<Pergunta>>();

        if (!mProfessor) {
            listDataHeader.add("Ativas");
            listDataHeader.add("Próximas");
            listDataHeader.add("Respondidas");
        } else {
            listDataHeader.add("Próximas");
        }

        // TODO: Tratar falta de internet para as três requisições

        ArrayList<Pergunta> perguntasAtivas = new ArrayList<>();

        if (!mProfessor) {
            RequisicaoAssincrona requisicao = new RequisicaoAssincrona();
            requisicao.setObject(mMateriaEmQuestao);
            System.out.println("CODIGO = " + mMateriaEmQuestao.getCodigo());

            try {
                JSONObject resultado_requisicao = requisicao.execute(RequisicaoAssincrona.BUSCAR_PERGUNTAS_ATIVAS_POR_MATERIA).get();

                if (resultado_requisicao.getString("status").equals("ok")) {

                    JSONArray perguntas_ativas_json = resultado_requisicao.getJSONArray("perguntas");

                    for (int i = 0; i < perguntas_ativas_json.length(); i++) {
                        JSONObject pergunta_json = perguntas_ativas_json.getJSONObject(i);
                        Pergunta pergunta = new Pergunta(pergunta_json);
                        perguntasAtivas.add(pergunta);
                    }

                } else {
                    Log.w("REQUISICAO", resultado_requisicao.toString());
                    Toast.makeText(MateriaDetalhesActivity.this,
                            resultado_requisicao.getString("descricao"),
                            Toast.LENGTH_LONG).show();
                }
            } catch (InterruptedException | ExecutionException | JSONException e) {
                e.printStackTrace();
            }
        }


        ArrayList<Pergunta> proximasPerguntas = new ArrayList<>();
        RequisicaoAssincrona requisicao2 = new RequisicaoAssincrona();
        requisicao2.setObject(mMateriaEmQuestao);

        try {
            JSONObject resultado_requisicao = requisicao2.execute(RequisicaoAssincrona.BUSCAR_PROXIMAS_PERGUNTAS_POR_MATERIA).get();

            if (resultado_requisicao.getString("status").equals("ok")) {

                JSONArray proximas_perguntas_json = resultado_requisicao.getJSONArray("perguntas");

                for (int i = 0; i < proximas_perguntas_json.length(); i++) {
                    JSONObject pergunta_json = proximas_perguntas_json.getJSONObject(i);
                    Pergunta pergunta = new Pergunta(pergunta_json);
                    proximasPerguntas.add(pergunta);
                }

            } else {
                Log.w("REQUISICAO", resultado_requisicao.toString());
                Toast.makeText(MateriaDetalhesActivity.this,
                        resultado_requisicao.getString("descricao"),
                        Toast.LENGTH_LONG).show();
            }
        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }


        ArrayList<Pergunta> perguntasRespondidas = new ArrayList<>();

        if (!mProfessor) {
            RequisicaoAssincrona requisicao3 = new RequisicaoAssincrona();
            requisicao3.setObject(mMateriaEmQuestao);

            try {
                JSONObject resultado_requisicao = requisicao3.execute(RequisicaoAssincrona.BUSCAR_PERGUNTAS_RESPONDIDAS_POR_MATERIA,
                        MainScreenActivity.getEmailDoUsuarioAtual()).get();

                if (resultado_requisicao.getString("status").equals("ok")) {

                    JSONArray perguntas_respondidas_json = resultado_requisicao.getJSONArray("perguntas");

                    System.out.println(perguntas_respondidas_json);

                    for (int i = 0; i < perguntas_respondidas_json.length(); i++) {
                        JSONObject pergunta_json = perguntas_respondidas_json.getJSONObject(i);
                        Pergunta pergunta = new Pergunta(pergunta_json);
                        perguntasRespondidas.add(pergunta);
                    }

                } else {
                    Log.w("REQUISICAO", resultado_requisicao.toString());
                    Toast.makeText(MateriaDetalhesActivity.this,
                            resultado_requisicao.getString("descricao"),
                            Toast.LENGTH_LONG).show();
                }
            } catch (InterruptedException | ExecutionException | JSONException e) {
                e.printStackTrace();
            }
        }

        if (!mProfessor) {
            listDataChild.put(listDataHeader.get(0), perguntasAtivas); // Header, Child data
            listDataChild.put(listDataHeader.get(1), proximasPerguntas);
            listDataChild.put(listDataHeader.get(2), perguntasRespondidas);
        } else {
            listDataChild.put(listDataHeader.get(0), proximasPerguntas);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        if (mProfessor) {
            getMenuInflater().inflate(R.menu.menu_materia_detalhes_professor, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_materia_detalhes_aluno, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.atualizar_lista) {
            carregarExpandableList();

            Toast.makeText(MateriaDetalhesActivity.this, "Lista de perguntas atualizada.",
                    Toast.LENGTH_LONG).show();
        } else if (id == R.id.gerar_qr_code) {
            RequisicaoAssincrona requisicao = new RequisicaoAssincrona();

            try {
                JSONObject resultado_requisicao = requisicao.execute(RequisicaoAssincrona.ENVIAR_QR_CODE_POR_EMAIL,
                        getEmailDoUsuarioAtual(), Integer.toString(mMateriaEmQuestao.getCodigo())).get();

                if (resultado_requisicao != null) {
                    if (resultado_requisicao.getString("status").equals("ok")) {
                        Toast.makeText(MateriaDetalhesActivity.this, "QR code desta matéria enviado com sucesso para o seu e-mail." +
                                "Verifique seu e-mail em instantes.",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MateriaDetalhesActivity.this, resultado_requisicao.getString("descricao"),
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    // TODO: Tratar falta de conexão à internet
                }
            } catch (InterruptedException | ExecutionException | JSONException e) {
                e.printStackTrace();
            }

        } else if (id == R.id.cancelar_inscricao) {
            AlertDialog.Builder adb = new AlertDialog.Builder(MateriaDetalhesActivity.this);

            if (mProfessor) {
                adb.setTitle("Desativar matéria");
                adb.setMessage("Tem certeza que deseja desativar a matéria \"" +
                        (mMateriaEmQuestao.getNomeDisciplina() + "\"?\n\n" +
                        "Os alunos cadastrados não poderão mais acessar os dados da matéria."));
            } else {
                adb.setTitle("Cancelar inscrição");
                adb.setMessage("Tem certeza que deseja cancelar sua inscrição na disciplina \"" +
                        (mMateriaEmQuestao.getNomeDisciplina() + "\"?"));
            }
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
                                email, Integer.toString(mMateriaEmQuestao.getCodigo())).get();

                        if (resultado_requisicao.getString("status").equals("ok")) {

                            if (!mProfessor) {
                                FirebaseMessaging.getInstance().unsubscribeFromTopic(mMateriaEmQuestao.getCodigoInscricao());
                            }

                            Log.w("REQUISICAO", resultado_requisicao.toString());


                            String mensagemDeFeedback = (mProfessor) ?
                                    "Matéria desativada com sucesso!" :
                                    "Inscrição cancelada com sucesso! A partir de agora, você não receberá mais notificações desta matéria.";

                            Toast.makeText(MateriaDetalhesActivity.this,
                                    mensagemDeFeedback,
                                    Toast.LENGTH_LONG).show();

                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("cancelouMateria", true);
                            returnIntent.putExtra("codigoInscricao", mMateriaEmQuestao.getCodigoInscricao());
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                        } else {
                            Log.w("REQUISICAO", resultado_requisicao.toString());
                            Toast.makeText(MateriaDetalhesActivity.this,
                                    resultado_requisicao.getString("descricao"),
                                    Toast.LENGTH_LONG).show();
                        }
                    } catch (InterruptedException | ExecutionException | JSONException e) {
                        e.printStackTrace();
                    }
                }});
            adb.show();
        } else if (id == R.id.ver_alunos_inscritos) {
            Intent alunosInscritos = new Intent(this, AlunosInscritosActivity.class);
            alunosInscritos.putExtra("tituloDisciplina", mMateriaEmQuestao.getNomeDisciplina());
            alunosInscritos.putExtra("codigoDisciplina", mMateriaEmQuestao.getCodigo());
            startActivity(alunosInscritos);
        }

        return super.onOptionsItemSelected(item);
    }

    public void carregarExpandableList() {
        prepareListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // opening groups with something in it
        int count = listAdapter.getGroupCount();
        for (int position = 0; position < count; position++) {
            if (listAdapter.getChildrenCount(position) > 0) {
                expListView.expandGroup(position);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
