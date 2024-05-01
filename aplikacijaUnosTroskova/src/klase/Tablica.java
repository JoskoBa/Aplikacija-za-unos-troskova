package klase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import gui.Gizbornik;

public class Tablica {


	
	public static JTable unosTablica(JTable tablica) {
		try
		{
			Baza.getConnection();
			
			String upitT="SELECT t.transakcija_id, t.datum, t.iznos, k.nazivKategorije, ko.korisnickoIme, ko.email FROM transakcija t JOIN kategorija k ON t.kategorija_id = k.kategorija_id JOIN korisnik ko ON t.korisnik_id = ko.korisnik_id ORDER BY t.korisnik_id;";
			Statement stmt=Baza.getConnection().createStatement();
			ResultSet rs=stmt.executeQuery(upitT);
			
			
			DefaultTableModel model=(DefaultTableModel)tablica.getModel();
			model.setRowCount(0);
			
			while(rs.next()) 
			{
				int ID=rs.getInt("transakcija_id");
				String Datum=rs.getString("datum");
				double Iznos=rs.getDouble("iznos");
				String Kategorija=rs.getString("nazivKategorije");
				String Korisnik=rs.getString("korisnickoIme");
				String Email=rs.getString("email");
				
				
				
				
				model.addRow(new Object[] {ID, Datum, Iznos, Kategorija, Korisnik, Email});
			
			}
			unosBudzet();
		}
		catch(Exception e1)
		{
			e1.printStackTrace();
		}
		
		return tablica;

	}
	
	public static void obrisiRedak(JTable tablica) {
		
		DefaultTableModel model=(DefaultTableModel) tablica.getModel();
		
		
	    int odabraniRedak = tablica.getSelectedRow();
	    
	    int idTransakcije=(int)model.getValueAt(odabraniRedak, 0);
	    
	    if (odabraniRedak != -1) { 
	        model.removeRow(odabraniRedak);
	        obrisiIzBaze(idTransakcije);
	    } else {
	        JOptionPane.showMessageDialog(null, "greska");
	    }
	}
	
	private static void obrisiIzBaze(int idTransakcije) {
	    try {
	    	
	    	Baza.getConnection();
	    	String upit="DELETE FROM transakcija WHERE transakcija_id=?";
	    	PreparedStatement ps=Baza.getConn().prepareStatement(upit);
	    	
	    	ps.setInt(1, idTransakcije);
	    	
	    	int rezultat=ps.executeUpdate();
	    	
	    	if(rezultat==1) {
	    		JOptionPane.showMessageDialog(null, "Uspjesno Obirsano!");
	    	}
	    	else {
	    		JOptionPane.showMessageDialog(null, "Dogodila se greska!!");
	    	}
	    	
	    }
	    catch(Exception e) {
	    	e.printStackTrace();
	    }
	        
	}
	
	public static double getSumaIznos() {
		
		double suma=0;
		try {
			Baza.getConn();
			String upit="SELECT SUM(iznos) AS sumaIznosa FROM transakcija t JOIN kategorija k ON t.kategorija_id = k.kategorija_id WHERE  t.korisnik_id = ?;";
					
			PreparedStatement ps=Baza.getConn().prepareStatement(upit);
			ps.setInt(1, KorisnikID.getID());
			ResultSet rs=ps.executeQuery();
			
			if(rs.next()) {
				suma=rs.getDouble("sumaIznosa");
			}
			else {
				JOptionPane.showMessageDialog(null, "Greška kod dobave sume iznosa!!");
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return suma;
	}
	
	public static double getStednja() {
		try {
			Baza.getConnection();
			String upit="SELECT SUM(ABS(iznos)) AS stednja FROM transakcija t JOIN kategorija k ON t.kategorija_id = k.kategorija_id WHERE k.nazivKategorije = 'Stednja' AND t.korisnik_id =?;";
			PreparedStatement ps=Baza.getConn().prepareStatement(upit);
			ps.setInt(1, KorisnikID.getID());
			ResultSet rs=ps.executeQuery();
			
			if(rs.next()) {
				return rs.getDouble("stednja");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return 0;
	}

	public static double vrstaKategorije(String kategorija, double trosak) {
		
		String predznak="";
		try {
			Baza.getConnection();
			String upit="SELECT vrsta FROM kategorija WHERE nazivKategorije=?";
			PreparedStatement ps=Baza.getConn().prepareStatement(upit);
			ps.setString(1, kategorija);
			
			ResultSet rs=ps.executeQuery();
			
			if(rs.next()) {
				predznak=rs.getString("vrsta");
			}
			
			
			if(predznak.equals("RASHOD")) {
				return -trosak;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return trosak;
	}
	
	public static void unosBudzet() {
		try {
			Baza.getConnection();
			String upit="SELECT korisnik_id FROM budzet WHERE korisnik_id=?";
			PreparedStatement ps=Baza.getConn().prepareStatement(upit);
			ps.setInt(1, KorisnikID.getID());
			ResultSet rs=ps.executeQuery();
			
			if(rs.next()) {
				String Update="UPDATE budzet SET iznos=? WHERE korisnik_id=?";
				PreparedStatement ps1=Baza.getConn().prepareStatement(Update);
				ps1.setDouble(1, Tablica.getSumaIznos());
				ps1.setInt(2, KorisnikID.getID());
				
				int updateRedaka=ps1.executeUpdate();
				if(updateRedaka>0) {
					
				}
				else {
					JOptionPane.showMessageDialog(null, "Dogodila se greška kod ažuriranja!!");
				}
			}
			else {
				String Unos="INSERT INTO budzet (korisnik_id, iznos) VALUES (?,?)";
				PreparedStatement ps2=Baza.getConn().prepareStatement(Unos);
				ps2.setInt(1, KorisnikID.getID());
				ps2.setDouble(2, Tablica.getSumaIznos());
				
				int ubaceno=ps2.executeUpdate();
				if(ubaceno!=-1) {
					JOptionPane.showMessageDialog(null, "Budzet postavljen!!");
				}
				else {
					JOptionPane.showMessageDialog(null, "Dogodila se greška kod postavljanja budzeta!!");
				}
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
public static int kategorijaID(String kategorija) {
		
		
		try {
			Baza.getConnection();
			String upit="SELECT kategorija_id FROM kategorija WHERE nazivKategorije=?";
			PreparedStatement ps=Baza.getConn().prepareStatement(upit);
			ps.setString(1, kategorija);
			
			ResultSet rs=ps.executeQuery();
			
			if(rs.next()) {
				return rs.getInt("kategorija_id");
			}
		}
		catch(Exception e1) {
			e1.printStackTrace();
		}
		
		return 0;
		
	}

public static void unosTroska(String datum, int kategorija, double trosak, int ID,JTable table) {
	
	
	
	try {
		Baza.getConnection();
		String upitTrosak="INSERT INTO transakcija(datum, iznos, kategorija_id, korisnik_id) VALUES(?,?,?,?);";
		
		PreparedStatement ps=Baza.getConn().prepareStatement(upitTrosak);
		
		
		ps.setString(1, datum);
		ps.setDouble(2, trosak);
		ps.setInt(3, kategorija);
		ps.setInt(4, ID);
		
		int ubacenoRedaka1=ps.executeUpdate();
		
		if(ubacenoRedaka1>0) {
		
			JOptionPane.showMessageDialog(null, "Uspješno upisano!!");
		}
		else {
			JOptionPane.showMessageDialog(null, "Dogodila se greška!!");
		}
		
		
		Tablica.unosTablica(table);
		
	}
	catch(Exception e) {
		e.printStackTrace();
	}
	

	
}
}
