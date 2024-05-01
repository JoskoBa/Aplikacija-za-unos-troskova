package gui;

import java.awt.EventQueue;
import klase.KorisnikID;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import klase.Lozinka;
import klase.Baza;
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

public class Prijava{

	private JFrame frame;
	private JTextField email;
	private JPasswordField lozinka;
	private String emails;
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Prijava window = new Prijava();
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
	public Prijava() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 362, 289);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("EMAIL");
		lblNewLabel.setBounds(67, 104, 103, 14);
		frame.getContentPane().add(lblNewLabel);
		
		email = new JTextField();
		email.setText("");
		email.setBounds(199, 101, 86, 20);
		frame.getContentPane().add(email);
		email.setColumns(10);
		
		JLabel lblLozinka = new JLabel("LOZINKA");
		lblLozinka.setBounds(71, 156, 74, 14);
		frame.getContentPane().add(lblLozinka);
		
		lozinka = new JPasswordField();
		lozinka.setText("");
		lozinka.setBounds(199, 153, 86, 20);
		frame.getContentPane().add(lozinka);
		
		JLabel lblNewLabel_1 = new JLabel("PRIJAVA");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblNewLabel_1.setBounds(112, 11, 121, 67);
		frame.getContentPane().add(lblNewLabel_1);
		
		
		
		
		
		JButton btnNewButton = new JButton("LOG IN");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String emails=email.getText();
				String lozinkas=new String(lozinka.getPassword());
				String lozinkaBaza="";
				
				
				try{
					Baza.getConnection();
					String upit="SELECT lozinka FROM korisnik WHERE email=? ";
					PreparedStatement psLozinka=Baza.getConn().prepareStatement(upit);
					psLozinka.setString(1, emails);
					ResultSet rs=psLozinka.executeQuery();
					
					if(rs.next()) {
						
						lozinkaBaza=rs.getString("lozinka");
						 
						 if(Lozinka.ProvjeraLozinke(lozinkas, lozinkaBaza)==true) {
							 KorisnikID.korID(emails);
							 Gizbornik gl=new Gizbornik();
							 frame.dispose();
							 Gizbornik.setList();
							 gl.setVisible();
							 
						 }
						 else {
							 JOptionPane.showMessageDialog(null, "Pogrešan Email/Lozinka!");
						 }
					}
					else {
						JOptionPane.showMessageDialog(null, "Pogrešan Email/Lozinka!");
					}
				}
				catch(Exception e1){
					System.out.println(e1);
				}
				
			}
		});
		
		btnNewButton.setBounds(132, 201, 89, 23);
		frame.getContentPane().add(btnNewButton);
	}
	
	public void setVisible() {
		frame.setVisible(true);
	}
}
