<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="br.com.desafiofitness.model.Usuario" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Usuários</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">
<div class="container py-4">

    <h1 class="mb-4">Usuários</h1>
    <a href="<%= request.getContextPath() %>/progresso" class="btn btn-outline-secondary mb-4">Voltar</a>

    <% if (request.getAttribute("erro") != null) { %>
    <div class="alert alert-danger"><%= request.getAttribute("erro") %></div>
    <% } %>

    <div class="card mb-4">
        <div class="card-header">
            <span id="cardTitle">Cadastrar</span>
        </div>
        <div class="card-body">
            <form action="<%= request.getContextPath() %>/usuarios" method="post" class="row g-3" id="formulario">
                <input type="hidden" name="acao" value="inserir" id="acao">
                <input type="hidden" name="usuario" id="usuarioId">
                <div class="col-md-5">
                    <input type="text" name="nome" class="form-control" placeholder="Nome" required id="nome">
                </div>
                <div class="col-md-5">
                    <input type="email" name="email" class="form-control" placeholder="E-mail" required id="email">
                </div>
                <div class="col-md-2">
                    <button type="submit" class="btn btn-primary w-100">Salvar</button>
                </div>
            </form>
        </div>
    </div>

    <table class="table table-bordered table-striped bg-white">
        <thead class="table-dark">
            <tr>
                <th>#</th>
                <th>Nome</th>
                <th>E-mail</th>
                <th>Ações</th>
            </tr>
        </thead>
        <tbody>
            <%
                List<Usuario> usuarios = (List<Usuario>) request.getAttribute("usuarios");
                for (Usuario u : usuarios) {
            %>
            <tr data-id="<%= u.getUsuario() %>" data-nome="<%= u.getNome() %>" data-email="<%= u.getEmail() %>">
                <td><%= u.getUsuario() %></td>
                <td><%= u.getNome() %></td>
                <td><%= u.getEmail() %></td>
                <td>
                    <button type="button" class="btn btn-warning btn-sm" onclick="editarUsuario(this)">Editar</button>
                    <form action="<%= request.getContextPath() %>/usuarios" method="post" class="d-inline">
                        <input type="hidden" name="acao" value="excluir">
                        <input type="hidden" name="usuario" value="<%= u.getUsuario() %>">
                        <button type="submit" class="btn btn-danger btn-sm">Excluir</button>
                    </form>
                </td>
            </tr>
            <% } %>
        </tbody>
    </table>

</div>
<script>
    function editarUsuario(botao) {
        const tr = botao.closest('tr');
        document.getElementById('cardTitle').innerText = 'Editar';
        document.getElementById('acao').value = 'atualizar';
        document.getElementById('usuarioId').value = tr.dataset.id;
        document.getElementById('nome').value = tr.dataset.nome;
        document.getElementById('email').value = tr.dataset.email;
        document.getElementById('formulario').scrollIntoView({ behavior: 'smooth' });
    }
</script>
</body>
</html>
