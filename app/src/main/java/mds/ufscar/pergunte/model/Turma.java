package mds.ufscar.pergunte.model;

public class Turma {
    private String turma;
    private int ano;
    private int semestre;
    private String nomeDisciplina;
    private int codigoInscricao;

    public Turma(String turma, int ano, int semestre, String nome_disciplina) {
        this.setTurma(turma);
        this.setAno(ano);
        this.setSemestre(semestre);
        this.setNomeDisciplina(nome_disciplina);
//        this.
    }

    public String getTurma() {
        return turma;
    }

    public void setTurma(String turma) {
        this.turma = turma;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    public String getNomeDisciplina() {
        return nomeDisciplina;
    }

    public void setNomeDisciplina(String nome_disciplina) {
        this.nomeDisciplina = nome_disciplina;
    }

    public void setCodigoInscricao(int codigo_inscricao) {
        this.codigoInscricao = codigo_inscricao;
    }

    public int getCodigoInscricao() {
        return this.codigoInscricao;
    }

    public Turma[] procurarTurmas() {
        // conexão com o banco

        return null;
    }

    public void excluirTurma() {
        // conexão com o banco
    }

    public void inscreverAluno(Aluno aluno) {
        // conexão com o banco
    }

    public void gerarQRCode() {

    }
}
