package mds.ufscar.pergunte.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import java.util.ArrayList;

import mds.ufscar.pergunte.R;
import mds.ufscar.pergunte.adapters.AlunoAdapter;
import mds.ufscar.pergunte.models.Aluno;

/**
 * Created by Danilo on 13/02/2017.
 */

public class AlunosInscritosActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ListView mListView;
    private ArrayList<Aluno> mListAlunos;
    private AlunoAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alunos_inscritos);

        Intent intent = getIntent();

        mListView = (ListView) findViewById(R.id.aluno_list_view);
        mToolbar = (Toolbar)findViewById(R.id.alunos_inscritos_toolbar);
        mToolbar.setTitle(intent.getStringExtra("tituloDisciplina"));
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));

        mListAlunos =  new ArrayList<>();
        int codigoDisciplina = intent.getIntExtra("codigoDisciplina", -1); // -1 caso não encontrado
        // TODO: Mah preencher com alunos

        adapter = new AlunoAdapter(this, mListAlunos);
        mListView.setAdapter(adapter);
    }
}
