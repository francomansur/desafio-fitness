## Subir tudo (Tomcat + MySQL + phpMyAdmin)

```bash
docker compose up -d --build
```

## Acessar

| Serviço        | URL                                              |
|----------------|--------------------------------------------------|
| Tomcat (home)  | http://localhost:8080/                            |
| Sua aplicação  | http://localhost:8080/desafio-fitness/   |
| phpMyAdmin     | http://localhost:8081/                            |

## Parar tudo

```bash
docker compose down
```

## Deploy / Redeploy (após alterar código)

```bash
docker compose up -d --build
```

## MySQL

Reseta/Cria Schema no Banco
```bash
docker exec -i mysql-db-fitness mysql -uteste -pteste desafio-fitness < schema.sql
```

| Campo        | Valor     |
|------------- |-----------|
| Host         | localhost |
| Porta        | 3306      |
| Banco        | desafio-fitness |
| Usuário      | teste     |
| Senha        | teste     |
| Senha root   | teste     |
