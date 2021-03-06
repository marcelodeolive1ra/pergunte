package mds.ufscar.pergunte.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import mds.ufscar.pergunte.R;
import mds.ufscar.pergunte.helpers.RequisicaoAssincrona;
import mds.ufscar.pergunte.models.Alternativa;
import mds.ufscar.pergunte.models.Pergunta;

/**
 * Created by Danilo on 27/01/2017.
 */

public class ResponderPerguntaActivity extends AppCompatActivity {

    private TextView mPergunta;
    private RadioGroup mAlternativas;
    private Button mResponder;
    private Toolbar mToolbar;
    private TextView mDetalhesMateria;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.responder_pergunta_activity);
        mPergunta = (TextView) findViewById(R.id.pergunta_content);
        mAlternativas = (RadioGroup) findViewById(R.id.radiogroup);
        mResponder = (Button) findViewById(R.id.btn_responder);
        mDetalhesMateria = (TextView)findViewById(R.id.detalhes_materia);

        mToolbar = (Toolbar)findViewById(R.id.tela_resposta_toolbar);
        mToolbar.setTitle("");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_dark);

        Intent intent = getIntent();

        final Pergunta pergunta = intent.getParcelableExtra("pergunta");
        final ArrayList<Alternativa> alternativas = intent.getParcelableArrayListExtra("alternativas");
        pergunta.setAlternativas(alternativas);
        int nCorretas = 0;
        for (Alternativa alternativa : alternativas) {
            if (alternativa.isCorreta()) {
                nCorretas++;
            }
        }

        String nomeMateria = intent.getStringExtra("nome_materia") + " - Pergunta #" + pergunta.getCodigo();
        mDetalhesMateria.setText(nomeMateria);

        mPergunta.setText(pergunta.getTextoPergunta());
        for (Alternativa alternativa : alternativas) {
            View btn;
            String textoComLetra = alternativa.getLetra() + ") " + alternativa.getTextoAlternativa();
            if (nCorretas == 1) {
                btn = new RadioButton(this);
                ((RadioButton)btn).setText(textoComLetra);
            } else {
                btn = new CheckBox(this);
                ((CheckBox)btn).setText(textoComLetra);
            }
            mAlternativas.addView(btn);
        }

        mResponder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = mAlternativas.getChildCount();
                ArrayList<Integer> posicaoEscolhidas = new ArrayList<>();
                for (int i=0; i<count; i++) {
                    View btn = mAlternativas.getChildAt(i);
                    if (btn instanceof RadioButton && ((RadioButton) btn).isChecked()) {
                        posicaoEscolhidas.add(i);
                    } else if (btn instanceof CheckBox && ((CheckBox) btn).isChecked()) {
                        posicaoEscolhidas.add(i);
                    }
                }

                String[] codigosAlternativasEscolhidas = new String[posicaoEscolhidas.size()];

                for (int i = 0; i < posicaoEscolhidas.size(); i++) {
                    codigosAlternativasEscolhidas[i] = Integer.toString(alternativas.get(posicaoEscolhidas.get(i)).getCodigo());
                }

                RequisicaoAssincrona requisicao = new RequisicaoAssincrona();
                requisicao.setObject(codigosAlternativasEscolhidas);

                try {
                    JSONObject resultado_requisicao = requisicao.execute(RequisicaoAssincrona.REGISTRAR_RESPOSTA,
                            MainScreenActivity.getEmailDoUsuarioAtual(), Integer.toString(pergunta.getCodigo())).get();

                    if (resultado_requisicao != null) {
                        if (resultado_requisicao.getString("status").equals("ok")) {
                            Toast.makeText(ResponderPerguntaActivity.this,
                                    "Resposta registrada!", Toast.LENGTH_LONG).show();
                        } else {
                            Log.w("REQUISICAO", resultado_requisicao.toString());
                            Toast.makeText(ResponderPerguntaActivity.this,
                                    resultado_requisicao.getString("descricao"), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        AlertDialog.Builder adb = new AlertDialog.Builder(ResponderPerguntaActivity.this);
                        adb.setTitle("Erro");
                        adb.setMessage("Não foi possível conectar à Internet.\n\nVerifique sua conexão e tente novamente.");
                        adb.setPositiveButton("Tentar novamente", new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                // Não faz nada aqui para voltar à tela de resposta da pergunta,
                                // permitindo uma nova tentativa de requisição
                            }
                        });
                        adb.setNegativeButton("Cancelar", new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                AlertDialog.Builder adb2 = new AlertDialog.Builder(ResponderPerguntaActivity.this);
                                adb2.setTitle("Abandonar questionário?");
                                adb2.setMessage("Tem certeza que deseja abandonar o questionário? A sua resposta não será gravada.");
                                adb2.setPositiveButton("Abandonar", new AlertDialog.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(ResponderPerguntaActivity.this, "Questionário abandonado.", Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                });
                                adb2.setNegativeButton("Voltar para o questionário", new AlertDialog.OnClickListener() {
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

                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder adb = new AlertDialog.Builder(ResponderPerguntaActivity.this);
        adb.setTitle("Sair da pergunta?");
        adb.setMessage("Sua resposta não será registrada.");
        adb.setPositiveButton("Sair", new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        adb.setNegativeButton("Voltar", new AlertDialog.OnClickListener() {
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
