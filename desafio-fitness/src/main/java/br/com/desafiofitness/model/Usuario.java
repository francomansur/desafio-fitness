package br.com.desafiofitness.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Usuario implements Mapeavel {
    private Integer usuario;
    private String nome;
    private String email;
    private String senha;

    @Override
    public void preencher(ResultSet rs) throws SQLException {
        this.usuario = rs.getInt("usuario");
        this.nome = rs.getString("nome");
        this.email = rs.getString("email");
        try {
            this.senha = rs.getString("senha");
        } catch (SQLException e) {
            // Em consultas parciais, pode ser que a senha não esteja selecionada
            this.senha = null;
        }
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

    @JsonIgnore
    public String getSenha() {
        return this.senha;
    }
}