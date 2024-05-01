package klase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;

public class Lista {
	
	
	public static void podaciuListu(DefaultListModel listModel) {
		
		try {
			Baza.getConnection();
			String upit="SELECT nazivKategorije FROM kategorija";
			Statement stmt=Baza.getConn().createStatement();
			ResultSet rs=stmt.executeQuery(upit);
			
			while(rs.next()) {
				listModel.addElement(rs.getString("nazivKategorije"));
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void obrisiIzBaze(String kategorija) {
		try {
			Baza.getConn();
			String upit="DELETE FROM kategorija WHERE nazivKategorije=?;";
			PreparedStatement ps=Baza.getConn().prepareStatement(upit);
			ps.setString(1, kategorija);
			int obrisanoRedaka=ps.executeUpdate();
			
			if(obrisanoRedaka==1) {
				JOptionPane.showMessageDialog(null, "Kategorija izbrisana iz baze!!");
			}
			else {
				JOptionPane.showMessageDialog(null, "Greska!"+kategorija);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static DefaultListModel podaciNaListu (DefaultListModel model, String naziv) {
		model.addElement(naziv);
		return model;
	}
	
	
	
}
