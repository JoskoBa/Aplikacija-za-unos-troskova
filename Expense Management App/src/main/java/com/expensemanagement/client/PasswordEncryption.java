package com.expensemanagement.client;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordEncryption {

	public static String hashPassword(String password) {

		String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
		return hashedPassword;
	}

	public static boolean passwordCheck(String password, String hashedPassword) {
		if (BCrypt.checkpw(password, hashedPassword)) {
			return true;
		} else {
			return false;
		}
	}

}
