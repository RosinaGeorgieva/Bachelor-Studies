public class SocialDistanceMaximizer {
	public static int maxDistance(int[] seats) {
		int lineLength = seats.length;
		int[] takenSeats = new int[lineLength];
		int numberOfTakenSeats = 0;
		for(int i = 0; i < lineLength; i++) {
			if(seats[i] == 1) {
				takenSeats[numberOfTakenSeats] = i;
				numberOfTakenSeats++;
			}
		}
		
		int maxDistance = 1;
		for(int i = 1; i < numberOfTakenSeats; i++) {
			maxDistance = Math.max(takenSeats[i] - takenSeats[i-1], maxDistance);
		}
		
		int leadingEmptySeats = takenSeats[0];
		int endingEmptySeats = lineLength - takenSeats[numberOfTakenSeats - 1] - 1;
		maxDistance /= 2;
		
		return Math.max(maxDistance, Math.max(leadingEmptySeats, endingEmptySeats));
	}
}
