package ch.engenius.bank.services;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import ch.engenius.bank.model.Account;

import java.math.BigDecimal;

public class Bank {

	private static final String ACCOUNT_NOT_FOUND_ERROR_MESSAGE = "Account not found.";
	private static final String ACCOUNT_ALREADY_EXISTS_ERROR_MESSAGE = "An account with account number %s already exists.";
	private static final String NEGATIVE_AMOUNT_MESSAGE = "Amount cannot be negative.";
	private static final String NOT_ENOUGH_CREDIT_ERROR_MESSAGE = "Not enough credits on account.";

	private Map<Integer, Account> accounts = new HashMap<>();

	public synchronized Account registerAccount(int accountNumber, BigDecimal moneyAmount) {
		if (accounts.containsKey(accountNumber)) {
			throw new IllegalArgumentException(String.format(ACCOUNT_ALREADY_EXISTS_ERROR_MESSAGE, accountNumber));
		}
		validateMoneyAmount(moneyAmount);
		Account account = new Account(moneyAmount);
		accounts.put(accountNumber, account);
		return account;
	}

	public synchronized Account getAccount(int accountId) {
		Account account = accounts.get(accountId);
		if (account == null) {
			throw new NoSuchElementException(ACCOUNT_NOT_FOUND_ERROR_MESSAGE);
		}
		return account;
	}
	
	public synchronized void transferMoney(int sourceAccountNumber, int targetAccountNumber, BigDecimal transferAmount) {
		if (sourceAccountNumber == targetAccountNumber) {
			return;
		}
		Account sourceAccount = getAccount(sourceAccountNumber);
		Account targetAccount = getAccount(targetAccountNumber);
		try {
			Account sourceAccountAfterWithdraw = new Account(calculateWithdrawAmount(sourceAccount, transferAmount));
			Account targetAccountAfterDeposit = new Account(calculateDepositAmount(targetAccount, transferAmount));
			accounts.put(sourceAccountNumber, sourceAccountAfterWithdraw);
			accounts.put(targetAccountNumber, targetAccountAfterDeposit);
		} catch (Exception e) {
			accounts.put(sourceAccountNumber, sourceAccount);
			accounts.put(targetAccountNumber, targetAccount);
			throw e;
		}
	}
	
	private synchronized BigDecimal calculateWithdrawAmount(Account account, BigDecimal transferAmount) {
		validateMoneyAmount(transferAmount);
		validateWithdrawAmount(account, transferAmount);
		return account.getMoney().subtract(transferAmount);
	}

	private synchronized BigDecimal calculateDepositAmount(Account account, BigDecimal transferAmount) {
		validateMoneyAmount(transferAmount);
		return account.getMoney().add(transferAmount);
	}
	
	private void validateMoneyAmount(BigDecimal moneyAmount) {
		boolean isNegativeAmount = moneyAmount.compareTo(BigDecimal.ZERO) < 0;
		if (isNegativeAmount) {
			throw new IllegalStateException(NEGATIVE_AMOUNT_MESSAGE);
		}
	}
	
	private void validateWithdrawAmount(Account account, BigDecimal transferAmount) {
		BigDecimal moneyAmount = account.getMoney().subtract(transferAmount);
		boolean cannotWithdrawAmount = moneyAmount.compareTo(BigDecimal.ZERO) < 0;
		if (cannotWithdrawAmount) {
			throw new IllegalStateException(NOT_ENOUGH_CREDIT_ERROR_MESSAGE);
		}
	}
}
