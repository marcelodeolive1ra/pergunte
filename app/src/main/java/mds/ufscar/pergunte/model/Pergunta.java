package mds.ufscar.pergunte.model;

import java.util.List;

public class Pergunta {

    private int codigo;
    private String pergunta;
    private List<String> respostas;
    private boolean disponivel;
    private String respostaCorreta;

    public Pergunta(int codigo, String pergunta, List<String> respostas, boolean disponivel, String respostaCorreta) {

    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getPergunta() {
        return pergunta;
    }

    public void setPergunta(String pergunta) {
        this.pergunta = pergunta;
    }

    public List<String> getRespostas() {
        return respostas;
    }

    public void setRespostas(List<String> respostas) {
        this.respostas = respostas;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public String getRespostaCorreta() {
        return respostaCorreta;
    }

    public void setRespostaCorreta(String resposta_correta) {
        this.respostaCorreta = resposta_correta;
    }


}
