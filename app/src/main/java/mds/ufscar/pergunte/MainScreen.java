package mds.ufscar.pergunte;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.Result;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

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
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mProfessor) {
                    Intent cadastroMateria = new Intent(MainScreen.this, CadastroMateria.class);
                    startActivity(cadastroMateria);
                } else {
                    IntentIntegrator integrator = new IntentIntegrator(activity);
                    integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                    integrator.setPrompt("Scan");
                    integrator.setCameraId(0);
                    integrator.setBeepEnabled(false);
                    integrator.setBarcodeImageEnabled(false);
                    integrator.initiateScan();
                }
            }
        });

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
                try {
                    String retorno_requisicao = requisicao.execute("buscarmateriaporqr", result.getContents()).get();
                    JSONObject retorno_requisicao_json = new JSONObject(retorno_requisicao);
                    nome_materia = retorno_requisicao_json.getString("nome_materia");
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

        @Override
        public Fragment getItem(int position) {
            // return the current tab
            switch (position) {
                case 0:
                    Tab1_Respondidas tab1Respondidas = new Tab1_Respondidas();
                    return tab1Respondidas;
                case 1:
                    Tab2_Materias tab2Materias = new Tab2_Materias();
                    return tab2Materias;
                case 2:
                    Tab3_Estatisticas tab3Estatisticas = new Tab3_Estatisticas();
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
                    return mProfessor ? "Histórico" : "Respondidas";
                case 1:
                    return "Matérias";
                case 2:
                    return "Estatísticas";
            }
            return null;
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
}