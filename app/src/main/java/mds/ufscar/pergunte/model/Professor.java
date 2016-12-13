package mds.ufscar.pergunte.model;

public class Professor extends Pessoa {
    private String universidade;

    public Professor(String nome, String sobrenome, String email, String universidade) {
        super(nome, sobrenome, email);
        this.setUniversidade(universidade);
    }

    public String getUniversidade() {
        return universidade;
    }

    public void setUniversidade(String universidade) {
        this.universidade = universidade;
    }

    public void verRelatorios() {

    }

    public void notificarTurma(Turma turma) {

    }

    public Turma[] consultarDisciplinasVinculadas() {
        return null;
    }

    public void salvarRelatorio(Pergunta pergunta) {

    }
}

