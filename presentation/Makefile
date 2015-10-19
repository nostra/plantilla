# Erlend Nossum

MAIN = plantilla

%.html : %.md
	rm -rf theme
	landslide config.cfg --copy-theme

all:	$(MAIN).html

clean:
	rm -f index.html
	rm -rf theme
