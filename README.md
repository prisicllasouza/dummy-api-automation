## Dummy-api-automation

## Tecnologias Utilizadas

- Java
- REST Assured
- Gradle

## Desgin Patter
Padrão Arrange-Act-Assert (AAA)

## Instalação

### Instruções de uso:

1. Clonar o projeto - git clone https://github.com/prisicllasouza/dummy-api-automation.git
2. Para executar os testes algumas ferramentas são necessárias:
- Intellij ou outra IDE de sua preferência
- Language: Java 
- Build system: Gradle
- JDK: Oracle OpenJDK version 15.0.2
- Gradle DSL: Kotlin
3. Executar testes da classe:
- RegistrationValidator
- OBS: Caso execute mais de uma vez, será necessário incluir um e-mail novo no "testCreateUser"

### Projeto: Validador de cadastros de usuários e postagens

Referências: 
- https://dummyapi.io/docs
- https://dummyapi.io/data/v1

Definições: Esse plano de teste foi criado para validar a API de validador de cadastros de usuários e postagens.

## Escopo: 

| Método | Endpoint | Descrição
| ------- | --------- | --------- 
| POST| https://dummyapi.io/data/v1/user/create | Criação de usuário
| GET | https://dummyapi.io/data/v1/user/:id | Buscar usuário por id
| DELETE| https://dummyapi.io/data/v1/user/:id | Deletar usuário por id
| POST| https://dummyapi.io/data/v1/post/create | Criação de postagem
| GET | https://dummyapi.io/data/v1/post/:id | Buscar postagem por id
| DELETE| https://dummyapi.io/data/v1/post/:id| Deletar postagem por id


## Cenários de teste

```gherkin
1. Scenario: Create User
Given I make a request to the API "https://dummyapi.io/data/v1/"
When I make a POST request to the "user/create" endpoint with valid data
Then the response status code should be 200
And the response body should return valid data

2. Scenario: Get User
Given that the user has been registered
When I make a GET request to the API "https://dummyapi.io/data/v1/user/:id" sending the user's ID
Then the response status code should be 200
And the response body should return the data of the registered user

3. Scenario: Create Post
Given I have an active user registration
When I send the user ID along with other valid data
And I make a POST request to the API "https://dummyapi.io/data/v1/post/create"
Then the response status code should be 200
And the response body should return valid data

4. Scenario: Get Post
Given that the post has been created
When I make a GET request to the API "https://dummyapi.io/data/v1/post/:id" sending the post's ID
Then the response status code should be 200
And the response body should return the post's data

5. Scenario: Delete Post
Given I have a post created by the user
When I make a DELETE request to the API "https://dummyapi.io/data/v1/post/:id" with the post ID
Then the response status code should be 200
And the response body should return the ID that was deleted

6. Scenario: Get Deleted Post ID
Given that the post has been deleted
When I make a GET request to the API "https://dummyapi.io/data/v1/post/:id" with the post ID
Then the response status code should be 404
And the response body should return the message "RESOURCE_NOT_FOUND"

7.Scenario: Delete User
Given I have an active user registration
When I make a DELETE request to the API "https://dummyapi.io/data/v1/user/:id" with the user ID
Then the response status code should be 200
And the response body should return the ID that was deleted

8. Scenario: Get Deleted UserID
Given that the user has been deleted
When I make a GET request to the API "https://dummyapi.io/data/v1/user/:id" with the user ID
Then the response status code should be 404
And the response body should return the message "RESOURCE_NOT_FOUND"

