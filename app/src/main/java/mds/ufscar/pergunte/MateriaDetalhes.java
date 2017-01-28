package mds.ufscar.pergunte;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mds.ufscar.pergunte.model.Materia;
import mds.ufscar.pergunte.model.Professor;

/**
 * Created by Danilo on 28/01/2017.
 */

public class MateriaDetalhes extends AppCompatActivity {

    private TextView mMateriaTitulo;
    private TextView mMateriaInfo;
    private TextView mMateriaCodigo;
    private ImageView mMateriaImagem;
    // para a lista expandível
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.materia_detalhes);
        mMateriaTitulo = (TextView) findViewById(R.id.materia_list_title);
        mMateriaInfo = (TextView) findViewById(R.id.materia_list_subtitle);
        mMateriaCodigo = (TextView) findViewById(R.id.materia_list_code);
        mMateriaImagem = (ImageView) findViewById(R.id.materia_list_thumbnail);

        Intent intent = getIntent();
        Materia materia = intent.getParcelableExtra("materia");
        Professor professor = new Professor();
        professor.setNome(intent.getStringExtra("nome"));
        professor.setSobrenome(intent.getStringExtra("sobrenome"));
        professor.setEmail(intent.getStringExtra("email"));
        professor.setUniversidade(intent.getStringExtra("universidade"));
        materia.setProfessor(professor);

        mMateriaTitulo.setText(materia.getNomeDisciplina());
        mMateriaInfo.setText(materia.getDescricao());
        String codigo = "Código de inscrição: " + materia.getCodigoInscricao();
        mMateriaCodigo.setText(codigo);
        Picasso.with(this).load(materia.getImageUrl()).placeholder(R.mipmap.ic_launcher).into(mMateriaImagem);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Ativas");
        listDataHeader.add("Próximas");
        listDataHeader.add("Respondidas");

        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("The Shawshank Redemption");
        top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");
        top250.add("The Good, the Bad and the Ugly");
        top250.add("The Dark Knight");
        top250.add("12 Angry Men");

        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("The Conjuring");
        nowShowing.add("Despicable Me 2");
        nowShowing.add("Turbo");
        nowShowing.add("Grown Ups 2");
        nowShowing.add("Red 2");
        nowShowing.add("The Wolverine");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("2 Guns");
        comingSoon.add("The Smurfs 2");
        comingSoon.add("The Spectacular Now");
        comingSoon.add("The Canyons");
        comingSoon.add("Europa Report");

        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
    }
}
