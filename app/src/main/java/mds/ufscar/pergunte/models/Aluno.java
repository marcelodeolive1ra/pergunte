package mds.ufscar.pergunte.models;

public class Aluno extends Pessoa {

    private String curso;

    public Aluno(String nome, String sobrenome, String email, String curso) {
        super(nome, sobrenome, email);
        this.setCurso(curso);
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }
}
