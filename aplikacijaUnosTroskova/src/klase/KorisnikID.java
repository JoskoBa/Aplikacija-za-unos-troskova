package klase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class KorisnikID {

	private static int korisnikID;
	
	public static void korID(String email) {
		
		try {
			Baza.getConnection();
			String upit="SELECT korisnik_id FROM korisnik WHERE email=?";
			PreparedStatement ps=Baza.getConn().prepareStatement(upit);
			ps.setString(1,email);
			
			ResultSet rs=ps.executeQuery();
			
			while(rs.next()) {
				korisnikID=rs.getInt("korisnik_id");
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static int getID() {
		return korisnikID;
	}
	
	public static void obrisiKorisnik(int korisnikId) {
        try  {
        	Baza.getConnection();
            String upit = "DELETE FROM korisnik WHERE korisnik_id = ?";
            try (PreparedStatement ps = Baza.getConn().prepareStatement(upit)) {
                ps.setInt(1, korisnikId);
                ps.executeUpdate();
             
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public static void obrisiBudzet(int korisnikId) {
        try  {
        	Baza.getConnection();
            String upit = "DELETE FROM budzet WHERE korisnik_id = ?";
            try (PreparedStatement ps = Baza.getConn().prepareStatement(upit)) {
                ps.setInt(1, korisnikId);
                ps.executeUpdate();
             
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public static void obrisiTransakcije(int korisnikId) {
        try  {
        	Baza.getConnection();
            String upit = "DELETE FROM transakcija WHERE korisnik_id = ?";
            try (PreparedStatement ps = Baza.getConn().prepareStatement(upit)) {
                ps.setInt(1, korisnikId);
                ps.executeUpdate();
             
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	
}
