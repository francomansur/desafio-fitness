package br.com.desafiofitness.controller.annotation;

import br.com.desafiofitness.model.Exercicio;
import br.com.desafiofitness.service.ExercicioService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/exercicios/*")
public class ExercicioController extends HttpServlet {

    private final ExercicioService exercicioService = new ExercicioService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            List<Exercicio> exercicios = this.exercicioService.listarTodosExercicios();
            objectMapper.writeValue(response.getWriter(), exercicios);
        } else {
            try {
                int id = Integer.parseInt(pathInfo.substring(1));
                Exercicio exercicio = this.exercicioService.buscarExercicioPorId(id);
                if (exercicio == null) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    Map<String, Object> error = new HashMap<>();
                    error.put("success", false);
                    error.put("error", "Exercicio nao encontrado.");
                    objectMapper.writeValue(response.getWriter(), error);
                } else {
                    objectMapper.writeValue(response.getWriter(), exercicio);
                }
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("error", "ID invalido.");
                objectMapper.writeValue(response.getWriter(), error);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> result = new HashMap<>();

        try {
            Map<String, String> body = objectMapper.readValue(request.getReader(), new TypeReference<Map<String, String>>() {});
            String nome = body.get("nome");
            String descricao = body.get("descricao");

            this.exercicioService.inserirExercicio(nome, descricao);
            response.setStatus(HttpServletResponse.SC_CREATED);
            result.put("success", true);
            result.put("message", "Exercicio criado com sucesso.");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            result.put("success", false);
            result.put("error", e instanceof RuntimeException ? e.getMessage() : "Corpo da requisicao invalido ou vazio.");
        }

        objectMapper.writeValue(response.getWriter(), result);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> result = new HashMap<>();
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            result.put("success", false);
            result.put("error", "ID de exercicio nao especificado.");
            objectMapper.writeValue(response.getWriter(), result);
            return;
        }

        try {
            int id = Integer.parseInt(pathInfo.substring(1));
            Map<String, String> body = objectMapper.readValue(request.getReader(), new TypeReference<Map<String, String>>() {});
            String nome = body.get("nome");
            String descricao = body.get("descricao");

            this.exercicioService.atualizarExercicio(id, nome, descricao);
            result.put("success", true);
            result.put("message", "Exercicio atualizado com sucesso.");
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            result.put("success", false);
            result.put("error", "ID invalido.");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            result.put("success", false);
            result.put("error", e instanceof RuntimeException ? e.getMessage() : "Corpo da requisicao invalido ou vazio.");
        }

        objectMapper.writeValue(response.getWriter(), result);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> result = new HashMap<>();
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            result.put("success", false);
            result.put("error", "ID de exercicio nao especificado.");
            objectMapper.writeValue(response.getWriter(), result);
            return;
        }

        try {
            int id = Integer.parseInt(pathInfo.substring(1));
            this.exercicioService.excluirExercicio(id);
            result.put("success", true);
            result.put("message", "Exercicio excluido com sucesso.");
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            result.put("success", false);
            result.put("error", "ID invalido.");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            result.put("success", false);
            result.put("error", e.getMessage());
        }

        objectMapper.writeValue(response.getWriter(), result);
    }
}
