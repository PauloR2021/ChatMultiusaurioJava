package pr.software.chat;


import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Paulo Ricardo Soares da Trindade
 */

public class Servidor {


    public static List<ClienteHandler> clientes = new ArrayList<ClienteHandler>();

    public static void main(String[] args) {
        try{

            ServerSocket servidor = new ServerSocket(50000);

            System.out.println("Servidor iniciado...");
            System.out.println("Aguardando clientes...");

            while (true){
                Socket socketCliente = servidor.accept();

                System.out.println("Novo cliente conectado!");

                ClienteHandler cliente = new ClienteHandler(socketCliente);

                clientes.add(cliente);

                Thread threadCliente = new Thread(cliente);

                threadCliente.start();
            }

        } catch (Exception e) {
            System.out.println("Erro ao tentar abrir o servidor");
        }
    }

    public static void enviarParatodos(
            String mensagem,
            ClienteHandler remetente
    ){
        for (ClienteHandler cliente: clientes){
            if (cliente != remetente){
                cliente.enviarMensagem(mensagem);
            }
        }
    }
}
