package br.com.desafiofitness.dao;

import br.com.desafiofitness.model.Desafio;
import br.com.desafiofitness.model.Exercicio;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DesafioDAO extends MySqlDAO {

    public List<Desafio> listarTodosDesafios() {
        try {
            ResultSet rs = this.executarConsulta("SELECT * FROM DESAFIOS ORDER BY DESAFIO");
            List<Desafio> desafios = new ArrayList<>();
            while (rs.next()) {
                Desafio desafio = this.mapearSimples(rs, Desafio.class);
                for (Exercicio e : this.listarExerciciosDoDesafio(desafio.getDesafio())) {
                    desafio.adicionarExercicio(e);
                }
                desafios.add(desafio);
            }
            return desafios;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar desafios.", e);
        }
    }

    public List<Exercicio> listarExerciciosDoDesafio(int codigoDesafio) {
        try {
            ResultSet rs = this.executarConsulta(
                "SELECT e.* FROM EXERCICIOS e INNER JOIN DESAFIOS_EXERCICIOS de ON e.exercicio = de.exercicio WHERE de.desafio = ? ORDER BY e.nome",
                codigoDesafio);
            List<Exercicio> exercicios = new ArrayList<>();
            while (rs.next()) {
                exercicios.add(this.mapearSimples(rs, Exercicio.class));
            }
            return exercicios;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar exercícios do desafio.", e);
        }
    }

    public int inserirDesafio(String nome, String descricao) {
        try {
            return this.executarUpdateRetornandoChave("INSERT INTO DESAFIOS (nome, descricao) VALUES (?, ?)", nome, descricao);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir desafio.", e);
        }
    }

    public void adicionarExercicioAoDesafio(int codigoDesafio, int codigoExercicio) {
        try {
            this.executarUpdate("INSERT INTO DESAFIOS_EXERCICIOS (desafio, exercicio) VALUES (?, ?)", codigoDesafio, codigoExercicio);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao adicionar exercício ao desafio.", e);
        }
    }

    public void removerExercicioDoDesafio(int codigoDesafio, int codigoExercicio) {
        try {
            this.executarUpdate("DELETE FROM DESAFIOS_EXERCICIOS WHERE desafio = ? AND exercicio = ?", codigoDesafio, codigoExercicio);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover exercício do desafio.", e);
        }
    }

    public void atualizarDesafio(int codigoDesafio, String nome, String descricao) {
        try {
            this.executarUpdate("UPDATE DESAFIOS SET nome = ?, descricao = ? WHERE desafio = ?", nome, descricao, codigoDesafio);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar desafio.", e);
        }
    }

    public void excluirDesafio(int codigoDesafio) {
        try {
            this.executarUpdate("DELETE FROM DESAFIOS WHERE desafio = ?", codigoDesafio);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir desafio.", e);
        }
    }
}