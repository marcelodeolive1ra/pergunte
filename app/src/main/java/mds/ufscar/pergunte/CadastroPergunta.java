package mds.ufscar.pergunte;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Danilo on 15/01/2017.
 */

public class CadastroPergunta extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText mInputTitle;
    private EditText mInputQuestion;
    private Button mDateSelector;
    private Button mEditAlternative;
    private Button mAddAlternative;
    private TextView mSelectedDate;
    private int mYear;
    private int mMonth;
    private int mDay;
    private DatePickerDialog mDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private String mTextAlternative;
    private final String mAlternativeLetters[] = {"A", "B", "C", "D", "E"};
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_pergunta);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.CANADA);
        final Context context = this;

        // linking UI components
        mInputTitle = (EditText)findViewById(R.id.input_title);
        mInputQuestion = (EditText)findViewById(R.id.input_question);
        mDateSelector = (Button)findViewById(R.id.btn_dateSelector);
        mSelectedDate = (TextView)findViewById(R.id.input_selectedDate);
        mEditAlternative = (Button)findViewById(R.id.btn_editAlternative);
        mAddAlternative = (Button)findViewById(R.id.btn_newAlternative);

        setCurrentDateOnView();

        Calendar newCalendar = Calendar.getInstance();
        mDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mSelectedDate.setText(dateFormatter.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        // Buttons
        index = 0;
        mDateSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePickerDialog.show();
            }
        });

        mAddAlternative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // custom dialog
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.alternativa);
                dialog.setTitle("Alternativa - ");

                // set the custom dialog components - text, image and button
                final EditText alternativa = (EditText) dialog.findViewById(R.id.input_alternative);
                final CheckBox correta = (CheckBox) dialog.findViewById(R.id.checkboxCorreta);

                Button cancelButton = (Button) dialog.findViewById(R.id.btn_cancel);
                Button okButton = (Button) dialog.findViewById(R.id.btn_ok);
                // if button ok is clicked, add alternative and close the custom dialog
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addRadioButton(alternativa.getText().toString(), correta.isChecked());
                        dialog.dismiss();
                    }
                });
                // cancel: just close the dialog
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
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
        if (id == R.id.logout) {
            mAuth.signOut();
        }

        return super.onOptionsItemSelected(item);
    }

    public void addRadioButton(String text, boolean correct) {
        RadioGroup radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(LinearLayout.VERTICAL);
        RadioButton rdbtn = new RadioButton(this);
        rdbtn.setText(text);
        radioGroup.addView(rdbtn);
        ((ViewGroup) findViewById(R.id.radiogroup)).addView(radioGroup);
    }
}
