var Hg={
		get:function(id) {
			if (id)
				return document.getElementById(id);
			else
				return null;
		},
		each:function(d, c) {
			if (typeof d.length == "undefined" || typeof d == "string") {
				d = [d]
			}
			for (var b = 0, a = d.length; b < a; b++) {
				if (c.call(d[b], d[b], b, d) === false) {
					return b
				}
			}
		}
};

function setTab(name, cursel, n) {
	for (i = 1; i <= n; i++) {
		var menu = Hg.get(name + i);
		var con = Hg.get("con_" + name + "_" + i);
		if (menu)
			menu.className = i == cursel ? "on" : "";
		if (con)
			con.style.display = i == cursel ? "block" : "none";
	}
}

function menuView(menu, className) {
	var s = Hg.get(menu);
	var cs = s.children || [];
	for (var i in cs) {
		var t = cs[i];
		t.onmouseover = function() {
			addClass(this,className);
		}
		t.onmouseout = function() {
			removeClass(this,className);
		}
	}
}

function tabView(tabs, contents, className, eventName) {
	var s = Hg.get(tabs);
	var c = Hg.get(contents) || {};
    
	var as = [];
	var bs = [];
	if(s){
		as = s.children || [];
	}
	if(c){
		bs = c.children || [];
	}

	for (var i in as) {
		var t = as[i];
		t[eventName] = function() {
			for (var j in as) {
				var ct = as[j];
				var c = bs[j];
				if (ct){
					if(this === ct){ 
						addClass(ct,className)
					}else{ 
						removeClass(ct,className)
					}
				}
				try {
						c.style.display = (this === ct ? "block" : "none");
				} catch (e) {
				}
			}
		}

	}

}



function addOnLoad(func) {
	var oldonload = window.onload;
	if (typeof window.onload != 'function') {
		window.onload = func;
	} else {
		window.onload = function() {
			oldonload();
			func();
		}
	}
}

var _Slide = function(c) {

	this.eventType = c.eventType || "mouseover",

	this.autoPlayInterval = c.autoPlayInterval || 3 * 1000;

	this._play = true;
	this._timer = null;
	this._fadeTimer = null;
	this._container = Hg.get(c.container);
	this._panelWrapper = Hg.get(c.panelWrapper);
	this._sliders = this._panelWrapper.children;
	this._navWrapper = Hg.get(c.navWrapper);
	this._navs = this._navWrapper.children;
	this._effect = c.effect || "scrollx";
	this._panelSize = (this._effect.indexOf("scrolly") == -1
			? c.width
			: c.height)
			|| getSize(this._sliders[0])[this._effect.indexOf("scrolly") == -1
					? 0
					: 1];
	this._count = c.count || this._sliders.length;
	this._navClassOn = c.navClassOn || "on";
	this._tget = 0;
	this._changeProperty = this._effect.indexOf("scrolly") == -1
			? "left"
			: "top";
	this.curIndex = 0;
	this.step = this._effect.indexOf("scroll") == -1 ? 1 : (c.Step || 5);
	this.slideTime = c.slideTime || 10;
	if (c.nextButton) {
		var d = this;
		Hg.get(c.nextButton).onclick = (function(e) {
			return function() {
				e.next()
			}
		})(d)
	}
	if (c.prevButton) {
		Hg.get(c.prevButton).onclick = (function(e) {
			return function() {
				e.prev()
			}
		})(d)
	}
	this.init();
	this.run(true)
};

