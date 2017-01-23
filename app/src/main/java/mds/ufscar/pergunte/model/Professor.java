package mds.ufscar.pergunte.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Professor extends Pessoa {
    private String universidade;

    public Professor(String nome, String sobrenome, String email, String universidade) {
        super(nome, sobrenome, email);
        this.setUniversidade(universidade);
    }

    public Professor() {
    }

    public Professor(JSONObject professor) {
        try {
            this.setNome(professor.getString("nome"));
            this.setSobrenome(professor.getString("sobrenome"));
            this.setEmail(professor.getString("email"));
            this.setUniversidade(professor.getString("universidade"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getUniversidade() {
        return universidade;
    }

    public void setUniversidade(String universidade) {
        this.universidade = universidade;
    }

    public void verRelatorios() {

    }

    public void notificarTurma(Materia materia) {

    }

    public Materia[] consultarDisciplinasVinculadas() {
        return null;
    }

    public void salvarRelatorio(Pergunta pergunta) {

    }
}

