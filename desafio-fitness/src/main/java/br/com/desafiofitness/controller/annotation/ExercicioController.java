package br.com.desafiofitness.controller.annotation;

import br.com.desafiofitness.model.Exercicio;
import br.com.desafiofitness.service.ExercicioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/exercicios")
public class ExercicioController extends HttpServlet {

    private final ExercicioService exercicioService = new ExercicioService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Exercicio> exercicios = this.exercicioService.listarTodosExercicios();
        request.setAttribute("exercicios", exercicios);
        request.getRequestDispatcher("/exercicio/lista.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao = request.getParameter("acao");

        if ("inserir".equals(acao)) {
            String nome = request.getParameter("nome");
            String descricao = request.getParameter("descricao");
            this.exercicioService.inserirExercicio(nome, descricao);

        } else if ("atualizar".equals(acao)) {
            int codigoExercicio = Integer.parseInt(request.getParameter("codigoExercicio"));
            String nome = request.getParameter("nome");
            String descricao = request.getParameter("descricao");
            this.exercicioService.atualizarExercicio(codigoExercicio, nome, descricao);

        } else if ("excluir".equals(acao)) {
            int codigoExercicio = Integer.parseInt(request.getParameter("codigoExercicio"));
            this.exercicioService.excluirExercicio(codigoExercicio);
        }

        response.sendRedirect(request.getContextPath() + "/exercicios");
    }
}