_Slide.prototype = {
	init : function() {
		if (this._container) {
			setStyle(this._container, "overflow", "hidden");
			setStyle(this._container, "position", "relative")
		}
		setStyle(this._panelWrapper, "position", "relative");
		if (this._effect.indexOf("scrolly") == -1) {
			setStyle(this._panelWrapper, "width", this._count
							* (this._panelSize + 200) + "px");
			Hg.each(this._sliders, function(d) {
						if (d.style)
							d.style.styleFloat = d.style.cssFloat = "left"
					})
		}
		var c = this;
		if (this._navs) {
			if (c.eventType == "click") {
				Hg.each(this._navs, function(e, d) {
							e.onclick = (function(f) {
								return function() {
									addClass(e, f._navClassOn);
									f._play = false;
									f.curIndex = d;
									f._play = true;
									f.run()
								}
							})(c)
						})
			} else {
				Hg.each(this._navs, function(e, d) {
							e.onmouseover = (function(f) {
								return function() {
									addClass(e, f._navClassOn);
									f._play = false;
									f.curIndex = d;
									f.run()
								}
							})(c);
							e.onmouseout = (function(f) {
								return function() {
									removeClass(e, f._navClassOn);
									f._play = true;
									f.run(false, true)
								}
							})(c)
						})
			}
		}
		Hg.each(this._sliders, function(e, d) {
					e.onmouseover = (function(f) {
						return function() {
							f._play = false;
							f.run(false, true)
						}
					})(c);
					e.onmouseout = (function(f) {
						return function() {
							f._play = true;
							f.run(false, true)
						}
					})(c)
				})
	},
	run : function(e, c) {
		if (this.curIndex < 0) {
			this.curIndex = this._count - 1
		} else {
			if (this.curIndex >= this._count) {
				this.curIndex = 0
			}
		}
		this._tget = -1 * this._panelSize * this.curIndex;
		var d = this;
		if (this._navs) {
			Hg.each(this._navs, function(g, f) {
						d.curIndex == (f)
								? addClass(g, d._navClassOn)
								: removeClass(g, d._navClassOn)
					})
		}
		this.scroll();
		if (this._effect.indexOf("fade") >= 0 && !c) {
			setStyle(this._panelWrapper, "opacity", e ? 0.5 : 0.1);
			this.fade()
		}
	},
	scroll : function() {
		clearTimeout(this._timer);
		try {
			if (QZFL.lazyLoad.isVisible(this._sliders[this.curIndex])) {
				QZFL.lazyLoad.loadHideImg(this._sliders[this.curIndex])
			}
		} catch (d) {
		}
		var g = this, f = parseInt(this._panelWrapper.style[this._changeProperty])
				|| 0, c = (this._tget - f) / this.step;
		if (Math.abs(c) < 1 && c != 0) {
			c = c > 0 ? 1 : -1
		}
		if (c != 0) {
			this._panelWrapper.style[this._changeProperty] = (f + c) + "px";
			this._timer = setTimeout(function() {
						g.scroll()
					}, this.slideTime)
		} else {
			this._panelWrapper.style[this._changeProperty] = this._tget + "px";
			if (this._play) {
				this._timer = setTimeout(function() {
							g.curIndex++;
							g.run()
						}, this.autoPlayInterval)
			}
		}
	},
	fade : function() {
		var c = getStyle(this._panelWrapper, "opacity");
		var d = this;
		if (c < 1) {
			setStyle(this._panelWrapper, "opacity", parseFloat(c) + 0.02);
			setTimeout(function() {
						d.fade()
					}, 1)
		}
	},
	next : function() {
		this._play = false;
		this.curIndex++;
		this._play = true;
		this.run()
	},
	prev : function() {
		this._play = false;
		this.curIndex--;
		this._play = true;
		this.run()
	}
};




function getStyle(key, index) {
	var _e = this.elements[index || 0];
	return !!_e ? getStyle(_e, key) : null;
}
function setStyle(el, property, value) {
	el = Hg.get(el);
	if (!el || el.nodeType == 9) {
		return false;
	}
	var w3cMode = document.defaultView && document.defaultView.getComputedStyle;
	switch (property) {
		case "float" :
			property = w3cMode ? "cssFloat" : "styleFloat";
			el.style[property] = value;
			return true;
			break;
		case "opacity" :
			if (!w3cMode) {
				if (value >= 1) {
					el.style.filter = "";
					return;
				}
				el.style.filter = 'alpha(opacity=' + (value * 100) + ')';
				return true;
			} else {
				el.style[property] = value;
				return true;
			}
			break;
		case "backgroundPositionX" :
			if (w3cMode) {
				var _y = getStyle(el, "backgroundPositionY");
				el.style["backgroundPosition"] = value + " " + (_y || "top");
			} else {
				el.style[property] = value;
			}
			break;
		case "backgroundPositionY" :
			if (w3cMode) {
				var _x = getStyle(el, "backgroundPositionX");
				el.style["backgroundPosition"] = (_x || "left") + " " + value;
			} else {
				el.style[property] = value;
			}
			break;
		default :
			if (typeof el.style[property] == "undefined") {
				return false
			}
			el.style[property] = value;
			return true;
	}
}

function hasClassName(elem, cname) {
	return (elem && cname) ? new RegExp('\\b' + cname + '\\b')
			.test(elem.className) : false;
}

function addClassName(elem, cname) {
	if (elem && cname) {
		if (elem.className) {
			if (hasClassName(elem, cname)) {
				return false;
			} else {
				elem.className += ' ' + cname;
				return true;
			}
		} else {
			elem.className = cname;
			return true;
		}
	} else {
		return false;
	}
}
function removeClassName(elem, cname) {
	if (elem && cname && elem.className) {
		var old = elem.className;
		elem.className = (elem.className.replace(new RegExp('\\b' + cname
						+ '\\b'), ''));
		return elem.className != old;
	} else {
		return false;
	}
}
function addClass(el,className) {
	
				addClassName(el, className);
}
function removeClass(el,className) {
				removeClassName(el, className);
}
