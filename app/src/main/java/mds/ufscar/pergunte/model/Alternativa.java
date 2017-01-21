package mds.ufscar.pergunte.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by marcelodeoliveiradasilva on 20/01/17.
 */

public class Alternativa {
    private int codigo;
    private String letra;
    private String textoAlternativa;
    private boolean correta;

    public Alternativa(String letra, String textoAlternativa, boolean correta) {
        this(0, letra, textoAlternativa, correta);
    }

    public Alternativa(int codigo, String letra, String textoAlternativa, boolean correta) {
        this.setCodigo(codigo);
        this.setLetra(letra);
        this.setTextoAlternativa(textoAlternativa);
        this.setCorreta(correta);
    }

    public Alternativa() {

    }

    public boolean construirObjetoComJSON(JSONObject resultado_requisicao, String alternativasCorretas) {
        try {
            this.setCodigo(0);
            this.setLetra(resultado_requisicao.getString("letra"));
            this.setTextoAlternativa(resultado_requisicao.getString("texto_alternativa"));

            if (alternativasCorretas.contains(this.getLetra())) {
                this.setCorreta(true);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
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
}
