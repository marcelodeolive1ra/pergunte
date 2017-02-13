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
        double resultado;
        Materia materia = new Materia();
        RequisicaoAssincrona requisicao = new RequisicaoAssincrona();
        requisicao.setObject(materia);
        try {
            JSONObject resultado_requisicao = requisicao.execute(RequisicaoAssincrona.CADASTRAR_NOVA_MATERIA, "rophos.rb@gmail.com").get();
            if(resultado_requisicao.getString("status").equals("error")){
                if(resultado_requisicao.getString("descricao").equals("Parâmetros inválidos.")){resultado = 1.0;}
                else if(resultado_requisicao.getString("descricao").equals("Código de inscrição já cadastrado para outra matéria.")){resultado = 2.0;}
                else if(resultado_requisicao.getString("descricao").equals("Professor não cadastrado.")){resultado = 3.0;}
                else{resultado = 0;}
            }else{resultado = -1.0;}

            assertEquals(1.0,resultado,0.0f);
        }catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void adicionaMateria2(){
        Professor professor = new Professor("Rodolfo", "Barcelar","rophos.rb@gmail.com", "UFSCar");
        double resultado;
        Materia materia = new Materia(0, "A", 2016, 2, "MDS", professor, "METDS01");
        RequisicaoAssincrona requisicao = new RequisicaoAssincrona();
        requisicao.setObject(materia);
        try {
            JSONObject resultado_requisicao = requisicao.execute(RequisicaoAssincrona.CADASTRAR_NOVA_MATERIA,
                    "rophos.rb@gmail.com").get();
            if (resultado_requisicao.getString("status").equals("ok")){resultado = 1.0;}
            else{
                if(resultado_requisicao.getString("descricao").equals("Parâmetros inválidos.")){resultado = 2.0;}
                else if(resultado_requisicao.getString("descricao").equals("Código de inscrição já cadastrado para outra matéria.")){resultado = 3.0;}
                else if(resultado_requisicao.getString("descricao").equals("Professor não cadastrado.")){resultado = 4.0;}
                else{resultado = 0;}
            }

            assertEquals(1.0, resultado, 0.0f);
        }catch (InterruptedException | ExecutionException| JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void adicionaMateria3(){
        Professor professor = new Professor("Rodolfo", "Barcelar","rophos.rb@gmail.com", "UFSCar");
        double resultado;

        Materia materia2 = new Materia(1, "B", 2016, 2, "CAP", professor, "MDS01");
        RequisicaoAssincrona requisicao = new RequisicaoAssincrona();
        requisicao.setObject(materia2);
        try {
            JSONObject resultado_requisicao = requisicao.execute(RequisicaoAssincrona.CADASTRAR_NOVA_MATERIA, "rophos.rb@gmail.com").get();

            if(resultado_requisicao.getString("status").equals("error")){
                if(resultado_requisicao.getString("descricao").equals("Parâmetros inválidos.")){resultado = 1.0;}
                else if(resultado_requisicao.getString("descricao").equals("Código de inscrição já cadastrado para outra matéria.")){resultado = 2.0;}
                else if(resultado_requisicao.getString("descricao").equals("Professor não cadastrado.")){resultado = 3.0;}
                else{resultado = 0;}
            }else{resultado = -1.0;}

            assertEquals(2.0,resultado,0.0f);
        }catch (InterruptedException | ExecutionException| JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void adicionaMateria4(){
        Professor professor = new Professor("Rodolfo", "Barcelar","rophos.rb@gmail.com", "UFSCar");
        double resultado;
        Materia materia = new Materia(0, "A", 2016, 2, "ABC", professor, "ABC01");
        RequisicaoAssincrona requisicao = new RequisicaoAssincrona();
        requisicao.setObject(materia);
        try {
            JSONObject resultado_requisicao = requisicao.execute(RequisicaoAssincrona.CADASTRAR_NOVA_MATERIA,
                    "abc@gmail.com").get();
            if (resultado_requisicao.getString("status").equals("ok")){resultado = 1.0;}
            else{
                if(resultado_requisicao.getString("descricao").equals("Parâmetros inválidos.")){resultado = 2.0;}
                else if(resultado_requisicao.getString("descricao").equals("Código de inscrição já cadastrado para outra matéria.")){resultado = 3.0;}
                else if(resultado_requisicao.getString("descricao").equals("Professor não cadastrado.")){resultado = 4.0;}
                else{resultado = 0;}
            }

            assertEquals(4.0, resultado, 0.0f);
        }catch (InterruptedException | ExecutionException| JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void buscarAluno1(){
        double resultado;
        RequisicaoAssincrona requisicao = new RequisicaoAssincrona();
        try {
            JSONObject resultado_requisicao = requisicao.execute(RequisicaoAssincrona.BUSCAR_PERFIL_DO_USUARIO, "rophos.rb@gmail.com").get();
            if(resultado_requisicao.getString("status").equals("ok")){resultado = 1.0;}
            else{
                if(resultado_requisicao.getString("descricao").equals("Perfil não encontrado")){resultado = 2.0;}
                else{resultado = 0;}
            }

            assertEquals(1.0,resultado,0.0f);
        }catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void buscarAluno2(){
        double resultado;
        RequisicaoAssincrona requisicao = new RequisicaoAssincrona();
        try {
            JSONObject resultado_requisicao = requisicao.execute(RequisicaoAssincrona.BUSCAR_PERFIL_DO_USUARIO, "abc@gmail.com").get();
            if(resultado_requisicao.getString("status").equals("ok")){resultado = 1.0;}
            else{
                if(resultado_requisicao.getString("descricao").equals("Perfil não encontrado")){resultado = 2.0;}
                else{resultado = 0;}
            }

            assertEquals(2.0,resultado,0.0f);
        }catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void inscreverAlunoEmMateria1(){
        double resultado;
        RequisicaoAssincrona requisicao = new RequisicaoAssincrona();
        try {
            JSONObject resultado_requisicao = requisicao.execute(RequisicaoAssincrona.INSCREVER_ALUNO_EM_MATERIA, "rophos.rb@gmail.com", "METDS01").get();
            if (resultado_requisicao.getString("status").equals("ok")){resultado = 1.0;}
            else{
                if(resultado_requisicao.getString("descricao").equals("Não é possível cadastrá-lo nesta matéria, pois ela está inativa")){resultado = 2.0;}
                else if(resultado_requisicao.getString("descricao").equals("Erro na requisição. Aluno ou matéria não encontrados.")){resultado = 3.0;}
                else {resultado = 0;}
            }

            assertEquals(1.0, resultado, 0.0f);
        }catch (InterruptedException | ExecutionException| JSONException e) {
            e.printStackTrace();
        }

        //Cancelando inscricao na disciplina
        RequisicaoAssincrona requisicao2 = new RequisicaoAssincrona();
        try {
            JSONObject resultado_requisicao2 = requisicao2.execute(RequisicaoAssincrona.CANCELAR_INSCRICAO_EM_MATERIA, "rophos.rb@gmail.com", "METDS01").get();
        }catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    /* CASO MATERIA JA CADASTRADA
    @Test
    public void inscreverAlunoEmMateria2(){
        double resultado;
        RequisicaoAssincrona requisicao1 = new RequisicaoAssincrona();
        try {
            JSONObject resultado_requisicao1 = requisicao1.execute(RequisicaoAssincrona.INSCREVER_ALUNO_EM_MATERIA, "rophos.rb@gmail.com", "MDS01").get();
        }catch (InterruptedException | ExecutionException| JSONException e) {
            e.printStackTrace();
        }

        RequisicaoAssincrona requisicao2 = new RequisicaoAssincrona();
        try {
            JSONObject resultado_requisicao2 = requisicao2.execute(RequisicaoAssincrona.INSCREVER_ALUNO_EM_MATERIA, "rophos.rb@gmail.com", "MDS01").get();
            if (resultado_requisicao.getString("status").equals("ok")){resultado = 1.0;}
            else{
                if(resultado_requisicao.getString("descricao").equals("Não é possível cadastrá-lo nesta matéria, pois ela está inativa")){resultado = 2.0;}
                else if(resultado_requisicao.getString("descricao").equals("Erro na requisição. Aluno ou matéria não encontrados.")){resultado = 3.0;}
                else {resultado = 0;}
            }

            assertEquals(1.0, resultado, 0.0f);
        }catch (InterruptedException | ExecutionException| JSONException e) {
            e.printStackTrace();
        }

        //Cancelando inscricao na disciplina
        try {
            JSONObject resultado_requisicao = requisicao.execute(RequisicaoAssincrona.CANCELAR_INSCRICAO_EM_MATERIA, "rophos.rb@gmail.com", "MDS01").get();
        }catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }*/

    @Test
    public void inscreverAlunoEmMateria3(){
        double resultado;
        RequisicaoAssincrona requisicao = new RequisicaoAssincrona();
        try {
            JSONObject resultado_requisicao = requisicao.execute(RequisicaoAssincrona.INSCREVER_ALUNO_EM_MATERIA, "abc@gmail.com", "MDS01").get();
            if (resultado_requisicao.getString("status").equals("ok")){resultado = 1.0;}
            else{
                if(resultado_requisicao.getString("descricao").equals("Não é possível cadastrá-lo nesta matéria, pois ela está inativa")){resultado = 2.0;}
                else if(resultado_requisicao.getString("descricao").equals("Erro na requisição. Aluno ou matéria não encontrados.")){resultado = 3.0;}
                else {resultado = 0;}
            }

            assertEquals(3.0, resultado, 0.0f);
        }catch (InterruptedException | ExecutionException| JSONException e) {
            e.printStackTrace();
        }
    }

    /*Caso materia INATIVA
    @Test
    public void inscreverAlunoEmMateria4(){
        double resultado;
        RequisicaoAssincrona requisicao = new RequisicaoAssincrona();
        try {
            JSONObject resultado_requisicao = requisicao.execute(RequisicaoAssincrona.INSCREVER_ALUNO_EM_MATERIA, "abc@gmail.com", "MDS01").get();
            if (resultado_requisicao.getString("status").equals("ok")){resultado = 1.0;}
            else{
                if(resultado_requisicao.getString("descricao").equals("Não é possível cadastrá-lo nesta matéria, pois ela está inativa")){resultado = 2.0;}
                else if(resultado_requisicao.getString("descricao").equals("Erro na requisição. Aluno ou matéria não encontrados.")){resultado = 3.0;}
                else {resultado = 0;}
            }

            assertEquals(2.0, resultado, 0.0f);
        }catch (InterruptedException | ExecutionException| JSONException e) {
            e.printStackTrace();
        }
    }*/

    @Test
    public void buscarProfessor1(){
        double resultado;
        RequisicaoAssincrona requisicao = new RequisicaoAssincrona();
        try {
            JSONObject resultado_requisicao = requisicao.execute(RequisicaoAssincrona.BUSCAR_PERFIL_DO_USUARIO, "rophos.rb@gmail.com").get();
            if(resultado_requisicao.getString("status").equals("ok")){resultado = 1.0;}
            else{
                if(resultado_requisicao.getString("descricao").equals("Perfil não encontrado")){resultado = 2.0;}
                else{resultado = 0;}
            }

            assertEquals(1.0,resultado,0.0f);
        }catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void buscarProfessor2(){
        double resultado;
        RequisicaoAssincrona requisicao = new RequisicaoAssincrona();
        try {
            JSONObject resultado_requisicao = requisicao.execute(RequisicaoAssincrona.BUSCAR_PERFIL_DO_USUARIO, "abc@gmail.com").get();
            if(resultado_requisicao.getString("status").equals("ok")){resultado = 1.0;}
            else{
                if(resultado_requisicao.getString("descricao").equals("Perfil não encontrado")){resultado = 2.0;}
                else{resultado = 0;}
            }

            assertEquals(2.0,resultado,0.0f);
        }catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void cancelarInscricaoEmMateria1(){
        double resultado;
        RequisicaoAssincrona requisicao = new RequisicaoAssincrona();
        try {
            JSONObject resultado_requisicao = requisicao.execute(RequisicaoAssincrona.INSCREVER_ALUNO_EM_MATERIA, "rophos.rb@gmail.com", "MDS01").get();

        }catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        //Cancelando inscricao na disciplina
        RequisicaoAssincrona requisicao2 = new RequisicaoAssincrona();
        try {
            JSONObject resultado_requisicao2 = requisicao2.execute(RequisicaoAssincrona.CANCELAR_INSCRICAO_EM_MATERIA, "rophos.rb@gmail.com", "MDS01").get();
            if (resultado_requisicao2.getString("status").equals("ok")){resultado = 1.0;}
            else{
                if(resultado_requisicao2.getString("descricao").equals("Erro na requisição. Aluno ou matéria não encontrados.")){resultado = 2.0;}
                else {resultado = 0;}
            }

            assertEquals(1.0, resultado, 0.0f);
        }catch (InterruptedException | ExecutionException| JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void cancelarInscricaoEmMateria2(){
        double resultado;
        RequisicaoAssincrona requisicao = new RequisicaoAssincrona();
        try {
            JSONObject resultado_requisicao = requisicao.execute(RequisicaoAssincrona.INSCREVER_ALUNO_EM_MATERIA, "abc@gmail.com", "METDS01").get();

        }catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        //Cancelando inscricao na disciplina
        RequisicaoAssincrona requisicao2 = new RequisicaoAssincrona();
        try {
            JSONObject resultado_requisicao2 = requisicao2.execute(RequisicaoAssincrona.CANCELAR_INSCRICAO_EM_MATERIA, "rophos.rb@gmail.com", "METDS01").get();
            if (resultado_requisicao2.getString("status").equals("ok")){resultado = 1.0;}
            else{
                if(resultado_requisicao2.getString("descricao").equals("Erro na requisição. Aluno ou matéria não encontrados.")){resultado = 2.0;}
                else {resultado = 0;}
            }

            assertEquals(2.0, resultado, 0.0f);
        }catch (InterruptedException | ExecutionException| JSONException e) {
            e.printStackTrace();
        }
    }
}
