package pr.software.chat;
import pr.software.chat.Servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClienteHandler implements Runnable {

    private Socket socket;

    private DataInputStream entrada;
    private DataOutputStream saida;

    private String nome;

    public ClienteHandler(Socket socket) {

        this.socket = socket;

        try {

            entrada =
                    new DataInputStream(
                            socket.getInputStream()
                    );

            saida =
                    new DataOutputStream(
                            socket.getOutputStream()
                    );

        } catch (IOException e) {

            System.out.println(
                    "Erro ao criar cliente."
            );
        }
    }

    @Override
    public void run() {

        try {

            // Primeira mensagem recebida será o nome
            nome = entrada.readUTF();

            System.out.println(
                    nome + " entrou no chat."
            );

            Servidor.enviarParatodos(
                    nome + " entrou no chat.",
                    this
            );

            while (true) {

                String mensagem =
                        entrada.readUTF();

                if (mensagem.equalsIgnoreCase("sair")) {

                    break;
                }

                System.out.println(
                        nome + ": " + mensagem
                );

                Servidor.enviarParatodos(
                        nome + ": " + mensagem,
                        this
                );
            }

        } catch (IOException e) {

            System.out.println(
                    nome + " perdeu a conexão."
            );

        } finally {

            desconectar();
        }
    }

    public void enviarMensagem(String mensagem) {

        try {

            saida.writeUTF(mensagem);

            saida.flush();

        } catch (IOException e) {

            System.out.println(
                    "Erro ao enviar mensagem."
            );
        }
    }

    private void desconectar() {

        try {

            Servidor.clientes.remove(this);

            Servidor.enviarParatodos(
                    nome + " saiu do chat.",
                    this
            );

            System.out.println(
                    nome + " saiu do chat."
            );

            entrada.close();
            saida.close();
            socket.close();

        } catch (IOException e) {

            System.out.println(
                    "Erro ao desconectar cliente."
            );
        }
    }
}