package bg.sofia.uni.fmi.mjt.revolut.card;

import java.time.LocalDate;

public class VirtualPermanentCard implements Card {
	private String number;
	private int pin;
	private LocalDate expirationDate;
	private boolean isBlocked;
	private int pinAttempts;
	
	public VirtualPermanentCard(String number, int pin, LocalDate expirationDate) {
		this.number = number;
		this.pin = pin;
		this.expirationDate = expirationDate;
		this.isBlocked = false;
		this.setPinAttempts(0);
	}
	
	public VirtualPermanentCard() {
		this.number = "";
		this.pin = 0;
		this.expirationDate = LocalDate.now();
		this.isBlocked = false;
		this.setPinAttempts(0);
	}

	@Override
	public String getType() {
		return "VIRTUALPERMANENT";
	}

	@Override
	public LocalDate getExpirationDate() {
		return expirationDate;
	}

	@Override
	public boolean checkPin(int pin) {
		return this.pin == pin;
	}

	@Override
	public boolean isBlocked() {
		return this.isBlocked;
	}

	@Override
	public void block() {
		isBlocked = true;
	}
	
	@Override
	public int getPinAttempts() {
		return pinAttempts;
	}

	@Override
	public void setPinAttempts(int pinAttempts) {
		this.pinAttempts = pinAttempts;
	}

	@Override
	public String getNumber() {
		return number;
	}

	@Override
	public boolean isUsable() {
		return true;
	}

	@Override
	public void increaseNumberOfPayments() {

	}
}
