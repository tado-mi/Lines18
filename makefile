# variable for java compiler
JC = javac
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
	src/Point.java \
	src/BallPath.java \
	src/Highlighter.java \
	src/HighScoreList.java \
	src/Lines18.java \
	src/Lines18GUI.java \
	src/main.java \
	src/Stack.java

# default target definition
default: classes

classes: $(CLASSES:.java=.class)

run:
	$(CD) bin/src && java main

clean:
	$(RM) *.class
