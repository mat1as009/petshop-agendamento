# Petshop - Sistema de Agendamento

Sistema de agendamento de serviços para petshop, feito em Java como projeto da disciplina de Padrões de Projeto. O mesmo núcleo atende duas portas de entrada: um menu de **terminal** e uma **API REST** com páginas web simples.

## Funcionalidades

- Cadastro, edição, consulta e exclusão de **clientes** (com endereço) e de **pets**
- **Agendamento** de serviços: Banho, Tosa, Vacina e Consulta
- **Adicionais** que mudam o preço do serviço: Hidratação, Perfume e Leva e traz
- Horários de 08:00 às 17:00, de hora em hora, com **um agendamento por horário**
- Cancelar e concluir agendamentos
- **Histórico de vacinas**: concluir um agendamento de vacina registra a aplicação e renova a próxima vacina do pet em um ano
- Ficha do pet e alerta de **pets com vacina vencida**

## Como rodar

Requisitos: JDK 21 ou superior e o VS Code com a extensão "Extension Pack for Java".

- **Terminal:** execute a classe `App` (`src/main/java/com/petshop/App.java`).
- **API e páginas web:** execute a classe `PetshopApiApplication` (`src/main/java/com/petshop/api/PetshopApiApplication.java`) e abra `http://localhost:8080/`.
- **Login:** a API e as páginas pedem usuário e senha. O padrão de demonstração fica em `SegurancaConfig.java`; para trocar, defina as variáveis de ambiente. 
- **Testes:** pelo painel de testes do VS Code (ícone do frasco), ou `mvn test` se o Maven estiver instalado.

O banco SQLite (`petshop.db`) é criado automaticamente na primeira execução.

## Telas

| Página | Função |
|---|---|
| `index.html` | Início |
| `clientes.html` e `cliente-form.html` | Lista, cadastro, edição e exclusão de clientes |
| `pets.html` e `pet-form.html` | Lista (com filtro de vacina vencida), cadastro, edição e exclusão de pets |
| `agendamentos.html` | Lista de agendamentos, com concluir e cancelar |
| `agendar.html` | Novo agendamento, com orçamento em tempo real e horários livres |

## Entidades e relacionamentos

| Entidade | Atributos |
|---|---|
| Cliente (10) | id, nome, cpf, dataNascimento, telefone, telefoneSecundario, email, endereco, observacoes, dataCadastro |
| Pet (12) | id, nome, especie, raca, peso, sexo, dataNascimento, cor, castrado, observacoes, proximaVacina, clienteId |
| Agendamento (10) | id, petId, servico, descricao, data, hora, valor, status, observacoes, dataCriacao |
| Endereco | cep, logradouro, numero, complemento, bairro, cidade, estado |
| Vacina | id, petId, dataAplicacao |

- **Cliente 1:1 Endereço** (a tabela `enderecos` usa o id do cliente como chave primária)
- **Cliente 1:n Pets**
- **Pet 1:n Agendamentos**
- **Pet 1:n Vacinas**

## Padrões de projeto e princípios

- **Decorator** (sem framework): `ServicoPetshop` (interface), `ServicoBase`, `AdicionalServico` (decorator abstrato) e os adicionais `Hidratacao`, `Perfume` e `LevaETraz`
- **Command:** cada ação do menu é um comando (`Comando`)
- **Factory Method:** cada opção do menu tem uma fábrica (`ComandoFactory`) que cria o seu comando
- **Builder:** `ClienteBuilder` e `PetBuilder` montam os objetos passo a passo, com validação
- **MVC:** `MenuView`, `MenuController` e as classes do modelo, no terminal
- **Repository e DIP:** os comandos e os services dependem das interfaces do pacote `repository`; as implementações com JDBC ficam em `dao`
- **SOLID:** a regra dos horários (`Agenda`) e a regra da vacina (`Vacinacao`) ficam em classes próprias, usadas pelo terminal e pela API; novos adicionais entram sem alterar o código existente (aberto/fechado)
- **Objetos imutáveis:** os campos do modelo são `final` e as validações acontecem na criação
- **Calistenia de objetos:** aplicada em parte (classes e métodos curtos, quase sem `else`, objetos imutáveis, classes pequenas); os getters ainda existem por causa da persistência e do JSON

