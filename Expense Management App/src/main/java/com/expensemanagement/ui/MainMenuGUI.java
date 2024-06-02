package com.expensemanagement.ui;

import java.awt.Color;



import java.awt.EventQueue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.swing.event.ListSelectionListener;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JButton;
import javax.swing.JComponent;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;
import javax.swing.JTable;

import com.expensemanagement.category.Category;
import com.expensemanagement.client.Client;
import com.expensemanagement.transaction.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.io.IOException;
import com.toedter.calendar.JCalendar;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.nio.file.Paths;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.JPanel;

public class MainMenuGUI{

	private JFrame frame;
	private static JList<Category> categoryList;
	private static RestTemplate restTemplate = new RestTemplate();
	private static int indCategory;
	private static JTable table;
	private JTextField transactionAmountTxt;
	private int indRow;
	private static int transactionId;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private JPanel diagram = new JPanel();
	private JFreeChart chart;
	private static ChartPanel diagramPanel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainMenuGUI window = new MainMenuGUI();
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
	public MainMenuGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	
	
	public void setVisible() {
		frame.setVisible(true);
	}

	public static int getTransactionId() {
		return transactionId;
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

	static void transactionsFromDatabaseToTable() {
		Transaction[] transactions = restTemplate.getForObject("http://localhost:8080/transaction/all",
				Transaction[].class);

		List<Transaction> transactionsDatabase = Arrays.asList(transactions);

		DefaultTableModel model = (DefaultTableModel) table.getModel();

		model.setRowCount(0);

		Client client = clientFromDatabase();

		for (Transaction transaction : transactionsDatabase) {
			if (transaction.getTransactionClient().getClientId() == client.getClientId()) {
				Object[] newRow = { String.valueOf(transaction.getTransactionId()), transaction.getTransactionDate(),
						String.valueOf(transaction.getTransactionAmount()),
						String.valueOf(transaction.getTransactionCategory()) };
				model.addRow(newRow);
			}

		}

	}

	private static Client clientFromDatabase() {
		try {
			Client client = restTemplate.getForObject("http://localhost:8080/client/email/" + LoginGUI.getEmail(),
					Client.class);

			if (client != null) {
				return client;
			} else {
				return null;
			}
		} catch (RestClientException e) {
			e.printStackTrace();
			return null;
		}

	}

	private void showMenu(int x, int y, JComponent component) {
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem menuItem = new JMenuItem("Delete");

		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (component instanceof JList) {
					Category category = categoryList.getModel().getElementAt(indCategory);
					String categoryName = category.getCategoryName();
					restTemplate.delete("http://localhost:8080/category/delete/" + categoryName);
					categoriesFromDatabaseToList();

				} else if (component instanceof JTable) {
					restTemplate.delete("http://localhost:8080/transaction/delete/" + transactionId);
					transactionsFromDatabaseToTable();
					updateDiagram();
				}
			}
		});

		popupMenu.add(menuItem);
		popupMenu.show(component, x, y);
	}

	private static JFreeChart createChart(PieDataset data) {
		JFreeChart chart = ChartFactory.createPieChart("Transaction diagram", data, true, true, false);
		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setSectionPaint("N", new Color(255, 200, 200));
		plot.setSectionPaint("", new Color(200, 255, 200));
		plot.setSectionPaint("", new Color(200, 200, 255));
		plot.setSectionPaint(" ", new Color(255, 255, 200));
		plot.setExplodePercent("", 0.10);
		plot.setSimpleLabels(true);

		return chart;
	}

	private static PieDataset createData(JTable table) {

		DefaultPieDataset data = new DefaultPieDataset();

		Map<String, Double> categoryMap = new HashMap<>();

		int columnCategory = 3;
		int columnAmount = 2;

		for (int i = 0; i < table.getRowCount(); i++) {
			String category = table.getValueAt(i, columnCategory).toString();
			double amount = Double.valueOf(table.getValueAt(i, columnAmount).toString());

			categoryMap.put(category, categoryMap.getOrDefault(category, 0.0) + amount);
		}

		for (Map.Entry<String, Double> entry : categoryMap.entrySet()) {
			data.setValue(entry.getKey(), entry.getValue());
		}

		return data;
	}

	static void updateDiagram() {
		PieDataset data = createData(table);
		JFreeChart chart = createChart(data);
		diagramPanel.setChart(chart);

	}

	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Expense Management");
		frame.setBounds(100, 100, 1179, 653);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(236, 50, 206, 153);
		frame.getContentPane().add(scrollPane);

		categoryList = new JList();
		categoryList.setFont(new Font("Tahoma", Font.PLAIN, 16));
		categoryList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					indCategory = categoryList.getSelectedIndex();
				}

			}
		});
		scrollPane.setViewportView(categoryList);

		categoryList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					int index = categoryList.locationToIndex(e.getPoint());

					categoryList.setSelectedIndex(index);
					showMenu(e.getX(), e.getY(), categoryList);
				}
			}
		});

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 324, 1143, 279);
		frame.getContentPane().add(scrollPane_1);

		table = new JTable(
				new DefaultTableModel(new Object[][] {}, new String[] { "ID", "DATE", "AMOUNT", "CATEGORY" }) {
					boolean[] columnEditables = new boolean[] { false, false, false, false, false, false };

					public boolean isCellEditable(int row, int column) {
						return columnEditables[column];
					}
				});
		table.setFont(new Font("Tahoma", Font.PLAIN, 16));

		TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
		((JLabel) headerRenderer).setHorizontalAlignment(JLabel.CENTER);
		((JLabel) headerRenderer).setFont(((JLabel) headerRenderer).getFont().deriveFont(Font.BOLD, 16));

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				if (e.getClickCount() == 1) {
					indRow = table.getSelectedRow();
					Object id = table.getValueAt(indRow, 0);
					transactionId = Integer.valueOf(id.toString());
				} else if (e.getClickCount() == 2) {
					indRow = table.getSelectedRow();

					Object id = table.getValueAt(indRow, 0);
					transactionId = Integer.valueOf(id.toString());

					TransactionGUI transaction = new TransactionGUI();
					transaction.setVisible();
				}

			}

		});

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					int row = table.rowAtPoint(e.getPoint());
					int column = table.columnAtPoint(e.getPoint());
					if (row >= 0 && column >= 0) {
						table.setRowSelectionInterval(row, row);
						table.setColumnSelectionInterval(column, column);
						showMenu(e.getX(), e.getY(), table);
					}
				}
			}
		});

		TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
		table.setRowSorter(sorter);

		scrollPane_1.setViewportView(table);

		JCalendar calendar = new JCalendar();
		calendar.getDayChooser().setFont(new Font("Tahoma", Font.PLAIN, 13));
		calendar.getYearChooser().getSpinner().setFont(new Font("Tahoma", Font.PLAIN, 16));
		calendar.getMonthChooser().getSpinner().setFont(new Font("Tahoma", Font.PLAIN, 16));
		calendar.getYearChooser().setFont(new Font("Tahoma", Font.PLAIN, 16));
		calendar.getMonthChooser().setFont(new Font("Tahoma", Font.PLAIN, 16));
		calendar.getDayChooser().getDayPanel().setFont(new Font("Tahoma", Font.PLAIN, 16));
		calendar.setBounds(10, 50, 216, 153);
		frame.getContentPane().add(calendar);

		JLabel lblNewLabel = new JLabel("AMOUNT");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel.setBounds(10, 212, 74, 23);
		frame.getContentPane().add(lblNewLabel);

		transactionAmountTxt = new JTextField();
		transactionAmountTxt.setFont(new Font("Tahoma", Font.PLAIN, 16));
		transactionAmountTxt.setBounds(10, 246, 198, 46);
		frame.getContentPane().add(transactionAmountTxt);
		transactionAmountTxt.setColumns(10);

		JMenuBar menu = new JMenuBar();
		menu.setBounds(0, 0, 1163, 27);
		frame.getContentPane().add(menu);
		JMenu clientMenu = new JMenu("Client");
		clientMenu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		clientMenu.setHorizontalAlignment(SwingConstants.LEFT);
		JMenu categoryMenu = new JMenu("Category");
		categoryMenu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		categoryMenu.setHorizontalAlignment(SwingConstants.LEFT);
		JMenu reportMenu = new JMenu("Report");
		reportMenu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		reportMenu.setHorizontalAlignment(SwingConstants.LEFT);

		JMenuItem updateClient = new JMenuItem("Update");
		updateClient.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		JMenuItem updateCategory = new JMenuItem("Add");
		updateCategory.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		JMenuItem generateReport = new JMenuItem("Transactions");
		generateReport.setFont(new Font("Segoe UI", Font.PLAIN, 14));

		clientMenu.add(updateClient);
		categoryMenu.add(updateCategory);
		reportMenu.add(generateReport);
		menu.add(clientMenu);
		menu.add(categoryMenu);
		menu.add(reportMenu);

		categoryMenu.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				categoryMenu.doClick();
			}
		});

		updateCategory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CategoryGUI category = new CategoryGUI();
				category.setVisible();
			}
		});

		clientMenu.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				clientMenu.doClick();
			}
		});

		updateClient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ClientGUI client = new ClientGUI();
				client.setVisible();
			}
		});

		reportMenu.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				reportMenu.doClick();
			}
		});

		generateReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {

					RestTemplate restTemplate = new RestTemplate();

					String url = "http://localhost:8080/transaction/" + clientFromDatabase().getClientId() + "/pdf";

					ResponseEntity<byte[]> responseEntity = restTemplate.getForEntity(url, byte[].class);

					if (responseEntity.getStatusCode().is2xxSuccessful()) {

						byte[] pdfBytes = responseEntity.getBody();

						Files.write(Paths.get("./transakcije_" + clientFromDatabase().getClientName() + ".pdf"),
								pdfBytes);

						JOptionPane.showMessageDialog(null, "PDF generated!");
					} else {

						JOptionPane.showMessageDialog(null, "Failed HTTP request!");
					}
				} catch (IOException | java.io.IOException ex) {
					ex.printStackTrace();

					JOptionPane.showMessageDialog(null, "PDF generation failed!");
				}
			}
		});

		categoriesFromDatabaseToList();
		transactionsFromDatabaseToTable();

		PieDataset data = createData(table);
		chart = createChart(data);
		diagramPanel = new ChartPanel(chart);

		diagram.setBounds(601, 50, 552, 263);
		diagram.addComponentListener(new java.awt.event.ComponentAdapter() {
			public void componentResized(java.awt.event.ComponentEvent evt) {
				diagramPanel.setPreferredSize(new java.awt.Dimension(diagram.getWidth(), diagram.getHeight()));
				diagram.revalidate();
			}
		});

		diagramPanel.setPreferredSize(new java.awt.Dimension(diagram.getWidth(), diagram.getHeight()));
		diagram.add(diagramPanel);
		frame.getContentPane().add(diagram);

		JButton btnNewButton_1 = new JButton("SAVE");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Date pickedDate = calendar.getDate();
				String date = dateFormat.format(pickedDate);

				double amount = Double.valueOf(transactionAmountTxt.getText());
				Category category = categoryList.getModel().getElementAt(indCategory);
				Client currentClient = clientFromDatabase();

				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);

				Transaction transaction = new Transaction(date, amount, category, currentClient);

				ObjectMapper mapper = new ObjectMapper();

				String json = new String();

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
				    JOptionPane.showMessageDialog(null, "Transaction is saved!");
				} else {
				    JOptionPane.showMessageDialog(null, "Transaction import failed!");
				}

				//JOptionPane.showMessageDialog(null, "Transaction is saved!");
				transactionsFromDatabaseToTable();
				updateDiagram();

			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnNewButton_1.setBounds(236, 226, 206, 66);
		frame.getContentPane().add(btnNewButton_1);

	}

}
