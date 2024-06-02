package com.expensemanagement.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientController {

	@Autowired
	ClientService clientService = new ClientService();

	@RequestMapping(method = RequestMethod.POST, value = "/client")
	public ResponseEntity<String> addClient(@RequestBody Client client) {
		try {
			clientService.addClient(client);
			return ResponseEntity.status(HttpStatus.CREATED).body("Client is regirested!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error!");
		}
	}

	@GetMapping("/client/all")
	public List<Client> getAllClients() {
		return clientService.getAllClients();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/client/{email}")
	public String findClientByEmail(@PathVariable String email) {
		return clientService.findClientByEmail(email);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/client/email/{email}")
	public Client getClientByEmail(@PathVariable String email) {
		return clientService.getClientByEmail(email);
	}
}
