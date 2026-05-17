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

    public void sincronizarProgressoUsuariosDesafios() {
        this.desafioDAO.sincronizarProgressoUsuariosDesafios();
    }

    public boolean exercicioConcluidoPorUsuario(int codigoUsuario, int codigoDesafio, int codigoExercicio) {
        if (codigoUsuario <= 0) {
            throw new IllegalArgumentException("Id de usuário inválido.");
        }
        if (codigoDesafio <= 0 || codigoExercicio <= 0) {
            throw new IllegalArgumentException("Ids inválidos.");
        }
        return this.desafioDAO.exercicioConcluidoPorUsuario(codigoUsuario, codigoDesafio, codigoExercicio);
    }

    public int contarExerciciosConcluidosPorUsuarioNoDesafio(int codigoUsuario, int codigoDesafio) {
        if (codigoUsuario <= 0) {
            throw new IllegalArgumentException("Id de usuário inválido.");
        }
        if (codigoDesafio <= 0) {
            throw new IllegalArgumentException("Id de desafio inválido.");
        }
        return this.desafioDAO.contarExerciciosConcluidosPorUsuarioNoDesafio(codigoUsuario, codigoDesafio);
    }

    public void atualizarProgressoExercicioUsuario(int codigoUsuario, int codigoDesafio, int codigoExercicio, boolean concluido) {
        if (codigoUsuario <= 0 || codigoDesafio <= 0 || codigoExercicio <= 0) {
            throw new IllegalArgumentException("Ids inválidos.");
        }
        this.desafioDAO.atualizarProgressoExercicioUsuario(codigoUsuario, codigoDesafio, codigoExercicio, concluido);
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
        this.desafioDAO.sincronizarProgressoUsuariosDesafios();
    }

    public void adicionarExercicioAoDesafio(int codigoDesafio, int codigoExercicio) {
        if (codigoDesafio <= 0 || codigoExercicio <= 0) {
            throw new IllegalArgumentException("Ids inválidos.");
        }
        this.desafioDAO.adicionarExercicioAoDesafio(codigoDesafio, codigoExercicio);
        this.desafioDAO.sincronizarProgressoUsuariosDesafios();
    }

    public void removerExercicioDoDesafio(int codigoDesafio, int codigoExercicio) {
        if (codigoDesafio <= 0 || codigoExercicio <= 0) {
            throw new IllegalArgumentException("Ids inválidos.");
        }
        this.desafioDAO.removerExercicioDoDesafio(codigoDesafio, codigoExercicio);
        this.desafioDAO.sincronizarProgressoUsuariosDesafios();
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
