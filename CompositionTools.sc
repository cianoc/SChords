

NoteSymbol {
	classvar notes;


	// build a table of note names
	classvar table;

	*initClass{
		var semitones, naturalNoteNames;

		notes = Dictionary[
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

		table = ();

		semitones = [0, 2, 4, 5, 7, 9, 11];
		naturalNoteNames = ["C", "D", "E", "F", "G", "A", "B"];

		(0..9).do {|o|
			naturalNoteNames.do {|c, i|
				var n = (o + 1) * 12 + semitones[i];
				table[(c ++ o).asSymbol] = n;
				table[(c ++ "s"  ++ o).asSymbol] = n + 1;
				table[(c ++ "ss" ++ o).asSymbol] = n + 2;
				table[(c ++ "b"  ++ o).asSymbol] = n - 1;
				table[(c ++ "bb" ++ o).asSymbol] = n - 2;
			};
		};
	}
}


Chord {
	classvar chords;

	classvar degree;

	*initClass{
		chords = [
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