package br.com.desafiofitness.controller.annotation;

import br.com.desafiofitness.model.Usuario;
import br.com.desafiofitness.service.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/usuarios")
public class UsuarioController extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Usuario> usuarios = this.usuarioService.listarTodosUsuarios();
        request.setAttribute("usuarios", usuarios);
        request.getRequestDispatcher("/usuario/lista.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao = request.getParameter("acao");

        if ("inserir".equals(acao)) {
            String nome = request.getParameter("nome");
            String email = request.getParameter("email");
            try {
                this.usuarioService.inserirUsuario(nome, email);
            } catch (RuntimeException e) {
                List<Usuario> usuarios = this.usuarioService.listarTodosUsuarios();
                request.setAttribute("usuarios", usuarios);
                request.setAttribute("erro", "Nao foi possivel criar o usuario. O email já está sendo usado.");
                request.getRequestDispatcher("/usuario/lista.jsp").forward(request, response);
                return;
            }

        } else if ("atualizar".equals(acao)) {
            int codigoUsuario = Integer.parseInt(request.getParameter("usuario"));
            String nome = request.getParameter("nome");
            String email = request.getParameter("email");
            this.usuarioService.atualizarUsuario(codigoUsuario, nome, email);

        } else if ("excluir".equals(acao)) {
            int codigoUsuario = Integer.parseInt(request.getParameter("usuario"));
            this.usuarioService.excluirUsuario(codigoUsuario);
        }

        response.sendRedirect(request.getContextPath() + "/usuarios");
    }
}
