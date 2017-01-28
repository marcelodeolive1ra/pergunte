package mds.ufscar.pergunte.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Pessoa implements Parcelable{
    private String nome;
    private String sobrenome;
    private String email;

    public Pessoa(String nome, String sobrenome, String email) {
        this.setNome(nome);
        this.setSobrenome(sobrenome);
        this.setEmail(email);
    }

    public Pessoa() {
    }

    public Pessoa(Parcel in) {
        this.nome = in.readString();
        this.sobrenome = in.readString();
        this.email = in.readString();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean login() {
        // conexão com o banco de dados + autenticação Google
        return true;
    }

    public boolean logout() {
        // encerrar sessão do Google
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.nome);
        out.writeString(this.sobrenome);
        out.writeString(this.email);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Pessoa> CREATOR = new Parcelable.Creator<Pessoa>() {
        public Pessoa createFromParcel(Parcel in) {
            return new Pessoa(in);
        }

        public Pessoa[] newArray(int size) {
            return new Pessoa[size];
        }
    };
}
