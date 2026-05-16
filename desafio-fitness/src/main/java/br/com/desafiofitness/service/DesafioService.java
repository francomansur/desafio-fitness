package br.com.desafiofitness.service;

import br.com.desafiofitness.dao.DesafioDAO;
import br.com.desafiofitness.model.Desafio;

import java.util.List;

public class DesafioService {

    private final DesafioDAO desafioDAO;

    public DesafioService() {
        this.desafioDAO = new DesafioDAO();
    }

    public List<Desafio> listarTodosDesafios() {
        return this.desafioDAO.listarTodosDesafios();
    }

    public void inserirDesafio(String nome, String descricao, int[] exercicioIds) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório.");
        }
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("Descrição é obrigatória.");
        }
        int novoId = this.desafioDAO.inserirDesafio(nome, descricao);
        if (exercicioIds != null) {
            for (int idExercicio : exercicioIds) {
                this.desafioDAO.adicionarExercicioAoDesafio(novoId, idExercicio);
            }
        }
    }

    public void adicionarExercicioAoDesafio(int codigoDesafio, int codigoExercicio) {
        if (codigoDesafio <= 0 || codigoExercicio <= 0) {
            throw new IllegalArgumentException("Ids inválidos.");
        }
        this.desafioDAO.adicionarExercicioAoDesafio(codigoDesafio, codigoExercicio);
    }

    public void removerExercicioDoDesafio(int codigoDesafio, int codigoExercicio) {
        if (codigoDesafio <= 0 || codigoExercicio <= 0) {
            throw new IllegalArgumentException("Ids inválidos.");
        }
        this.desafioDAO.removerExercicioDoDesafio(codigoDesafio, codigoExercicio);
    }

    public void atualizarDesafio(int codigoDesafio, String nome, String descricao) {
        if (codigoDesafio <= 0) {
            throw new IllegalArgumentException("Id inválido.");
        }
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório.");
        }
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("Descrição é obrigatória.");
        }
        this.desafioDAO.atualizarDesafio(codigoDesafio, nome, descricao);
    }

    public void excluirDesafio(int codigoDesafio) {
        if (codigoDesafio <= 0) {
            throw new IllegalArgumentException("Id inválido.");
        }
        this.desafioDAO.excluirDesafio(codigoDesafio);
    }
}
