package br.com.desafiofitness.config;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebFilter("/api/*")
public class JwtFilter implements Filter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Permitir requisições de criação de usuário (registro) sem token
        String path = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();

        if (path.endsWith("/api/usuarios") && "POST".equalsIgnoreCase(method)) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = httpRequest.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            responderNaoAutorizado(httpResponse, "Token de autorizacao ausente ou invalido.");
            return;
        }

        String token = authHeader.substring(7);

        try {
            DecodedJWT jwt = JwtUtil.validarToken(token);
            httpRequest.setAttribute("usuarioId", Integer.parseInt(jwt.getSubject()));
            httpRequest.setAttribute("usuarioEmail", jwt.getClaim("email").asString());
            chain.doFilter(request, response);
        } catch (Exception e) {
            responderNaoAutorizado(httpResponse, "Token invalido ou expirado.");
        }
    }

    private void responderNaoAutorizado(HttpServletResponse response, String mensagem) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Map<String, Object> erro = new HashMap<>();
        erro.put("success", false);
        erro.put("error", mensagem);
        objectMapper.writeValue(response.getWriter(), erro);
    }

    @Override
    public void destroy() {}
}
