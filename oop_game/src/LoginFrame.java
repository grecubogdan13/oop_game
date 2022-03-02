import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class LoginFrame extends JFrame implements ActionListener {

    Container container=getContentPane();
    JLabel userLabel = new JLabel("EMAIL");
    JLabel passwordLabel = new JLabel("PASSWORD");
    JTextField userTextField = new JTextField();
    JTextField passwordTextField = new JTextField();
    JButton loginButton = new JButton("LOGIN");
    ArrayList<Account> login_accounts = new ArrayList<Account>();
    int chosen_account = -1;

    public LoginFrame(ArrayList<Account> login_accounts){
        Image icon = Toolkit.getDefaultToolkit().getImage("./Imagini/WoM.png");
        setIconImage(icon);
        setTitle("Login");
        setVisible(true);
        setBounds(10,10,300,450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        this.login_accounts=login_accounts;
        container.setLayout(null);
        userLabel.setBounds(50,50,100,30);
        container.add(userLabel);
        passwordLabel.setBounds(50,150,100,30);
        container.add(passwordLabel);
        userTextField.setBounds(50,100,150,30);
        container.add(userTextField);
        passwordTextField.setBounds(50,200,150,30);
        container.add(passwordTextField);
        loginButton.setBounds(50,300,100,30);
        loginButton.addActionListener(this);
        container.add(loginButton);
    }

    public int getAccount() {
        return chosen_account;
    }

    public void actionPerformed(ActionEvent a) {
        String formUser = userTextField.getText();
        String formPassword = passwordTextField.getText();
        for (int i = 0; i < login_accounts.size(); i++) {
            String user = login_accounts.get(i).getPlayer_info().getCredentials().getEmail();
            String password = login_accounts.get(i).getPlayer_info().getCredentials().getPassword();
            if (user.equals(formUser) && password.equals(formPassword)) {
                chosen_account = i;
                JOptionPane.showMessageDialog(this, "Login Successful");
                setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                dispose();
            }
        }
        if (chosen_account == -1) {
            JOptionPane.showMessageDialog(this, "Invalid username or password");
        }
    }
}
