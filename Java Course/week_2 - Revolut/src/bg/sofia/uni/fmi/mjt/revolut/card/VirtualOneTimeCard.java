package bg.sofia.uni.fmi.mjt.revolut.card;

import java.time.LocalDate;

public class VirtualOneTimeCard implements Card {
	private String number;
	private int pin;
	private LocalDate expirationDate;
	private boolean isBlocked;
	private int pinAttempts;
	private int numberOfPayments;
	
	public VirtualOneTimeCard(String number, int pin, LocalDate expirationDate) {
		this.number = number;
		this.pin = pin;
		this.expirationDate = expirationDate;
		this.isBlocked = false;
		this.setPinAttempts(0);
		this.numberOfPayments = 0;
	}

	public VirtualOneTimeCard() {
		this.number = "";
		this.pin = 0;
		this.expirationDate = LocalDate.now();
		this.isBlocked = false;
		this.setPinAttempts(0);
		this.numberOfPayments = 0;
	}
	
	@Override
	public String getType() {
		return "VIRTUALONETIME";
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
		return isBlocked;
	}

	@Override
	public void block() {
		isBlocked = true;
	}

	@Override
	public boolean isUsable() {
		return numberOfPayments == 0;
	}

	@Override
	public void increaseNumberOfPayments() {
		numberOfPayments += 1;
	}

	@Override
	public int getPinAttempts() {
		// TODO Auto-generated method stub
		return this.pinAttempts;
	}

	@Override
	public void setPinAttempts(int attempts) {
		this.pinAttempts = attempts;
		
	}

	@Override
	public String getNumber() {
		return this.number;
	}
}
