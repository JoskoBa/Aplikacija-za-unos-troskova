package com.expensemanagement.client;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client, Integer> {

	public Optional<Client> findByclientEmail(String email);
}
