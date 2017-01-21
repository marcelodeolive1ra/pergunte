package mds.ufscar.pergunte;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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

import static mds.ufscar.pergunte.R.id.radiogroup;

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
    private DatePickerDialog mDatePickerDialog;
    private SimpleDateFormat mDateFormat;
    private RadioGroup mRadioGroup;
    private final int maxAlternativas = 5;
    private final String mAlternativeLetters[] = {"A", "B", "C", "D", "E"};
    private int index;
    private ArrayList<Alternativa> mAlternativas;
    private int mCodigoMateria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_pergunta);
        mDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.CANADA);
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
        mRadioGroup = (RadioGroup)findViewById(radiogroup);
        mCadastrarPergunta = (Button)findViewById(R.id.btn_cadastrar);

        setCurrentDateOnView();
        mRadioGroup.setOrientation(LinearLayout.VERTICAL);

        Calendar newCalendar = Calendar.getInstance();
        mDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mSelectedDate.setText(mDateFormat.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        // Buttons
        index = 0;
        mDateSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
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

                    final Button cancelButton = (Button) dialog.findViewById(R.id.btn_cancel);
                    final Button okButton = (Button) dialog.findViewById(R.id.btn_ok);

                    okButton.setEnabled(false);
                    addTextChangedListener(alternativa, okButton);

                    // if button ok is clicked, add alternative and close the custom dialog
                    okButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String textoAlternativa = alternativa.getText().toString();
                            // criando objecto alternativa
                            Alternativa alternativa = new Alternativa(mAlternativeLetters[index],
                                    textoAlternativa, correta.isChecked());
                            mAlternativas.add(alternativa);
                            index++;
                            setRadioButtons(mAlternativas);
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
                String warning = null;
                if (mAlternativas.size() == 0 ) {
                    warning = "Deve existir pelo menos uma alternativa";
                }
                if (mInputQuestion.getText().toString().trim().length() == 0) {
                    warning = "Pergunta deve ser preenchida";
                }
                if (mInputTitle.getText().toString().trim().length() == 0) {
                    warning = "Título deve ser preenchido";
                }

                if (warning == null) {  // se validou os dados (sem warnings)
                    Date date = new Date();
                    try {
                        date = mDateFormat.parse(mSelectedDate.getText().toString());
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
                } else {
                    new AlertDialog.Builder(context)
                            .setTitle("Atenção")
                            .setMessage(warning)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {}
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });

        mEditAlternative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioButtonID = mRadioGroup.getCheckedRadioButtonId();
                View radioButton = mRadioGroup.findViewById(radioButtonID);
                final int pos = mRadioGroup.indexOfChild(radioButton);

                if (pos > -1) {
                    // custom dialog
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.alternativa);

                    // set the custom dialog components - text, editText and button
                    final TextView letra = (TextView) dialog.findViewById(R.id.label_alternativa);
                    String title = "Alternativa - " + mAlternativeLetters[pos];
                    letra.setText(title);
                    final EditText alternativa = (EditText) dialog.findViewById(R.id.input_alternative);
                    alternativa.setText(mAlternativas.get(pos).getTextoAlternativa());
                    final CheckBox correta = (CheckBox) dialog.findViewById(R.id.checkboxCorreta);
                    correta.setChecked(mAlternativas.get(pos).isCorreta());

                    final Button cancelButton = (Button) dialog.findViewById(R.id.btn_cancel);
                    String excluir = "Excluir";
                    cancelButton.setText(excluir);
                    final Button okButton = (Button) dialog.findViewById(R.id.btn_ok);
                    String salvar = "Salvar";
                    okButton.setText(salvar);

                    addTextChangedListener(alternativa, okButton);

                    // if button Salvar is clicked, edit alternative and close the custom dialog
                    okButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String textoAlternativa = alternativa.getText().toString();
                            // editando objecto alternativa
                            mAlternativas.get(pos).setTextoAlternativa(textoAlternativa);
                            mAlternativas.get(pos).setCorreta(correta.isChecked());
                            setRadioButtons(mAlternativas);
                            dialog.dismiss();
                        }
                    });
                    // excluir alternativa
                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mAlternativas.remove(pos);
                            // reset letters
                            index = 0;
                            for (Alternativa alternativa : mAlternativas) {
                                alternativa.setLetra(mAlternativeLetters[index]);
                                index++;
                            }
                            setRadioButtons(mAlternativas);
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }
            }
        });
    }

    private void setCurrentDateOnView(){
        final Date actualDate = Calendar.getInstance().getTime();
        // set current date into textview
        mSelectedDate.setText(mDateFormat.format(actualDate));
    }

    public void setRadioButtons(ArrayList<Alternativa> alternativas) {
        mRadioGroup.removeAllViews();   // clearing added buttons if any
        for (Alternativa alternativa : alternativas) {
            RadioButton rdbtn = new RadioButton(this);
            String textoComLetra = alternativa.getLetra() + ") " + alternativa.getTextoAlternativa();
            rdbtn.setText(textoComLetra);
            mRadioGroup.addView(rdbtn);
        }
    }

    public void addTextChangedListener(final EditText editText, final Button btn) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editText.getText().toString().trim().length() > 0) {
                    btn.setEnabled(true);
                } else {
                    btn.setEnabled(false);
                }
            }
        });
    }
}
