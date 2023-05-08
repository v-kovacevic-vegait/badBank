package ch.engenius.bank;

import java.math.BigDecimal;

public class Account {

	private static final String NOT_ENOUGH_CREDIT_ERROR_MESSAGE = "Not enough credits on account.";
	private static final String NEGATIVE_AMOUNT_MESSAGE = "Amount cannot be negative.";

	private BigDecimal money;

	public Account(BigDecimal money) {
		boolean isNegativeAmount = isNegativeAmount(money);
		if (isNegativeAmount) {
			throw new IllegalStateException(NEGATIVE_AMOUNT_MESSAGE);
		}
		this.money = money;
	}

	public synchronized void withdraw(BigDecimal transferMoney) {
		boolean isNegativeAmount = isNegativeAmount(transferMoney);
		if (isNegativeAmount) {
			throw new IllegalStateException(NEGATIVE_AMOUNT_MESSAGE);
		}
		boolean cannotWithdrawAmount = cannotWithdrawAmount(transferMoney);
		if (cannotWithdrawAmount) {
			throw new IllegalStateException(NOT_ENOUGH_CREDIT_ERROR_MESSAGE);
		}
		money = money.subtract(transferMoney);
	}

	public synchronized void deposit(BigDecimal transferMoney) {
		boolean isNegativeAmount = isNegativeAmount(transferMoney);
		if (isNegativeAmount) {
			throw new IllegalStateException(NEGATIVE_AMOUNT_MESSAGE);
		}
		money = money.add(transferMoney);
	}

	public synchronized BigDecimal getMoney() {
		return money;
	}

	private boolean cannotWithdrawAmount(BigDecimal transferMoney) {
		BigDecimal amount = money.subtract(transferMoney);
		return amount.compareTo(BigDecimal.ZERO) < 0;
	}

	private boolean isNegativeAmount(BigDecimal transferMoney) {
		return transferMoney.compareTo(BigDecimal.ZERO) < 0;
	}
}
