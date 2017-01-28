package mds.ufscar.pergunte;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

        // when child is clicked
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition),
                        Toast.LENGTH_SHORT).show();
                // TODO: fazer isso apenas para pergunta ativa e se perfil aluno
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

        // Adding child data
        ArrayList<Pergunta> perguntasAtivas = new ArrayList<>();
        // TODO: Marcelo popule a pergunta ativa (mesmo com uma tem que ser Array, sorry) (da mMateriaEmQuestao)

        ArrayList<Pergunta> proximasPerguntas = new ArrayList<>();
        // TODO: Marcelo popule o ArrayList acima com as próximas pergutas (da mMateriaEmQuestao)

        ArrayList<Pergunta> perguntasRespondidas = new ArrayList<>();
        // TODO: Marcelo popule o ArrayList acima com as perguntas respondidas (da mMateriaEmQuestao)

        listDataChild.put(listDataHeader.get(0), perguntasAtivas); // Header, Child data
        listDataChild.put(listDataHeader.get(1), proximasPerguntas);
        listDataChild.put(listDataHeader.get(2), perguntasRespondidas);
    }
}
