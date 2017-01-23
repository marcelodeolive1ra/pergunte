package mds.ufscar.pergunte.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import mds.ufscar.pergunte.ListItem;

public class Pergunta implements ListItem {

    private int codigo;
    private String titulo;
    private String textoPergunta;
    private List<Alternativa> alternativas;
    private boolean disponivel;
    private Date dataAproximada;

    public Pergunta(int codigo, String titulo, String textoPergunta, List<Alternativa> alternativas,
                    Date dataAproximada) {
        this.setCodigo(codigo);
        this.setTitulo(titulo);
        this.setTextoPergunta(textoPergunta);
        this.setAlternativas(alternativas);
        this.setDisponivel(false);
        this.setDataAproximada(dataAproximada);
    }

    public Pergunta(String titulo, String textoPergunta, List<Alternativa> alternativas,
                    Date dataAproximada) {
        this(0, titulo, textoPergunta, alternativas, dataAproximada);
    }

    public Pergunta() {
        this.alternativas = new ArrayList<>();
    }

    public Pergunta(JSONObject pergunta) {
        try {
            this.alternativas = new ArrayList<>();
            this.setCodigo(pergunta.getInt("codigo"));
            this.setDisponivel(false);
            this.setTitulo(pergunta.getString("titulo"));
            this.setTextoPergunta(pergunta.getString("texto_pergunta"));

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

    public List<Alternativa> getAlternativas() {
        return alternativas;
    }

    public void setAlternativas(List<Alternativa> alternativas) {
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
}
