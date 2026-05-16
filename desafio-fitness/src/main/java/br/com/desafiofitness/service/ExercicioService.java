package br.com.desafiofitness.service;

import br.com.desafiofitness.dao.ExercicioDAO;
import br.com.desafiofitness.model.Exercicio;

import java.util.List;

public class ExercicioService {

    private final ExercicioDAO exercicioDAO;

    public ExercicioService() {
        this.exercicioDAO = new ExercicioDAO();
    }

    public Exercicio buscarExercicioPorId(int codigoExercicio) {
        if (codigoExercicio <= 0) {
            return null;
        }
        return this.exercicioDAO.buscarExercicioPorId(codigoExercicio);
    }

    public List<Exercicio> listarTodosExercicios() {
        return this.exercicioDAO.listarTodosExercicios();
    }

    public void inserirExercicio(String nome, String descricao) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório.");
        }
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("Descrição é obrigatória.");
        }
        this.exercicioDAO.inserirExercicio(nome, descricao);
    }

    public void atualizarExercicio(int codigoExercicio, String nome, String descricao) {
        if (codigoExercicio <= 0) {
            throw new IllegalArgumentException("Id inválido.");
        }
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório.");
        }
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("Descrição é obrigatória.");
        }
        this.exercicioDAO.atualizarExercicio(codigoExercicio, nome, descricao);
    }

    public void excluirExercicio(int codigoExercicio) {
        if (codigoExercicio <= 0) {
            throw new IllegalArgumentException("Id inválido.");
        }
        this.exercicioDAO.excluirExercicio(codigoExercicio);
    }
}
