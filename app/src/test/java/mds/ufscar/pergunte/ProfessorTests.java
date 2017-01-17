package mds.ufscar.pergunte;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import mds.ufscar.pergunte.model.Materia;
import mds.ufscar.pergunte.model.Pergunta;
import mds.ufscar.pergunte.model.Professor;

import static org.junit.Assert.*;

public class ProfessorTests {

    /*@Before
    public void myTest(){
    }*/

    @Test(expected = RuntimeException.class)
    public void adicionaMateria1(){
        try{
            Materia materia = new Materia();
        }catch(RuntimeException e) {
            throw e;
        }
    }

    @Test(expected = RuntimeException.class)
    public void adicionaMateria2(){
        try{
            Materia materia = new Materia("MDS", 2017);
        }catch(RuntimeException e){
            throw e;
        }
    }

    @Test
    public void adicionaMateria3(){
        Materia materia = new Materia(0, "MDS", 2017, 1, "MDS", "001");
        assertEquals(0, materia.getCodigo(), 0f);
    }

    @Test(expected = RuntimeException.class)
    public void adicionaMateria4(){
        try{
            Materia materia = new Materia(0, "MDS", 2017, 1, "MDS", "001", "sala 2");
        }catch(RuntimeException e){
            throw e;
        }
    }

    @Test(expected = RuntimeException.class)
    public void adicionaPergunta1(){
        try{
            Pergunta q = new Pergunta();
        }catch(RuntimeException e){
            throw e;
        }
    }

    @Test
    public void adicionaPergunta2(){
        List<String> respostas = Arrays.asList("resp1");
        Pergunta q = new Pergunta(0, "myQuestion", respostas, false, "resp1");
        assertEquals(0, q.getCodigo(), 0f);
    }

    @Test(expected = RuntimeException.class)
    public void adicionaPergunta3(){
        try{
            List<String> respostas = Arrays.asList("resp1");
            Pergunta q = new Pergunta(0, "myQuestion", respostas, "resp1");
        }catch(RuntimeException e){
            throw e;
        }
    }

    @Test(expected = RuntimeException.class)
    public void cadastro1(){
        try{
            Professor p = new Professor();
        }catch(RuntimeException e){
            throw e;
        }
    }

    @Test(expected = RuntimeException.class)
    public void cadastro2(){
        Professor p = new Professor("Rodolfo", "B", "ro_b@", "UFSCar");
        assertEquals("Rodolfo", p.getNome(), 0f);
    }
}