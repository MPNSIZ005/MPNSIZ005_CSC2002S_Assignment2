public class ControlThread implements Runnable{
	WordRecord word;
	static boolean check;
	Thread t;
	public ControlThread(WordRecord word,boolean check)
	{
		this.word=word;
		this.check=check;
		
	}
   
	public static void setFalse()
	{
		check=false;
	}
	public void run() {
		while(check) 	
		{     
				word.drop(20*word.getSpeed()/500);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				e.printStackTrace();
			}	
		}
	}
}
