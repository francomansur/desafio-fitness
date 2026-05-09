# Bloco 1: montagem do projeto
# Usamos o Maven (programa que compila e monta projetos Java) para pegar
# o codigo-fonte e transformar em um arquivo .war (um unico arquivo compactado que
# contem todas as paginas, codigo e configuracoes da aplicacao, similar a um .zip
# que o servidor sabe abrir e executar).
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY desafio-fitness/pom.xml .
COPY desafio-fitness/src ./src
RUN mvn clean package -DskipTests

# Bloco 2: publicacao no servidor
# Aqui pegamos o .war gerado acima e colocamos dentro do Tomcat (programa que
# serve aplicacoes Java no navegador), que vai rodar sua aplicacao na porta 8080.
FROM tomcat:11.0-jdk17
RUN cp -r /usr/local/tomcat/webapps.dist/* /usr/local/tomcat/webapps/
COPY --from=build /app/target/desafio-fitness.war /usr/local/tomcat/webapps/desafio-fitness.war
EXPOSE 8080
CMD ["catalina.sh", "run"]
