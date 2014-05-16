

NoteUtils {
	classvar noteNames, notes, intervalNames;


	*initClass{
		var semitones, naturalNoteNames;

		noteNames = Dictionary[
	 	\C->0, \Bs->0,
	 	\Cs->1, \Db->1,
	 	\D->2,
	 	\Ds->3, \Eb->3,
	 	\E->4,
	 	\Es->5, \F->5,
	 	\Fs->6, \Gb->6,
	 	\G->7,
	 	\Gs->8, \Ab->8,
	 	\A->9,
	 	\As->10, \Bb->10,
		\B->11, \Cb->11];

		notes = ();

		semitones = [0, 2, 4, 5, 7, 9, 11];
		naturalNoteNames = ["C", "D", "E", "F", "G", "A", "B"];

		(0..9).do {|o|
			naturalNoteNames.do {|c, i|
				var n = (o + 1) * 12 + semitones[i];
				notes[(c ++ o).asSymbol] = n;
				notes[(c ++ "s"  ++ o).asSymbol] = n + 1;
				notes[(c ++ "b"  ++ o).asSymbol] = n - 1;
			};
		};

		intervalNames = Dictionary[
			0->"Unison",
			1->"Minor 2nd",
			2->"Major 2nd",
			3->"Minor 3rd",
			4->"Major 3rd",
			5->"Perfect 4th",
			6->"Diminished 5th",
			7->"Perfect 5th",
			8->"Minor 6th",
			9->"Major 7th",
			10->"Minor 7th",
			11->"Major 7th",
			12->"Octave"];
	}

	*noteFromMidi{arg midi;
		^notes.findKeyForValue(midi);
	}

	*midiFromNote {arg note;
		^notes[note];
	}

	*interval {arg note1, note2;
		var diff = note2-note1;
		if (diff == 0, {^intervalNames[0];});
		diff = diff % 12;
		if (diff == 0, {^intervalNames[12];});
		^intervalNames[diff];
	}
}



Chord {
	var <>notes;

	classvar <chordTypes, <degree;

	*new {arg note, type, invert ... voices;
		var obj = super.new;
		var notes = Chord.chordTypes[type] + NoteUtils.midiFromNote(note);
		notes = Chord.chordInvert(notes, invert);

		if (voices.size == 0, {
			obj.notes = notes;
			^obj;
		});

		obj.notes = Array.newClear(voices.size+1);
		obj.notes[0] = notes[0];

		voices.do{ arg voice, i;
			obj.notes[i+1] = (voice[0] * 12) + notes[voice[1]];
		}
		^obj;
	}

	noteNames{
		var noteNames = Array.newClear(this.notes.size);
		do(this.notes, {arg midi, i;
			noteNames[i] = NoteUtils.noteFromMidi(midi);
		});
		^noteNames;
	}

	intervals{
		var intervals = Array.newClear(notes.size-1);
		for(0, notes.size-2,{ arg i;
			intervals[i] = NoteUtils.interval(this.notes[i], this.notes[i+1]);
		});
		^intervals;
	}

	inversion{arg inversion;
		notes.postln;
		^Chord.chordInvert(notes, inversion);
	}

	*chordInvert{arg chord, inversion;
		if (inversion == 0,
			{
				^chord;
			},
			{
				var sign = inversion.sign;
				chord[0] = chord[0] + (sign * 12);
				chord = chord.rotate (0-sign);
				^Chord.chordInvert(chord, inversion - sign);
			});
	}

	*initClass{
		chordTypes = Dictionary[
			\maj -> [0, 4, 7],
			\min -> [0, 3, 7],
			\maj7 -> [0, 4, 7, 11],
			\dom7 -> [0, 4, 7, 10],
			\min7 -> [0, 3, 7, 10],
			\aug -> [0, 4, 8],
			\dim -> [0, 3, 6],
			\dim7 -> [0, 3, 6, 9]];

		degree = Dictionary[
			\i   -> 1,
			\ii  -> 2,
			\iii -> 3,
			\iv  -> 4,
			\v   -> 5,
			\vi  -> 6,
			\vii -> 7];
	}
}




ChordProgression
{
	var chords;

	*new {arg ... chordseq;
		var obj = super.new;
		obj.init(chordseq);
		^obj;
	}

	init{arg chordSequence;
		chords = chordSequence;
	}

}