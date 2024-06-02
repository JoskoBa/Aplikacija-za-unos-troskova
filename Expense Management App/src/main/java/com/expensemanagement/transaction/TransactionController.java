package com.expensemanagement.transaction;

import java.io.ByteArrayInputStream;


import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {

	@Autowired
	private TransactionService transactionService;

	@RequestMapping(method = RequestMethod.POST, value = "/transaction")
	public ResponseEntity<String> addTransaction(@RequestBody Transaction transaction) {
		try {
			transactionService.addTransaction(transaction);
			return ResponseEntity.status(HttpStatus.CREATED).body("Transakcija je uspešno dodata.");
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Došlo je do greške pri dodavanju transakcije.");
		}
		
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/transaction/delete/{id}")
	public void obrisiTransakciju(@PathVariable int id) {
		transactionService.deleteTransaction(id);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/transaction/all")
	public List<Transaction> getAllTransactions() {
		return transactionService.getAllTransactions();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/transaction/id/{id}")
	public Transaction getTransactionById(@PathVariable int id) {
		return transactionService.getTransactionById(id);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/transaction/{id}/pdf")
	public ResponseEntity<InputStreamResource> exportTransactionsInPdf(@PathVariable int id) {
		List<Transaction> transactions = transactionService.getTransactionsById(id);

		ByteArrayInputStream bis = transactionService.generatePdf(transactions);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=transactions.pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bis));
	}
}
