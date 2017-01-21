package mds.ufscar.pergunte;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import mds.ufscar.pergunte.model.Alternativa;
import mds.ufscar.pergunte.model.Pergunta;

/**
 * Created by Danilo on 15/01/2017.
 */

public class CadastroPergunta extends AppCompatActivity {

    private EditText mInputTitle;
    private EditText mInputQuestion;
    private Button mDateSelector;
    private Button mEditAlternative;
    private Button mAddAlternative;
    private Button mCadastrarPergunta;
    private TextView mSelectedDate;
    private int mYear;
    private int mMonth;
    private int mDay;
    private DatePickerDialog mDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private String mTextAlternative;
    private final int maxAlternativas = 5;
    private final String mAlternativeLetters[] = {"A", "B", "C", "D", "E"};
    private int index;
    private int correta;
    private ArrayList<Alternativa> mAlternativas;
    private int mCodigoMateria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_pergunta);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.CANADA);
        final Context context = this;
        index = 0;  // para letra da alternativa
        mAlternativas = new ArrayList<>();
        // -1 = erro, alguem chamou essa activity sem passar o codigo da materia
        mCodigoMateria = this.getIntent().getIntExtra("materiaID", -1);

        // linking UI components
        mInputTitle = (EditText)findViewById(R.id.input_title);
        mInputQuestion = (EditText)findViewById(R.id.input_question);
        mDateSelector = (Button)findViewById(R.id.btn_dateSelector);
        mSelectedDate = (TextView)findViewById(R.id.input_selectedDate);
        mEditAlternative = (Button)findViewById(R.id.btn_editAlternative);
        mAddAlternative = (Button)findViewById(R.id.btn_newAlternative);
        mCadastrarPergunta = (Button)findViewById(R.id.btn_cadastrar);

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
                if (index < maxAlternativas) {
                    // custom dialog
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.alternativa);

                    // set the custom dialog components - text, editText and button
                    final TextView letra = (TextView) dialog.findViewById(R.id.label_alternativa);
                    String title = "Alternativa - " + mAlternativeLetters[index];
                    letra.setText(title);
                    final EditText alternativa = (EditText) dialog.findViewById(R.id.input_alternative);
                    final CheckBox correta = (CheckBox) dialog.findViewById(R.id.checkboxCorreta);

                    Button cancelButton = (Button) dialog.findViewById(R.id.btn_cancel);
                    Button okButton = (Button) dialog.findViewById(R.id.btn_ok);
                    // if button ok is clicked, add alternative and close the custom dialog
                    okButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String textoAlternativa =
                                    mAlternativeLetters[index] + ") " + alternativa.getText().toString();
                            addRadioButton(textoAlternativa, correta.isChecked());
                            // criando objecto alternativa
                            Alternativa alternativa = new Alternativa(mAlternativeLetters[index],
                                    textoAlternativa, correta.isChecked());
                            mAlternativas.add(alternativa);
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
                } else {
                    new AlertDialog.Builder(context)
                            .setTitle("Atenção")
                            .setMessage("Não é possível adicionar mais que " + maxAlternativas + " mAlternativas")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {}
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });

        mCadastrarPergunta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                try {
                    date = dateFormatter.parse(mSelectedDate.getText().toString());
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                // criando objeto pergunta
                Pergunta pergunta = new Pergunta(
                        mInputTitle.getText().toString(),
                        mInputQuestion.getText().toString(),
                        mAlternativas,
                        date
                );



                // TODO: Marcelo, persista no BD. Grato.

                finish();
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

    public void addRadioButton(String text, boolean correct) {
        RadioGroup radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(LinearLayout.VERTICAL);
        RadioButton rdbtn = new RadioButton(this);
        rdbtn.setText(text);
        radioGroup.addView(rdbtn);
        ((ViewGroup) findViewById(R.id.radiogroup)).addView(radioGroup);
        index++;
    }
}
