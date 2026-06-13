package br.com.desafiofitness.controller.annotation;

import br.com.desafiofitness.model.Usuario;
import br.com.desafiofitness.service.UsuarioService;
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

@WebServlet("/api/usuarios/*")
public class UsuarioController extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            List<Usuario> usuarios = this.usuarioService.listarTodosUsuarios();
            objectMapper.writeValue(response.getWriter(), usuarios);
        } else {
            try {
                int id = Integer.parseInt(pathInfo.substring(1));
                Usuario usuario = this.usuarioService.buscarUsuarioPorId(id);
                if (usuario == null) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    Map<String, Object> error = new HashMap<>();
                    error.put("success", false);
                    error.put("error", "Usuario nao encontrado.");
                    objectMapper.writeValue(response.getWriter(), error);
                } else {
                    objectMapper.writeValue(response.getWriter(), usuario);
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
            String email = body.get("email");
            String senha = body.get("senha");

            this.usuarioService.inserirUsuario(nome, email, senha);
            response.setStatus(HttpServletResponse.SC_CREATED);
            result.put("success", true);
            result.put("message", "Usuario criado com sucesso.");
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
            result.put("error", "ID de usuario nao especificado.");
            objectMapper.writeValue(response.getWriter(), result);
            return;
        }

        try {
            int id = Integer.parseInt(pathInfo.substring(1));
            Map<String, String> body = objectMapper.readValue(request.getReader(), new TypeReference<Map<String, String>>() {});
            String nome = body.get("nome");
            String email = body.get("email");

            this.usuarioService.atualizarUsuario(id, nome, email);
            result.put("success", true);
            result.put("message", "Usuario atualizado com sucesso.");
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
            result.put("error", "ID de usuario nao especificado.");
            objectMapper.writeValue(response.getWriter(), result);
            return;
        }

        try {
            int id = Integer.parseInt(pathInfo.substring(1));
            this.usuarioService.excluirUsuario(id);
            result.put("success", true);
            result.put("message", "Usuario excluido com sucesso.");
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
