# variable for java compiler
JC = javac
J = java
D = -d
CD = cd

# damage control
.SUFFIXES: .java .class

# target for creating .class from .java in format:
#	.original_extention.target_extention:
#		rule
.java.class:
	$(JC) $*.java $(D) bin

# macro for each java source file
CLASSES = \
	util/Point.java \
	util/HighScoreList.java \
	BallPath.java \
	Highlighter.java \
	Lines18GUI.java \
	Lines18.java \
	main.java

# default target definition
default: classes

classes: $(CLASSES:.java=.class)

run:
	$(CD) bin && $(J) main

clean:
	$(RM) *.class
