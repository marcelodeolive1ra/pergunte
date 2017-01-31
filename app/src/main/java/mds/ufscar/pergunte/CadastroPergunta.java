package mds.ufscar.pergunte;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

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
    private Toolbar mToolbar;
    private Button mBotaoCadastrar;

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

        mToolbar = (Toolbar)findViewById(R.id.cadastro_pergunta_toolbar);
        mToolbar.setTitle("");

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_dark);

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

                    LayoutInflater factory = LayoutInflater.from(context);
                    final View dialogView = factory.inflate(R.layout.alternativa, null);

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setView(dialogView);
                    alertDialog.setTitle("Alternativa " + mAlternativeLetters[index]);

                    final EditText alternativa = (EditText)dialogView.findViewById(R.id.input_alternative);
                    final CheckBox correta = (CheckBox)dialogView.findViewById(R.id.checkboxCorreta);

                    alertDialog.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String textoAlternativa = alternativa.getText().toString();
                            // criando objecto alternativa
                            Alternativa alternativa = new Alternativa(mAlternativeLetters[index],
                                    textoAlternativa, correta.isChecked());
                            mAlternativas.add(alternativa);
                            index++;
                            setRadioButtons(mAlternativas);
                        }
                    });
                    alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    alertDialog.show();

                } else {
                    new AlertDialog.Builder(context)
                            .setTitle("Atenção")
                            .setMessage("Não é possível adicionar mais que " + maxAlternativas + " alternativas")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {}
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });


        mBotaoCadastrar = (Button)findViewById(R.id.botao_cadastrar_pergunta);
        mBotaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String warning = null;
                if (mAlternativas.size() == 0) {
                    warning = "Deve existir pelo menos uma alternativa.";
                }
                if (mInputQuestion.getText().toString().trim().length() == 0) {
                    warning = "Pergunta deve ser preenchida.";
                }
                if (mInputTitle.getText().toString().trim().length() == 0) {
                    warning = "Título deve ser preenchido.";
                }

                if (warning == null) {  // se validou os dados (sem warnings)
                    boolean algumaCorreta = false;

                    for (int i = 0; i < mAlternativas.size() && !algumaCorreta; i++) {
                        if (mAlternativas.get(i).isCorreta()) {
                            algumaCorreta = true;
                        }
                    }

                    if (algumaCorreta) {
                        cadastrarPergunta();
                    } else {
                        AlertDialog.Builder adb = new AlertDialog.Builder(CadastroPergunta.this);
                        adb.setTitle("Nenhuma alternativa marcada como correta");
                        adb.setMessage("Você não marcou nenhuma alternativa como correta.\n\n" +
                                "Tem certeza que deseja cadastrar a pergunta assim mesmo?");
                        adb.setPositiveButton("Sim", new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                cadastrarPergunta();
                            }
                        });
                        adb.setNegativeButton("Voltar para edição", new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        adb.show();
                    }

                } else {
                    new AlertDialog.Builder(context)
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

            public void cadastrarPergunta() {
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

                RequisicaoAssincrona requisicao = new RequisicaoAssincrona();
                requisicao.setObject(pergunta);

                try {
                    JSONObject resultado_requisicao = requisicao.execute(RequisicaoAssincrona.CADASTRAR_NOVA_PERGUNTA,
                            MainScreen.getEmailDoUsuarioAtual(), Integer.toString(mCodigoMateria)).get();

                    if (resultado_requisicao != null) {
                        Intent returnIntent = new Intent();
                        if (resultado_requisicao.getString("status").equals("ok")) {
                            Toast.makeText(CadastroPergunta.this, "Pergunta cadastrada com sucesso.", Toast.LENGTH_LONG).show();
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                        } else {
                            Log.w("REQUISICAO", resultado_requisicao.toString());
                            Toast.makeText(CadastroPergunta.this, resultado_requisicao.getString("descricao"), Toast.LENGTH_LONG).show();
                            setResult(Activity.RESULT_CANCELED, returnIntent);
                        }
                    } else {
                        AlertDialog.Builder adb = new AlertDialog.Builder(CadastroPergunta.this);
                        adb.setTitle("Erro");
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
                                AlertDialog.Builder adb2 = new AlertDialog.Builder(CadastroPergunta.this);
                                adb2.setTitle("Cancelar cadastro de pergunta?");
                                adb2.setMessage("Tem certeza que deseja cancelar o cadastro? Os dados informados serão perdidos.");
                                adb2.setPositiveButton("Sim", new AlertDialog.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(CadastroPergunta.this, "Cadastro de pergunta cancelado.", Toast.LENGTH_LONG).show();
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
                    LayoutInflater factory = LayoutInflater.from(context);
                    final View dialogView = factory.inflate(R.layout.alternativa, null);

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setView(dialogView);
                    alertDialog.setTitle("Alternativa " + mAlternativeLetters[pos]);

                    // set the custom dialog components - text, editText and button
                    final EditText alternativa = (EditText)dialogView.findViewById(R.id.input_alternative);
                    final CheckBox correta = (CheckBox)dialogView.findViewById(R.id.checkboxCorreta);

                    alternativa.setText(mAlternativas.get(pos).getTextoAlternativa());
                    correta.setChecked(mAlternativas.get(pos).isCorreta());

                    alertDialog.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String textoAlternativa = alternativa.getText().toString();
                            // criando objeto alternativa
                            Alternativa alternativa = new Alternativa(mAlternativeLetters[index],
                                    textoAlternativa, correta.isChecked());
                            mAlternativas.add(alternativa);
                            index++;
                            setRadioButtons(mAlternativas);
                        }
                    });

                    // excluir alternativa
                    alertDialog.setNegativeButton("Excluir alternativa", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
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

                    alertDialog.show();
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
            if (alternativa.isCorreta()) {
                rdbtn.setTypeface(null, Typeface.BOLD);
                rdbtn.setTextColor(getResources().getColor(R.color.green_600));
            }
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

    public void dismissKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id==android.R.id.home) {
            onBackPressed();
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        dismissKeyboard();
        AlertDialog.Builder adb = new AlertDialog.Builder(CadastroPergunta.this);
        adb.setTitle("Descartar nova pergunta?");
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
}
