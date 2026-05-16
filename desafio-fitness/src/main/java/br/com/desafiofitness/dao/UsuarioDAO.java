package br.com.desafiofitness.dao;

import br.com.desafiofitness.model.Usuario;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO extends MySqlDAO {

    public Usuario buscarUsuarioPorId(int codigoUsuario) {
        try {
            ResultSet rs = this.executarConsulta("SELECT * FROM USUARIOS WHERE usuario = ?", codigoUsuario);
            if (!rs.next()) {
                return null;
            }
            return this.mapearSimples(rs, Usuario.class);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário.", e);
        }
    }

    public List<Usuario> listarTodosUsuarios() {
        try {
            ResultSet rs = this.executarConsulta("SELECT * FROM USUARIOS ORDER BY nome");
            List<Usuario> usuarios = new ArrayList<>();
            while (rs.next()) {
                usuarios.add(this.mapearSimples(rs, Usuario.class));
            }
            return usuarios;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar usuários.", e);
        }
    }

    public void inserirUsuario(String nome, String email) {
        try {
            this.executarUpdate("INSERT INTO USUARIOS (nome, email) VALUES (?, ?)", nome, email);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir usuário.", e);
        }
    }

    public void atualizarUsuario(int codigoUsuario, String nome, String email) {
        try {
            this.executarUpdate("UPDATE USUARIOS SET nome = ?, email = ? WHERE usuario = ?", nome, email, codigoUsuario);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar usuário.", e);
        }
    }

    public void excluirUsuario(int codigoUsuario) {
        try {
            this.executarUpdate("DELETE FROM USUARIOS WHERE usuario = ?", codigoUsuario);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir usuário.", e);
        }
    }
}