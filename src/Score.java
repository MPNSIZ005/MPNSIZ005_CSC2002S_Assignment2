public class Score {
	private int missedWords;
	private volatile int caughtWords;
	private int gameScore;
	
	Score() {
		missedWords=0;
		caughtWords=0;
		gameScore=0;
	}
		
	// all getters and setters must be synchronized
	
	public synchronized int getMissed() {
		return missedWords;
	}

	public synchronized int getCaught() {
		return caughtWords;
	}
	
	public synchronized int getTotal() {
		return (missedWords+caughtWords);
	}

	public int getScore() {
		return gameScore;
	}
	
	public synchronized void missedWord() {
		missedWords++;
	}

	public void caughtWord(int length) {
		synchronized(this){
			caughtWords++;
			gameScore+=length;
		}
	}

	public synchronized void resetScore() {
		caughtWords=0;
		missedWords=0;
		gameScore=0;
	}
}
