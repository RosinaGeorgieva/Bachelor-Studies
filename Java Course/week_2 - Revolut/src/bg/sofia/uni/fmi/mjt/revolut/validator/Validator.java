package bg.sofia.uni.fmi.mjt.revolut.validator;

import java.time.LocalDate;

import bg.sofia.uni.fmi.mjt.revolut.Revolut;
import bg.sofia.uni.fmi.mjt.revolut.account.Account;
import bg.sofia.uni.fmi.mjt.revolut.card.Card;
import bg.sofia.uni.fmi.mjt.revolut.card.PhysicalCard;

public class Validator {
	static final String UNTRUSTWORTHY_DOMAIN = ".biz";
	
	public static boolean isValidPhysicalPayment(Card card, int pin, double amount, String currency) {
		return isValidPayment(card, pin, amount, currency) && canPayByPOS(card);
	}
	
	public static boolean isValidOnlinePayment(Card card, int pin, double amount, String currency, String shopURL) {
		return isValidPayment(card, pin, amount, currency) && isValidURL(shopURL);
	}
	
	public static boolean isValidTransfer(Account from, Account to, double amount) {
		return existsInRevolut(from) && existsInRevolut(to) && amountIsEnough(from, amount) && areDifferent(from, to);
	}
	
	public static boolean isValidAddition(Account account, double amount) {
		return existsInRevolut(account);
	}
	
	public static boolean amountIsEnough(Account account, double amount) {
		return account.getAmount() >= amount && amount > 0;
	}
	
	private static boolean isValidPayment(Card card, int pin, double amount, String currency) {
		return existsInRevolut(card) && isNotBlocked(card) && isNotExpired(card) && isUsable(card) && pinIsCorrect(card, pin);
	}
	
	private static boolean existsInRevolut(Account account) {
		for(Account element : Revolut.getAccounts()) {
			if(element.getIBAN().equals(account.getIBAN())) {
				return true;
			}
		}
		return false;
	}

	private static boolean isNotBlocked(Card card) {
		return !card.isBlocked();
	}
	
	private static boolean isNotExpired(Card card) {
		return card.getExpirationDate().isAfter(LocalDate.now()) || card.getExpirationDate().isEqual(LocalDate.now());
	}
	
	private static boolean isUsable(Card card) {
		return card.isUsable();
	}
	
	private static boolean pinIsCorrect(Card card, int pin) {
		if(card.checkPin(pin) == false) {
			card.setPinAttempts(card.getPinAttempts() + 1);
			if(card.getPinAttempts() == 3) {
				card.block();
			}
			return false;
		} 
		card.setPinAttempts(0);
		return true;
	}
	
	private static boolean canPayByPOS(Card card) {
		return card.getType().equals(new PhysicalCard().getType());
	}
	
	private static boolean isValidURL(String shopURL) {
		return !shopURL.endsWith(UNTRUSTWORTHY_DOMAIN);
	}
	
	private static boolean existsInRevolut(Card card) {
		for(Card element : Revolut.getCards()) {
			if(element.getNumber().equals(card.getNumber())) {
				return true;
			}
		}
		return false;
	}
	
	private static boolean areDifferent(Account from, Account to) {
		return !from.getIBAN().equals(to.getIBAN());
	}
}
