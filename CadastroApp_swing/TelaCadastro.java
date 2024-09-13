package CadastroApp_swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TelaCadastro extends JFrame {

    private JTextField nomeField;
    private JTextField sobrenomeField;
    private JTextField emailField;
    private JPasswordField senhaField;
    private JTextField telefoneField;
    private JTextField enderecoField;
    private JButton cadastrarButton;
    private JButton editarButton;
    private JButton removerButton;
    private JList<Usuario> userList;
    private DefaultListModel<Usuario> userModel;
    private List<Usuario> usuarios;

    public TelaCadastro() {
        // Configurações da Janela
        setTitle("Tela de Cadastro");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Inicializa a lista de usuários e carrega os dados salvos
        usuarios = new ArrayList<>();
        loadUsuarios();

        // Criação dos Componentes
        nomeField = new JTextField(20);
        sobrenomeField = new JTextField(20);
        emailField = new JTextField(20);
        senhaField = new JPasswordField(20);
        telefoneField = new JTextField(20);
        enderecoField = new JTextField(20);
        cadastrarButton = new JButton("Cadastrar");
        editarButton = new JButton("Editar");
        removerButton = new JButton("Remover");

        userModel = new DefaultListModel<>();
        userList = new JList<>(userModel);
        JScrollPane userScrollPane = new JScrollPane(userList);

        // Layout da Janela
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        add(nomeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Sobrenome:"), gbc);
        gbc.gridx = 1;
        add(sobrenomeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("E-mail:"), gbc);
        gbc.gridx = 1;
        add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1;
        add(senhaField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Telefone:"), gbc);
        gbc.gridx = 1;
        add(telefoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        add(new JLabel("Endereço:"), gbc);
        gbc.gridx = 1;
        add(enderecoField, gbc);

        // Ajusta a posição dos botões
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        add(cadastrarButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        add(editarButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 7;
        add(removerButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        add(userScrollPane, gbc);

        // Adiciona o action listener aos botões
        cadastrarButton.addActionListener(this::cadastrarUsuario);
        editarButton.addActionListener(this::editarUsuario);
        removerButton.addActionListener(this::removerUsuario);

        refreshUserList();
    }

    private void cadastrarUsuario(ActionEvent e) {
        String nome = nomeField.getText();
        String sobrenome = sobrenomeField.getText();
        String email = emailField.getText();
        String senha = new String(senhaField.getPassword());
        String telefone = telefoneField.getText();
        String endereco = enderecoField.getText();

        if (!validarCampos(nome, sobrenome, email, senha, telefone, endereco)) {
            return;
        }

        Usuario usuario = new Usuario(nome, sobrenome, email, senha, telefone, endereco);
        usuarios.add(usuario);
        salvarDados();
        refreshUserList();
        JOptionPane.showMessageDialog(this, "Usuário " + nome + " cadastrado com sucesso!");
        limparCampos();
    }

    private void editarUsuario(ActionEvent e) {
        Usuario selectedUser = userList.getSelectedValue();
        if (selectedUser != null) {
            String nome = nomeField.getText();
            String sobrenome = sobrenomeField.getText();
            String email = emailField.getText();
            String senha = new String(senhaField.getPassword());
            String telefone = telefoneField.getText();
            String endereco = enderecoField.getText();

            if (!validarCampos(nome, sobrenome, email, senha, telefone, endereco)) {
                return;
            }

            selectedUser.setNome(nome);
            selectedUser.setSobrenome(sobrenome);
            selectedUser.setEmail(email);
            selectedUser.setSenha(senha);
            selectedUser.setTelefone(telefone);
            selectedUser.setEndereco(endereco);
            salvarDados();
            refreshUserList();
            JOptionPane.showMessageDialog(this, "Usuário " + selectedUser.getNome() + " editado com sucesso!");
            limparCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um usuário para editar.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerUsuario(ActionEvent e) {
        Usuario selectedUser = userList.getSelectedValue();
        if (selectedUser != null) {
            usuarios.remove(selectedUser);
            salvarDados();
            refreshUserList();
            JOptionPane.showMessageDialog(this, "Usuário " + selectedUser.getNome() + " removido com sucesso!");
            limparCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um usuário para remover.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validarCampos(String nome, String sobrenome, String email, String senha, String telefone, String endereco) {
        if (nome.isEmpty() || sobrenome.isEmpty() || email.isEmpty() || senha.isEmpty() || telefone.isEmpty() || endereco.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos.", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!validarNomeSobrenome(nome, sobrenome)) {
            JOptionPane.showMessageDialog(this, "O nome e sobrenome devem ser válidos.", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!validarEmail(email)) {
            JOptionPane.showMessageDialog(this, "E-mail inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!validarSenha(senha)) {
            JOptionPane.showMessageDialog(this, "A senha deve ter pelo menos 8 caracteres e incluir um caractere especial.", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!validarTelefone(telefone)) {
            JOptionPane.showMessageDialog(this, "O telefone deve ter 10 ou 11 dígitos.", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private boolean validarNomeSobrenome(String nome, String sobrenome) {
        return !nome.trim().isEmpty() && !sobrenome.trim().isEmpty();
    }

    private boolean validarEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.find();
    }

    private boolean validarSenha(String senha) {
        if (senha.length() < 8) {
            return false;
        }
        Pattern specialCharPattern = Pattern.compile("[^a-zA-Z0-9]");
        Matcher specialCharMatcher = specialCharPattern.matcher(senha);
        return specialCharMatcher.find();
    }

    private boolean validarTelefone(String telefone) {
        return telefone.matches("\\d{10,11}");
    }

    private void limparCampos() {
        nomeField.setText("");
        sobrenomeField.setText("");
        emailField.setText("");
        senhaField.setText("");
        telefoneField.setText("");
        enderecoField.setText("");
    }

    private void salvarDados() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("usuarios.txt"))) {
            for (Usuario usuario : usuarios) {
                writer.write("Nome: " + usuario.getNome() + ", Sobrenome: " + usuario.getSobrenome() + ", Email: " + usuario.getEmail() + ", Senha: " + usuario.getSenha() +
                        ", Telefone: " + usuario.getTelefone() + ", Endereço: " + usuario.getEndereco());
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar os dados.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadUsuarios() {
        try (BufferedReader reader = new BufferedReader(new FileReader("usuarios.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length == 6) {
                    String nome = parts[0].split(": ")[1];
                    String sobrenome = parts[1].split(": ")[1];
                    String email = parts[2].split(": ")[1];
                    String senha = parts[3].split(": ")[1];
                    String telefone = parts[4].split(": ")[1];
                    String endereco = parts[5].split(": ")[1];

                    Usuario usuario = new Usuario(nome, sobrenome, email, senha, telefone, endereco);
                    usuarios.add(usuario);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar os dados", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshUserList() {
        userModel.clear();
        for (Usuario usuario : usuarios) {
            userModel.addElement(usuario);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TelaCadastro::new);
    }
}
