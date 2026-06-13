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

    public void inserirUsuario(String nome, String email, String senha) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório.");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("E-mail é obrigatório.");
        }
        if (senha == null || senha.length() < 6) {
            throw new IllegalArgumentException("Senha é obrigatória e deve ter pelo menos 6 caracteres.");
        }
        String senhaHash = org.mindrot.jbcrypt.BCrypt.hashpw(senha, org.mindrot.jbcrypt.BCrypt.gensalt());
        this.usuarioDAO.inserirUsuario(nome, email, senhaHash);
    }

    public Usuario autenticar(String email, String senha) {
        if (email == null || email.isBlank() || senha == null || senha.isBlank()) {
            return null;
        }
        Usuario usuario = this.usuarioDAO.buscarUsuarioPorEmail(email);
        if (usuario == null || usuario.getSenha() == null) {
            return null;
        }
        if (org.mindrot.jbcrypt.BCrypt.checkpw(senha, usuario.getSenha())) {
            return usuario;
        }
        return null;
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
