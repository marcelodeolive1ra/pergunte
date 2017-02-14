package mds.ufscar.pergunte.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import mds.ufscar.pergunte.R;
import mds.ufscar.pergunte.adapters.AlunoAdapter;
import mds.ufscar.pergunte.helpers.RequisicaoAssincrona;
import mds.ufscar.pergunte.models.Aluno;
import mds.ufscar.pergunte.models.Pergunta;

/**
 * Created by Danilo on 13/02/2017.
 */

public class AlunosInscritosActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ListView mListView;
    private ArrayList<Aluno> mListAlunos;
    private AlunoAdapter adapter;
    private TextView mLabelAlunos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alunos_inscritos);

        Intent intent = getIntent();

        String tituloDisciplina = intent.getStringExtra("tituloDisciplina");

        mListView = (ListView) findViewById(R.id.aluno_list_view);
        mToolbar = (Toolbar)findViewById(R.id.alunos_inscritos_toolbar);
        mToolbar.setTitle("Alunos inscritos");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));

        mLabelAlunos = (TextView)findViewById(R.id.label_alunos);
        mLabelAlunos.setText(tituloDisciplina);

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mListAlunos =  new ArrayList<>();
        int codigoDisciplina = intent.getIntExtra("codigoDisciplina", -1); // -1 caso não encontrado

        RequisicaoAssincrona requisicao = new RequisicaoAssincrona();

        try {
            JSONObject resultado_requisicao = requisicao.execute(RequisicaoAssincrona.BUSCAR_ALUNOS_INSCRITOS_POR_MATERIA,
                    Integer.toString(codigoDisciplina)).get();

            if (resultado_requisicao.getString("status").equals("ok")) {

                JSONArray alunos_inscritos_json = resultado_requisicao.getJSONArray("alunos");

                for (int i = 0; i < alunos_inscritos_json.length(); i++) {
                    JSONObject aluno_json = alunos_inscritos_json.getJSONObject(i);
                    Aluno aluno = new Aluno(aluno_json);
                    mListAlunos.add(aluno);
                }

            } else {
                Log.w("REQUISICAO", resultado_requisicao.toString());
                Toast.makeText(AlunosInscritosActivity.this,
                        resultado_requisicao.getString("descricao"),
                        Toast.LENGTH_LONG).show();
            }
        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }


        adapter = new AlunoAdapter(this, mListAlunos);
        mListView.setAdapter(adapter);
    }
}
