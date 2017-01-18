package mds.ufscar.pergunte;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.ExecutionException;

import mds.ufscar.pergunte.model.Materia;
import mds.ufscar.pergunte.model.Professor;

import static org.junit.Assert.*;


@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void adicionaMateria1(){
        String emailUsuarioAtual = "cardoso.nilo@gmail.com";
        float passouTeste;
        Materia materia = new Materia();
        RequisicaoAssincrona requisicao = new RequisicaoAssincrona();
        requisicao.setObject(materia);
        try {
            JSONObject resultado_requisicao = requisicao.execute(RequisicaoAssincrona.CADASTRAR_NOVA_MATERIA,
                    emailUsuarioAtual).get();
            if (resultado_requisicao.getString("status").equals("ok")){ passouTeste = 1; }
            else{ passouTeste = 0; }
            assertEquals(0, passouTeste, 0f);
        }catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void adicionaMateria2(){
        Professor professor = new Professor("Danilo", "Cardoso","cardoso.nilo@gmail.com", "UFSCar");
        String emailUsuarioAtual = "cardoso.nilo@gmail.com";
        int passouTeste;
        Materia materia = new Materia(0, "A", 2016, 2, "MDS", professor, "01");
        RequisicaoAssincrona requisicao = new RequisicaoAssincrona();
        requisicao.setObject(materia);
        try {
            JSONObject resultado_requisicao = requisicao.execute(RequisicaoAssincrona.CADASTRAR_NOVA_MATERIA,
                    emailUsuarioAtual).get();
            if (resultado_requisicao.getString("status").equals("ok")){ passouTeste = 1; }
            else{ passouTeste = 0; }
            assertEquals(1, passouTeste, 0f);
        }catch (InterruptedException | ExecutionException| JSONException e) {
            e.printStackTrace();
        }
    }
}
