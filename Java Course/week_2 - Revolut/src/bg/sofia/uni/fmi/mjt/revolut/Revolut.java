package bg.sofia.uni.fmi.mjt.revolut;

import bg.sofia.uni.fmi.mjt.revolut.account.Account;
import bg.sofia.uni.fmi.mjt.revolut.card.Card;
import bg.sofia.uni.fmi.mjt.revolut.validator.Validator;

public class Revolut implements RevolutAPI {
	static final String BULGARIAN_CURRENCY = "BGN";
	static final String EUROPEAN_CURRENCY = "EUR";
	static final double EXCHANGE_RATE = 1.95583;
	static Account[] accounts;
	static Card[] cards;
	
	public Revolut(Account[] accounts, Card[] cards) {
		this.accounts = accounts.clone();
		this.cards = cards.clone();
	}
	
	@Override
	public boolean pay(Card card, int pin, double amount, String currency) {
		if(!Validator.isValidPhysicalPayment(card, pin, amount, currency)) {
			return false;
		}
		return pay(findByParamenters(amount, currency), amount, card);
	}

	@Override
	public boolean payOnline(Card card, int pin, double amount, String currency, String shopURL) {
		if(!Validator.isValidOnlinePayment(card, pin, amount, currency, shopURL)) {
			return false;
		}
		return pay(findByParamenters(amount, currency), amount, card);
	}

	@Override
	public boolean addMoney(Account account, double amount) {
		if(!Validator.isValidAddition(account, amount)) {
			return false;
		}
		account.setAmount(account.getAmount() + amount);
		return true;
	}

	@Override
	public boolean transferMoney(Account from, Account to, double amount) {
		if(! (Validator.isValidTransfer(from, to, amount))) {
			return false;
		}
		from.setAmount(from.getAmount() - amount);
		if(to.getCurrency().equals(from.getCurrency())) {
			to.setAmount(to.getAmount() + amount);
		} else if (to.getCurrency().equals(BULGARIAN_CURRENCY)) {
			to.setAmount(to.getAmount() + amount * EXCHANGE_RATE);
		} else {
			to.setAmount(to.getAmount() + amount / EXCHANGE_RATE);
		}
		return true;
	}

	@Override
	public double getTotalAmount() {
		double totalAmount = 0;
		for(Account account : accounts) {
			if(account.getCurrency().equals(EUROPEAN_CURRENCY)) {
				totalAmount += EXCHANGE_RATE * account.getAmount();
			} else {
				totalAmount += account.getAmount();
			}
		}
		return totalAmount;
	}
	
	public static Card[] getCards() {
		return cards;
	}

	public static Account[] getAccounts() {
		return accounts;
	}
	
	private boolean pay(Account account, double amount, Card card) {
		if(account == null) {
			return false;
		}
		account.setAmount(account.getAmount() - amount);
		card.increaseNumberOfPayments();
		return true;
	}
	
	private Account findByParamenters(double amount, String currency) {
		for(Account account : accounts) {
			if(account.getCurrency().equals(currency) && Validator.amountIsEnough(account, amount)) {
				return account;
			}
		}
		return null;
	}
}
