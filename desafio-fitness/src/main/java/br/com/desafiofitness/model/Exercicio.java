package br.com.desafiofitness.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Exercicio implements Mapeavel {
    private Integer exercicio;
    private String nome;
    private String descricao;

    @Override
    public void preencher(ResultSet rs) throws SQLException {
        this.exercicio = rs.getInt("exercicio");
        this.nome = rs.getString("nome");
        this.descricao = rs.getString("descricao");
    }

    public Integer getExercicio() {
        return this.exercicio;
    }

    public String getNome() {
        return this.nome;
    }

    public String getDescricao() {
        return this.descricao;
    }
}