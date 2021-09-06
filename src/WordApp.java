
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import java.util.Scanner;
import java.util.concurrent.*;
//model is separate

public class WordApp {
//shared variables
	static int noWords=4;
	static int totalWords;

   	static int frameX=1000;
	static int frameY=600;
	static int yLimit=480;

	static WordDictionary dict = new WordDictionary(); //use default dictionary, to read from file eventually

	static WordRecord[] words;
	static volatile boolean done;  //must be volatile
	static 	Score score = new Score();
    static String text="";
	static WordPanel w;
	static WordPanel w2;
    static  JLabel missed;
	static ControlMatch m;
	 static JLabel caught;
	 static JLabel scr;
	 static JButton reset;
	 static JButton startB;
	
	public static void setupGUI(int frameX,int frameY,int yLimit) {
		// Frame init and dimensions
    	JFrame frame = new JFrame("WordGame"); 
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setSize(frameX, frameY);
    	
      	JPanel g = new JPanel();
        g.setLayout(new BoxLayout(g, BoxLayout.PAGE_AXIS)); 
      	g.setSize(frameX,frameY);
 
		w = new WordPanel(words,yLimit);
		w.setSize(frameX,yLimit+100);
	    g.add(w);
	    	    
	    JPanel txt = new JPanel();
	    txt.setLayout(new BoxLayout(txt, BoxLayout.LINE_AXIS)); 
	    caught =new JLabel("Caught: " + score.getCaught() + "    ");
	    missed =new JLabel("Missed:" + score.getMissed()+ "    ");
	    scr =new JLabel("Score:" + score.getScore()+ "    "); 
	    
	    txt.add(caught);
	    txt.add(missed);
	    txt.add(scr);  
       
	    final JTextField textEntry = new JTextField("",20);
       
	   textEntry.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent evt) {
	          text = textEntry.getText();
	          
	          Thread t2=new Thread(new ControlMatch(words,noWords,text));
	          t2.start();
	          
	          textEntry.setText("");
	          textEntry.requestFocus();
	      }
	    });
	   
	   txt.add(textEntry);
	   txt.setMaximumSize( txt.getPreferredSize() );
	   g.add(txt);
	    
	    JPanel b = new JPanel();
       b.setLayout(new BoxLayout(b, BoxLayout.LINE_AXIS)); 
	    startB = new JButton("Start");;
		
			startB.addActionListener(new ActionListener()
		    {
		      public void actionPerformed(ActionEvent e)
		      { Thread t=new Thread(w);
			    t.start();
                startB.setEnabled(false);
		    	  textEntry.requestFocus();  //return focus to the text entry field
		      }
		    });
          
		JButton endB = new JButton("End");;
	   reset = new JButton("Restart");;
		JButton exit = new JButton("Quit");;
		
		ActionListener al=new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		     System.exit(0);
		    }
		};
		exit.addActionListener(al);
		
				// add the listener to the jbutton to handle the "pressed" event
				endB.addActionListener(new ActionListener()
			    {
			      public void actionPerformed(ActionEvent e)
			      {
				    Thread t4=new Thread(new ThreadEnd());//caught,missed,scr,startB));
				    t4.start();
	             startB.setEnabled(true);
			      }
			    });
				exit.addActionListener(al);
				
				reset.addActionListener(new ActionListener()
			    {
			      public void actionPerformed(ActionEvent e)
			      {
				    Thread t5=new Thread(new ThreadReset());//caught,missed,scr,startB));
				    t5.start();
	             startB.setEnabled(false);
			      }
			    });
		
		b.add(startB);
		b.add(endB);
		b.add(reset);
		b.add(exit);
		
		g.add(b);
    	
      	frame.setLocationRelativeTo(null);  // Center window on screen.
      	frame.add(g); //add contents to window
        frame.setContentPane(g);     
       	//frame.pack();  // don't do this - packs it into small space
        frame.setVisible(true);
	}
	
public synchronized static void set(){
	caught.setText("Caught: " + WordApp.score.getCaught() + "    ");
    missed.setText("Missed:" +WordApp.score.getMissed()+ "    ");
    scr.setText	("Score:" + WordApp.score.getScore() + "    ");
}
	
public static void GameOver(){
   JOptionPane.showMessageDialog(null,"GAME OVER! "+totalWords +" Words has fallen"+"           \nCaught Words: " + WordApp.score.getCaught()+"   "+"Missed:" +WordApp.score.getMissed());
   Thread t4=new Thread(new ThreadEnd());//caught,missed,scr,startB));
   t4.start();
   startB.setEnabled(true); 
}

public static void set2(){
	caught.setText("Caught: " + WordApp.score.getCaught() + "    ");
	scr.setText("Score:" + WordApp.score.getScore()+ "    "); 
}

public static void set3(){
	  WordApp.missed.setText("Missed:" + WordApp.score.getMissed()+ "    ");
}

public static String[] getDictFromFile(String filename) {
		String [] dictStr = null;
		try {
			Scanner dictReader = new Scanner(new FileInputStream(filename));
			int dictLength = dictReader.nextInt();
			//System.out.println("read '" + dictLength+"'");

			dictStr=new String[dictLength];
			for (int i=0;i<dictLength;i++) {
				dictStr[i]=new String(dictReader.next());
				//System.out.println(i+ " read '" + dictStr[i]+"'"); //for checking
			}
			dictReader.close();
		} catch (IOException e) {
	        System.err.println("Problem reading file " + filename + " default dictionary will be used");
	    }
		return dictStr;
	}

	public static void main(String[] args) {
    	
		//deal with command line arguments
		totalWords=Integer.parseInt(args[0]);  //total words to fall
		noWords=Integer.parseInt(args[1]); // total words falling at any point
		assert(totalWords>=noWords); // this could be done more neatly
		
		String[] tmpDict=getDictFromFile(args[2]); //file of words
		if (tmpDict!=null)
			dict= new WordDictionary(tmpDict);
		
		WordRecord.dict=dict; //set the class dictionary for the words.
		
		words = new WordRecord[noWords];  //shared array of current words
			
		setupGUI(frameX, frameY, yLimit);  
    	//Start WordPanel thread - for redrawing animation

		int x_inc=(int)frameX/noWords;
	  	//initialize shared array of current words

		for (int i=0;i<noWords;i++) {
			words[i]=new WordRecord(dict.getNewWord(),i*x_inc,yLimit);
		}
	}
}
