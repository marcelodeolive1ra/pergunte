package mds.ufscar.pergunte.model;

import java.util.Date;
import java.util.List;

public class Pergunta {

    private int codigo;
    private String titulo;
    private String textoPergunta;
    private List<Alternativa> alternativas;
    private boolean disponivel;
    private Date dataAproximada;

    public Pergunta(String titulo, String textoPergunta, List<Alternativa> alternativas,
                    Date dataAproximada) {
        this.setCodigo(0);
        this.setTitulo(titulo);
        this.setTextoPergunta(textoPergunta);
        this.setAlternativas(alternativas);
        this.setDisponivel(false);
        this.setDataAproximada(dataAproximada);
    }

//    public Pergunta(String titulo, String textoPergunta, List<Alternativa> alternativas, Date dataAproximada) {
//        this(0, titulo, textoPergunta, alternativas, false, dataAproximada);
//    }

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

    public void setDataAproximada(Date dataAproximada) {
        this.dataAproximada = dataAproximada;
    }
}
