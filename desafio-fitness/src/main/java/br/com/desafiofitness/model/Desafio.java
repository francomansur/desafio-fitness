package br.com.desafiofitness.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Desafio implements Mapeavel {
    private Integer desafio;
    private String nome;
    private String descricao;
    private List<Exercicio> exercicios = new ArrayList<>();

    @Override
    public void preencher(ResultSet rs) throws SQLException {
        this.desafio = rs.getInt("desafio");
        this.nome = rs.getString("nome");
        this.descricao = rs.getString("descricao");
    }

    public Integer getDesafio() {
        return this.desafio;
    }

    public String getNome() {
        return this.nome;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public List<Exercicio> getExercicios() {
        return this.exercicios;
    }

    public void adicionarExercicio(Exercicio exercicio) {
        this.exercicios.add(exercicio);
    }

    public void removerExercicio(int codigoExercicio) {
        this.exercicios.removeIf(e -> e.getExercicio().equals(codigoExercicio));
    }
}