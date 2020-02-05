package edu.mccc.cos210.ds.fp.bugattitng;


import java.io.IOException;
import javax.sound.midi.Sequencer;

/**
 * BugattiTNG
 *
 * @author Khan, Numayr A.
 * @author Kleinkauf, Brian R.
 * @author Zhao, Junfeng
 * @author Zhao, Jinchao
 * @author Shaikh, Sahal A.
 *
 * @version 0.9
 */
public class BugattiTNG {
	private Bugatti5 b;
	/**
	 * The secret number. Blah, blah, blah.
	 */
	private int number = 42;
	private static Sequencer sequencer;
	private void initGame(String name, boolean ownDesign, boolean multi, boolean server, String ip) {
		try {
			b = new Bugatti5(name,ownDesign,multi,server,ip);
			if (multi) {
				System.out.println(1);
				Thread th = new Thread(b);
				System.out.println(2);
				th.start();
				Thread th1 = new Thread(b.getNet());
				th1.start();
			} else {
				b.initSwing();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *
	 * Constructor. Create the original object
	 */
	public BugattiTNG() {
//		MidiPlayer mp = new MidiPlayer("./data/bugatti.mid");
//		sequencer = mp.getSequencer();
//		sequencer.start();
		b = null;
		SplashScreen title = new SplashScreen();
		while(!title.isDone()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		title.disJf();
		this.initGame(title.getName(),title.ownDesign(),title.getMultiPlay(),title.getServer(),title.getIp());
	}
		
	

	/**
	 * In the begining there was main. main is where it all
	 * begins. Blah, blah, blah.
	 *
	 * @param args The command line arguments
	 */
	public static void main(String... args) {
		new BugattiTNG();
	}

	/**
	 * Inspector in charge of return the secret number. Blah, blah, blah.
	 * @return The secret number.
	 * @throws Exception The number is Invalid.
	 */
	public int getNumber() throws Exception {
		if (this.number != 42) {
			throw new Exception();
		}
		return this.number;
	}
}
