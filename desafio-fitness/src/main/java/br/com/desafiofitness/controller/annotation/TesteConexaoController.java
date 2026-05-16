package br.com.desafiofitness.controller.annotation;

import br.com.desafiofitness.config.MySqlSingleton;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLSyntaxErrorException;

@WebServlet("/teste-conexao")
public class TesteConexaoController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain; charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            MySqlSingleton.getInstance().executarConsulta("SELECT 1");
            out.println("Conexão com o banco: OK");

        } catch (SQLSyntaxErrorException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println("Erro de sintaxe SQL: " + e.getMessage());
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println("Erro ao conectar: " + e.getMessage());
        }
    }
}
