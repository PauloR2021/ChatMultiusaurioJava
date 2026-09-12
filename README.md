# 💬 Chat Multiusuário em Java

Projeto desenvolvido em **Java** para estudo e aplicação prática de conceitos de **Redes de Computadores, Sockets, Threads e arquitetura Cliente-Servidor**.

A aplicação implementa um **chat multiusuário**, permitindo que vários clientes se conectem simultaneamente a um servidor e realizem a troca de mensagens.

O projeto também possui uma interface gráfica desenvolvida com **Java Swing**, separando a parte visual da lógica responsável pela comunicação com o servidor.

---

## 🎯 Objetivo

O principal objetivo do projeto é colocar em prática conceitos relacionados à comunicação entre aplicações através de uma rede.

Durante o desenvolvimento foram trabalhados conceitos como:

- Arquitetura Cliente-Servidor
- Comunicação utilizando TCP/IP
- Sockets em Java
- Programação concorrente com Threads
- Múltiplos clientes conectados simultaneamente
- Entrada e saída de dados através de Streams
- Interface gráfica com Java Swing
- Gerenciamento de conexão e desconexão
- Distribuição de mensagens entre usuários
- Separação de responsabilidades entre classes

---

## 🛠️ Tecnologias utilizadas

- Java
- Java Swing
- Java Socket API
- TCP/IP
- Threads
- IntelliJ IDEA

---

## 🏗️ Arquitetura

O projeto foi separado em diferentes classes para manter cada responsabilidade isolada.

```text
Servidor
   │
   ├── Thread → Cliente 1
   │
   ├── Thread → Cliente 2
   │
   ├── Thread → Cliente 3
   │
   └── Thread → Cliente N
```

Cada cliente conectado ao servidor possui uma **Thread própria**, permitindo que vários usuários permaneçam conectados e troquem mensagens simultaneamente.

---

## 📂 Estrutura do projeto

```text
src/
└── pr/
    └── software/
        └── chat/
            ├── Servidor.java
            ├── ClienteHandler.java
            ├── Cliente.java
            └── TelaCliente.java
```

### `Servidor.java`

Responsável por:

- Iniciar o `ServerSocket`
- Aguardar novas conexões
- Manter os clientes conectados
- Criar uma Thread para cada cliente
- Distribuir mensagens entre os usuários

### `ClienteHandler.java`

Representa um cliente conectado ao servidor.

Cada instância é executada em uma Thread independente e fica responsável por:

- Receber mensagens do cliente
- Identificar o usuário
- Enviar mensagens para os demais clientes
- Detectar a saída do usuário
- Encerrar a conexão

### `Cliente.java`

Responsável pela comunicação entre a interface gráfica e o servidor.

Utiliza:

```java
Socket
DataInputStream
DataOutputStream
```

Possui operações para conectar, enviar mensagens, receber mensagens e desconectar.

### `TelaCliente.java`

Interface gráfica desenvolvida utilizando **Java Swing**.

A tela permite:

- Informar o nome do usuário
- Conectar ao servidor
- Digitar mensagens
- Enviar mensagens
- Visualizar o histórico do chat
- Receber mensagens de outros usuários
- Sair do chat

---

## 🔄 Funcionamento

Quando o servidor é iniciado, ele fica aguardando conexões:

```java
ServerSocket servidor = new ServerSocket(50000);

while (true) {

    Socket socketCliente = servidor.accept();

    ClienteHandler cliente =
            new ClienteHandler(socketCliente);

    Thread thread =
            new Thread(cliente);

    thread.start();
}
```

Cada nova conexão cria uma nova Thread.

Por exemplo:

```text
                    SERVIDOR
                       │
           ┌───────────┼───────────┐
           │           │           │
        Thread 1    Thread 2    Thread 3
           │           │           │
        Paulo         Ana         João
```

---

## 💬 Distribuição das mensagens

Quando um usuário envia uma mensagem:

```text
Paulo: Olá pessoal!
```

o cliente envia os dados para o servidor:

