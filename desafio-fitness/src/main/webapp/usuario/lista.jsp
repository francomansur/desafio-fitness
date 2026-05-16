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

    <div class="card mb-4">
        <div class="card-header">Cadastrar</div>
        <div class="card-body">
            <form action="<%= request.getContextPath() %>/usuarios" method="post" class="row g-3">
                <input type="hidden" name="acao" value="inserir">
                <div class="col-md-5">
                    <input type="text" name="nome" class="form-control" placeholder="Nome" required>
                </div>
                <div class="col-md-5">
                    <input type="email" name="email" class="form-control" placeholder="E-mail" required>
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
            <tr>
                <td><%= u.getUsuario() %></td>
                <td><%= u.getNome() %></td>
                <td><%= u.getEmail() %></td>
                <td>
                    <form action="<%= request.getContextPath() %>/usuarios" method="post">
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
</body>
</html>
