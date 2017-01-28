package mds.ufscar.pergunte;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import mds.ufscar.pergunte.model.Alternativa;
import mds.ufscar.pergunte.model.Pergunta;

/**
 * Created by Danilo on 27/01/2017.
 */

public class RespostaTela extends AppCompatActivity{

    private TextView mPergunta;
    private RadioGroup mAlternativas;
    private Button mResponder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.resposta);
        mPergunta = (TextView) findViewById(R.id.pergunta_content);
        mAlternativas = (RadioGroup) findViewById(R.id.radiogroup);
        mResponder = (Button) findViewById(R.id.btn_responder);

        Intent intent = getIntent();
        Pergunta pergunta = intent.getParcelableExtra("pergunta");
        ArrayList<Alternativa> alternativas = intent.getParcelableArrayListExtra("alternativas");
        pergunta.setAlternativas(alternativas);
        int nCorretas = 0;
        for (Alternativa alternativa : alternativas) {
            if (alternativa.isCorreta()) {
                nCorretas++;
            }
        }

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

                // TODO: Marcelo cadastrar resposta,
                // as alternativas escolhidas podem ser acessadas a partir de susas posições da lista acima
                finish();
            }
        });

    }
}
