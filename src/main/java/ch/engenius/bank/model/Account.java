package ch.engenius.bank.model;

import java.math.BigDecimal;

public class Account {

	private BigDecimal money;

	public Account(BigDecimal money) {
		this.money = money;
	}

	public synchronized BigDecimal getMoney() {
		return money;
	}

	public synchronized void setMoney(BigDecimal money) {
		this.money = money;
	}
}
