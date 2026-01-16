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
envio de e-mail

🛡️ Segurança
Hash de senha com bcrypt
JWT com expiração curta
Refresh Token persistido no banco
Rate limit no login
Bloqueio por múltiplas tentativas
Validações globais
Tratamento centralizado de exceções
