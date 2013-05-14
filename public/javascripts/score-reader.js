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
			//setKeyBindings(this, element);
			setNavigationBindings(this, this.options.navigation);
			//element.focus();
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
				this.staffPosition++;
			} else {
				this.staffPosition = 0;
			}

			var duration = this.getCurrentDuration();
			this.notePosition = this.findNearPosition(duration);
			renderStaff(this, element);
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
		element.find("#measure").html(self.getCurrentMeasure()["name"])
		renderStaff(self, element);
	}

	function renderStaff(self, element) {
		element.find("#staff").html(self.getCurrentStaff()["name"])
		renderNote(self, element);
	}

	function renderNote(self, element) {
		element.find("#note").html(self.getCurrentNote()["name"]);
	}

	function setKeyBindings(self, element) {
		element.keydown(function(e) {
			var keyCode = e.keyCode || e.which, key = {
				home: 74, // J
				left : 37,
				up : 38,
				right : 39,
				down : 40,
				play: 70 // F
			};

			switch (keyCode) {
			case key.home:
				self.begin();
    			e.preventDefault();
				break;
			case key.left:
				self.prev();
    			e.preventDefault();
				break;
			case key.up:
				self.prevStaff();
    			e.preventDefault();
				break;
			case key.right:
				self.next();
    			e.preventDefault();
				break;
			case key.down:
				self.nextStaff();
    			e.preventDefault();
				break;
			case key.play:
				self.play();
    			e.preventDefault();
				break;
			}			
		});
	}

	function setNavigationBindings(self, element) {
		element.find(".begin").click(function() {
			self.begin();			
		})
		element.find(".next").click(function() {
			self.next();			
		})
		element.find(".hand").click(function() {
			self.nextStaff();			
		})
		element.find(".prev").click(function() {
			self.prev();			
		})
		element.find(".up").click(function() {
			self.prevMeasure();			
		})
		element.find(".down").click(function() {
			self.nextMeasure();			
		})
		element.find(".next").click(function() {
			self.next();			
		})
	}

})(jQuery);
