package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;

import klase.Baza;
import klase.Lista;

import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.event.ActionEvent;

public class NovaKategorija {
	String vrstaKategorije;
	private JFrame frame;
	private JTextField novaKategorija;
	private final ButtonGroup vrsta = new ButtonGroup();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NovaKategorija window = new NovaKategorija();
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
	public NovaKategorija() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 335, 266);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("NAZIV:");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel.setBounds(31, 58, 72, 28);
		frame.getContentPane().add(lblNewLabel);
		
		novaKategorija = new JTextField();
		novaKategorija.setBounds(113, 58, 137, 20);
		frame.getContentPane().add(novaKategorija);
		novaKategorija.setColumns(10);
		
		JRadioButton prihod = new JRadioButton("PRIHOD");
		prihod.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				vrstaKategorije=prihod.getText();
			}
		});
		prihod.setFont(new Font("Tahoma", Font.BOLD, 16));
		vrsta.add(prihod);
		prihod.setBounds(31, 113, 109, 23);
		frame.getContentPane().add(prihod);
		
		JRadioButton rashod = new JRadioButton("RASHOD");
		rashod.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				vrstaKategorije=rashod.getText();
			}
		});
		rashod.setFont(new Font("Tahoma", Font.BOLD, 16));
		vrsta.add(rashod);
		rashod.setBounds(31, 158, 134, 23);
		frame.getContentPane().add(rashod);
		
		JButton btnNewButton = new JButton("KREIRAJ");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String nazivKategorije=novaKategorija.getText();
				
				try {
					if(provjeraKategorije(nazivKategorije)==true)
					{
						JOptionPane.showMessageDialog(null, "Kategorija već postoji!!");
					}
					
				else {
					String upit="INSERT INTO kategorija (nazivKategorije, vrsta) VALUES (?,?)" ;
					PreparedStatement ps=Baza.getConn().prepareStatement(upit);
					ps.setString(1, nazivKategorije);
					ps.setString(2, vrstaKategorije);
					
					int ubacenoRedaka=ps.executeUpdate();
					
					if(ubacenoRedaka==1) {
						Lista.podaciNaListu(Gizbornik.getListModel(), nazivKategorije);
						JOptionPane.showMessageDialog(null, "Kategorija je dodana!");
						
					}
				
				}
					
					
				}
				catch(Exception e1) {
					JOptionPane.showMessageDialog(null, "Potrebno je odabrati vrstu kategorije!!");
					e1.printStackTrace();
				}
				
				
			}
		});
		btnNewButton.setBounds(166, 119, 118, 77);
		frame.getContentPane().add(btnNewButton);
	}
	
	public void setVisible() {
		frame.setVisible(true);
	}
	
	private boolean provjeraKategorije(String kategorija) {
		try {
			Baza.getConnection();
			String upit="SELECT nazivKategorije FROM kategorija WHERE nazivKategorije=?";
			PreparedStatement ps=Baza.getConn().prepareStatement(upit);
			ps.setString(1, kategorija);
			
			ResultSet rs=ps.executeQuery();
			
			return rs.next();
		}
		catch(Exception e1) {
			e1.printStackTrace();
		}
		return false;
	}
}
