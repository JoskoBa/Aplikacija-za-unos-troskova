package com.expensemanagement.ui;

import java.awt.EventQueue;



import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.expensemanagement.client.Client;
import com.expensemanagement.client.PasswordEncryption;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RegistrationGUI {

	private JFrame frame;
	private JTextField clientNameTxt;
	private JTextField clientSurnameTxt;
	private JTextField clientEmailTxt;
	private JPasswordField clientPassword1Txt;
	private JPasswordField clientPassword2Txt;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegistrationGUI window = new RegistrationGUI();
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
	public RegistrationGUI() {
		initialize();
	}

	public void setVisible() {
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 463, 457);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("NAME");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel.setBounds(59, 79, 57, 25);
		frame.getContentPane().add(lblNewLabel);

		JLabel lblPrezime = new JLabel("SURNAME");
		lblPrezime.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblPrezime.setBounds(59, 130, 130, 48);
		frame.getContentPane().add(lblPrezime);

		JLabel lblEmail = new JLabel("EMAIL");
		lblEmail.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblEmail.setBounds(59, 201, 113, 25);
		frame.getContentPane().add(lblEmail);

		JLabel lblLozinka = new JLabel("PASSWORD");
		lblLozinka.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblLozinka.setBounds(59, 263, 149, 25);
		frame.getContentPane().add(lblLozinka);

		JLabel lblPonovljenaLozinka = new JLabel("REPEAT PASSWORD");
		lblPonovljenaLozinka.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblPonovljenaLozinka.setBounds(10, 311, 241, 43);
		frame.getContentPane().add(lblPonovljenaLozinka);

		JLabel lblRegistracija = new JLabel("REGISTRATION");
		lblRegistracija.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblRegistracija.setBounds(144, 11, 143, 61);
		frame.getContentPane().add(lblRegistracija);

		clientNameTxt = new JTextField();
		clientNameTxt.setFont(new Font("Tahoma", Font.PLAIN, 16));
		clientNameTxt.setBounds(216, 74, 163, 35);
		frame.getContentPane().add(clientNameTxt);
		clientNameTxt.setColumns(10);

		clientSurnameTxt = new JTextField();
		clientSurnameTxt.setFont(new Font("Tahoma", Font.PLAIN, 16));
		clientSurnameTxt.setColumns(10);
		clientSurnameTxt.setBounds(216, 137, 163, 35);
		frame.getContentPane().add(clientSurnameTxt);

		clientEmailTxt = new JTextField();
		clientEmailTxt.setFont(new Font("Tahoma", Font.PLAIN, 16));
		clientEmailTxt.setColumns(10);
		clientEmailTxt.setBounds(216, 196, 163, 35);
		frame.getContentPane().add(clientEmailTxt);

		clientPassword1Txt = new JPasswordField();
		clientPassword1Txt.setFont(new Font("Tahoma", Font.PLAIN, 16));
		clientPassword1Txt.setBounds(216, 258, 163, 35);
		frame.getContentPane().add(clientPassword1Txt);

		clientPassword2Txt = new JPasswordField();
		clientPassword2Txt.setFont(new Font("Tahoma", Font.PLAIN, 16));
		clientPassword2Txt.setBounds(216, 315, 163, 35);
		frame.getContentPane().add(clientPassword2Txt);

		JButton btnNewButton = new JButton("SAVE");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String clientName = clientNameTxt.getText();
				String clientSurname = clientSurnameTxt.getText();
				String clientEmail = clientEmailTxt.getText();
				String clientPassword1 = new String(clientPassword1Txt.getPassword());
				String clientPassword2 = new String(clientPassword2Txt.getPassword());

				String hashedPassword = PasswordEncryption.hashPassword(clientPassword1);

				if (clientPassword1.equals(clientPassword2) && clientEmail.contains("@") && clientPassword1.length() > 3) {
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.APPLICATION_JSON);

					Client client = new Client(clientName, clientSurname, clientEmail, hashedPassword);

					ObjectMapper mapper = new ObjectMapper();

					String json = new String();

					try {
						json = mapper.writeValueAsString(client);
					} catch (JsonProcessingException e1) {
						e1.printStackTrace();
					}

					HttpEntity<String> entity = new HttpEntity<>(json, headers);

					RestTemplate restTemplate = new RestTemplate();
					String url = "http://localhost:8080/client";

					ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);

					if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
					    JOptionPane.showMessageDialog(null, "Client is registered!");
					} else {
					    JOptionPane.showMessageDialog(null, "Error!");
					}
					
					LoginGUI login = new LoginGUI();
					login.setVisible();
					frame.dispose();
				} else {
					JOptionPane.showMessageDialog(null, "The entered data is not correct!!");
					clientNameTxt.setText("");
					clientSurnameTxt.setText("");
					clientEmailTxt.setText("");
					clientPassword1Txt.setText("");
					clientPassword2Txt.setText("");
				}

			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnNewButton.setBounds(216, 365, 163, 35);
		frame.getContentPane().add(btnNewButton);
	}
}
