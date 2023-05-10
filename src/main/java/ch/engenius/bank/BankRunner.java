package ch.engenius.bank;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.engenius.bank.model.Account;
import ch.engenius.bank.services.Bank;

public class BankRunner {

	private static final int NUMBER_OF_ACCOUNS = 100;
	private static final int DEFAULT_DEPOSIT_VALUE = 1000;
	private static final int NUMBER_OF_ITERATIONS = 10000;
	private static final String SANITY_CHECK_FAILED_MESSAGE = "We got %s != %s (expected).";
	private static final String SANITY_CHECK_OK_MESSAGE = "Sanity check OK.";

	private static final ExecutorService executor = Executors.newFixedThreadPool(8);
	private static final Logger logger = LoggerFactory.getLogger(BankRunner.class);

	private final Random random = new Random(43);
	private final Bank bank = new Bank();
	
	public static void main(String[] args) {
		BankRunner runner = new BankRunner();
		int accounts = NUMBER_OF_ACCOUNS;
		int defaultDeposit = DEFAULT_DEPOSIT_VALUE;
		int iterations = NUMBER_OF_ITERATIONS;
		int totalExpectedMoney = accounts * defaultDeposit;
		runner.registerAccounts(accounts, defaultDeposit);
		runner.sanityCheck(accounts, totalExpectedMoney);
		runner.runBank(iterations, accounts);
		runner.sanityCheck(accounts, totalExpectedMoney);
	}

	private void runBank(int iterations, int maxAccount) {
		for (int i = 0; i < iterations; i++) {
			executor.submit(() -> runRandomOperation(maxAccount));
		}
		try {
			executor.shutdown();
			executor.awaitTermination(100, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void runRandomOperation(int maxAccount) {
		BigDecimal transferAmount = generateTransferAmount();
		int sourceAccountNumber = random.nextInt(maxAccount);
		int targetAccountNumber = random.nextInt(maxAccount);
		bank.transferMoney(sourceAccountNumber, targetAccountNumber, transferAmount);
	}

	private BigDecimal generateTransferAmount() {
		return BigDecimal.valueOf(random.nextDouble()).multiply(BigDecimal.valueOf(100.0));
	}

	private void registerAccounts(int number, int defaultMoney) {
		BigDecimal defaultMoneyConverted = BigDecimal.valueOf(defaultMoney);
		for (int i = 0; i < number; i++) {
			bank.registerAccount(i, defaultMoneyConverted);
		}
	}

	private void sanityCheck(int accountMaxNumber, int totalExpectedMoney) {
		BigDecimal sum = IntStream.range(0, accountMaxNumber).mapToObj(bank::getAccount).map(Account::getMoney)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		if (sum.intValue() != totalExpectedMoney) {
			String errorMessage = String.format(SANITY_CHECK_FAILED_MESSAGE, sum, totalExpectedMoney);
			logger.error(errorMessage);
			throw new IllegalStateException(errorMessage);
		}
		logger.debug(SANITY_CHECK_OK_MESSAGE);
	}
}
