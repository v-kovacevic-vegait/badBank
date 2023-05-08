package ch.engenius.bank;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.math.BigDecimal;

public class Bank {

	private static final String ACCOUNT_NOT_FOUND_ERROR_MESSAGE = "Account not found.";
	private static final String ACCOUNT_ALREADY_EXISTS_ERROR_MESSAGE = "An account with account number %s already exists.";

	private Map<Integer, Account> accounts = new HashMap<>();

	public synchronized Account registerAccount(int accountNumber, BigDecimal money) {
		if (accounts.containsKey(accountNumber)) {
			throw new IllegalArgumentException(String.format(ACCOUNT_ALREADY_EXISTS_ERROR_MESSAGE, accountNumber));
		}
		Account account = new Account(money);
		accounts.put(accountNumber, account);
		return account;
	}

	public synchronized Account getAccount(int number) {
		Account account = accounts.get(number);
		if (account == null) {
			throw new NoSuchElementException(ACCOUNT_NOT_FOUND_ERROR_MESSAGE);
		}
		return account;
	}
}
