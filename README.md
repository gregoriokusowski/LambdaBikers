# LambdaBikers
Trabalho para a disciplina de Tópicos Avançados em Computação II. Tema: Cloud Computing - PaaS.

# Descrição da Atividade

## Atividade Avaliativa I - Platform as a Service

Usando sua linguagem de programação preferida, deve ser implementada uma aplicação web simples, conforme especificação abaixo. O deployment desta aplicação deve ser feito utilizando uma plataforma como serviço (PaaS). O deployment desta aplicação deverá ser apresentado em aula, juntamente com a apresentação do PaaS escolhido.

Especificação da aplicação:
Um grupo de amigos motociclistas está criando um portal para registro de seus passeios e de seus integrantes (motociclistas). Cada motociclista possui um nome, data de nascimento, tipo sanguíneo, email, telefone e uma moto (armazenar marca e modelo). Quando ocorrem os passeios, diversos motociclistas podem participar (obviamente, um motociclista pode ir em diversos passeios). Cada passeio tem uma data, destino (nome do lugar) e quilometragem. Em cada passeio são tiradas diversas fotos, as quais são armazenadas pelo sistema. A foto, além do arquivo, deve ter uma descrição.

Funcionalidades do sistema:
- CRUD motociclista (próprio motociclista se cadastra)
- Login motociclista
- Criar e consultar passeios (qualquer motociclista pode fazer)
- Adicionar fotos a um passeio (qualquer motociclista pode fazer)

# Objetivo

Implementar uma aplicação que cumpra os requisitos de funcionalidades. Cada aluno escolheu uma tecnologia e provider PaaS. Minha escolha foi *Clojure* como linguagem e a *Getup Cloud* como provider.

## Prerequisites

You will need [Leiningen][1] 2.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein run # ring server

## License

MIT ;)
