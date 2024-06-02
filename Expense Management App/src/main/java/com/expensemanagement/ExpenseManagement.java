package com.expensemanagement;





import javax.swing.SwingUtilities;


import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.expensemanagement.ui.LoginGUI;


@SpringBootApplication
public class ExpenseManagement  implements CommandLineRunner {

	public static void main(String[] args) {
		System.setProperty("java.awt.headless", "false");
	
		SpringApplication.run(ExpenseManagement.class, args);
		

	}
	/*
	@EventListener(ContextRefreshedEvent.class)
	public void onApplicationEvent(ContextRefreshedEvent event) {
		SwingUtilities.invokeLater(() -> {
			LogInGUI login = new LogInGUI();
			login.setVisible();
		});
	}
	*/
	
	 
	@Override
    public void run(String... args) {
     
	 	try {
	 		 UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
	    } catch (UnsupportedLookAndFeelException ex) {
	        ex.printStackTrace();
	    }

       
        SwingUtilities.invokeLater(() -> {
        	LoginGUI login = new LoginGUI();
			login.setVisible();
        });
    }
	

}
