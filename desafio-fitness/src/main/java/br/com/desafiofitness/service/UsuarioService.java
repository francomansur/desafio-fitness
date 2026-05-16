package br.com.desafiofitness.service;

import br.com.desafiofitness.dao.UsuarioDAO;
import br.com.desafiofitness.model.Usuario;

import java.util.List;

public class UsuarioService {

    private final UsuarioDAO usuarioDAO;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public Usuario buscarUsuarioPorId(int codigoUsuario) {
        if (codigoUsuario <= 0) {
            return null;
        }
        return this.usuarioDAO.buscarUsuarioPorId(codigoUsuario);
    }

    public List<Usuario> listarTodosUsuarios() {
        return this.usuarioDAO.listarTodosUsuarios();
    }

    public void inserirUsuario(String nome, String email) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório.");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("E-mail é obrigatório.");
        }
        this.usuarioDAO.inserirUsuario(nome, email);
    }

    public void atualizarUsuario(int codigoUsuario, String nome, String email) {
        if (codigoUsuario <= 0) {
            throw new IllegalArgumentException("Id inválido.");
        }
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório.");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("E-mail é obrigatório.");
        }
        this.usuarioDAO.atualizarUsuario(codigoUsuario, nome, email);
    }

    public void excluirUsuario(int codigoUsuario) {
        if (codigoUsuario <= 0) {
            throw new IllegalArgumentException("Id inválido.");
        }
        this.usuarioDAO.excluirUsuario(codigoUsuario);
    }
}
