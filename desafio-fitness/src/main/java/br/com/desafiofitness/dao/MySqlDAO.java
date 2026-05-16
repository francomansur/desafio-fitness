package br.com.desafiofitness.dao;

import br.com.desafiofitness.config.MySqlSingleton;
import br.com.desafiofitness.model.Mapeavel;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class MySqlDAO {

    protected ResultSet executarConsulta(String sql, Object... parametros) throws SQLException {
        return MySqlSingleton.getInstance().executarConsulta(sql, parametros);
    }

    protected int executarUpdate(String sql, Object... parametros) throws SQLException {
        return MySqlSingleton.getInstance().executarUpdate(sql, parametros);
    }

    protected <T extends Mapeavel> T mapearSimples(ResultSet rs, Class<T> classe) {
        try {
            T objeto = classe.getDeclaredConstructor().newInstance();
            objeto.preencher(rs);
            return objeto;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao mapear objeto.", e);
        }
    }
}