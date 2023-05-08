package unit;

import ch.engenius.bank.Account;
import ch.engenius.bank.Bank;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BankTests {

	private static final int ACCOUNT_NUMBER = 1;
	private static final BigDecimal INITIAL_AMOUNT = BigDecimal.valueOf(1000.0);
	private static final BigDecimal NEGATIVE_AMOUNT = BigDecimal.valueOf(-100.0);

	private Bank bank;

	@BeforeEach
	public void setUp() {
		bank = new Bank();
	}

	@Test
	public void testRegisterAccount_ValidAmount() {
		Account account = bank.registerAccount(ACCOUNT_NUMBER, INITIAL_AMOUNT);

		assertEquals(INITIAL_AMOUNT, account.getMoney());
	}

	@Test
	public void testRegisterAccount_InvalidAmount() {
		assertThrows(IllegalStateException.class, () -> bank.registerAccount(ACCOUNT_NUMBER, NEGATIVE_AMOUNT));
	}

	@Test
	public void testRegisterAccount_AlreadyExists() {
		bank.registerAccount(ACCOUNT_NUMBER, INITIAL_AMOUNT);

		assertThrows(IllegalArgumentException.class, () -> {
			bank.registerAccount(ACCOUNT_NUMBER, INITIAL_AMOUNT);
		});
	}

	@Test
	public void testGetAccount_ExistingAccount() {
		bank.registerAccount(ACCOUNT_NUMBER, INITIAL_AMOUNT);

		Account account = bank.getAccount(ACCOUNT_NUMBER);

		assertEquals(INITIAL_AMOUNT, account.getMoney());
	}

	@Test
	public void testGetAccount_NonExistingAccount() {
		assertThrows(NoSuchElementException.class, () -> bank.getAccount(ACCOUNT_NUMBER));
	}
}
