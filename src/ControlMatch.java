public class ControlMatch implements Runnable{	
	private WordRecord[] words;
	private int noWords;
    
	String text="";
	
	public ControlMatch(WordRecord[] words,int noWords,String text)
	{   
		this.words=words;
		this.noWords=words.length;
		this.text=text;
	}
	
	public void run() {
		for(int i=0;i<noWords;i++)
		{
			if(words[i].matchWord(text)==true)
			{ 
				WordApp.score.caughtWord(text.length());	

				WordApp.set2();
			}
		}
	}
}
