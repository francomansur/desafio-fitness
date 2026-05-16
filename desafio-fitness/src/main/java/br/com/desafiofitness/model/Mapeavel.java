package br.com.desafiofitness.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Mapeavel {
    void preencher(ResultSet rs) throws SQLException;
}