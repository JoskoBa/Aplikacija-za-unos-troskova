package gui;
import java.awt.Color;
import klase.Baza;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import klase.Lozinka;

import javax.swing.JPasswordField;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class Registracija{

	private JFrame frame;
	private JTextField korisnickoIme;
	private JTextField email;
	private JPasswordField lozinka;
	private JPasswordField ponLozinka;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Registracija window = new Registracija();
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
	public Registracija() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @return 
	 */
	
	
	
	
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 400, 407);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("KORISNICKO IME");
		lblNewLabel.setBounds(42, 112, 116, 14);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblEmail = new JLabel("EMAIL");
		lblEmail.setBounds(42, 154, 116, 14);
		frame.getContentPane().add(lblEmail);
		
		JLabel lblLozinka = new JLabel("LOZINKA");
		lblLozinka.setBounds(42, 191, 116, 14);
		frame.getContentPane().add(lblLozinka);
		
		JLabel lblPonoviLozinku = new JLabel("PONOVI LOZINKU");
		lblPonoviLozinku.setBounds(42, 232, 116, 14);
		frame.getContentPane().add(lblPonoviLozinku);
		
		korisnickoIme = new JTextField();
		korisnickoIme.setBounds(205, 109, 137, 20);
		frame.getContentPane().add(korisnickoIme);
		korisnickoIme.setColumns(10);
		
		email = new JTextField();
		email.setColumns(10);
		email.setBounds(205, 151, 137, 20);
		frame.getContentPane().add(email);
		
		
		lozinka = new JPasswordField();
		lozinka.setBounds(205, 188, 137, 20);
		frame.getContentPane().add(lozinka);
		
		ponLozinka = new JPasswordField();
		ponLozinka.setBounds(205, 229, 137, 20);
		frame.getContentPane().add(ponLozinka);
		
		JLabel lblNewLabel_1 = new JLabel("STVORI RAČUN");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblNewLabel_1.setBounds(101, 34, 221, 44);
		frame.getContentPane().add(lblNewLabel_1);
		
		JButton btnNewButton = new JButton("REGISTRACIJA");
		btnNewButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				String emails=email.getText();
				String korisnickoImes=korisnickoIme.getText();
				String lozinkas=new String(lozinka.getPassword());
				String ponLozinkas=new String(ponLozinka.getPassword());
				
				String hashedLozinka=Lozinka.hashLozinka(lozinkas);
				
				
				
				
				if(lozinkas.equals(ponLozinkas) && emails.contains("@") && lozinkas.length()>3) {
					
					
				try {
					
					Baza.getConnection();
					String provjeraKorisnika="SELECT * FROM korisnik WHERE email=?";
					PreparedStatement regProvjera=Baza.getConn().prepareStatement(provjeraKorisnika);
					regProvjera.setString(1, emails);
					ResultSet rs=regProvjera.executeQuery();

					if(rs.next()) {
						JOptionPane.showMessageDialog(null, "Korisnik sa tim emailom već postoji!!");
					}
					
					else {
						String upisKorisnika="INSERT INTO korisnik(korisnickoIme,email,lozinka) VALUES (?,?,?)";
						PreparedStatement regUpis=Baza.getConn().prepareStatement(upisKorisnika);
						regUpis.setString(1, korisnickoImes);
						regUpis.setString(2, emails);
						regUpis.setString(3, hashedLozinka);
						
						
						int ubacenoRedaka=regUpis.executeUpdate();
						
						if
						(ubacenoRedaka==1)
						{
							JOptionPane.showMessageDialog(null,"Registracija uspješna");
							Prijava prijava=new Prijava();
							frame.dispose();
							prijava.setVisible();
							
						}
						else
						{
							JOptionPane.showMessageDialog(null,"Registracija neuspješna");
						}
					}
				}
				catch(Exception e1){
					JOptionPane.showMessageDialog(null, e1);
				}
				
			}
			else {
				JOptionPane.showMessageDialog(null,"Uneseni podaci nisu ispravni!!");
				korisnickoIme.setText("");
				email.setText("");
				lozinka.setText("");
				ponLozinka.setText("");
			}
				
		}
		});
		btnNewButton.setBounds(110, 282, 143, 23);
		frame.getContentPane().add(btnNewButton);
	}
	
	public void setVisible() {
		frame.setVisible(true);
	}
	
	
}
