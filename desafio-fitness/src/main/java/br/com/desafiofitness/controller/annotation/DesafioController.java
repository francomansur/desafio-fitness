package br.com.desafiofitness.controller.annotation;

import br.com.desafiofitness.model.Desafio;
import br.com.desafiofitness.service.DesafioService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/desafios/*")
public class DesafioController extends HttpServlet {

    private final DesafioService desafioService = new DesafioService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            List<Desafio> desafios = this.desafioService.listarTodosDesafios();
            objectMapper.writeValue(response.getWriter(), desafios);
        } else {
            try {
                int id = Integer.parseInt(pathInfo.substring(1));
                Desafio desafio = this.desafioService.buscarDesafioPorId(id);
                if (desafio == null) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    Map<String, Object> error = new HashMap<>();
                    error.put("success", false);
                    error.put("error", "Desafio nao encontrado.");
                    objectMapper.writeValue(response.getWriter(), error);
                } else {
                    objectMapper.writeValue(response.getWriter(), desafio);
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
        String pathInfo = request.getPathInfo();
        String[] parts = (pathInfo != null) ? pathInfo.split("/") : new String[0];

        try {
            // Caso 1: POST /api/desafios/{id}/exercicios
            if (parts.length >= 3 && "exercicios".equals(parts[2])) {
                int codigoDesafio = Integer.parseInt(parts[1]);
                Map<String, Object> body = objectMapper.readValue(request.getReader(), new TypeReference<Map<String, Object>>() {});
                int codigoExercicio = Integer.parseInt(body.get("exercicio").toString());

                this.desafioService.adicionarExercicioAoDesafio(codigoDesafio, codigoExercicio);
                result.put("success", true);
                result.put("message", "Exercicio adicionado ao desafio com sucesso.");
            } 
            // Caso 2: POST /api/desafios
            else if (pathInfo == null || pathInfo.equals("/")) {
                Map<String, Object> body = objectMapper.readValue(request.getReader(), new TypeReference<Map<String, Object>>() {});
                String nome = (String) body.get("nome");
                String descricao = (String) body.get("descricao");
                List<Integer> exerciciosList = (List<Integer>) body.get("exercicios");
                int[] exercicioIds = null;
                if (exerciciosList != null) {
                    exercicioIds = exerciciosList.stream().mapToInt(Integer::intValue).toArray();
                }

                this.desafioService.inserirDesafio(nome, descricao, exercicioIds);
                response.setStatus(HttpServletResponse.SC_CREATED);
                result.put("success", true);
                result.put("message", "Desafio criado com sucesso.");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                result.put("success", false);
                result.put("error", "Rota invalida.");
            }
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
            result.put("error", "ID de desafio nao especificado.");
            objectMapper.writeValue(response.getWriter(), result);
            return;
        }

        try {
            int id = Integer.parseInt(pathInfo.substring(1));
            Map<String, String> body = objectMapper.readValue(request.getReader(), new TypeReference<Map<String, String>>() {});
            String nome = body.get("nome");
            String descricao = body.get("descricao");

            this.desafioService.atualizarDesafio(id, nome, descricao);
            result.put("success", true);
            result.put("message", "Desafio atualizado com sucesso.");
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
        String[] parts = (pathInfo != null) ? pathInfo.split("/") : new String[0];

        try {
            // Caso 1: DELETE /api/desafios/{id}/exercicios/{exercicioId}
            if (parts.length >= 4 && "exercicios".equals(parts[2])) {
                int codigoDesafio = Integer.parseInt(parts[1]);
                int codigoExercicio = Integer.parseInt(parts[3]);

                this.desafioService.removerExercicioDoDesafio(codigoDesafio, codigoExercicio);
                result.put("success", true);
                result.put("message", "Exercicio removido do desafio com sucesso.");
            }
            // Caso 2: DELETE /api/desafios/{id}
            else if (parts.length >= 2) {
                int id = Integer.parseInt(parts[1]);
                this.desafioService.excluirDesafio(id);
                result.put("success", true);
                result.put("message", "Desafio excluido com sucesso.");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                result.put("success", false);
                result.put("error", "Rota invalida.");
            }
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
