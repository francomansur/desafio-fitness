<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="br.com.desafiofitness.model.Exercicio" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Exercícios</title>
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

    <h1 class="mb-4">Exercícios</h1>
    <a href="<%= request.getContextPath() %>/progresso" class="btn btn-outline-secondary mb-4">Voltar</a>

    <div class="card mb-4">
        <div class="card-header">
            <span id="cardTitle">Cadastrar</span>
        </div>
        <div class="card-body">
            <form action="<%= request.getContextPath() %>/exercicios" method="post" class="row g-3" id="formulario">
                <input type="hidden" name="acao" value="inserir" id="acao">
                <input type="hidden" name="codigoExercicio" id="codigoExercicio">
                <div class="col-12">
                    <input type="text" name="nome" class="form-control" placeholder="Nome" required id="nome">
                </div>
                <div class="col-12">
                    <textarea name="descricao" class="form-control" placeholder="Descrição" rows="4" required id="descricao"></textarea>
                </div>
                <div class="col-12">
                    <button type="submit" class="btn btn-primary">Salvar</button>
                </div>
            </form>
        </div>
    </div>

    <table class="table table-bordered table-striped bg-white table-fixed">
        <colgroup>
            <col style="width: 6%;">
            <col style="width: 18%;">
            <col style="width: 60%;">
            <col style="width: 16%;">
        </colgroup>
        <thead class="table-dark">
            <tr>
                <th>#</th>
                <th>Nome</th>
                <th>Descrição</th>
                <th>Ações</th>
            </tr>
        </thead>
        <tbody>
            <%
                List<Exercicio> exercicios = (List<Exercicio>) request.getAttribute("exercicios");
                for (Exercicio e : exercicios) {
            %>
            <tr data-id="<%= e.getExercicio() %>" data-nome="<%= e.getNome() %>" data-descricao="<%= e.getDescricao() %>">
                <td><%= e.getExercicio() %></td>
                <td><%= e.getNome() %></td>
                <td class="descricao-cell"><%= e.getDescricao() %></td>
                <td>
                    <button type="button" class="btn btn-warning btn-sm" onclick="editarExercicio(this)">Editar</button>
                    <form action="<%= request.getContextPath() %>/exercicios" method="post" class="d-inline">
                        <input type="hidden" name="acao" value="excluir">
                        <input type="hidden" name="codigoExercicio" value="<%= e.getExercicio() %>">
                        <button type="submit" class="btn btn-danger btn-sm">Excluir</button>
                    </form>
                </td>
            </tr>
            <% } %>
        </tbody>
    </table>

</div>
<script>
    function editarExercicio(botao) {
        const tr = botao.closest('tr');
        document.getElementById('cardTitle').innerText = 'Editar';
        document.getElementById('acao').value = 'atualizar';
        document.getElementById('codigoExercicio').value = tr.dataset.id;
        document.getElementById('nome').value = tr.dataset.nome;
        document.getElementById('descricao').value = tr.dataset.descricao;
        document.getElementById('formulario').scrollIntoView({ behavior: 'smooth' });
    }
</script>
</body>
</html>
