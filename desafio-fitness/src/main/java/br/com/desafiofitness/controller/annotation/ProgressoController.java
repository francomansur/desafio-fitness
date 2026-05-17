package br.com.desafiofitness.controller.annotation;

import br.com.desafiofitness.model.Desafio;
import br.com.desafiofitness.model.Usuario;
import br.com.desafiofitness.service.DesafioService;
import br.com.desafiofitness.service.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet({"/", "/progresso"})
public class ProgressoController extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService();
    private final DesafioService desafioService = new DesafioService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        this.desafioService.sincronizarProgressoUsuariosDesafios();

        List<Usuario> usuarios = this.usuarioService.listarTodosUsuarios();
        List<Desafio> desafios = this.desafioService.listarTodosDesafios();

        Integer usuarioSelecionado = this.resolverUsuarioSelecionado(request, usuarios);
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

        request.setAttribute("usuarios", usuarios);
        request.setAttribute("desafios", desafios);
        request.setAttribute("usuarioSelecionado", usuarioSelecionado);
        request.setAttribute("exerciciosConcluidos", exerciciosConcluidos);
        request.setAttribute("progressoPorDesafio", progressoPorDesafio);

        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String acao = request.getParameter("acao");
        Integer usuarioSelecionado = Integer.parseInt(request.getParameter("usuario"));

        if ("atualizarProgresso".equals(acao)) {
            Integer codigoDesafio = Integer.parseInt(request.getParameter("desafio"));
            Integer codigoExercicio = Integer.parseInt(request.getParameter("exercicio"));
            boolean concluido = "true".equals(request.getParameter("concluido"));

            this.desafioService.atualizarProgressoExercicioUsuario(
                usuarioSelecionado,
                codigoDesafio,
                codigoExercicio,
                concluido);
        }

        String destino = request.getContextPath() + "/progresso";
        if (usuarioSelecionado != null) {
            destino += "?usuario=" + usuarioSelecionado;
        }
        response.sendRedirect(destino);
    }

    private Integer resolverUsuarioSelecionado(HttpServletRequest request, List<Usuario> usuarios) {
        try {
            return Integer.parseInt(request.getParameter("usuario"));
        } catch (Exception e) {
            if (!usuarios.isEmpty()) {
                return usuarios.get(0).getUsuario();
            }
            return null;
        }
    }
}
