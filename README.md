# Petshop - Sistema de Agendamento

Sistema de agendamento de serviços para petshop feito em Java, como trabalho da disciplina de Padrões de Projeto. Versão em terminal.

## Funcionalidades

- Cadastro, edição, listagem e exclusão de clientes (com endereço) e pets
- Agendamento de serviços (Banho, Tosa, Vacina e Consulta) com data e horário
- Regra de negócio: um agendamento por horário, sem conflitos
- Cancelar e concluir agendamentos
- Histórico de vacinas, ficha completa do pet e alerta de vacina vencida

## Padrões e princípios

- Command: cada ação do menu é um comando
- Factory Method: cada opção do menu tem uma fábrica que cria o seu comando
- Builder: criação de Cliente e Pet passo a passo, com validações
- MVC: separação entre model, view e controller
- Repository: interfaces de persistência, com implementações em JDBC (DIP)

## Relacionamentos

- Cliente 1:1 Endereço
- Cliente 1:n Pets
- Pet 1:n Agendamentos
- Pet 1:n Vacinas

## Tecnologias

Java, Maven e SQLite (via JDBC).

## Como rodar

1. Instale o JDK 21 ou superior.
2. Abra a pasta no VS Code com a extensão "Extension Pack for Java".
3. Rode a classe App (src/main/java/com/petshop/App.java).

O banco petshop.db é criado automaticamente na primeira execução.