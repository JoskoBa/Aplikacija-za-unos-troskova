package gui;

import java.awt.Component;


import java.awt.EventQueue;



import gui.Azuriranje;
import klase.Baza;
import klase.KorisnikID;




import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionListener;



import javax.swing.event.ListSelectionEvent;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import com.toedter.calendar.JCalendar;

import klase.Lista;
import klase.Tablica;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.List;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Gizbornik {
	
	
	public void setVisible() {
		frame.setVisible(true);
	}
	
	private static DefaultListModel listModel=new DefaultListModel();
	int ind;
	String odbKategorija;
	private JFrame frame;
	private JTextField trosak;
	private JTable table;
	int odbRedak;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gizbornik window = new Gizbornik();
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
	public Gizbornik() {
		initialize();
	}
	
	

	/**
	 * Initialize the contents of the frame.
	 */
	

	public static DefaultListModel getListModel() {
		return listModel;
	}
	
	public  JTable getTable() {
		return table;
	}
	
	public static void setList() {
		Lista.podaciuListu(listModel);
	}
	
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1023, 560);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("ODABERI DATUM:");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel.setBounds(37, 59, 170, 22);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblOdaberiKategoriju = new JLabel("ODABERI KATEGORIJU:");
		lblOdaberiKategoriju.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblOdaberiKategoriju.setBounds(280, 59, 223, 22);
		frame.getContentPane().add(lblOdaberiKategoriju);
		
		JLabel lblUnesiTroak = new JLabel("UNESI TROŠAK:");
		lblUnesiTroak.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblUnesiTroak.setBounds(528, 59, 170, 22);
		frame.getContentPane().add(lblUnesiTroak);
		
		trosak = new JTextField();
		trosak.setBounds(528, 92, 170, 20);
		frame.getContentPane().add(trosak);
		trosak.setColumns(10);
		
		
		JCalendar datum = new JCalendar();
		datum.setBounds(37, 92, 198, 153);
		frame.getContentPane().add(datum);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date newDate = datum.getDate();
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(280, 92, 198, 156);
		frame.getContentPane().add(scrollPane);
		
		JList<String> lista = new JList<String>();
		lista.setModel(listModel);
		
		
		
	
		lista.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				odbKategorija=lista.getSelectedValue();
				
				ind=lista.getSelectedIndex();
				
			}
		});
		scrollPane.setViewportView(lista);
		
		
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(37, 298, 661, 212);
		frame.getContentPane().add(scrollPane_1);
		
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				odbRedak=table.getSelectedRow();
			}
		});
		
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"ID", "Datum", "Iznos", "Kategorija", "Korisnik", "Email"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		
		

		
		scrollPane_1.setViewportView(table);
	
		Tablica.unosTablica(table);
		
		JLabel lblNewLabel_1 = new JLabel("STANJE:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel_1.setBounds(736, 295, 81, 30);
		frame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("ŠTEDNJA:");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel_1_1.setBounds(736, 370, 106, 30);
		frame.getContentPane().add(lblNewLabel_1_1);
		
		JLabel stanje = new JLabel("/");
		stanje.setFont(new Font("Tahoma", Font.BOLD, 18));
		stanje.setBounds(872, 295, 81, 30);
		frame.getContentPane().add(stanje);
		
		stanje.setText(Double.toString(Tablica.getSumaIznos()));
		
		JLabel stednja = new JLabel("/");
		stednja.setFont(new Font("Tahoma", Font.BOLD, 18));
		stednja.setBounds(872, 370, 81, 30);
		frame.getContentPane().add(stednja);
		
		stednja.setText(Double.toString(Tablica.getStednja()));
		
		JButton btnNewButton = new JButton("SPREMI");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				double trosaks=Tablica.vrstaKategorije(odbKategorija, Double.valueOf(trosak.getText()));
				
				Tablica.unosTroska(dateFormat.format(newDate), Tablica.kategorijaID(odbKategorija), trosaks, KorisnikID.getID(),table);
				
				
				stanje.setText(Double.toString(Tablica.getSumaIznos()));
				stednja.setText(Double.toString(Tablica.getStednja()));
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnNewButton.setBounds(528, 137, 170, 69);
		frame.getContentPane().add(btnNewButton);
		
		JButton btnObrii = new JButton("OBRIŠI");
		btnObrii.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Tablica.obrisiRedak(table);
				
			}
		});
		btnObrii.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnObrii.setBounds(802, 137, 170, 69);
		frame.getContentPane().add(btnObrii);
		
		JButton btnAuriraj = new JButton("AŽURIRAJ");
		btnAuriraj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				Azuriranje az=new Azuriranje();
				az.podaciIzTablice(table, odbRedak);
				az.setVisible();
			}
		});
		btnAuriraj.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnAuriraj.setBounds(802, 36, 170, 69);
		frame.getContentPane().add(btnAuriraj);
		
		JButton btnNewButton_1 = new JButton("+");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NovaKategorija nk=new NovaKategorija();
				nk.setVisible();
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnNewButton_1.setBounds(347, 253, 50, 23);
		frame.getContentPane().add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("-");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Lista.obrisiIzBaze(odbKategorija);
				listModel.removeElementAt(ind);
			}
		});
		btnNewButton_2.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnNewButton_2.setBounds(280, 253, 50, 23);
		frame.getContentPane().add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("AŽURIRAJ OSOBNE PODATKE");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OsobniPodaci op= new OsobniPodaci();
				op.setVisible();
			}
		});
		btnNewButton_3.setBounds(0, 0, 207, 23);
		frame.getContentPane().add(btnNewButton_3);
		
		JButton btnNewButton_3_1 = new JButton("OBRIŠI PROFIL");
		btnNewButton_3_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 Object[] options = {"Da", "Ne"};

			        
			        int odgovor = JOptionPane.showOptionDialog(null, "Želite li nastaviti?", "Potvrda",
			                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			        
			        if (odgovor == JOptionPane.YES_OPTION) {
			        	
			        	KorisnikID.obrisiTransakcije(KorisnikID.getID());
			        	KorisnikID.obrisiBudzet(KorisnikID.getID());
			        	KorisnikID.obrisiKorisnik(KorisnikID.getID());
	
			        	
			        	frame.dispose();
			        	listModel.clear();
			        	RegPri rp=new RegPri();
			        	rp.setVisible();
			            
			        } else {
			            System.out.println("Odabrali ste 'Ne' ili zatvorili dijalog.");
			           
			        }
			}
		});
		btnNewButton_3_1.setBounds(210, 0, 156, 23);
		frame.getContentPane().add(btnNewButton_3_1);}
	
	
}
