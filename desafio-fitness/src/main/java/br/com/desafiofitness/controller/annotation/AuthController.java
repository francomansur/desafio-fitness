package br.com.desafiofitness.controller.annotation;

import br.com.desafiofitness.config.JwtUtil;
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
import java.util.Map;

@WebServlet("/auth/login")
public class AuthController extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> result = new HashMap<>();

        try {
            Map<String, String> credenciais = objectMapper.readValue(request.getReader(), new TypeReference<Map<String, String>>() {});
            String email = credenciais.get("email");
            String senha = credenciais.get("senha");

            if (email == null || senha == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                result.put("success", false);
                result.put("error", "Email e senha sao obrigatorios.");
                objectMapper.writeValue(response.getWriter(), result);
                return;
            }

            Usuario usuario = usuarioService.autenticar(email, senha);
            if (usuario == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                result.put("success", false);
                result.put("error", "Email ou senha incorretos.");
            } else {
                String token = JwtUtil.gerarToken(usuario.getUsuario(), usuario.getEmail());
                result.put("success", true);
                result.put("token", token);
                result.put("usuario", usuario);
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            result.put("success", false);
            result.put("error", "Formato de requisicao invalido.");
        }

        objectMapper.writeValue(response.getWriter(), result);
    }
}
