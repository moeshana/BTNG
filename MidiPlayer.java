package edu.mccc.cos210.ds.fp.bugattitng;

import java.io.File;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

public class MidiPlayer implements MetaEventListener {
	private static final int META_EndofTrack = 47;
	private Sequencer sequencer;
	private Sequence sequence;
	public MidiPlayer(String s) {
		try {
			sequencer = MidiSystem.getSequencer();
			sequencer.open();
			sequence = MidiSystem.getSequence(new File(s));
			sequencer.setSequence(sequence);
			sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
			//sequencer.addMetaEventListener(this);
		} catch (Exception ex) {
			System.err.println(ex);
		}
	}
	public Sequencer getSequencer() {
		return sequencer;
	}
	public static void main(String... args) {
		if (args.length != 1) {
			System.err.println("usage: java MidiPlayer file.mid");
			System.exit(-1);
		} else {
			MidiPlayer mp = new MidiPlayer(args[0]);
			mp.sequencer.start();
		}
	}
	public void meta(MetaMessage message) {
		if (message.getType() == META_EndofTrack) {
			try {
				Thread.sleep(1000);
				sequencer.close();
			} catch (Exception ex) {
				// ignore
			} finally {
				System.exit(0);
			}
		}
	}
}
