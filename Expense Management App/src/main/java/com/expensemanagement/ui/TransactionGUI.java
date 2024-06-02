package com.expensemanagement.ui;


import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.client.RestTemplate;

import com.expensemanagement.category.Category;
import com.expensemanagement.transaction.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.awt.event.ActionEvent;
import com.toedter.calendar.JCalendar;
import javax.swing.JList;
import javax.swing.JScrollPane;

public class TransactionGUI {

	private static JList<Category> categoryList;
	private JFrame frame;
	private JTextField dateTxt;
	private JTextField amountTxt;
	private JTextField categoryTxt;
	private static RestTemplate restTemplate = new RestTemplate();
	private Transaction transaction;
	private int indCategory = -1;

	JCalendar calendar = new JCalendar();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TransactionGUI window = new TransactionGUI();
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
	public TransactionGUI() {
		initialize();
	}

	public void setVisible() {
		frame.setVisible(true);
	}

	static void categoriesFromDatabaseToList() {

		Category[] categories = restTemplate.getForObject("http://localhost:8080/category/all", Category[].class);

		List<Category> categoriesDatabase = new ArrayList<>();

		DefaultListModel<Category> model = new DefaultListModel<>();

		for (Category category : categories) {
			categoriesDatabase.add(category);
		}

		for (Category c : categoriesDatabase) {
			model.addElement(c);
		}
		categoryList.setModel(model);

	}

	private void transactionInformation() {
		try {
			transaction = restTemplate.getForObject(
					"http://localhost:8080/transaction/id/" + MainMenuGUI.getTransactionId(), Transaction.class);

			if (transaction != null) {
				dateTxt.setText(transaction.getTransactionDate());
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date date = dateFormat.parse(transaction.getTransactionDate());
				calendar.setDate(date);
				amountTxt.setText(String.valueOf(transaction.getTransactionAmount()));
				categoryTxt.setText(transaction.getTransactionCategory().getCategoryName().toUpperCase());

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 894, 410);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		dateTxt = new JTextField();
		dateTxt.setFont(new Font("Tahoma", Font.PLAIN, 16));
		dateTxt.setBounds(168, 22, 172, 40);
		frame.getContentPane().add(dateTxt);
		dateTxt.setColumns(10);

		amountTxt = new JTextField();
		amountTxt.setFont(new Font("Tahoma", Font.PLAIN, 16));
		amountTxt.setBounds(168, 99, 172, 40);
		amountTxt.setColumns(10);
		frame.getContentPane().add(amountTxt);

		categoryTxt = new JTextField();
		categoryTxt.setFont(new Font("Tahoma", Font.PLAIN, 16));
		categoryTxt.setBounds(168, 176, 172, 40);
		categoryTxt.setColumns(10);
		frame.getContentPane().add(categoryTxt);

		JLabel lblNewLabel = new JLabel("DATE");
		lblNewLabel.setBounds(25, 22, 110, 40);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		frame.getContentPane().add(lblNewLabel);

		JLabel lblIznos = new JLabel("AMOUNT");
		lblIznos.setBounds(25, 101, 95, 37);
		lblIznos.setFont(new Font("Tahoma", Font.BOLD, 16));
		frame.getContentPane().add(lblIznos);

		JLabel lblKategorija = new JLabel("CATEGORY");
		lblKategorija.setBounds(10, 178, 155, 37);
		lblKategorija.setFont(new Font("Tahoma", Font.BOLD, 16));
		frame.getContentPane().add(lblKategorija);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(626, 35, 198, 246);
		frame.getContentPane().add(scrollPane);

		categoryList = new JList<>();
		categoryList.setFont(new Font("Tahoma", Font.PLAIN, 16));
		categoryList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					indCategory = categoryList.getSelectedIndex();
					categoryTxt.setText(categoryList.getSelectedValue().getCategoryName());
				}
			}
		});
		scrollPane.setViewportView(categoryList);

		calendar.getYearChooser().getSpinner().setFont(new Font("Tahoma", Font.PLAIN, 16));
		calendar.getMonthChooser().getSpinner().setFont(new Font("Tahoma", Font.PLAIN, 16));
		calendar.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				Date odabraniDatum = calendar.getDate();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				dateTxt.setText(dateFormat.format(odabraniDatum));

			}
		});

		calendar.setBounds(389, 35, 203, 153);
		frame.getContentPane().add(calendar);

		transactionInformation();
		categoriesFromDatabaseToList();

		JButton btnNewButton = new JButton("UPDATE");
		btnNewButton.setBounds(168, 243, 172, 56);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String date = dateTxt.getText();

				Category category;
				if (indCategory != -1) {
					category = categoryList.getModel().getElementAt(indCategory);
				} else {
					category = transaction.getTransactionCategory();
				}

				transaction.setTransactionDate(date);
				transaction.setTransactionAmount(Double.valueOf(amountTxt.getText()));
				transaction.setTransactionCategory(category);

				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);

				ObjectMapper mapper = new ObjectMapper();

				String json = "";

				try {
					json = mapper.writeValueAsString(transaction);
				} catch (JsonProcessingException e1) {
					e1.printStackTrace();
				}

				HttpEntity<String> entity = new HttpEntity<>(json, headers);

				RestTemplate restTemplate = new RestTemplate();
				String url = "http://localhost:8080/transaction";

				ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);

				if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
					JOptionPane.showMessageDialog(null, "Transaction is updated!");
				} else {
					JOptionPane.showMessageDialog(null, "Transaction not found!");
				}

				MainMenuGUI.transactionsFromDatabaseToTable();
				MainMenuGUI.updateDiagram();
				frame.dispose();
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 16));
		frame.getContentPane().add(btnNewButton);
	}
}
