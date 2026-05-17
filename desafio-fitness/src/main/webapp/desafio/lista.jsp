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
    <a href="<%= request.getContextPath() %>/progresso" class="btn btn-outline-secondary mb-4">Voltar</a>

    <div class="card mb-4">
        <div class="card-header">
            <span id="cardTitle">Cadastrar</span>
        </div>
        <div class="card-body">
            <form action="<%= request.getContextPath() %>/desafios" method="post" class="row g-3" id="formulario">
                <input type="hidden" name="acao" value="inserir" id="acao">
                <input type="hidden" name="codigoDesafio" id="codigoDesafio">
                <div class="col-12">
                    <input type="text" name="nome" class="form-control" placeholder="Nome" required id="nome">
                </div>
                <div class="col-12">
                    <textarea name="descricao" class="form-control" placeholder="Descrição" rows="4" required id="descricao"></textarea>
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
                               value="<%= ex.getExercicio() %>" id="ex_<%= ex.getExercicio() %>">
                        <label class="form-check-label" for="ex_<%= ex.getExercicio() %>">
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
                    <button type="submit" class="btn btn-primary" id="botaoSalvar">Salvar</button>
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
                    String exerciciosJson = "";
                    for (Exercicio ex : d.getExercicios()) {
                        if (!exerciciosJson.isEmpty()) exerciciosJson += ",";
                        exerciciosJson += ex.getExercicio();
                    }
            %>
            <tr data-id="<%= d.getDesafio() %>" data-nome="<%= d.getNome() %>" data-descricao="<%= d.getDescricao() %>" data-exercicios="<%= exerciciosJson %>">
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
                    <button type="button" class="btn btn-warning btn-sm" onclick="editarDesafio(this)">Editar</button>
                    <form action="<%= request.getContextPath() %>/desafios" method="post" class="d-inline">
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
<script>
    function editarDesafio(botao) {
        const tr = botao.closest('tr');
        const id = tr.dataset.id;
        const nome = tr.dataset.nome;
        const descricao = tr.dataset.descricao;
        const exerciciosIds = tr.dataset.exercicios ? tr.dataset.exercicios.split(',') : [];
        
        document.getElementById('cardTitle').innerText = 'Editar';
        document.getElementById('acao').value = 'atualizar';
        document.getElementById('codigoDesafio').value = id;
        document.getElementById('nome').value = nome;
        document.getElementById('descricao').value = descricao;
        
        document.querySelectorAll('input[name="exercicios"]').forEach(cb => cb.checked = false);
        exerciciosIds.forEach(exId => {
            const checkbox = document.getElementById('ex_' + exId);
            if (checkbox) checkbox.checked = true;
        });
        
        document.getElementById('formulario').scrollIntoView({ behavior: 'smooth' });
    }
</script>
</body>
</html>
