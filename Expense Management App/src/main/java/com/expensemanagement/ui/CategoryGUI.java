package com.expensemanagement.ui;

import java.awt.EventQueue;


import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.json.simple.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpHeaders;

import javax.swing.JButton;
import java.awt.event.ActionListener;

import java.awt.event.ActionEvent;
import org.springframework.http.MediaType;

import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

public class CategoryGUI {

	private JFrame frame;
	private JTextField nameTxt;
	private final ButtonGroup buttonGroup = new ButtonGroup();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CategoryGUI window = new CategoryGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void setVisible() {
		frame.setVisible(true);
	}

	/**
	 * Create the application.
	 */
	public CategoryGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 306, 339);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		nameTxt = new JTextField();
		nameTxt.setFont(new Font("Tahoma", Font.PLAIN, 16));
		nameTxt.setBounds(51, 55, 182, 40);
		frame.getContentPane().add(nameTxt);
		nameTxt.setColumns(10);

		JLabel lblNewLabel = new JLabel("CATEGORY TYPE");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel.setBounds(51, 106, 182, 47);
		frame.getContentPane().add(lblNewLabel);

		JLabel lblCategoryName = new JLabel("CATEGORY NAME");
		lblCategoryName.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblCategoryName.setBounds(51, 11, 182, 33);
		frame.getContentPane().add(lblCategoryName);

		JRadioButton income = new JRadioButton("INCOME");
		buttonGroup.add(income);
		income.setFont(new Font("Tahoma", Font.BOLD, 16));
		income.setBounds(51, 160, 109, 23);
		frame.getContentPane().add(income);

		JRadioButton expense = new JRadioButton("EXPENSE");
		buttonGroup.add(expense);
		expense.setFont(new Font("Tahoma", Font.BOLD, 16));
		expense.setBounds(51, 200, 109, 23);
		frame.getContentPane().add(expense);

		JButton btnNewButton = new JButton("SAVE");
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String categoryName = nameTxt.getText().toUpperCase();
				String categoryType = new String();

				if (income.isSelected()) {
					categoryType = "INCOME";
				} else if (expense.isSelected()) {
					categoryType = "EXPENSE";
				}

				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);

				JSONObject request = new JSONObject();
				request.put("categoryName", categoryName);
				request.put("categoryType", categoryType);

				HttpEntity<String> entity = new HttpEntity<>(request.toString(), headers);

				RestTemplate restTemplate = new RestTemplate();
				String url0 = "http://localhost:8080/category/" + categoryName;
				boolean duplicate = restTemplate.getForObject(url0, boolean.class);

				if (duplicate == true) {
					JOptionPane.showMessageDialog(null, "Category already exist!");
				} else {
					String url = "http://localhost:8080/category";
					restTemplate.postForObject(url, entity, String.class);
					MainMenuGUI.categoriesFromDatabaseToList();
					JOptionPane.showMessageDialog(null, "Category is created!");
					frame.dispose();
				}

			}
		});
		btnNewButton.setBounds(51, 240, 182, 47);
		frame.getContentPane().add(btnNewButton);

	}

}
