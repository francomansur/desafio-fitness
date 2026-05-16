package br.com.desafiofitness.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Singleton para gerenciar conexão MySQL.
 * Responsável por: criar/reutilizar conexão, preparar statements e executar queries.
 */
public class MySqlSingleton {
    private static final String URL = "jdbc:mysql://mysql:3306/desafio-fitness";
    private static final String USER = "teste";
    private static final String PASSWORD = "teste";

    private static MySqlSingleton instance;
    private Connection conexao;

    private MySqlSingleton() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver MySQL nao encontrado no projeto.", e);
        }
    }

    public static synchronized MySqlSingleton getInstance() {
        if (instance == null) {
            instance = new MySqlSingleton();
        }
        return instance;
    }

    private Connection obterConexao() throws SQLException {
        if (this.conexao == null || this.conexao.isClosed()) {
            this.conexao = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return this.conexao;
    }

    private void vincularParametros(PreparedStatement ps, Object... parametros) throws SQLException {
        for (int i = 0; i < parametros.length; i++) {
            ps.setObject(i + 1, parametros[i]);
        }
    }

    public ResultSet executarConsulta(String sql, Object... parametros) throws SQLException {
        PreparedStatement ps = this.obterConexao().prepareStatement(sql);
        this.vincularParametros(ps, parametros);
        return ps.executeQuery();
    }

    public int executarUpdate(String sql, Object... parametros) throws SQLException {
        try (PreparedStatement ps = this.obterConexao().prepareStatement(sql)) {
            this.vincularParametros(ps, parametros);
            return ps.executeUpdate();
        }
    }
}
