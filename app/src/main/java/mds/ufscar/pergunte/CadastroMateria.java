package mds.ufscar.pergunte;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_materia);

        emailUsuarioAtual = MainScreen.getEmailDoUsuarioAtual();

        // bounding code with interface
        mBtnCadastrarMat = (Button)findViewById(R.id.btn_cadastrar);
        mClassName = (EditText)findViewById(R.id.input_name);
        mSpinYear = (Spinner)findViewById(R.id.spinner_ano);
        mSpinSemester = (Spinner)findViewById(R.id.spinner_semestre);
        mSpinLetter = (Spinner)findViewById(R.id.spinner_turma);
        mClassCode = (EditText)findViewById(R.id.input_codigo);

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
        mBtnCadastrarMat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    Intent returnIntent = new Intent();
                    if (resultado_requisicao.getString("status").equals("ok")) {
                        returnIntent.putExtra("materia", materia);
                        setResult(Activity.RESULT_OK, returnIntent);
                    } else {
                        Toast.makeText(CadastroMateria.this, "Erro ao cadastrar matéria no BD.", Toast.LENGTH_LONG).show();
                        setResult(Activity.RESULT_CANCELED, returnIntent);
                    }

                } catch (InterruptedException | ExecutionException | JSONException e) {
                    e.printStackTrace();
                }

                finish();
            }
        });
    }
}
