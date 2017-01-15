package mds.ufscar.pergunte;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Danilo on 14/01/2017.
 */

public class CadastroMateria extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button mBtnCadastrarMat;
    private EditText mClassName;
    private Spinner mSpinYear;
    private Spinner mSpinSemester;
    private Spinner mSpinLetter;
    private EditText mClassCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_materia);

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
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        Spinner spinYear = (Spinner)findViewById(R.id.spinner_ano);
        spinYear.setAdapter(yearAdapter);

        //filling semestre dropdown button
        ArrayList<Integer> semesters = new ArrayList<>();
        semesters.add(1);
        semesters.add(2);
        ArrayAdapter<Integer> semAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, semesters);
        Spinner spinSem = (Spinner)findViewById(R.id.spinner_semestre);
        spinSem.setAdapter(semAdapter);

        // filling turma dropdown button
        String str = "ABCDEZ";
        ArrayList<Character> chars = new ArrayList<Character>();
        for (char c : str.toCharArray()) {
            chars.add(c);
        }
        ArrayAdapter<Character> turmaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, chars);
        Spinner spinTurma = (Spinner)findViewById(R.id.spinner_turma);
        spinTurma.setAdapter(turmaAdapter);

        // button click
        mBtnCadastrarMat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String turma = mSpinLetter.getSelectedItem().toString();
                int ano = Integer.valueOf(mSpinYear.getSelectedItem().toString());
                int sem = Integer.valueOf(mSpinSemester.getSelectedItem().toString());
                String nomeDisc = mClassName.getText().toString();
                String codigoDisc = mClassCode.getText().toString();
//                Materia materia = new Materia();
                // TODO: Marcelo coloque no BD, grato. haha
            }
        });
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
}
