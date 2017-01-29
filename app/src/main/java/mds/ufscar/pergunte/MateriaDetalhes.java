package mds.ufscar.pergunte;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

/**
 * Created by Danilo on 28/01/2017.
 */

public class MateriaDetalhes extends AppCompatActivity {

    private TextView mMateriaTitulo;
    private TextView mMateriaInfo;
    private TextView mMateriaCodigo;
    private ImageView mMateriaImagem;
    private Materia mMateriaEmQuestao;
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
        mMateriaCodigo = (TextView) findViewById(R.id.materia_list_code);
        mMateriaImagem = (ImageView) findViewById(R.id.materia_list_thumbnail);

        Intent intent = getIntent();
        mMateriaEmQuestao = intent.getParcelableExtra("materia");
        Professor professor = new Professor();
        professor.setNome(intent.getStringExtra("nome"));
        professor.setSobrenome(intent.getStringExtra("sobrenome"));
        professor.setEmail(intent.getStringExtra("email"));
        professor.setUniversidade(intent.getStringExtra("universidade"));
        mMateriaEmQuestao.setProfessor(professor);

        mMateriaTitulo.setText(mMateriaEmQuestao.getNomeDisciplina());
        mMateriaInfo.setText(mMateriaEmQuestao.getDescricao());
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

        // opening all groups
        int count = listAdapter.getGroupCount();
        for (int position = 1; position <= count; position++)
            expListView.expandGroup(position - 1);

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
    public void onBackPressed() {
        super.onBackPressed();
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
