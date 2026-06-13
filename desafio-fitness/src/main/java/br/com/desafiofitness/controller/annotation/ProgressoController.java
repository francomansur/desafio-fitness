package br.com.desafiofitness.controller.annotation;

import br.com.desafiofitness.model.Desafio;
import br.com.desafiofitness.model.Usuario;
import br.com.desafiofitness.service.DesafioService;
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

@WebServlet("/api/progresso/*")
public class ProgressoController extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService();
    private final DesafioService desafioService = new DesafioService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        this.desafioService.sincronizarProgressoUsuariosDesafios();

        List<Usuario> usuarios = this.usuarioService.listarTodosUsuarios();
        List<Desafio> desafios = this.desafioService.listarTodosDesafios();

        // Tenta pegar o ID do usuário autenticado a partir do filtro JWT
        Integer usuarioSelecionado = (Integer) request.getAttribute("usuarioId");

        // Se passar o parâmetro de query "usuario", permite visualizar o progresso de outro usuário (útil para admins)
        String paramUsuario = request.getParameter("usuario");
        if (paramUsuario != null && !paramUsuario.isBlank()) {
            try {
                usuarioSelecionado = Integer.parseInt(paramUsuario);
            } catch (NumberFormatException e) {
                // Mantém o autenticado
            }
        }

        Map<String, Boolean> exerciciosConcluidos = new HashMap<>();
        Map<Integer, Integer> progressoPorDesafio = new HashMap<>();

        if (usuarioSelecionado != null) {
            for (Desafio desafio : desafios) {
                int concluidos = this.desafioService.contarExerciciosConcluidosPorUsuarioNoDesafio(
                    usuarioSelecionado,
                    desafio.getDesafio());
                progressoPorDesafio.put(desafio.getDesafio(), concluidos);

                for (var exercicio : desafio.getExercicios()) {
                    boolean marcado = this.desafioService.exercicioConcluidoPorUsuario(
                        usuarioSelecionado,
                        desafio.getDesafio(),
                        exercicio.getExercicio());
                    exerciciosConcluidos.put(desafio.getDesafio() + "-" + exercicio.getExercicio(), marcado);
                }
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("usuarios", usuarios);
        result.put("desafios", desafios);
        result.put("usuarioSelecionado", usuarioSelecionado);
        result.put("exerciciosConcluidos", exerciciosConcluidos);
        result.put("progressoPorDesafio", progressoPorDesafio);

        objectMapper.writeValue(response.getWriter(), result);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> result = new HashMap<>();

        try {
            // Pega o ID do usuário logado via JWT
            Integer usuarioSelecionado = (Integer) request.getAttribute("usuarioId");

            Map<String, Object> body = objectMapper.readValue(request.getReader(), new TypeReference<Map<String, Object>>() {});
            Integer codigoDesafio = Integer.parseInt(body.get("desafio").toString());
            Integer codigoExercicio = Integer.parseInt(body.get("exercicio").toString());
            boolean concluido = Boolean.parseBoolean(body.get("concluido").toString());

            // Se o corpo contiver um usuário explicitamente, permite a sobrescrita (ex: admins alterando progresso de terceiros)
            if (body.containsKey("usuario")) {
                usuarioSelecionado = Integer.parseInt(body.get("usuario").toString());
            }

            if (usuarioSelecionado == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                result.put("success", false);
                result.put("error", "Usuario nao autenticado ou nao especificado.");
                objectMapper.writeValue(response.getWriter(), result);
                return;
            }

            this.desafioService.atualizarProgressoExercicioUsuario(
                usuarioSelecionado,
                codigoDesafio,
                codigoExercicio,
                concluido);

            result.put("success", true);
            result.put("message", "Progresso atualizado com sucesso.");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            result.put("success", false);
            result.put("error", e.getMessage());
        }

        objectMapper.writeValue(response.getWriter(), result);
    }
}
