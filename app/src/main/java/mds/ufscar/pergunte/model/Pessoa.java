package mds.ufscar.pergunte.model;

public class Pessoa {
    private String nome;
    private String sobrenome;
    private String email;

    public Pessoa(String nome, String sobrenome, String email) {
        this.setNome(nome);
        this.setSobrenome(sobrenome);
        this.setEmail(email);
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
}
