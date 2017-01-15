package mds.ufscar.pergunte.model;

import java.util.ArrayList;

public class Materia {
    private String turma;
    private int ano;
    private int semestre;
    private String nomeDisciplina;
    private String codigoInscricao;
    private Professor professor;
    private ArrayList<Pergunta> perguntas;
    private ArrayList<Aluno> alunos;
    private String imageUrl;

    public Materia(String turma, int ano, int semestre, String nome_disciplina,
                   Professor professor, String codigoInscricao) {
        this.setTurma(turma);
        this.setAno(ano);
        this.setSemestre(semestre);
        this.setNomeDisciplina(nome_disciplina);
        this.setProfessor(professor);
        this.setCodigoInscricao(codigoInscricao);
        perguntas = new ArrayList<>();
        alunos = new ArrayList<>();
        imageUrl = "https://pt.wikipedia.org/wiki/Turma_da_M%C3%B4nica#/media/File:Turma_da_M%C3%B4nica.jpg";
    }

    public String getTurma() {
        return turma;
    }

    public void setTurma(String turma) {
        this.turma = turma;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public ArrayList<Pergunta> getPerguntas() {
        return perguntas;
    }

    public void setPerguntas(ArrayList<Pergunta> perguntas) {
        this.perguntas = perguntas;
    }

    public ArrayList<Aluno> getAlunos() {
        return alunos;
    }

    public void setAlunos(ArrayList<Aluno> alunos) {
        this.alunos = alunos;
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

    public void setCodigoInscricao(String codigo_inscricao) {
        this.codigoInscricao = codigo_inscricao;
    }

    public String getCodigoInscricao() {
        return this.codigoInscricao;
    }

    public Materia[] procurarMaterias() {
        // conexão com o banco

        return null;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void excluirMateria() {
        // conexão com o banco
    }

    public void inscreverAluno(Aluno aluno) {
        // conexão com o banco
    }

    public void gerarQRCode() {

    }

    public String getDescricao() {
        String newLine = System.getProperty("line.separator");
        StringBuilder descricao = new StringBuilder();
        descricao.append("Professor: " + professor.getNome() + newLine);
        descricao.append("Turma: " + turma + " " + ano + "/" + semestre);
        return descricao.toString();
    }
}
