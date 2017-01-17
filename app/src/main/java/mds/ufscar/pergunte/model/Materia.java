package mds.ufscar.pergunte.model;

import java.util.ArrayList;

public class Materia {
    private int codigo;
    private String turma;
    private int ano;
    private int semestre;
    private String nomeDisciplina;
    private String codigoInscricao;
    private Professor professor;
    private ArrayList<Pergunta> perguntas;
    private ArrayList<Aluno> alunos;
    private String imageUrl;

    public Materia(int codigo, String turma, int ano, int semestre, String nomeDisciplina,
                   Professor professor, String codigoInscricao) {
        this.setCodigo(codigo);
        this.setTurma(turma);
        this.setAno(ano);
        this.setSemestre(semestre);
        this.setNomeDisciplina(nomeDisciplina);
        this.setProfessor(professor);
        this.setCodigoInscricao(codigoInscricao);
        perguntas = new ArrayList<>();
        alunos = new ArrayList<>();
        imageUrl = null;
//        imageUrl = "https://www.edamam.com/web-img/1d0/1d0075b30ec5fc368ce9ccaf3156462a.jpg";
    }

    public Materia(int codigo, String turma, int ano, int semestre, String nomeDisciplina,
                   String codigoInscricao) {
        this(codigo, turma, ano, semestre, nomeDisciplina, null, codigoInscricao);
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
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
