<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="br.com.desafiofitness.model.Usuario" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Usuários</title>
</head>
<body>
    <h1>Usuários</h1>

    <h2>Cadastrar</h2>
    <form action="${pageContext.request.contextPath}/usuarios" method="post">
        <input type="hidden" name="acao" value="inserir">
        <label>Nome: <input type="text" name="nome" required></label>
        <label>E-mail: <input type="email" name="email" required></label>
        <button type="submit">Salvar</button>
    </form>

    <h2>Lista</h2>
    <table border="1">
        <thead>
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
                    <form action="${pageContext.request.contextPath}/usuarios" method="post" style="display:inline">
                        <input type="hidden" name="acao" value="excluir">
                        <input type="hidden" name="usuario" value="<%= u.getUsuario() %>">
                        <button type="submit">Excluir</button>
                    </form>
                </td>
            </tr>
            <% } %>
        </tbody>
    </table>
</body>
</html>
