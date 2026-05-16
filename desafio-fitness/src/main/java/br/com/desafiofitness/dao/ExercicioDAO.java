package br.com.desafiofitness.dao;

import br.com.desafiofitness.model.Exercicio;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExercicioDAO extends MySqlDAO {

    public Exercicio buscarExercicioPorId(int codigoExercicio) {
        try {
            ResultSet rs = this.executarConsulta("SELECT * FROM EXERCICIOS WHERE exercicio = ?", codigoExercicio);
            if (!rs.next()) {
                return null;
            }
            return this.mapearSimples(rs, Exercicio.class);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar exercício.", e);
        }
    }

    public List<Exercicio> listarTodosExercicios() {
        try {
            ResultSet rs = this.executarConsulta("SELECT * FROM EXERCICIOS ORDER BY nome");
            List<Exercicio> exercicios = new ArrayList<>();
            while (rs.next()) {
                exercicios.add(this.mapearSimples(rs, Exercicio.class));
            }
            return exercicios;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar exercícios.", e);
        }
    }

    public void inserirExercicio(String nome, String descricao) {
        try {
            this.executarUpdate("INSERT INTO EXERCICIOS (nome, descricao) VALUES (?, ?)", nome, descricao);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir exercício.", e);
        }
    }

    public void atualizarExercicio(int codigoExercicio, String nome, String descricao) {
        try {
            this.executarUpdate("UPDATE EXERCICIOS SET nome = ?, descricao = ? WHERE exercicio = ?", nome, descricao, codigoExercicio);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar exercício.", e);
        }
    }

    public void excluirExercicio(int codigoExercicio) {
        try {
            this.executarUpdate("DELETE FROM EXERCICIOS WHERE exercicio = ?", codigoExercicio);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir exercício.", e);
        }
    }
}