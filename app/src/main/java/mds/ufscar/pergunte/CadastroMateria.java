package mds.ufscar.pergunte;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import mds.ufscar.pergunte.model.Materia;

/**
 * Created by Danilo on 14/01/2017.
 */

public class CadastroMateria extends AppCompatActivity {

    private Button mBtnCadastrarMat;
    private EditText mClassName;
    private Spinner mSpinYear;
    private Spinner mSpinSemester;
    private Spinner mSpinLetter;
    private EditText mClassCode;
    private String emailUsuarioAtual;
    private Toolbar mToolbar;

    private Button mBotaoCancelar;
    private Button mBotaoCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_materia);

        emailUsuarioAtual = MainScreen.getEmailDoUsuarioAtual();

        // bounding code with interface
//        mBtnCadastrarMat = (Button)findViewById(R.id.btn_cadastrar);
        mClassName = (EditText)findViewById(R.id.input_name);
        mSpinYear = (Spinner)findViewById(R.id.spinner_ano);
        mSpinSemester = (Spinner)findViewById(R.id.spinner_semestre);
        mSpinLetter = (Spinner)findViewById(R.id.spinner_turma);
        mClassCode = (EditText)findViewById(R.id.input_codigo);
        mToolbar = (Toolbar)findViewById(R.id.cadastro_materia_toolbar);
        mToolbar.setTitle("");

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_dark);


        // filling year dropdown button
        ArrayList<String> years = new ArrayList<>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = thisYear; i >= thisYear-5; i--) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, years);
        mSpinYear.setAdapter(yearAdapter);

        //filling semestre dropdown button
        ArrayList<Integer> semesters = new ArrayList<>();
        semesters.add(1);
        semesters.add(2);
        ArrayAdapter<Integer> semAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, semesters);
        mSpinSemester.setAdapter(semAdapter);

        // filling turma dropdown button
        ArrayList<String> turmas = new ArrayList<>();
        turmas.add("A");
        turmas.add("B");
        turmas.add("C");
        turmas.add("D");
        turmas.add("E");
        turmas.add("F");
        ArrayAdapter<String> turmaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, turmas);
        mSpinLetter.setAdapter(turmaAdapter);

        // button click

        mBotaoCadastrar = (Button)findViewById(R.id.botao_cadastrar_materia);
        mBotaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissKeyboard();

                String warning = null;

                if (mClassName.getText().toString().trim().length() == 0) {
                    warning = "O campo nome da matéria é obrigatório.";
                } else if (mClassCode.getText().toString().trim().length() == 0) {
                    warning = "O campo código da matéria é obrigatório.";
                }

                if (warning == null) {
                    String turma = mSpinLetter.getSelectedItem().toString();
                    int ano = Integer.valueOf(mSpinYear.getSelectedItem().toString());
                    int semestre = Integer.valueOf(mSpinSemester.getSelectedItem().toString());
                    String nomeDisciplina = mClassName.getText().toString();
                    String codigoDisciplina = mClassCode.getText().toString();
                    Materia materia = new Materia(
                            0, // código irrelevante aqui, pois será gerado automaticamente no banco
                            turma,
                            ano,
                            semestre,
                            nomeDisciplina,
                            codigoDisciplina
                    );

                    RequisicaoAssincrona requisicao = new RequisicaoAssincrona();
                    requisicao.setObject(materia);

                    try {
                        JSONObject resultado_requisicao = requisicao.execute(RequisicaoAssincrona.CADASTRAR_NOVA_MATERIA,
                                emailUsuarioAtual).get();

                        if (resultado_requisicao != null) {
                            Intent returnIntent = new Intent();
                            if (resultado_requisicao.getString("status").equals("ok")) {
                                returnIntent.putExtra("materia", materia);
                                setResult(Activity.RESULT_OK, returnIntent);
                                finish();
                            } else {
                                Toast.makeText(CadastroMateria.this, resultado_requisicao.getString("descricao"), Toast.LENGTH_LONG).show();
                                setResult(Activity.RESULT_CANCELED, returnIntent);
                            }
                        } else {
                            AlertDialog.Builder adb = new AlertDialog.Builder(CadastroMateria.this);
                            adb.setTitle("Sem acesso à Internet");
                            adb.setMessage("Não foi possível conectar à Internet.\n\nVerifique sua conexão e tente novamente.");
                            adb.setPositiveButton("Tentar novamente", new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    // Não faz nada aqui para voltar à tela de edição da pergunta,
                                    // permitindo ao usuário clicar novamente no botão Cadastrar
                                }
                            });
                            adb.setNegativeButton("Cancelar", new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    AlertDialog.Builder adb2 = new AlertDialog.Builder(CadastroMateria.this);
                                    adb2.setTitle("Cancelar cadastro de matéria?");
                                    adb2.setMessage("Tem certeza que deseja cancelar o cadastro? Os dados informados serão perdidos.");
                                    adb2.setPositiveButton("Sim", new AlertDialog.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(CadastroMateria.this, "Cadastro de matéria cancelado.", Toast.LENGTH_LONG).show();
                                            finish();
                                        }
                                    });
                                    adb2.setNegativeButton("Não", new AlertDialog.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    adb2.show();
                                }
                            });
                            adb.show();
                        }

                    } catch (InterruptedException | ExecutionException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    new AlertDialog.Builder(CadastroMateria.this)
                            .setTitle("Atenção")
                            .setMessage(warning)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });

    }

    public void dismissKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {
        dismissKeyboard();
        AlertDialog.Builder adb = new AlertDialog.Builder(CadastroMateria.this);
        adb.setTitle("Descartar nova matéria?");
        adb.setMessage("Os dados digitados serão perdidos.");
        adb.setPositiveButton("Descartar", new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        adb.setNegativeButton("Voltar para edição", new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        adb.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id==android.R.id.home) {
            onBackPressed();
        }

        return true;
    }
}
