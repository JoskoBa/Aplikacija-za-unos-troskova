package com.expensemanagement.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

	@Autowired
	ClientRepository clientRepository;

	public void addClient(Client client) {
		clientRepository.save(client);
	}

	public List<Client> getAllClients() {
		List<Client> clients = new ArrayList<>();
		clientRepository.findAll().forEach(clients::add);

		return clients;
	}

	public String findClientByEmail(String email) {
		String password = new String();
		Optional<Client> client = clientRepository.findByclientEmail(email);

		if (client.isPresent()) {
			Client clientt = client.get();
			System.out.println(clientt.getClientPassword());
			password = clientt.getClientPassword();
			System.out.println(password);
			return password;

		} else {
			return null;
		}

	}

	public Client getClientByEmail(String email) {

		Client currentClient;
		Optional<Client> client = clientRepository.findByclientEmail(email);

		if (client.isPresent()) {
			currentClient = client.get();
			return currentClient;

		} else {
			return null;
		}

	}
}
