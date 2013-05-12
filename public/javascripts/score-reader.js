(function($) {

	$.scoreReader = function(element, options) {
		this.options = {};
		this.measurePosition = 0;
		this.staffPosition = 0;
		this.notePosition = 0;
		this.score = null;

		element.data('scoreReader', this);

		this.init = function(element, options) {
			this.options = $.extend({}, $.scoreReader.defaultOptions, options);

			// Manipulate element here ...
			this.setScore(this.options.score);
			setKeyBindings(this, element);
		};

		// Public function
		this.setScore = function(score) {
			this.score = score;
			this.measurePosition = 0;
			this.staffPosition = 0;
			this.notePosition = 0;

			render(this, element)
		};

		this.getMeasures = function() {
			return this.score["measures"];
		}
		
		this.getCurrentMeasure = function() {
			return this.getMeasures()[this.measurePosition];
		}
		
		this.getStaves = function() {
			return this.getCurrentMeasure()["staves"]
		}
		
		this.getCurrentStaff = function() {
			return this.getStaves()[this.staffPosition];
		}

		this.getNotes = function() {
			return this.getCurrentStaff()["notes"];
		}

		this.getCurrentNote = function() {
			return this.getNotes()[this.notePosition];
		}

		this.getCurrentDuration = function() {
			var duration = 0;
			var notes = this.getNotes();
			for(i = 0; i < this.notePosition; i++) {
				duration += notes[i]["duration"];
			}
			return duration;
		}

		this.findNearPosition = function(targetDuration) {
			var duration = 0;
			var notes = this.getNotes();
			i = 0
			for(; i < this.notePosition; i++) {
				duration += notes[i]["duration"];
				if (duration >= targetDuration) {
					break;
				}
			}
			console.log("findNearPosition: target: " + targetDuration + " achieved: " + duration + " position: " + i);
			return i;
		}

		this.next = function() {
			if (this.notePosition < this.getNotes().length - 1) {
				this.notePosition++;
				renderNote(this, element);
			} else {
				this.nextMeasure();
			}
		};

		this.prev = function() {
			if (this.notePosition > 0) {
				this.notePosition--;
				renderNote(this, element);
			} else {
				this.prevMeasure(false, true);
			}
		};

		this.nextStaff = function() {
			if (this.staffPosition < this.getStaves().length - 1) {

				var duration = this.getCurrentDuration();
				this.staffPosition++;
				this.notePosition = this.findNearPosition(duration);
				renderStaff(this, element);
			} else {
				this.staffPosition = 0;
				this.nextMeasure();
			}
		};

		this.prevStaff = function() {
			if (this.staffPosition > 0) {
				var duration = this.getCurrentDuration();
				this.staffPosition--;
				this.notePosition = this.findNearPosition(duration);
				renderStaff(this, element);
			} else {
				this.prevMeasure(true);
			}			
		};

		this.nextMeasure = function() {
			if (this.measurePosition < this.getMeasures().length - 1) {
				this.measurePosition++;
				this.notePosition = 0;
				renderMeasure(this, element);
			}			
		};

		this.prevMeasure = function(resetStaff, avoidResetNote) {
			if (this.measurePosition > 0) {
				this.measurePosition--;
				if (resetStaff) {
					this.staffPosition = this.getStaves().length - 1;
				}
				if (avoidResetNote) {
					this.notePosition = this.getNotes().length - 1;
				} else {
					this.notePosition = 0;
				}
				renderMeasure(this, element);
			}			
		};

		this.begin = function() {
			this.setScore(this.score);
		};
		
		this.play = function() {
			var currentNote = this.getCurrentNote();
			var noteName = currentNote.pitch + currentNote.octave;
			note = MIDI.keyToNote[noteName];
			MIDI.noteOn(0, note, 127, 0);
			MIDI.noteOff(0, note, 0.75);			
		}


		this.init(element, options);
	};

	$.fn.scoreReader = function(options) { // Using only one method off of $.fn
		return this.each(function() {
			(new $.scoreReader($(this), options));
		});
	};

	$.scoreReader.defaultOptions = {}

	// Private fn
	function render(self, element) {
		renderMeasure(self, element);
	}

	function renderMeasure(self, element) {
		element.children("#measure").html(self.getCurrentMeasure()["name"])
		renderStaff(self, element);
	}

	function renderStaff(self, element) {
		element.children("#staff").html(self.getCurrentStaff()["name"])
		renderNote(self, element);
	}

	function renderNote(self, element) {
		element.children("#note").html(self.getCurrentNote()["name"]);
	}

	function setKeyBindings(self, element) {
		element.children("#commands").keydown(function(e) {
			var keyCode = e.keyCode || e.which, arrow = {
				I: 73,
				home : 36,
				left : 72,
				up : 85,
				right : 75,
				down : 74,
				Y: 89
			};

			switch (keyCode) {
			case arrow.I:
				self.begin();
				break;
			case arrow.left:
				self.prev();
				break;
			case arrow.up:
				self.prevStaff();
				break;
			case arrow.right:
				self.next();
				break;
			case arrow.down:
				self.nextStaff();
				break;
			case arrow.Y:
				self.play();
				break;
			}
			
			e.preventDefault();
		});
		element.children("#commands").focus();
	}

})(jQuery);