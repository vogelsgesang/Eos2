package de.lathanda.eos.base;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;
import static javax.sound.sampled.AudioSystem.getAudioInputStream;

import java.io.BufferedInputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
/**
 * Klasse zum Abspielen von Ton und Musik.
 * 
 * Für eine Unterstützung von mp3 oder ogg müssen die entsprechenden Bibliotheken im Klassenpfad liegen.
 *
 * @author Peter (Lathanda) Schneider
 */
public class Sound {
	private Clip clip;
	/**
	 * Erzeugt eine Tondatei zur Quelle.
	 * @param source Dateiname
	 */
	public Sound(String source) {
		try {
		final AudioInputStream stream = getAudioInputStream(new BufferedInputStream(ResourceLoader.getResourceAsStream(source)));
		final AudioFormat format = getOutFormat(stream.getFormat());
		DataLine.Info info = new DataLine.Info(Clip.class, format);
		clip = (Clip) AudioSystem.getLine(info);
		clip.open(stream);		
		} catch (Exception e) {
			clip = null;
		}
	}
	/**
	 * Ton in einer Schleife abspielen.
	 */
	public void playLoop() {
		if (clip != null) {
			clip.setFramePosition(0);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
			clip.start();		
		}
	}
	/**
	 * Ton abspielen.
	 */
	public void play() {
		if (clip != null) {
			clip.setFramePosition(0);
			clip.loop(0);
			clip.start();		
		}
	}
	/**
	 * Abspielen anhalten.
	 */
	public void stop() {
		if (clip != null) {
			clip.stop();
		}
	}
	/**
	 * Audioformat abfragen.
	 * @param inFormat Quelle
	 * @return Format
	 */
	private AudioFormat getOutFormat(AudioFormat inFormat) {
		final int ch = inFormat.getChannels();
		final float rate = inFormat.getSampleRate();
		return new AudioFormat(PCM_SIGNED, rate, 16, ch, ch * 2, rate, false);
	}

}
