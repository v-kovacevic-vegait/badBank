package unit;

import ch.engenius.bank.Account;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AccountTests {

	private static final BigDecimal DEFAULT_AMOUNT = BigDecimal.valueOf(1000.0);
	private static final BigDecimal NEGATIVE_AMOUNT = BigDecimal.valueOf(-100.0);
	private static final BigDecimal VALID_TRANSFER_AMOUNT = BigDecimal.valueOf(500.0);
	private static final BigDecimal TOO_BIG_TRANSFER_AMOUNT = BigDecimal.valueOf(1500.0);

	private Account account;

	@BeforeEach
	public void setUp() {
		account = new Account(DEFAULT_AMOUNT);
	}

	@Test
	public void testWithdraw_ValidAmount() {
		BigDecimal expectedMoney = DEFAULT_AMOUNT.subtract(VALID_TRANSFER_AMOUNT);

		account.withdraw(VALID_TRANSFER_AMOUNT);

		assertEquals(expectedMoney, account.getMoney());
	}

	@Test
	public void testWithdraw_NegativeAmount() {
		assertThrows(IllegalStateException.class, () -> {
			account.withdraw(NEGATIVE_AMOUNT);
		});
	}

	@Test
	public void testWithdraw_NotEnoughCredit() {
		assertThrows(IllegalStateException.class, () -> {
			account.withdraw(TOO_BIG_TRANSFER_AMOUNT);
		});
	}

	@Test
	public void testDeposit_ValidAmount() {
		BigDecimal expectedMoney = DEFAULT_AMOUNT.add(VALID_TRANSFER_AMOUNT);

		account.deposit(VALID_TRANSFER_AMOUNT);

		assertEquals(expectedMoney, account.getMoney());
	}

	@Test
	void testDeposit_NegativeAmount() {
		assertThrows(IllegalStateException.class, () -> account.deposit(NEGATIVE_AMOUNT));
	}
}
