package br.com.desafiofitness.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Usuario implements Mapeavel {
    private Integer usuario;
    private String nome;
    private String email;

    @Override
    public void preencher(ResultSet rs) throws SQLException {
        this.usuario = rs.getInt("usuario");
        this.nome = rs.getString("nome");
        this.email = rs.getString("email");
    }

    public Integer getUsuario() {
        return this.usuario;
    }

    public String getNome() {
        return this.nome;
    }

    public String getEmail() {
        return this.email;
    }
}