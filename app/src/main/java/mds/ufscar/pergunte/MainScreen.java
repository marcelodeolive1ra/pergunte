package mds.ufscar.pergunte;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import mds.ufscar.pergunte.model.Materia;
import mds.ufscar.pergunte.model.Pessoa;
import mds.ufscar.pergunte.model.Professor;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainScreen extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String mPerfil;
    private boolean mProfessor;
    private ZXingScannerView mScanner;
    private Materia materiaScanneada;
    static Pessoa usuarioAtual;

    private Map<Integer, Fragment> mPageReferenceMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        mPageReferenceMap = new HashMap<>();

        // setting selected profile
        mPerfil = this.getIntent().getStringExtra("perfil");
        Toast.makeText(this, "Bem vindo(a) " + mPerfil, Toast.LENGTH_SHORT).show();
        if (mPerfil.equalsIgnoreCase("professor(a)")) {
            mProfessor = true;
        } else {
            mProfessor = false;
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(1);   // tab Materias is the default tab
        final Activity activity = this;

        // authentication code
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(MainScreen.this, MainActivity.class));
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if(result != null){
            if(result.getContents()==null){
                Toast.makeText(this, "Voce cancelou o scanning", Toast.LENGTH_LONG).show();
            }
            else {
                String nome_materia = "";

                RequisicaoAssincrona requisicao = new RequisicaoAssincrona();
                final String codigoInscricao = result.getContents();

                try {
                    JSONObject resultado_requisicao = requisicao.execute("buscarmateriaporqr", codigoInscricao).get();
                    nome_materia = resultado_requisicao.getString("nome_materia");
                    JSONObject professor_json = resultado_requisicao.getJSONObject("professor");
                    materiaScanneada = new Materia(
                            resultado_requisicao.getInt("codigo"),
                            resultado_requisicao.getString("turma"),
                            resultado_requisicao.getInt("ano"),
                            resultado_requisicao.getInt("semestre"),
                            resultado_requisicao.getString("nome_materia"),
                            new Professor(professor_json.getString("nome"),
                                    professor_json.getString("sobrenome"),
                                    professor_json.getString("email"),
                                    professor_json.getString("universidade")),
                            resultado_requisicao.getString("codigo_inscricao")
                    );
                } catch (InterruptedException | ExecutionException | JSONException e) {
                    e.printStackTrace();
                }
                if (nome_materia.equals("")) {
                    new AlertDialog.Builder(this)
                            .setTitle("Erro na leitura. Tente novamente!")

                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle("Tem certeza que deseja se cadastrar nessa matéria?")
                            .setMessage(nome_materia)
                            .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    String email = (user != null) ? user.getEmail() : "";

                                    RequisicaoAssincrona requisicao = new RequisicaoAssincrona();

                                    try {
                                        JSONObject resultado_requisicao =
                                                requisicao.execute("inscreveralunoemmateria", email, codigoInscricao).get();

                                        if (resultado_requisicao.getString("status").equals("ok")) {
                                            Tab2_Materias.materias.add(materiaScanneada);
                                            Toast.makeText(MainScreen.this, "Cadastro feito com sucesso", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(MainScreen.this, "Erro ao cadastrar, status: " + status, Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (InterruptedException | ExecutionException | JSONException e) {
                                        e.printStackTrace();
                                    }
                                    // TODO: Marcelo por favor monte o objeto Materia já pronto para ser adicionado. Grato.

                                    int index = mViewPager.getCurrentItem();
                                    SectionsPagerAdapter adapter = ((SectionsPagerAdapter)mViewPager.getAdapter());
                                    Tab2_Materias fragment = (Tab2_Materias)adapter.getFragment(1);
                                    if (fragment != null)
                                        fragment.addMateria();
                                    else
                                        Toast.makeText(MainScreen.this, "Erro ao atualizar lista de matérias", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }

            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    protected void onPause(){
        super.onPause();
//        mScanner.stopCamera();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            mAuth.signOut();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getFragment(int key) {
            return mPageReferenceMap.get(key);
        }

        @Override
        public Fragment getItem(int position) {
            // return the current tab
            switch (position) {
                case 0:
                    Tab1_Respondidas tab1Respondidas = new Tab1_Respondidas();
                    mPageReferenceMap.put(position, tab1Respondidas);
                    return tab1Respondidas;
                case 1:
                    Tab2_Materias tab2Materias = new Tab2_Materias();
                    mPageReferenceMap.put(position, tab2Materias);
                    return tab2Materias;
                case 2:
                    Tab3_Estatisticas tab3Estatisticas = new Tab3_Estatisticas();
                    mPageReferenceMap.put(position, tab3Estatisticas);
                    return tab3Estatisticas;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return isProfessor() ? "Histórico" : "Respondidas";
                case 1:
                    return "Matérias";
                case 2:
                    return "Estatísticas";
            }
            return null;
        }

        /**
         * After an orientation change, the fragments are saved in the adapter, and
         * I don't want to double save them: I will retrieve them and put them in my
         * list again here.
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            mPageReferenceMap.put(position, fragment);
            return fragment;
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            mPageReferenceMap.remove(position);
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    public boolean isProfessor() {
        return mProfessor;
    }

    public static String getEmailDoUsuarioAtual() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return (user != null) ? user.getEmail() : "";
    }
}