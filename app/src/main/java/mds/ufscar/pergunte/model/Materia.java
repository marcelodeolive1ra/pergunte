package mds.ufscar.pergunte.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mds.ufscar.pergunte.MateriaItem;

public class Materia implements Parcelable, MateriaItem {
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
        this.perguntas = new ArrayList<>();
        this.alunos = new ArrayList<>();
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

    public boolean construirObjetoComJSON(JSONObject resultado_requisicao) {
        this.perguntas = new ArrayList<>();
        this.alunos = new ArrayList<>();
        JSONObject professor_json = null;
        try {
            professor_json = resultado_requisicao.getJSONObject("professor");
            this.setCodigo(resultado_requisicao.getInt("codigo"));
            this.setTurma(resultado_requisicao.getString("turma"));
            this.setAno(resultado_requisicao.getInt("ano"));
            this.setSemestre(resultado_requisicao.getInt("semestre"));
            this.setNomeDisciplina(resultado_requisicao.getString("nome_materia"));
            this.setProfessor(new Professor(
                    professor_json.getString("nome"),
                    professor_json.getString("sobrenome"),
                    professor_json.getString("email"),
                    professor_json.getString("universidade")));
            this.setCodigoInscricao(resultado_requisicao.getString("codigo_inscricao"));
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Materia(int codigo, String turma, int ano, int semestre, String nomeDisciplina,
                   String codigoInscricao) {
        this(codigo, turma, ano, semestre, nomeDisciplina, null, codigoInscricao);
        this.perguntas = new ArrayList<>();
        this.alunos = new ArrayList<>();
    }

    public Materia() {
        this.perguntas = new ArrayList<>();
        this.alunos = new ArrayList<>();
    }

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Materia(Parcel in) {
        codigo = in.readInt();
        turma = in.readString();
        ano = in.readInt();
        semestre = in.readInt();
        nomeDisciplina = in.readString();
        codigoInscricao = in.readString();
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

    // 99.9% of the time you can just ignore this
    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(codigo);
        out.writeString(turma);
        out.writeInt(ano);
        out.writeInt(semestre);
        out.writeString(nomeDisciplina);
        out.writeString(codigoInscricao);
//        out.writeString(imageUrl);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Materia> CREATOR = new Parcelable.Creator<Materia>() {
        public Materia createFromParcel(Parcel in) {
            return new Materia(in);
        }

        public Materia[] newArray(int size) {
            return new Materia[size];
        }
    };

    @Override
    public boolean isSection() {
        return false;
    }
}
