package mds.ufscar.pergunte;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Danilo on 15/01/2017.
 */

public class CadastroPergunta extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText mInputTitle;
    private EditText mInputQuestion;
    private Spinner mNAlternatives;
//    private Spinner mNRight;
    private Button mDateSelector;
    private TextView mSelectedDate;
    private int mYear;
    private int mMonth;
    private int mDay;
    private DatePickerDialog mDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_pergunta);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.CANADA);

        // linking UI components
        mInputTitle = (EditText)findViewById(R.id.input_title);
        mInputQuestion = (EditText)findViewById(R.id.input_question);
        mNAlternatives = (Spinner)findViewById(R.id.spinner_nAlternatives);
//        mNRight = (Spinner)findViewById(R.id.spinner_nRight);
        mDateSelector = (Button)findViewById(R.id.btn_dateSelector);
        mSelectedDate = (TextView)findViewById(R.id.input_selectedDate);

        // filling spinners
        final ArrayList<Integer> numbersFrom1 = new ArrayList<>();
        for (int i = 1; i<=5; i++)
            numbersFrom1.add(i);
        ArrayAdapter<Integer> nAlternativesAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item, numbersFrom1);
        mNAlternatives.setAdapter(nAlternativesAdapter);
        // check what user selected
        mNAlternatives.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Toast.makeText(CadastroPergunta.this, "Numero de alternativas: " + numbersFrom1.get(position), Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // nothing so far
            }
        });

        setCurrentDateOnView();

        Calendar newCalendar = Calendar.getInstance();
        mDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mSelectedDate.setText(dateFormatter.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        mDateSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePickerDialog.show();
            }
        });
    }

    private void setCurrentDateOnView(){
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // set current date into textview
        mSelectedDate.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(mDay).append("-").append(mMonth + 1).append("-")
                .append(mYear));
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
