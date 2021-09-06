import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class WordPanel extends JPanel implements Runnable {
		public static volatile boolean done;
		static WordRecord[] words;
		private int noWords;
		private static int maxY;
		static boolean check=true;
       
		Thread t;
		
		public void paintComponent(Graphics g) {
		    int width = getWidth();
		    int height = getHeight();
		    g.clearRect(0,0,width,height);
		    g.setColor(Color.red);
		    g.fillRect(0,maxY-10,width,height);

		    g.setColor(Color.black);
		    g.setFont(new Font("Helvetica", Font.PLAIN, 26));
		   //draw the words
		   //animation must be added 
		    for (int i=0;i<noWords;i++){	    	
		    	g.drawString(words[i].getWord(),words[i].getX(),words[i].getY());	
			if(words[i].getY()==maxY)
			{
			  words[i].resetWord();
			  WordApp.score.missedWord();
			  WordApp.set3();
			}
		    }
		  }
		
		WordPanel(JLabel missed) {
			this(words,maxY);			
		}
	
		WordPanel(WordRecord[] words, int maxY) {
			this.words=words; //will this work?
			noWords = words.length;
			done=false;
			this.maxY=maxY;		
		}
	    
		int count=WordApp.totalWords;
	
		public synchronized void count(){
			count--;
		}
		
		int a=0;
		public void run() {
			for(int i=0;i<words.length;i++){  
					t= new Thread(new ControlThread(words[i],check));
					t.start();
			}
			
			while(WordApp.score.getTotal()<=WordApp.totalWords){
				if(WordApp.score.getTotal()==WordApp.totalWords && a==0){
					WordApp.GameOver();
					a++;
				}
				repaint();
			}
		}
	}
