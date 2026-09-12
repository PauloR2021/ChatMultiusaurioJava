package pr.software.chat;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Cliente {

    private Socket socket;
    private DataInputStream entrada;
    private DataOutputStream saida;

    public void conectar(String ip, int porta) throws IOException {

        socket = new Socket(ip, porta);

        entrada = new DataInputStream(
                socket.getInputStream()
        );

        saida = new DataOutputStream(
                socket.getOutputStream()
        );
    }

    public void enviarMensagem(String mensagem) throws IOException {

        saida.writeUTF(mensagem);
        saida.flush();
    }

    public String receberMensagem() throws IOException {

        return entrada.readUTF();
    }

    public boolean estaConectado() {

        return socket != null
                && socket.isConnected()
                && !socket.isClosed();
    }

    public void desconectar() {

        try {

            if (entrada != null) {
                entrada.close();
            }

            if (saida != null) {
                saida.close();
            }

            if (socket != null) {
                socket.close();
            }

        } catch (IOException e) {

            System.out.println(
                    "Erro ao desconectar: "
                            + e.getMessage()
            );
        }
    }
}