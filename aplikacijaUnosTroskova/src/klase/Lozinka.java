package klase;

import org.mindrot.jbcrypt.BCrypt;

public class Lozinka {
	
	public static String hashLozinka(String lozinka) {
	
		 String hashedLozinka = BCrypt.hashpw(lozinka, BCrypt.gensalt());
		 return hashedLozinka;
	}
   
	
	
    public static boolean ProvjeraLozinke(String lozinka, String hashedLozinka) {
    	 if (BCrypt.checkpw(lozinka, hashedLozinka)) {
    	       return true;
    	    } else {
    	    	return false;
    	    }
    }
   
   
}
