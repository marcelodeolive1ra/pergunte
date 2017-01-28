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
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

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
    private SparseArrayCompat<Fragment> mPageReferenceMap;

    // padronizações
    static final int cadastroMateriaCode = 2; // for differentiate at onResultActivity
    static final int cadastroPerguntaCode = 3; // for differentiate at onResultActivity
    static final String perfilProfessor = "professor(a)";
    static final String perfilAluno = "aluno(a)";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        mPageReferenceMap = new SparseArrayCompat<>();

        // setting selected profile
        mPerfil = this.getIntent().getStringExtra("perfil");
        Toast.makeText(this, "Bem vindo(a) " + mPerfil, Toast.LENGTH_SHORT).show();
        if (mPerfil.equalsIgnoreCase(perfilProfessor)) {
            mProfessor = true;
        } else {
            mProfessor = false;
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if ((getSupportActionBar() != null) && (isProfessor())) {
            getSupportActionBar().setTitle("Pergunte - Perfil Professor");
        } else {
            getSupportActionBar().setTitle("Pergunte - Perfil Aluno");
        }
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
        Log.v("resultCode", String.valueOf(resultCode));
        if (requestCode == cadastroMateriaCode) {
            if (resultCode == Activity.RESULT_OK) {
                Materia materiaASerAdd = data.getParcelableExtra("materia");
                if (adicionouMateria(materiaASerAdd, true)) { // true = é professor (linkar com o professor)
                    Toast.makeText(this, "Matéria cadastrada com sucesso!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Erro ao atualizar lista de matérias", Toast.LENGTH_LONG).show();
                }
            }
        } else if (requestCode == cadastroPerguntaCode) {
            if (resultCode == Activity.RESULT_OK) {
                if (!refreshPerguntasTab1()) {
                    Toast.makeText(this, "Não foi possível atualizar a lista de perguntas neste momento", Toast.LENGTH_LONG).show();
                }
            }
        } else {    // TODO: Thiago, seria legal ser else if (requestCode == ScannerRequestCode) algo assim
            if(result != null){
                if(result.getContents() == null){
                    Toast.makeText(this, "Voce cancelou o scanning", Toast.LENGTH_LONG).show();
                }
                else {
                    RequisicaoAssincrona requisicao = new RequisicaoAssincrona();
                    final String codigoInscricao = result.getContents();

                    try {
                        JSONObject resultado_requisicao = requisicao.execute(RequisicaoAssincrona.BUSCAR_MATERIA_POR_QR_CODE,
                                getEmailDoUsuarioAtual(), codigoInscricao).get();

                        String status_requisicao = resultado_requisicao.getString("status");

                        if (!status_requisicao.equals("ok")) {
                            new AlertDialog.Builder(this)
                                    .setTitle("Erro!")
                                    .setMessage(resultado_requisicao.getString("descricao"))
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {}
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        } else {
                            new AlertDialog.Builder(this)
                                    .setTitle(codigoInscricao)
                                    .setMessage("Tem certeza que deseja se cadastrar na matéria \"" +
                                            resultado_requisicao.getString("nome_materia") + "\"?")
                                    .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            RequisicaoAssincrona requisicao = new RequisicaoAssincrona();

                                            try {
                                                JSONObject resultado_requisicao =
                                                        requisicao.execute(RequisicaoAssincrona.INSCREVER_ALUNO_EM_MATERIA,
                                                                getEmailDoUsuarioAtual(), codigoInscricao).get();

                                                if (resultado_requisicao.getString("status").equals("ok")) {
                                                    materiaScanneada = new Materia(resultado_requisicao);
                                                    if (!adicionouMateria(materiaScanneada, false)) {   // false = é aluno
                                                        Toast.makeText(MainScreen.this, "Erro ao atualizar lista de matérias.", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(MainScreen.this, "Cadastro realizado com sucesso.", Toast.LENGTH_SHORT).show();
                                                        FirebaseMessaging.getInstance().subscribeToTopic(materiaScanneada.getCodigoInscricao());
                                                    }
                                                } else {
                                                    Toast.makeText(MainScreen.this, resultado_requisicao.getString("descricao"), Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (InterruptedException | ExecutionException | JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    })
                                    .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }

                    } catch (InterruptedException | ExecutionException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }

    }

    @Override
    protected void onPause(){
        super.onPause();
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
        if (id == R.id.logout) {
            mAuth.signOut();
        } else if (id == R.id.change_profile) {
            finish();
            if (!isProfessor()) { //se não é professor
                startActivity(getIntent().putExtra("perfil", perfilProfessor)); // agora é
            } else {
                startActivity(getIntent().putExtra("perfil", perfilAluno)); // agora é professor então
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean adicionouMateria(Materia materia, boolean linkProfessor) {
        int indexCurrentTab = mViewPager.getCurrentItem();
        SectionsPagerAdapter adapter = ((SectionsPagerAdapter) mViewPager.getAdapter());
        Tab2_Materias fragment = (Tab2_Materias) adapter.getFragment(indexCurrentTab);
        if (fragment == null) {
            return false;
        } else {
            if (linkProfessor) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String nomeProfessor = user.getDisplayName();
                String[] nomes = nomeProfessor.split(" ");
                StringBuilder sobrenome = new StringBuilder();
                for (int i = 1; i<nomes.length; i++) {
                    sobrenome.append(nomes[i]).append(" ");
                }
                // TODO: universidade truncada no código, mudar isso
                Professor professor = new Professor(nomes[0], sobrenome.toString(), user.getEmail(), "UFSCar");
                materia.setProfessor(professor);
            }
            fragment.addMateria(materia);
        }
        return true;
    }

    public boolean refreshPerguntasTab1() {
        SectionsPagerAdapter adapter = ((SectionsPagerAdapter) mViewPager.getAdapter());
        Tab1_Perguntas fragment = (Tab1_Perguntas) adapter.getFragment(0);  // cuidado aqui, sempre 0?
        if (fragment == null) {
            return false;
        } else {
            fragment.buscaPerguntasServidor();
        }
        return true;
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
                    Tab1_Perguntas tab1Respondidas = new Tab1_Perguntas();
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
                    return isProfessor() ? "Perguntas" : "Respondidas";
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