## Testes

Testes unitários com JUnit, sem banco e sem servidor: os repositórios de verdade são trocados por versões falsas em memória (DIP).

- Decorator: preço e descrição dos serviços com adicionais
- `Agenda`: regra do horário único, horário cancelado liberado e horários que já passaram
- `Vacinacao`: histórico e renovação da próxima vacina
- Validações de `Cliente`, `Endereco`, `Pet` e `Agendamento`
- `AgendamentoService`: agendar, conflito de horário, cancelar, concluir vacina e códigos de erro

## Decisões de projeto

- **Spring Boot só na camada da API.** O núcleo (modelo, Decorator, regras e testes) é Java puro, o que atende à exigência de Decorator sem framework e permite testar sem subir o Spring.
- **Sem JPA.** O JPA exige classes mutáveis e anotadas. Preferimos manter o modelo imutável e usar repositórios com JDBC.
- **SQLite.** Banco em arquivo único, sem instalação. Trocar de banco é possível porque tudo depende das interfaces `Repository`.
- **Valor e descrição gravados no agendamento.** O histórico não muda se os preços mudarem depois.
- **Horário único em duas camadas.** A regra está na classe `Agenda` e há um índice único parcial no banco (`status = 'AGENDADO'`), que impede dois agendamentos no mesmo horário mesmo por caminhos diferentes.
- **Uma só regra de vacina.** Terminal e API usam a mesma classe `Vacinacao`.
- **Cliente e endereço gravados em transação.** Ou grava os dois, ou nenhum.

## Segurança

- **Autenticação:** Spring Security com HTTP Basic. Todas as rotas exigem login.
 **Senha com hash:** a senha é guardada com BCrypt.
- **Sem sessão no servidor:** a API não guarda estado, e cada pedido leva a credencial.
- **Cabeçalhos de segurança:** acrescentados pelo Spring Security nas respostas (por exemplo, `X-Content-Type-Options` e `X-Frame-Options`).
- **Injeção de SQL:** `PreparedStatement` nas consultas com dados informados pelo usuário.
- **XSS:** as páginas inserem os dados com `textContent`, e não com `innerHTML`.
- **Validação de entrada:** CPF, CEP, email, datas e peso são validados na criação dos objetos, e dados inválidos devolvem 400.
- **Arquivos sensíveis fora do repositório:** o banco de dados e os arquivos gerados estão no `.gitignore`.

Limitações conhecidas: o CSRF está desativado de propósito, porque a API é usada por clientes HTTP que enviam a credencial em cada pedido; em produção o ideal seria usar token (JWT) e HTTPS. A senha padrão existe só para demonstração.

## Estrutura

- `model`: entidades e enums
- `builder`: builders de Cliente e Pet
- `servico`: Decorator
- `agenda`: regras de horário e de vacina
- `repository`: interfaces de acesso a dados
- `dao`: implementações com JDBC e a conexão com o SQLite
- `command` e `factory`: Command e Factory Method
- `view` e `controller`: menu de terminal
- `api`: controllers, services e objetos de entrada e saída da API REST
- `src/main/resources/static`: páginas web
- `api-testes`: exemplos de pedidos para a API

## Diagramas

Os diagramas de classes e de sequência ficam na pasta `docs`.

A API segue o padrão REST: recursos no plural (`/api/clientes`, `/api/pets`, `/api/agendamentos`), verbos HTTP (GET, POST, PUT, PATCH e DELETE) e códigos de resposta (200, 201, 204, 400, 404 e 409). Exemplos de pedidos estão na pasta `api-testes`.