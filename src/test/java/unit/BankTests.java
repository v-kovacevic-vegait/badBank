package unit;

import ch.engenius.bank.model.Account;
import ch.engenius.bank.services.Bank;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertAll;

public class BankTests {

	private static final int ACCOUNT_NUMBER = 1;
	private static final int OTHER_ACCOUNT_NUMBER = 2;
	private static final BigDecimal DEFAULT_AMOUNT = BigDecimal.valueOf(1000.0);
	private static final BigDecimal NEGATIVE_AMOUNT = BigDecimal.valueOf(-100.0);
	private static final BigDecimal VALID_TRANSFER_AMOUNT = BigDecimal.valueOf(500.0);
	private static final BigDecimal TOO_BIG_TRANSFER_AMOUNT = BigDecimal.valueOf(1500.0);

	private Bank bank;

	@BeforeEach
	public void setUp() {
		bank = new Bank();
	}

	@Test
	public void testRegisterAccount_ValidAmount() {
		Account account = bank.registerAccount(ACCOUNT_NUMBER, DEFAULT_AMOUNT);

		assertEquals(DEFAULT_AMOUNT, account.getMoney());
	}

	@Test
	public void testRegisterAccount_InvalidAmount() {
		assertThrows(IllegalStateException.class, () -> bank.registerAccount(ACCOUNT_NUMBER, NEGATIVE_AMOUNT));
	}

	@Test
	public void testRegisterAccount_AlreadyExists() {
		bank.registerAccount(ACCOUNT_NUMBER, DEFAULT_AMOUNT);

		assertThrows(IllegalArgumentException.class, () -> {
			bank.registerAccount(ACCOUNT_NUMBER, DEFAULT_AMOUNT);
		});
	}

	@Test
	public void testGetAccount_ExistingAccount() {
		bank.registerAccount(ACCOUNT_NUMBER, DEFAULT_AMOUNT);

		Account account = bank.getAccount(ACCOUNT_NUMBER);

		assertEquals(DEFAULT_AMOUNT, account.getMoney());
	}

	@Test
	public void testGetAccount_NonExistingAccount() {
		assertThrows(NoSuchElementException.class, () -> bank.getAccount(ACCOUNT_NUMBER));
	}

	@Test
	void testTransferMoney_validAccounts_success() {
		bank.registerAccount(ACCOUNT_NUMBER, DEFAULT_AMOUNT);
		bank.registerAccount(OTHER_ACCOUNT_NUMBER, DEFAULT_AMOUNT);

		bank.transferMoney(ACCOUNT_NUMBER, OTHER_ACCOUNT_NUMBER, VALID_TRANSFER_AMOUNT);

		Account sourceAccount = bank.getAccount(ACCOUNT_NUMBER);
		Account targetAccount = bank.getAccount(OTHER_ACCOUNT_NUMBER);
		assertAll(() -> assertEquals(DEFAULT_AMOUNT.subtract(VALID_TRANSFER_AMOUNT), sourceAccount.getMoney()),
				() -> assertEquals(DEFAULT_AMOUNT.add(VALID_TRANSFER_AMOUNT), targetAccount.getMoney()));
	}
	
	@Test
	void testTransferMoney_invalidSourceAccount_throwsException() {
		bank.registerAccount(ACCOUNT_NUMBER, DEFAULT_AMOUNT);
		bank.registerAccount(OTHER_ACCOUNT_NUMBER, DEFAULT_AMOUNT);

		assertThrows(NoSuchElementException.class,
				() -> bank.transferMoney(0, OTHER_ACCOUNT_NUMBER, VALID_TRANSFER_AMOUNT));
	}

	@Test
	void testTransferMoney_invalidTargetAccount_throwsException() {
		bank.registerAccount(ACCOUNT_NUMBER, DEFAULT_AMOUNT);
		bank.registerAccount(OTHER_ACCOUNT_NUMBER, DEFAULT_AMOUNT);

		assertThrows(NoSuchElementException.class, () -> bank.transferMoney(ACCOUNT_NUMBER, 0, VALID_TRANSFER_AMOUNT));
	}

	@Test
	void testTransferMoney_sameAccount_noChange() {
		bank.registerAccount(ACCOUNT_NUMBER, DEFAULT_AMOUNT);

		bank.transferMoney(ACCOUNT_NUMBER, ACCOUNT_NUMBER, VALID_TRANSFER_AMOUNT);

		Account sourceAccount = bank.getAccount(ACCOUNT_NUMBER);
		assertEquals(DEFAULT_AMOUNT, sourceAccount.getMoney());
	}

	@Test
	void testTransferMoney_insufficientBalance_throwsException() {
		bank.registerAccount(ACCOUNT_NUMBER, DEFAULT_AMOUNT);
		bank.registerAccount(OTHER_ACCOUNT_NUMBER, DEFAULT_AMOUNT);

		assertThrows(IllegalStateException.class,
				() -> bank.transferMoney(ACCOUNT_NUMBER, OTHER_ACCOUNT_NUMBER, TOO_BIG_TRANSFER_AMOUNT));

		Account sourceAccount = bank.getAccount(ACCOUNT_NUMBER);
		Account targetAccount = bank.getAccount(OTHER_ACCOUNT_NUMBER);
		assertAll(() -> assertEquals(DEFAULT_AMOUNT, sourceAccount.getMoney()),
				() -> assertEquals(DEFAULT_AMOUNT, targetAccount.getMoney()));
	}

	@Test
	void testTransferMoney_negativeTransferAmount_throwsException() {
		bank.registerAccount(ACCOUNT_NUMBER, DEFAULT_AMOUNT);
		bank.registerAccount(OTHER_ACCOUNT_NUMBER, DEFAULT_AMOUNT);

		assertThrows(IllegalStateException.class,
				() -> bank.transferMoney(ACCOUNT_NUMBER, OTHER_ACCOUNT_NUMBER, NEGATIVE_AMOUNT));
	}
}
