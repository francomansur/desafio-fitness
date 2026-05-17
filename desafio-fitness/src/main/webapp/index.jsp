<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="br.com.desafiofitness.model.Usuario" %>
<%@ page import="br.com.desafiofitness.model.Desafio" %>
<%@ page import="br.com.desafiofitness.model.Exercicio" %>
<%@ page import="java.util.Collections" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%
	if (request.getAttribute("usuarios") == null || request.getAttribute("desafios") == null) {
		response.sendRedirect(request.getContextPath() + "/progresso");
		return;
	}

	List<Usuario> usuarios = (List<Usuario>) request.getAttribute("usuarios");
	if (usuarios == null) {
		usuarios = Collections.emptyList();
	}

	List<Desafio> desafios = (List<Desafio>) request.getAttribute("desafios");
	if (desafios == null) {
		desafios = Collections.emptyList();
	}

	Map<String, Boolean> exerciciosConcluidos = (Map<String, Boolean>) request.getAttribute("exerciciosConcluidos");
	if (exerciciosConcluidos == null) {
		exerciciosConcluidos = Collections.emptyMap();
	}

	Map<Integer, Integer> progressoPorDesafio = (Map<Integer, Integer>) request.getAttribute("progressoPorDesafio");
	if (progressoPorDesafio == null) {
		progressoPorDesafio = Collections.emptyMap();
	}

	Integer usuarioSelecionado = (Integer) request.getAttribute("usuarioSelecionado");
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>Progresso dos Desafios</title>
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">
<div class="container py-4">

	<h1 class="mb-4">Progresso dos Desafios</h1>

	<div class="d-flex flex-wrap gap-2 mb-4">
		<a href="<%= request.getContextPath() %>/usuarios" class="btn btn-outline-primary">Usuários</a>
		<a href="<%= request.getContextPath() %>/exercicios" class="btn btn-outline-primary">Exercícios</a>
		<a href="<%= request.getContextPath() %>/desafios" class="btn btn-outline-primary">Desafios</a>
	</div>

	<div class="card mb-4">
		<div class="card-header">Selecionar Usuário</div>
		<div class="card-body">
			<form action="<%= request.getContextPath() %>/progresso" method="get" class="row g-3 align-items-end">
				<div class="col-12">
					<label for="usuario" class="form-label">Usuário</label>
					<select id="usuario" name="usuario" class="form-select" onchange="this.form.submit()" <%= usuarios.isEmpty() ? "disabled" : "" %>>
						<% for (Usuario u : usuarios) { %>
						<option value="<%= u.getUsuario() %>" <%= (usuarioSelecionado != null && usuarioSelecionado.equals(u.getUsuario())) ? "selected" : "" %>>
							<%= u.getNome() %>
						</option>
						<% } %>
					</select>
				</div>
			</form>
		</div>
	</div>

	<% if (usuarios.isEmpty()) { %>
	<div class="alert alert-info">Cadastre pelo menos um usuário para acompanhar progresso.</div>
	<% } else if (desafios.isEmpty()) { %>
	<div class="alert alert-info">Cadastre desafios para iniciar o acompanhamento.</div>
	<% } else { %>
		<% for (Desafio d : desafios) {
			int totalExercicios = d.getExercicios().size();
			int concluidos = progressoPorDesafio.getOrDefault(d.getDesafio(), 0);
			int percentual = totalExercicios == 0 ? 0 : (concluidos * 100) / totalExercicios;
		%>
		<div class="card mb-3">
			<div class="card-body">
				<div class="d-flex justify-content-between align-items-center mb-2">
					<h5 class="card-title mb-0"><%= d.getNome() %></h5>
					<span class="text-muted"><%= concluidos %>/<%= totalExercicios %> concluídos (<%= percentual %>%)</span>
				</div>

				<p class="card-text text-muted mb-3"><%= d.getDescricao() %></p>

				<progress class="w-100 mb-3" value="<%= percentual %>" max="100"><%= percentual %>%</progress>

				<% if (d.getExercicios().isEmpty()) { %>
				<p class="text-muted mb-0">Este desafio ainda não possui exercícios.</p>
				<% } else { %>
				<ul class="list-group">
					<% for (Exercicio e : d.getExercicios()) {
						String chave = d.getDesafio() + "-" + e.getExercicio();
						boolean marcado = exerciciosConcluidos.getOrDefault(chave, false);
					%>
					<li class="list-group-item">
						<form action="<%= request.getContextPath() %>/progresso" method="post" class="d-flex align-items-center gap-2">
							<input type="hidden" name="acao" value="atualizarProgresso">
							<input type="hidden" name="usuario" value="<%= usuarioSelecionado %>">
							<input type="hidden" name="desafio" value="<%= d.getDesafio() %>">
							<input type="hidden" name="exercicio" value="<%= e.getExercicio() %>">
							<input class="form-check-input mt-0" type="checkbox" name="concluido" value="true" <%= marcado ? "checked" : "" %> onchange="this.form.submit()">
							<div class="flex-grow-1 <%= marcado ? "text-decoration-line-through text-muted" : "" %>">
								<strong><%= e.getNome() %></strong>
								<div class="small"><%= e.getDescricao() %></div>
							</div>
						</form>
					</li>
					<% } %>
				</ul>
				<% } %>
			</div>
		</div>
		<% } %>
	<% } %>

</div>
</body>
</html>
