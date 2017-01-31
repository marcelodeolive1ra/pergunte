package mds.ufscar.pergunte;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.text.Text;
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

import mds.ufscar.pergunte.model.Alternativa;
import mds.ufscar.pergunte.model.Materia;
import mds.ufscar.pergunte.model.Pergunta;
import mds.ufscar.pergunte.model.Professor;

import static mds.ufscar.pergunte.MainScreen.cadastroPerguntaCode;
import static mds.ufscar.pergunte.MainScreen.getEmailDoUsuarioAtual;
import static mds.ufscar.pergunte.MainScreen.perfilAluno;
import static mds.ufscar.pergunte.MainScreen.perfilProfessor;

/**
 * Created by Danilo on 28/01/2017.
 */

public class MateriaDetalhes extends AppCompatActivity {

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

        setContentView(R.layout.materia_detalhes);
        mMateriaTitulo = (TextView) findViewById(R.id.materia_list_title);
        mMateriaInfo = (TextView) findViewById(R.id.materia_list_subtitle);
        mMateriaInfo2 = (TextView)findViewById(R.id.materia_list_subtitle2);
        mMateriaCodigo = (TextView) findViewById(R.id.materia_list_code);
        mMateriaImagem = (ImageView) findViewById(R.id.materia_list_thumbnail);

        mToolbar = (Toolbar)findViewById(R.id.materia_detalhes_toolbar);
        mToolbar.setTitle("Perguntas da matéria");
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
        Picasso.with(this).load(mMateriaEmQuestao.getImageUrl()).placeholder(R.mipmap.ic_launcher).into(mMateriaImagem);

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
                // TODO: fazer isso apenas para pergunta ativa e se mPerfilUsuario aluno
                Intent respostaTela = new Intent(v.getContext(), RespostaTela.class);
                Pergunta pergunta = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
                respostaTela.putExtra("pergunta", pergunta);
                ArrayList<Alternativa> alternativas = pergunta.getAlternativas();
                respostaTela.putParcelableArrayListExtra("alternativas", alternativas);
                startActivity(respostaTela);
                return false;
            }
        });

        // fab
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        // case professor - show fab
        if (mProfessor) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) fab.getLayoutParams();
            fab.requestLayout();
            fab.setVisibility(View.VISIBLE);
        } else { // case student - hide it
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) fab.getLayoutParams();
            fab.requestLayout();
            fab.setVisibility(View.GONE);
        }
        // on click
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cadastroPergunta = new Intent(view.getContext(), CadastroPergunta.class);
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

        // Adding child data
        listDataHeader.add("Ativas");
        listDataHeader.add("Próximas");
        listDataHeader.add("Respondidas");

        // TODO: Tratar falta de internet para as três requisições

        // Adding child data
        ArrayList<Pergunta> perguntasAtivas = new ArrayList<>();
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
                Toast.makeText(MateriaDetalhes.this,
                        resultado_requisicao.getString("descricao"),
                        Toast.LENGTH_LONG).show();
            }
        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
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
                Toast.makeText(MateriaDetalhes.this,
                        resultado_requisicao.getString("descricao"),
                        Toast.LENGTH_LONG).show();
            }
        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }


        ArrayList<Pergunta> perguntasRespondidas = new ArrayList<>();
        RequisicaoAssincrona requisicao3 = new RequisicaoAssincrona();
        requisicao3.setObject(mMateriaEmQuestao);

        try {
            JSONObject resultado_requisicao = requisicao3.execute(RequisicaoAssincrona.BUSCAR_PERGUNTAS_RESPONDIDAS_POR_MATERIA,
                    MainScreen.getEmailDoUsuarioAtual()).get();

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
                Toast.makeText(MateriaDetalhes.this,
                        resultado_requisicao.getString("descricao"),
                        Toast.LENGTH_LONG).show();
            }
        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }

        listDataChild.put(listDataHeader.get(0), perguntasAtivas); // Header, Child data
        listDataChild.put(listDataHeader.get(1), proximasPerguntas);
        listDataChild.put(listDataHeader.get(2), perguntasRespondidas);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_materia_detalhes, menu);
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

            Toast.makeText(MateriaDetalhes.this, "Lista de perguntas atualizada.",
                    Toast.LENGTH_LONG).show();
        } else if (id == R.id.gerar_qr_code) {
            RequisicaoAssincrona requisicao = new RequisicaoAssincrona();

            try {
                JSONObject resultado_requisicao = requisicao.execute(RequisicaoAssincrona.ENVIAR_QR_CODE_POR_EMAIL,
                        getEmailDoUsuarioAtual(), Integer.toString(mMateriaEmQuestao.getCodigo())).get();

                if (resultado_requisicao != null) {
                    if (resultado_requisicao.getString("status").equals("ok")) {
                        Toast.makeText(MateriaDetalhes.this, "QR code desta matéria enviado com sucesso para o seu e-mail." +
                                "Verifique seu e-mail em instantes.",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MateriaDetalhes.this, resultado_requisicao.getString("descricao"),
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    // TODO: Tratar falta de conexão à internet
                }
            } catch (InterruptedException | ExecutionException | JSONException e) {
                e.printStackTrace();
            }

        } else if (id == R.id.cancelar_inscricao) {
            // TODO: implementar cancelar inscrição aqui?
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
