package com.expensemanagement.transaction;

import java.io.ByteArrayInputStream;

import java.io.ByteArrayOutputStream;

import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expensemanagement.category.Category;
import com.expensemanagement.category.CategoryRepository;
import com.expensemanagement.client.Client;
import com.expensemanagement.client.ClientRepository;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.kernel.pdf.PdfDocument;

@Service
public class TransactionService {

	@Autowired
	TransactionRepository transactionRepository;
	@Autowired
	CategoryRepository categoryRepository;
	@Autowired
	ClientRepository clientRepository;

	/*
	 * public List<Transakcija> getSveTransakcije() { List<Transakcija> transakcije
	 * = new ArrayList<>();
	 * transakcijaRepository.findAll().forEach(transakcije::add);
	 * 
	 * return transakcije; }
	 * 
	 * public List<Transakcija> getTransakcijeById(int id) { return
	 * transakcijaRepository.findByKorisnikId(id); }
	 * 
	 */

	public List<Transaction> getAllTransactions() {
		return (List<Transaction>) transactionRepository.findAll();
	}

	public List<Transaction> getTransactionsById(int id) {
		return getAllTransactions().stream().filter(transakcija -> transakcija.getTransactionClient().getClientId() == id)
				.collect(Collectors.toList());
	}

	public void addTransaction(Transaction transaction) {

		Category category = categoryRepository.findById(transaction.getTransactionCategory().getCategoryId()).orElse(null);
		Client client = clientRepository.findByclientEmail(transaction.getTransactionClient().getClientEmail()).orElse(null);

		if (category == null) {

			throw new IllegalArgumentException("Category does not exist!!");
		}

		transaction.setTransactionCategory(category);
		transaction.setTransactionClient(client);

		transactionRepository.save(transaction);

	}

	public Transaction getTransactionById(int id) {

		Transaction transaction;
		Optional<Transaction> transactionDatabase = transactionRepository.findById(id);

		if (transactionDatabase.isPresent()) {
			transaction = transactionDatabase.get();
			return transaction;

		} else {
			return null;
		}

	}

	public void deleteTransaction(int id) {
		transactionRepository.deleteById(id);
	}

	public ByteArrayInputStream generatePdf(List<Transaction> transactions) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {

			PdfWriter writer = new PdfWriter(out);

			PdfDocument pdf = new PdfDocument(writer);

			Document document = new Document(pdf, PageSize.A4);

			Paragraph title = new Paragraph("TRANSACTION REPORT").setTextAlignment(TextAlignment.CENTER)
					.setFontSize(20).setBold();

			document.add(title);

			float marginWidth = 72;
			float pageWidth = PageSize.A4.getWidth();

			float tableWidth = pageWidth - 2 * marginWidth;

			float[] columnWidths = { tableWidth * 0.05f, tableWidth * 0.30f, tableWidth * 0.30f, tableWidth * 0.35f };

			Table table = new Table(columnWidths);

			table.addCell(new Cell().add(new Paragraph("ID")).setHeight(20f).setBold()
					.setTextAlignment(TextAlignment.CENTER));
			table.addCell(new Cell().add(new Paragraph("DATE")).setHeight(20f).setBold()
					.setTextAlignment(TextAlignment.CENTER));

			table.addCell(new Cell().add(new Paragraph("EXPENSE")).setHeight(20f).setBold()
					.setTextAlignment(TextAlignment.CENTER));
			table.addCell(new Cell().add(new Paragraph("CATEGORY")).setHeight(20f).setBold()
					.setTextAlignment(TextAlignment.CENTER));

			for (Transaction transaction : transactions) {

				table.addCell(new Cell().add(
						new Paragraph(String.valueOf(transaction.getTransactionId())).setTextAlignment(TextAlignment.CENTER)));
				table.addCell(
						new Cell().add(new Paragraph(transaction.getTransactionDate()).setTextAlignment(TextAlignment.CENTER)));

				double amount = transaction.getTransactionAmount();
				if (transaction.getTransactionCategory().getCategoryType().equals("EXPENSE")) {
					amount *= -1;
				}
				table.addCell(
						new Cell().add(new Paragraph(String.valueOf(amount)).setTextAlignment(TextAlignment.CENTER)));
				table.addCell(new Cell().add(new Paragraph(String.valueOf(transaction.getTransactionCategory()))
						.setTextAlignment(TextAlignment.CENTER)));
				
				
			}
			
			double totalIncome = 0;
			double totalExpense = 0;

			
			for (Transaction transaction : transactions) {
			    double amount = transaction.getTransactionAmount();
			    if (transaction.getTransactionCategory().getCategoryType().equals("INCOME")) {
			        totalIncome += amount;
			    } else if (transaction.getTransactionCategory().getCategoryType().equals("EXPENSE")) {
			        totalExpense += amount;
			    }
			}

			double total = totalIncome - totalExpense;
	
			
			table.addCell(new Cell(1, 2).add(new Paragraph("TOTAL:")).setTextAlignment(TextAlignment.CENTER));
			table.addCell(new Cell().add(new Paragraph(String.valueOf(total)).setTextAlignment(TextAlignment.CENTER)));
			
			table.setFontSize(10);

			document.add(table);
			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ByteArrayInputStream(out.toByteArray());
	}

}
