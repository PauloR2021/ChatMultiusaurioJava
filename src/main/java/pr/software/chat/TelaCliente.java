package pr.software.chat;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class TelaCliente extends JFrame {
    private JTextArea areaMensagem;
    private JTextField campoNome;
    private JTextField campoMensagem;

    private JButton btnConectar;
    private JButton btnEnviar;
    private JButton btnSair;

    private Cliente cliente;

    public TelaCliente() {
        cliente = new Cliente();

        setTitle("Chat");
        setSize(500,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        criarInterface();
    }

    void criarInterface(){
        setLayout(new BorderLayout());

        /*Parte superior da Tela*/
        JPanel painelSuperior = new JPanel(new FlowLayout());

        campoNome = new JTextField(15);

        btnConectar = new JButton("Conectar");
        btnSair = new JButton("Sair");
        btnSair.setEnabled(false);

        painelSuperior.add(new JLabel("Nome:"));

        painelSuperior.add(campoNome);
        painelSuperior.add(btnConectar);
        painelSuperior.add(btnSair);

        /*Area da mensagem*/
        areaMensagem = new JTextArea();
        areaMensagem.setEditable(false);
        areaMensagem.setBorder(BorderFactory.createLineBorder(Color.black));
        JScrollPane scroll = new JScrollPane(areaMensagem);

        /*Parte inferior*/
        JPanel painelInferior = new JPanel(new BorderLayout());
        painelInferior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        campoMensagem = new JTextField();
        btnEnviar = new JButton("Enviar");
        btnEnviar.setEnabled(false);
        painelInferior.add(campoMensagem,BorderLayout.CENTER);
        painelInferior.add(btnEnviar, BorderLayout.EAST);

        /*Parte do footer */
        JPanel painelFooter = new JPanel(new BorderLayout());
        painelFooter.setBorder(BorderFactory.createEmptyBorder(2, 10, 5, 10));
        JLabel footerLabel = new JLabel("Desenvolvedor: Paulo Ricardo");
        footerLabel.setFont(new Font("Serif", Font.BOLD, 14));
        painelFooter.add(footerLabel,BorderLayout.NORTH);

        /*Painel para agrupar o Painel Inferior e o Footer*/
        JPanel painelRodape = new JPanel();
        painelRodape.setLayout(
                new BoxLayout(painelRodape,BoxLayout.Y_AXIS)
        );

        painelRodape.add(painelInferior);
        painelRodape.add(painelFooter);

        /* Adicionando na Janela */
        add(painelSuperior, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(painelRodape, BorderLayout.SOUTH);

        /*Eventos*/
        btnConectar.addActionListener( e -> conectar());
        btnEnviar.addActionListener(e->enviarMensagem());
        btnSair.addActionListener(e -> sairDoChat());
        campoMensagem.addActionListener(e -> enviarMensagem());


    }

    private void conectar(){
        String nome = campoNome.getText().trim();

        if(nome.isEmpty()){
            JOptionPane.showMessageDialog(null, "Digite seu nome.");
            return;
        }
        try{
            cliente.conectar("127.0.0.1",50000);

            /*Primeira mensagem enviada, nome do cliente */
            cliente.enviarMensagem(nome);
            areaMensagem.append("Conectado com sucesso!\n");

            btnConectar.setEnabled(false);
            btnEnviar.setEnabled(true);
            campoNome.setEditable(false);
            btnSair.setEnabled(true);

            iniciarThreadRecebimento();
        }catch (IOException e){
            JOptionPane.showMessageDialog(null, "Erro ao conectar: " +e.getMessage());
        }
    }

    private void enviarMensagem(){
        String mensagem = campoMensagem.getText().trim();

        if(mensagem.isEmpty()){return;}

        try{
            cliente.enviarMensagem(mensagem);

            /* Mostrar mensagem*/
            areaMensagem.append("Você: " + mensagem + "\n");
            campoMensagem.setText("");

        }catch(IOException e){
            areaMensagem.append("Erro ao enviar mensagem.\n");
        }
    }

    private void iniciarThreadRecebimento(){
        Thread thread = new Thread(()->{
            try{
                while(cliente.estaConectado()){
                    String mensagem = cliente.receberMensagem();

                    /*Atualização Visual deve ser feita pelo Thread do Swing*/
                    SwingUtilities.invokeLater(() -> areaMensagem.append(mensagem + "\n"));
                }

            }catch (IOException e){
                SwingUtilities.invokeLater(()->{
                    areaMensagem.append("Conexão encerrada.\n");
                });
            }

        });

        thread.start();
    }

    public void sairDoChat(){
        try{
            if(cliente != null && cliente.estaConectado()){
                //Avisa o Servidor que vai sair
                cliente.enviarMensagem("sair");

                //Fecha a conexão
                cliente.desconectar();
            }

            areaMensagem.append("Você saiu do chat.\n");

            btnEnviar.setEnabled(false);
            btnSair.setEnabled(false);
            btnConectar.setEnabled(true);

            campoNome.setEnabled(true);

        }catch (IOException e){
            JOptionPane.showMessageDialog(null, "Erro ao sair doChat: " +e.getMessage());
        }
    }
    public static void main(String[] args){
        SwingUtilities.invokeLater(() ->{
            TelaCliente tela = new TelaCliente();

            tela.setVisible(true);
        });
    }
}
