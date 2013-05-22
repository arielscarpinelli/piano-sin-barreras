(function($) {

	$.scoreReader = function(element, options) {
		this.options = {};
		this.measurePosition = 0;
		this.staffPosition = 0;
		this.symbolPosition = 0;
		this.score = null;

		element.data('scoreReader', this);

		this.init = function(element, options) {
			this.options = $.extend({}, $.scoreReader.defaultOptions, options);

			// Manipulate element here ...
			this.setScore(this.options.score);
			setNavigationBindings(this, element);
		};

		// Public function
		this.setScore = function(score) {
			this.score = score;
			this.measurePosition = 0;
			this.staffPosition = 0;
			this.symbolPosition = 0;

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

		this.getSymbols = function() {
			return this.getCurrentStaff()["symbols"];
		}

		this.getCurrentSymbol = function() {
			return this.getSymbols()[this.symbolPosition];
		}

		this.getCurrentDuration = function() {
			var duration = 0;
			var symbols = this.getSymbols();
			for(i = 0; i < this.symbolPosition; i++) {
				duration += symbols[i]["duration"];
			}
			return duration;
		}

		this.findNearPosition = function(targetDuration) {
			var duration = 0;
			var symbols = this.getSymbols();
			i = 0
			for(; i < this.symbolPosition; i++) {
				duration += symbols[i]["duration"];
				if (duration >= targetDuration) {
					break;
				}
			}
			console.log("findNearPosition: target: " + targetDuration + " achieved: " + duration + " position: " + i);
			return i;
		}

		this.next = function() {
			if (this.symbolPosition < this.getSymbols().length - 1) {
				this.symbolPosition++;
				renderSymbol(this, element);
			} else {
				this.nextMeasure();
			}
		};

		this.prev = function() {
			if (this.symbolPosition > 0) {
				this.symbolPosition--;
				renderSymbol(this, element);
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
			this.symbolPosition = this.findNearPosition(duration);
			renderStaff(this, element);
		};

        this.nextMeasure = function() {
			if (this.measurePosition < this.getMeasures().length - 1) {
				this.measurePosition++;
				this.symbolPosition = 0;
				renderMeasure(this, element);
			}			
		};

		this.prevMeasure = function(resetStaff, avoidResetSymbol) {
			if (this.measurePosition > 0) {
				this.measurePosition--;
				if (resetStaff) {
					this.staffPosition = this.getStaves().length - 1;
				}
				if (avoidResetSymbol) {
					this.symbolPosition = this.getSymbols().length - 1;
				} else {
					this.symbolPosition = 0;
				}
				renderMeasure(this, element);
			}			
		};

		this.begin = function() {
			this.setScore(this.score);
		};
		
		this.play = function() {
      if (window.MIDI) {
        var sounds = this.getCurrentSymbol()["sounds"];
        if (sounds) {
          for(i=0; i < sounds.length; i++) {
            note = MIDI.keyToNote[sounds[i]];
            MIDI.noteOn(0, note, 127, 0);
            MIDI.noteOff(0, note, 0.75);      
          }
        }
			}
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
		renderSymbol(self, element);
	}

	function renderSymbol(self, element) {
		element.find("#symbol").html(self.getCurrentSymbol()["name"]);
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
		element.find(".play").click(function() {
			self.play();			
		})
	}

})(jQuery);
