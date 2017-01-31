package mds.ufscar.pergunte.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Professor extends Pessoa implements Parcelable {
    private String universidade;

    public Professor(String nome, String sobrenome, String email, String universidade) {
        super(nome, sobrenome, email);
        this.setUniversidade(universidade);
    }

    public Professor(Parcel in) {
        this.universidade = in.readString();
    }

    public Professor() {}

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.universidade);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Professor> CREATOR = new Parcelable.Creator<Professor>() {
        public Professor createFromParcel(Parcel in) {
            return new Professor(in);
        }

        public Professor[] newArray(int size) {
            return new Professor[size];
        }
    };

    public String toString() {
        return "Ministrante: " + this.getNome() + " " + this.getSobrenome();
    }
}

