public class ThreadReset extends Thread{
	public ThreadReset()
	{}
	
	public void run()
	{
		WordApp.score.resetScore();
      
		for(int i=0;i<WordPanel.words.length;i++){
			WordPanel.words[i].resetWord();
		}
      
		WordApp.set();
	}
}
