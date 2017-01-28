package mds.ufscar.pergunte.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import mds.ufscar.pergunte.ListItem;

public class Pergunta implements Parcelable, ListItem {

    private int codigo;
    private String titulo;
    private String textoPergunta;
    private ArrayList<Alternativa> alternativas;
    private boolean disponivel;
    private Date dataAproximada;
    private int numRespostas;

    public Pergunta(int codigo, String titulo, String textoPergunta, ArrayList<Alternativa> alternativas,
                    Date dataAproximada) {
        this.setCodigo(codigo);
        this.setTitulo(titulo);
        this.setTextoPergunta(textoPergunta);
        this.setAlternativas(alternativas);
        this.setDisponivel(false);
        this.setDataAproximada(dataAproximada);
        this.numRespostas = 0;
    }

    public Pergunta(String titulo, String textoPergunta, ArrayList<Alternativa> alternativas,
                    Date dataAproximada) {
        this(0, titulo, textoPergunta, alternativas, dataAproximada);
    }

    public Pergunta(Parcel in) {
        this.codigo = in.readInt();
        this.titulo = in.readString();
        this.textoPergunta = in.readString();
        this.disponivel = (in.readInt() == 1);
        long tmpDate = in.readLong();
        this.dataAproximada = (tmpDate == -1) ? null : new Date(tmpDate);
        this.numRespostas = in.readInt();
    }

    public Pergunta(JSONObject pergunta) {
        try {
            this.alternativas = new ArrayList<>();
            this.setCodigo(pergunta.getInt("codigo"));
            this.setDisponivel(false);
            this.setTitulo(pergunta.getString("titulo"));
            this.setTextoPergunta(pergunta.getString("texto_pergunta"));
            this.setNumRespostas(0);

            String dataAproximadaString = pergunta.getString("data_aproximada");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
            Date data_aproximada = sdf.parse(dataAproximadaString);

            this.setDataAproximada(data_aproximada);

            JSONArray alternativas_json = pergunta.getJSONArray("alternativas");

            JSONArray alternativasCorretas_json = pergunta.getJSONArray("alternativas_corretas");
            String alternativasCorretas = "";

            for (int i = 0; i < alternativasCorretas_json.length(); i++) {
                alternativasCorretas += alternativasCorretas_json.getString(i);
            }

            for (int i = 0; i < alternativas_json.length(); i++) {
                this.alternativas.add(new Alternativa(alternativas_json.getJSONObject(i),
                        alternativasCorretas));
            }
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTextoPergunta() {
        return textoPergunta;
    }

    public void setTextoPergunta(String textoPergunta) {
        this.textoPergunta = textoPergunta;
    }

    public ArrayList<Alternativa> getAlternativas() {
        return alternativas;
    }

    public void setAlternativas(ArrayList<Alternativa> alternativas) {
        this.alternativas = alternativas;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public Date getDataAproximada() {
        return dataAproximada;
    }

    public String getDataAproximadaString() {
        SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy", Locale.CANADA);
        return s.format(dataAproximada);
    }

    public void setDataAproximada(Date dataAproximada) {
        this.dataAproximada = dataAproximada;
    }

    @Override
    public boolean isSection() {
        return false;
    }

    public int getNumRespostas() {
        return numRespostas;
    }

    public void setNumRespostas(int numRespostas) {
        this.numRespostas = numRespostas;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(codigo);
        out.writeString(titulo);
        out.writeString(textoPergunta);
        out.writeInt(disponivel ? 1 : 0);
        out.writeLong(dataAproximada != null ? dataAproximada.getTime() : -1);
        out.writeInt(numRespostas);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Pergunta> CREATOR = new Parcelable.Creator<Pergunta>() {
        public Pergunta createFromParcel(Parcel in) {
            return new Pergunta(in);
        }

        public Pergunta[] newArray(int size) {
            return new Pergunta[size];
        }
    };
}
