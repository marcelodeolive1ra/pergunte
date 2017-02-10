package mds.ufscar.pergunte.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by marcelodeoliveiradasilva on 20/01/17.
 */

public class Alternativa implements Parcelable {
    private int codigo;
    private String letra;
    private String textoAlternativa;
    private boolean correta;
    private int nRespostas;

    public Alternativa(String letra, String textoAlternativa, boolean correta) {
        this(0, letra, textoAlternativa, correta);
    }

    public Alternativa(int codigo, String letra, String textoAlternativa, boolean correta) {
        this.setCodigo(codigo);
        this.setLetra(letra);
        this.setTextoAlternativa(textoAlternativa);
        this.setCorreta(correta);
        this.setnRespostas(0);
    }

    public Alternativa() {
    }

    public Alternativa(JSONObject alternativa, String alternativasCorretas) {
        try {
            this.setCodigo(alternativa.getInt("codigo"));
            this.setLetra(alternativa.getString("letra"));
            this.setTextoAlternativa(alternativa.getString("texto_alternativa"));

            if (alternativasCorretas.contains(this.getLetra())) {
                this.setCorreta(true);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Alternativa(Parcel in) {
        this.codigo = in.readInt();
        this.letra = in.readString();
        this.textoAlternativa = in.readString();
        this.correta = (in.readInt() == 1);
        this.nRespostas = in.readInt();
    }

    public int getCodigo() {
        return this.codigo;
    }

    public String getLetra() {
        return this.letra;
    }

    public String getTextoAlternativa() {
        return this.textoAlternativa;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public void setLetra(String letra) {
        this.letra = letra;
    }

    public void setTextoAlternativa(String textoAlternativa) {
        this.textoAlternativa = textoAlternativa;
    }

    public boolean isCorreta() {
        return correta;
    }

    public void setCorreta(boolean correta) {
        this.correta = correta;
    }

    public int getnRespostas() {
        return nRespostas;
    }

    public void setnRespostas(int nRespostas) {
        this.nRespostas = nRespostas;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(codigo);
        dest.writeString(letra);
        dest.writeString(textoAlternativa);
        dest.writeInt(correta ? 1 : 0);
        dest.writeInt(nRespostas);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Alternativa> CREATOR = new Parcelable.Creator<Alternativa>() {
        public Alternativa createFromParcel(Parcel in) {
            return new Alternativa(in);
        }

        public Alternativa[] newArray(int size) {
            return new Alternativa[size];
        }
    };
}
