package br.com.desafiofitness.dao;

import br.com.desafiofitness.model.Desafio;
import br.com.desafiofitness.model.Exercicio;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DesafioDAO extends MySqlDAO {

    public void sincronizarProgressoUsuariosDesafios() {
        try {
            this.executarUpdate("""
                INSERT INTO PROGRESSO_DESAFIOS (usuario, desafio, exercicio, concluido) 
                SELECT u.usuario, de.desafio, de.exercicio, FALSE 
                FROM USUARIOS u 
                INNER JOIN DESAFIOS_EXERCICIOS de ON 1 = 1 
                LEFT JOIN PROGRESSO_DESAFIOS pd ON pd.usuario = u.usuario 
                AND pd.desafio = de.desafio AND pd.exercicio = de.exercicio 
                WHERE pd.usuario IS NULL
                """);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao sincronizar progresso dos usuários.", e);
        }
    }

    public boolean exercicioConcluidoPorUsuario(int codigoUsuario, int codigoDesafio, int codigoExercicio) {
        try {
            ResultSet rs = this.executarConsulta(
                "SELECT concluido FROM PROGRESSO_DESAFIOS WHERE usuario = ? AND desafio = ? AND exercicio = ?",
                codigoUsuario,
                codigoDesafio,
                codigoExercicio);
            if (rs.next()) {
                return rs.getBoolean("concluido");
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consultar conclusão do exercício.", e);
        }
    }

    public int contarExerciciosConcluidosPorUsuarioNoDesafio(int codigoUsuario, int codigoDesafio) {
        try {
            ResultSet rs = this.executarConsulta(
                "SELECT COUNT(*) AS total FROM PROGRESSO_DESAFIOS WHERE usuario = ? AND desafio = ? AND concluido = TRUE",
                codigoUsuario,
                codigoDesafio);
            if (rs.next()) {
                return rs.getInt("total");
            }
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar exercícios concluídos do desafio.", e);
        }
    }

    public void atualizarProgressoExercicioUsuario(int codigoUsuario, int codigoDesafio, int codigoExercicio, boolean concluido) {
        try {
            this.executarUpdate("""
                INSERT INTO PROGRESSO_DESAFIOS (usuario, desafio, exercicio, concluido) VALUES (?, ?, ?, ?) 
                ON DUPLICATE KEY UPDATE concluido = VALUES(concluido)
                """,
                codigoUsuario,
                codigoDesafio,
                codigoExercicio,
                concluido);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar progresso do exercício.", e);
        }
    }

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