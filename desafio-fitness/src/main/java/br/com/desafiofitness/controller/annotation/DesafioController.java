package br.com.desafiofitness.controller.annotation;

import br.com.desafiofitness.model.Desafio;
import br.com.desafiofitness.model.Exercicio;
import br.com.desafiofitness.service.DesafioService;
import br.com.desafiofitness.service.ExercicioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebServlet("/desafios")
public class DesafioController extends HttpServlet {

    private final DesafioService desafioService = new DesafioService();
    private final ExercicioService exercicioService = new ExercicioService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Desafio> desafios = this.desafioService.listarTodosDesafios();
        List<Exercicio> todosExercicios = this.exercicioService.listarTodosExercicios();
        request.setAttribute("desafios", desafios);
        request.setAttribute("todosExercicios", todosExercicios);
        request.getRequestDispatcher("/desafio/lista.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao = request.getParameter("acao");

        if ("inserir".equals(acao)) {
            String nome = request.getParameter("nome");
            String descricao = request.getParameter("descricao");
            String[] exerciciosParam = request.getParameterValues("exercicios");
            int[] exercicioIds = null;
            if (exerciciosParam != null) {
                exercicioIds = Arrays.stream(exerciciosParam)
                        .mapToInt(Integer::parseInt)
                        .toArray();
            }
            this.desafioService.inserirDesafio(nome, descricao, exercicioIds);

        } else if ("atualizar".equals(acao)) {
            int codigoDesafio = Integer.parseInt(request.getParameter("codigoDesafio"));
            String nome = request.getParameter("nome");
            String descricao = request.getParameter("descricao");
            this.desafioService.atualizarDesafio(codigoDesafio, nome, descricao);

        } else if ("excluir".equals(acao)) {
            int codigoDesafio = Integer.parseInt(request.getParameter("codigoDesafio"));
            this.desafioService.excluirDesafio(codigoDesafio);

        } else if ("adicionarExercicio".equals(acao)) {
            int codigoDesafio = Integer.parseInt(request.getParameter("codigoDesafio"));
            int codigoExercicio = Integer.parseInt(request.getParameter("codigoExercicio"));
            this.desafioService.adicionarExercicioAoDesafio(codigoDesafio, codigoExercicio);

        } else if ("removerExercicio".equals(acao)) {
            int codigoDesafio = Integer.parseInt(request.getParameter("codigoDesafio"));
            int codigoExercicio = Integer.parseInt(request.getParameter("codigoExercicio"));
            this.desafioService.removerExercicioDoDesafio(codigoDesafio, codigoExercicio);
        }

        response.sendRedirect(request.getContextPath() + "/desafios");
    }
}
