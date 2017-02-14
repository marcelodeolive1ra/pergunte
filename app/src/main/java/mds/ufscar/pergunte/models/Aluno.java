package mds.ufscar.pergunte.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Aluno extends Pessoa {

    private String curso;

    public Aluno() {

    }

    public Aluno(String nome, String sobrenome, String email, String curso) {
        super(nome, sobrenome, email);
        this.setCurso(curso);
    }

    public Aluno(JSONObject aluno) {
        try {
            this.setNome(aluno.getString("nome"));
            this.setSobrenome(aluno.getString("sobrenome"));
            this.setEmail(aluno.getString("email"));
            this.setCurso(aluno.getString("curso"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }
}
