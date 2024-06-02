package com.expensemanagement.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;


import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.web.client.RestTemplate;


import com.expensemanagement.client.PasswordEncryption;


import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LoginGUI {

	private JFrame frame;
	private JTextField loginEmailTxt;
	private JPasswordField loginPasswordTxt;
	private static String clientEmail = new String();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginGUI window = new LoginGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LoginGUI() {
		initialize();
	}

	public static String getEmail() {
		return clientEmail;
	}

	public void setVisible() {
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 369, 301);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("EMAIL");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel.setBounds(22, 26, 85, 35);
		frame.getContentPane().add(lblNewLabel);

		JLabel lblLozinka = new JLabel("PASSWORD");
		lblLozinka.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblLozinka.setBounds(22, 85, 100, 36);
		frame.getContentPane().add(lblLozinka);

		loginEmailTxt = new JTextField();
		loginEmailTxt.setFont(new Font("Tahoma", Font.PLAIN, 16));
		loginEmailTxt.setBounds(136, 27, 162, 36);
		frame.getContentPane().add(loginEmailTxt);
		loginEmailTxt.setColumns(10);

		loginPasswordTxt = new JPasswordField();
		loginPasswordTxt.setFont(new Font("Tahoma", Font.PLAIN, 16));
		loginPasswordTxt.setBounds(136, 87, 162, 36);
		frame.getContentPane().add(loginPasswordTxt);

		JButton btnNewButton = new JButton("LOG IN");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String loginEmail = loginEmailTxt.getText();
				clientEmail = loginEmail;
				String clientPassword = new String(loginPasswordTxt.getPassword());

				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);

				RestTemplate restTemplate = new RestTemplate();

				String url = "http://localhost:8080/client/" + loginEmail;

				String passwordDtb = restTemplate.getForObject(url, String.class);

				try {
					if (PasswordEncryption.passwordCheck(clientPassword, passwordDtb)) {
						JOptionPane.showMessageDialog(null, "Login successful!");
						MainMenuGUI mainMenu = new MainMenuGUI();
						mainMenu.setVisible();
						frame.dispose();
					} else {
						JOptionPane.showMessageDialog(null, "Incorrect password!!");
					}
				} catch (NullPointerException e1) {
					JOptionPane.showMessageDialog(null, "Incorrect email!");
				}

			}

		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnNewButton.setBounds(136, 150, 162, 44);
		frame.getContentPane().add(btnNewButton);

		JButton btnNewButton_1 = new JButton("REGISTRATION");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegistrationGUI registration = new RegistrationGUI();
				registration.setVisible();
				frame.dispose();
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnNewButton_1.setBounds(136, 205, 162, 43);
		frame.getContentPane().add(btnNewButton_1);
	}
}
