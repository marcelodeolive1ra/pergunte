package mds.ufscar.pergunte.model;

/**
 * Created by marcelodeoliveiradasilva on 20/01/17.
 */

public class Alternativa {
    private String letra;
    private String textoAlternativa;

    public Alternativa(String letra, String textoAlternativa) {
        this.setLetra(letra);
        this.setTextoAlternativa(textoAlternativa);
    }

    public String getLetra() {
        return this.letra;
    }

    public String getTextoAlternativa() {
        return this.textoAlternativa;
    }

    public void setLetra(String letra) {
        this.letra = letra;
    }

    public void setTextoAlternativa(String textoAlternativa) {
        this.textoAlternativa = textoAlternativa;
    }
}
