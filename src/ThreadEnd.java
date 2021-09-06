
public class ThreadEnd extends Thread{
	public ThreadEnd()
	{}
	
	public void run()
	{
		WordApp.score.resetScore();
		for(int i=0;i<WordPanel.words.length;i++)
		{
			WordPanel.words[i].resetWord();
			ControlThread.setFalse();
		}
		WordApp.set();
	}
}
