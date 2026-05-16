<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="br.com.desafiofitness.model.Desafio" %>
<%@ page import="br.com.desafiofitness.model.Exercicio" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Desafios</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
    <style>
        .table-fixed {
            table-layout: fixed;
            width: 100%;
        }

        .descricao-cell {
            white-space: normal;
            overflow-wrap: anywhere;
            word-break: break-word;
        }
    </style>
</head>
<body class="bg-light">
<div class="container py-4">

    <h1 class="mb-4">Desafios</h1>

    <div class="card mb-4">
        <div class="card-header">Cadastrar</div>
        <div class="card-body">
            <form action="<%= request.getContextPath() %>/desafios" method="post" class="row g-3">
                <input type="hidden" name="acao" value="inserir">
                <div class="col-12">
                    <input type="text" name="nome" class="form-control" placeholder="Nome" required>
                </div>
                <div class="col-12">
                    <textarea name="descricao" class="form-control" placeholder="Descrição" rows="4" required></textarea>
                </div>
                <div class="col-12">
                    <label class="form-label fw-semibold">Exercícios</label>
                    <%
                        List<Exercicio> todosExercicios = (List<Exercicio>) request.getAttribute("todosExercicios");
                        if (todosExercicios != null && !todosExercicios.isEmpty()) {
                            for (Exercicio ex : todosExercicios) {
                    %>
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" name="exercicios"
                               value="<%= ex.getExercicio() %>" id="novo_ex_<%= ex.getExercicio() %>">
                        <label class="form-check-label" for="novo_ex_<%= ex.getExercicio() %>">
                            <%= ex.getNome() %>
                        </label>
                    </div>
                    <%
                            }
                        } else {
                    %>
                    <p class="text-muted small mb-0">Nenhum exercício cadastrado.</p>
                    <% } %>
                </div>
                <div class="col-12">
                    <button type="submit" class="btn btn-primary">Salvar</button>
                </div>
            </form>
        </div>
    </div>

    <table class="table table-bordered table-striped bg-white table-fixed">
        <colgroup>
            <col style="width: 5%;">
            <col style="width: 15%;">
            <col style="width: 35%;">
            <col style="width: 30%;">
            <col style="width: 15%;">
        </colgroup>
        <thead class="table-dark">
            <tr>
                <th>#</th>
                <th>Nome</th>
                <th>Descrição</th>
                <th>Exercícios</th>
                <th>Ações</th>
            </tr>
        </thead>
        <tbody>
            <%
                List<Desafio> desafios = (List<Desafio>) request.getAttribute("desafios");
                for (Desafio d : desafios) {
            %>
            <tr>
                <td><%= d.getDesafio() %></td>
                <td><%= d.getNome() %></td>
                <td class="descricao-cell"><%= d.getDescricao() %></td>
                <td>
                    <%
                        for (Exercicio ex : d.getExercicios()) {
                    %>
                    <span><%= ex.getNome() %></span>
                    <form action="<%= request.getContextPath() %>/desafios" method="post" class="d-inline">
                        <input type="hidden" name="acao" value="removerExercicio">
                        <input type="hidden" name="codigoDesafio" value="<%= d.getDesafio() %>">
                        <input type="hidden" name="codigoExercicio" value="<%= ex.getExercicio() %>">
                        <button type="submit" class="btn btn-link btn-sm p-0 text-danger" title="Remover">✕</button>
                    </form>
                    <% } %>
                </td>
                <td>
                    <form action="<%= request.getContextPath() %>/desafios" method="post">
                        <input type="hidden" name="acao" value="excluir">
                        <input type="hidden" name="codigoDesafio" value="<%= d.getDesafio() %>">
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

</div>
</body>
</html>
