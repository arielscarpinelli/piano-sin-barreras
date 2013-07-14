package models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import models.note.Note;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.audiveris.proxymusic.Backup;
import com.audiveris.proxymusic.Print;

@JsonSerialize(include = Inclusion.NON_NULL)
public class Measure {

	private int number;
	private Attributes attributes = null;
	private List<Symbol> prependSymbols = null;
	private List<Symbol> appendSymbols = null;

	private LinkedHashMap<String, Voice> voiceMap = new LinkedHashMap<String, Voice>();

	public Collection<Voice> getVoices() {
		return voiceMap.values();
	}

	public int getNumber() {
		return number;
	}

	public Integer getDivisions() {
		return attributes != null ? attributes.divisions : null;
	}

	public String getName() {
		return "Compás " + number;
	}

	public static Measure fromXmlMeasure(
			com.audiveris.proxymusic.ScorePartwise.Part.Measure xmlMeasure,
			ScoreContent score) {

		Measure measure = new Measure();
		measure.number = Integer.parseInt(xmlMeasure.getNumber());

		Note last = null;

		for (Object noteOrBackupOrForward : xmlMeasure
				.getNoteOrBackupOrForward()) {
			if (noteOrBackupOrForward instanceof com.audiveris.proxymusic.Note) {
				Note note = Note
						.fromXmlNote((com.audiveris.proxymusic.Note) noteOrBackupOrForward);
				if (note != null) {
					// Note may be null if print-object = no
					if (note.chord() && (last != null)) {
						note = last.chordWidth(note);
						measure.getVoiceFor(last).getSymbols().remove(last);
					}
					measure.addNote(note);
				}
				last = note;
			} else {
				last = null;
				if (noteOrBackupOrForward instanceof com.audiveris.proxymusic.Attributes) {
					measure.attributes = Attributes
							.fromXmlAttributes((com.audiveris.proxymusic.Attributes) noteOrBackupOrForward);
				} else if (noteOrBackupOrForward instanceof com.audiveris.proxymusic.Barline) {
					measure.addBarline(Barline
							.fromXml((com.audiveris.proxymusic.Barline) noteOrBackupOrForward));
				} else if (noteOrBackupOrForward instanceof Backup) {
					// Ignore backup
				} else if (noteOrBackupOrForward instanceof Print) {
					// Ignore print settings
				} else {
					ScoreContent.unknown("Measure", noteOrBackupOrForward);
				}
			}
		}

		measure.applySymbols();

		return measure;
	}

	private Voice getVoiceFor(Note note) {
		String voiceName = note.getVoice();
		Voice voice = voiceMap.get(voiceName);
		if (voice == null) {
			voice = new Voice(voiceName, voiceMap.size() + 1);
			voiceMap.put(voiceName, voice);
		}
		return voice;
	}

	private void addNote(Note note) {
		getVoiceFor(note).add(note);
	}

	private void addBarline(Barline barline) {
		if (barline.isDirectionForward()) {
			prependSymbol(barline);
		} else {
			appendSymbol(barline);
		}

	}

	private void prependSymbol(Symbol symbol) {
		if (prependSymbols == null) {
			prependSymbols = new ArrayList<Symbol>();
		}
		prependSymbols.add(symbol);
	}

	private void appendSymbol(Symbol symbol) {
		if (appendSymbols == null) {
			appendSymbols = new ArrayList<Symbol>();
		}
		appendSymbols.add(symbol);
	}

	private void applySymbols() {
		for (Voice voice : getVoices()) {
			
			if (attributes != null) {
				voice.prepend(attributes.cloneFor(voice.getStaff()));
			}
			
			if (prependSymbols !=  null) {
				for (Symbol symbol : prependSymbols) {
					voice.prepend(symbol);
				}
			}
			
			if(appendSymbols != null) {
				for (Symbol symbol : appendSymbols) {
					voice.add(symbol);
				}
			}
		}
	}

	public void join(Measure other) {
		for (Voice voice : other.getVoices()) {
			voice.setPosition(voiceMap.size() + 1);
			voiceMap.put(other.getName() + " - " + voice.getOriginalName(),
					voice);
		}
		// TODO: join attributes
	}

	public void addToAllVoices(End end) {
		for (Voice voice : getVoices()) {
			voice.add(end);
		}
	}

}
