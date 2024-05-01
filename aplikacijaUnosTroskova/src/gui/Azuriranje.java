package gui;

import java.awt.EventQueue;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import com.toedter.calendar.JCalendar;

import klase.Baza;
import klase.KorisnikID;
import klase.Lista;
import klase.Lozinka;
import klase.Tablica;

import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.awt.event.ActionEvent;

public class Azuriranje {
	Gizbornik g=new Gizbornik();
	int id_kategorije;
	int id_korisnika;
	private static DefaultListModel listModel=new DefaultListModel();
	private JFrame frame;
	private JTextField datumA;
	private JTextField kategorijaA;
	private JTextField iznosA;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Azuriranje window = new Azuriranje();
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
	public Azuriranje() {
		initialize();
	}
	
	public void setVisible() {
		frame.setVisible(true);
	}
	
	public void azuriranje(int id_korisnika, int id_kategorija) {
		try {
			Baza.getConnection();
			String update="UPDATE transakcija SET datum=?, iznos=?, kategorija_id=? WHERE transakcija_id=?";
			PreparedStatement ps=Baza.getConn().prepareStatement(update);
			ps.setString(1, datumA.getText());
			ps.setDouble(2, Double.valueOf(iznosA.getText()));
			ps.setInt(3, id_kategorija);
			ps.setInt(4, id_korisnika);
			
			int updateRedak=ps.executeUpdate();
			
			if(updateRedak>0) {
				JOptionPane.showMessageDialog(null, "Podaci Ažurirani!!");
				Tablica.unosTablica(g.getTable());
				g.setVisible();
			}
			
		}
		catch(Exception e1)
		{
			e1.printStackTrace();
		}
	}
	
	public void podaciIzTablice(JTable tablica, int odbRedak) {
		
		id_korisnika=Integer.valueOf(tablica.getValueAt(odbRedak, 0).toString());
		datumA.setText(tablica.getValueAt(odbRedak, 1).toString());
		iznosA.setText(tablica.getValueAt(odbRedak, 2).toString());
		kategorijaA.setText(tablica.getValueAt(odbRedak, 3).toString());
		
	}
	
	public int idKategorije() {
		try {
			Baza.getConnection();
			String upit="SELECT kategorija_id FROM kategorija WHERE nazivKategorije=?";
			PreparedStatement ps=Baza.getConn().prepareStatement(upit);
			ps.setString(1, kategorijaA.getText());
			
			ResultSet rs=ps.executeQuery();
			
			if(rs.next()) {
				
				  id_kategorije=rs.getInt("kategorija_id");		 
		}
			
			
	}
		
		
		catch(Exception e1) {
			e1.printStackTrace();
		}
		return id_kategorije;
	}
	
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 271, 411);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		Lista.podaciuListu(listModel);
		
		datumA = new JTextField();
		datumA.setBounds(24, 53, 198, 20);
		frame.getContentPane().add(datumA);
		datumA.setColumns(10);
		
		kategorijaA = new JTextField();
		kategorijaA.setColumns(10);
		kategorijaA.setBounds(24, 123, 198, 20);
		frame.getContentPane().add(kategorijaA);
		
		iznosA = new JTextField();
		iznosA.setColumns(10);
		iznosA.setBounds(24, 210, 198, 20);
		frame.getContentPane().add(iznosA);
		
		JLabel lblNewLabel_1 = new JLabel("DATUM");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel_1.setBounds(24, 11, 86, 14);
		frame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblKategorija_1 = new JLabel("KATEGORIJA");
		lblKategorija_1.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblKategorija_1.setBounds(24, 98, 134, 14);
		frame.getContentPane().add(lblKategorija_1);
		
		JLabel lblIznos_1 = new JLabel("IZNOS");
		lblIznos_1.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblIznos_1.setBounds(24, 185, 134, 14);
		frame.getContentPane().add(lblIznos_1);
		
		JButton btnNewButton = new JButton("SPREMI");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				azuriranje(id_korisnika,idKategorije());
				
			
				
			}
		});
		
		
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnNewButton.setBounds(24, 270, 198, 79);
		frame.getContentPane().add(btnNewButton);
	}
}