```text
Paulo
  │
  │ "Olá pessoal!"
  ▼
Servidor
```

O servidor recebe a mensagem e distribui para os demais usuários conectados:

```text
                   ┌──────> Ana
Paulo ──> Servidor ┤
                   └──────> João
```

Assim, os clientes recebem:

```text
Paulo: Olá pessoal!
```

---

## 🧵 Utilização de Threads

As Threads são fundamentais para o funcionamento do projeto.

Sem elas, o servidor ficaria ocupado atendendo apenas um cliente por vez.

Com uma Thread para cada conexão:

```text
Thread Servidor
    │
    ├── ClienteHandler #1
    ├── ClienteHandler #2
    ├── ClienteHandler #3
    └── ...
```

os clientes podem permanecer conectados simultaneamente.

No lado do cliente também é utilizada uma Thread para receber mensagens sem bloquear a interface gráfica.

Isso permite que o usuário continue utilizando a aplicação enquanto novas mensagens chegam do servidor.

---

## 🖥️ Interface gráfica

A interface foi desenvolvida utilizando **Java Swing**.

Principais componentes utilizados:

```text
JFrame
JPanel
JTextArea
JTextField
JButton
JLabel
JScrollPane
```

O `JTextArea` mantém o histórico das mensagens recebidas e enviadas.

O histórico é atualizado utilizando:

```java
areaMensagem.append(mensagem + "\n");
```

Também é utilizado:

```java
SwingUtilities.invokeLater(...)
```

para realizar atualizações da interface gráfica de forma adequada quando as mensagens são recebidas pela Thread responsável pela comunicação.

---

## 🚪 Desconexão

O usuário pode sair do chat através do botão **Sair do Chat**.

Ao sair:

```text
Cliente
   │
   │ "sair"
   ▼
Servidor
   │
   ├── remove o cliente
   ├── encerra a conexão
   └── informa os demais usuários
```

Os outros clientes podem receber uma mensagem como:

```text
Paulo saiu do chat.
```

---

## ▶️ Como executar

### 1. Clone o projeto

```bash
git clone https://github.com/PauloR2021/ChatMultiusaurioJava.git
```

Entre na pasta:

```bash
cd ChatMultiusaurioJava
```

### 2. Execute o servidor

Execute primeiro:

```text
Servidor.java
```

O servidor ficará aguardando conexões na porta:

```text
50000
```

### 3. Execute os clientes

Execute:

```text
TelaCliente.java
```

Você pode executar várias instâncias da classe para simular diferentes usuários.

Exemplo:

```text
Servidor.java

TelaCliente.java → Paulo
TelaCliente.java → Ana
TelaCliente.java → João
```

---

## 🌐 Configuração de rede

Para executar cliente e servidor no mesmo computador:

```java
new Socket("127.0.0.1", 50000);
```

O endereço:

```text
127.0.0.1
```

representa a própria máquina (`localhost`).

Para testes em uma rede local, o cliente pode utilizar o endereço IP local da máquina onde o servidor está sendo executado.

---

## 📚 Conceitos praticados

Este projeto permitiu estudar na prática:

- Redes de computadores
- TCP/IP
- Sockets
- Cliente-Servidor
- Threads
- Concorrência
- Streams
- Programação Orientada a Objetos
- Java Swing
- Eventos
- Tratamento de exceções
- Organização e separação de responsabilidades

---

## 🚀 Possíveis melhorias

O projeto pode continuar evoluindo com funcionalidades como:

- Lista de usuários online
- Mensagens privadas
- Salas de conversa
- Data e horário das mensagens
- Persistência do histórico em banco de dados
- Tela de login
- Autenticação de usuários
- Emojis
- Envio de arquivos
- Melhorias na interface gráfica
- Reconexão automática
- Criptografia da comunicação

---

## 👨‍💻 Autor

**Paulo Ricardo Soares da Trindade**

Projeto desenvolvido para estudo de **Java, Redes de Computadores, Sockets e programação concorrente com Threads**.