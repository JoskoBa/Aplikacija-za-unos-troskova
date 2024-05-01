package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;

import klase.Baza;
import klase.KorisnikID;
import klase.Lozinka;
import klase.Tablica;

import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.sql.PreparedStatement;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class OsobniPodaci {

	private JFrame frame;
	private JTextField korIme;
	private JTextField email;
	private JPasswordField lozinka;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OsobniPodaci window = new OsobniPodaci();
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
	public OsobniPodaci() {
		initialize();
	}
	
	public void setVisible() {
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	
	public void osobniPodaci() {
		String lozinkas= new String(lozinka.getPassword());
		String hashedLozinka=Lozinka.hashLozinka(lozinkas);
		
		if(email.getText().contains("@") && lozinkas.length()>3)
		{
			try {
				Baza.getConnection();
				String update="UPDATE korisnik SET korisnickoIme=?, email=?, lozinka=? WHERE korisnik_id=?";
				PreparedStatement ps=Baza.getConn().prepareStatement(update);
				ps.setString(1, korIme.getText());
				ps.setString(2, email.getText());
				ps.setString(3, hashedLozinka);
				ps.setInt(4, KorisnikID.getID());
				
				int updateRedak=ps.executeUpdate();
				
				if(updateRedak>0) {
					JOptionPane.showMessageDialog(null, "Podaci Ažurirani!!");
					frame.dispose();
				}
			}
			catch(Exception e1) {
				e1.printStackTrace();
			}
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Uneseni podaci nisu ispravni!!");
		}
		
	}
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 378, 337);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		korIme = new JTextField();
		korIme.setBounds(169, 26, 137, 20);
		frame.getContentPane().add(korIme);
		korIme.setColumns(10);
		
		email = new JTextField();
		email.setColumns(10);
		email.setBounds(169, 85, 137, 20);
		frame.getContentPane().add(email);
		
		lozinka = new JPasswordField();
		lozinka.setBounds(169, 142, 137, 20);
		frame.getContentPane().add(lozinka);
		
		JLabel lblNewLabel = new JLabel("KORISNICKO IME");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel.setBounds(22, 27, 155, 14);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblEmail = new JLabel("EMAIL");
		lblEmail.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblEmail.setBounds(22, 88, 155, 14);
		frame.getContentPane().add(lblEmail);
		
		JLabel lblLozinka = new JLabel("LOZINKA");
		lblLozinka.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblLozinka.setBounds(22, 145, 155, 14);
		frame.getContentPane().add(lblLozinka);
		
		JButton btnNewButton = new JButton("SPREMI");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				osobniPodaci();
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnNewButton.setBounds(107, 206, 117, 56);
		frame.getContentPane().add(btnNewButton);
	}
}
