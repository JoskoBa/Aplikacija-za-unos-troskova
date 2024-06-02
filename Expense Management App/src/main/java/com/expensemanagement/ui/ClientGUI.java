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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.expensemanagement.client.Client;
import com.expensemanagement.client.PasswordEncryption;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ClientGUI {

	private JFrame frame;
	private JTextField clientNameTxt;
	private JTextField clientSurnameTxt;
	private JTextField clientEmailTxt;
	private JPasswordField clientPassword1Txt;
	private JPasswordField clientPassword2Txt;
	private Client client;
	private RestTemplate restTemplate = new RestTemplate();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientGUI window = new ClientGUI();
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
	public ClientGUI() {
		initialize();
	}

	public void setVisible() {
		frame.setVisible(true);
	}

	private void personalInformation() {
		try {
			client = restTemplate.getForObject("http://localhost:8080/client/email/" + LoginGUI.getEmail(),
					Client.class);

			if (client != null) {
				clientNameTxt.setText(client.getClientName());
				clientSurnameTxt.setText(client.getClientSurname());
				clientEmailTxt.setText(client.getClientEmail());
			}
		} catch (RestClientException e) {
			e.printStackTrace();

		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 455, 456);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel clientNameLbl = new JLabel("NAME");
		clientNameLbl.setFont(new Font("Tahoma", Font.BOLD, 16));
		clientNameLbl.setBounds(49, 77, 101, 47);
		frame.getContentPane().add(clientNameLbl);

		JLabel clientSurnameLbl = new JLabel("SURNAME");
		clientSurnameLbl.setFont(new Font("Tahoma", Font.BOLD, 16));
		clientSurnameLbl.setBounds(49, 135, 101, 47);
		frame.getContentPane().add(clientSurnameLbl);

		JLabel lblEmail = new JLabel("EMAIL");
		lblEmail.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblEmail.setBounds(49, 185, 101, 47);
		frame.getContentPane().add(lblEmail);

		JLabel lblLozinka = new JLabel("PASSWORD");
		lblLozinka.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblLozinka.setBounds(49, 243, 101, 47);
		frame.getContentPane().add(lblLozinka);

		JLabel lblPonoviLozinku = new JLabel("REPEAT PASSWORD");
		lblPonoviLozinku.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblPonoviLozinku.setBounds(10, 294, 183, 47);
		frame.getContentPane().add(lblPonoviLozinku);

		JLabel lblPodaciORaunu = new JLabel("PERSONAL INFORMATION");
		lblPodaciORaunu.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblPodaciORaunu.setBounds(118, 11, 229, 47);
		frame.getContentPane().add(lblPodaciORaunu);

		clientNameTxt = new JTextField();
		clientNameTxt.setFont(new Font("Tahoma", Font.PLAIN, 16));
		clientNameTxt.setBounds(201, 84, 146, 32);
		frame.getContentPane().add(clientNameTxt);
		clientNameTxt.setColumns(10);

		clientSurnameTxt = new JTextField();
		clientSurnameTxt.setFont(new Font("Tahoma", Font.PLAIN, 16));
		clientSurnameTxt.setColumns(10);
		clientSurnameTxt.setBounds(201, 142, 146, 32);
		frame.getContentPane().add(clientSurnameTxt);

		clientEmailTxt = new JTextField();
		clientEmailTxt.setFont(new Font("Tahoma", Font.PLAIN, 16));
		clientEmailTxt.setColumns(10);
		clientEmailTxt.setBounds(201, 192, 146, 32);
		frame.getContentPane().add(clientEmailTxt);

		clientPassword1Txt = new JPasswordField();
		clientPassword1Txt.setFont(new Font("Tahoma", Font.PLAIN, 16));
		clientPassword1Txt.setBounds(201, 250, 146, 32);
		frame.getContentPane().add(clientPassword1Txt);

		clientPassword2Txt = new JPasswordField();
		clientPassword2Txt.setFont(new Font("Tahoma", Font.PLAIN, 16));
		clientPassword2Txt.setBounds(201, 301, 146, 32);
		frame.getContentPane().add(clientPassword2Txt);

		JButton btnNewButton = new JButton("UPDATE");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String clientName = clientNameTxt.getText();
				String clientSurname = clientSurnameTxt.getText();
				String clientEmail = clientEmailTxt.getText();
				String clientPassword1 = new String(clientPassword1Txt.getPassword());
				String clientPassword2 = new String(clientPassword2Txt.getPassword());
				String hashedPassword= PasswordEncryption.hashPassword(clientPassword1);
				
				client.setClientName(clientName);;
				client.setClientSurname(clientSurname);;
				client.setClientEmail(clientEmail);
				client.setClientPassword(hashedPassword);

				if (clientPassword1.equals(clientPassword2) && clientEmail.contains("@") && clientPassword1.length() > 3) {
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.APPLICATION_JSON);

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
						JOptionPane.showMessageDialog(null, "Data has been successfully updated!");
						frame.dispose();
					} else {
						JOptionPane.showMessageDialog(null, "The entered data is not correct!!");
						clientNameTxt.setText("");
						clientEmailTxt.setText("");
						clientSurnameTxt.setText("");
						clientPassword1Txt.setText("");
						clientPassword2Txt.setText("");
					}
					
				} 
				else {
					JOptionPane.showMessageDialog(null, "The entered data is not correct!!");
					clientNameTxt.setText("");
					clientEmailTxt.setText("");
					clientSurnameTxt.setText("");
					clientPassword1Txt.setText("");
					clientPassword2Txt.setText("");
				}

			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnNewButton.setBounds(201, 359, 146, 32);
		frame.getContentPane().add(btnNewButton);
		personalInformation();
	}
}
