package mds.ufscar.pergunte.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mds.ufscar.pergunte.helpers.ListItem;

public class Materia implements Parcelable, ListItem {
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

    public Materia(JSONObject materia) {
        this.perguntas = new ArrayList<>();
        this.alunos = new ArrayList<>();
        try {
            this.setCodigo(materia.getInt("codigo"));
            this.setTurma(materia.getString("turma"));
            this.setAno(materia.getInt("ano"));
            this.setSemestre(materia.getInt("semestre"));
            this.setNomeDisciplina(materia.getString("nome_materia"));
            this.setCodigoInscricao(materia.getString("codigo_inscricao"));

            try {
                JSONObject professor_json = materia.getJSONObject("professor");
                this.setProfessor(new Professor(professor_json));
            } catch (JSONException e) {
                this.setProfessor(null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        descricao.append("Ministrante: " + professor.getNome() + " " + professor.getSobrenome() + newLine);
        descricao.append("Turma " + this.getTurma() + " - " + this.getAno() + "/" + this.getSemestre());
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
