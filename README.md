🔐 *Auth Service* – Sistema de Autenticação Completo

API de autenticação e autorização desenvolvida com foco em segurança, boas práticas e arquitetura profissional, podendo ser reutilizada como serviço base em qualquer aplicação.

🚀 Visão Geral
Este projeto implementa um Auth Service completo, responsável por:
Gerenciamento de usuários, Autenticação segura com JWT, Autorização baseada em roles, Refresh Token, Reset de senha, Controle de sessões.

O objetivo é simular um cenário real de mercado, seguindo padrões utilizados em sistemas modernos.

🧱 Funcionalidades
✅ Autenticação
Cadastro de usuário
Login com JWT
Refresh Token
Logout com revogação de token

🔐 Autorização
Controle de acesso por roles (USER, ADMIN)
Proteção de rotas

🔁 Recuperação de Senha
Geração de token temporário
Token com expiração
Envio de e-mail (atualmente simulado no serviço)

🛡️ Segurança
Hash de senha com bcrypt
JWT com expiração curta
Refresh Token persistido no banco
Rate limit no login
Bloqueio por múltiplas tentativas
Validações globais
Tratamento centralizado de exceções

## Execução local com Docker Compose

O projeto utiliza Java 21, Spring Boot, PostgreSQL e Docker.

Na raiz do repositório, execute:

```bash
docker compose -f backend/docker-compose.yml up --build
```

A API ficará disponível em `http://localhost:8080`.

## Deploy no Render com Neon

Crie um Web Service conectado a este repositório com:
- Runtime: Docker
- Root Directory: vazio (raiz do repositório)
- Dockerfile Path: `./Dockerfile`
- Branch: `main` após integrar as alterações desta configuração

Configure estas variáveis de ambiente no Render:
- `SPRING_DATASOURCE_URL`: JDBC URL do Neon, no formato `jdbc:postgresql://HOST/DB?sslmode=require`
- `SPRING_DATASOURCE_USERNAME`: usuário do Neon
- `SPRING_DATASOURCE_PASSWORD`: senha do Neon
- `JWT_SECRET`: segredo aleatório forte, com pelo menos 32 bytes

Não versione credenciais ou arquivos `.env`. O schema inicial é criado/atualizado pelo Hibernate (`ddl-auto=update`).


## Swagger UI e testes da API

Com a aplicação em execução, acesse:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`
- Health check: `http://localhost:8080/health`

No Render, substitua o host local pelo domínio público do serviço.

### Fluxo de teste sugerido

1. Execute `POST /auth/register` para cadastrar um usuário.
2. Execute `POST /auth/login` com o mesmo e-mail e senha.
3. Copie o valor de `accessToken` retornado pelo login.
4. Clique em **Authorize** no Swagger e informe o access token (sem o prefixo `Bearer `, pois o Swagger adiciona esse prefixo automaticamente).
5. Execute `GET /api/me` para validar a autenticação JWT. Sem token válido, a rota responde `401 Unauthorized`.

As rotas de cadastro, login, refresh, logout e recuperação de senha são públicas porque fazem parte do fluxo de autenticação. As demais rotas exigem um access token JWT válido.
