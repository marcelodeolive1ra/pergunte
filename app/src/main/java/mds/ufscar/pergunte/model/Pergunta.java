package mds.ufscar.pergunte.model;

import java.util.List;

public class Pergunta {

    private int codigo;
    private String textoPergunta;
    private List<Alternativa> alternativas;
    private boolean disponivel;
    private String[] respostaCorreta;

    public Pergunta(int codigo, String textoPergunta, List<Alternativa> alternativas,
                    boolean disponivel, String[] respostaCorreta) {
        this.setCodigo(codigo);
        this.setTextoPergunta(textoPergunta);
        this.setAlternativas(alternativas);
        this.setDisponivel(disponivel);
        this.setRespostaCorreta(respostaCorreta);
    }

    public Pergunta(String textoPergunta, List<Alternativa> alternativas, boolean disponivel,
                    String[] respostaCorreta) {
        this(0, textoPergunta, alternativas, disponivel, respostaCorreta);
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
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

    public String[] getRespostaCorreta() {
        return respostaCorreta;
    }

    public void setRespostaCorreta(String[] resposta_correta) {
        this.respostaCorreta = resposta_correta;
    }
}
