package mds.ufscar.pergunte.model;

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

    public Pergunta[] verificarPerguntasDisponiveis() {
        return null;
    }

    public Pergunta[] verificarHistoricoDePerguntasRespondidas() {
        return null;
    }

    public Turma[] consultarTurmasInscritas() {
        return null;
    }
}
