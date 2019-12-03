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
	BallPath.java \
	Highlighter.java \
	HighScoreList.java \
	Lines18.java \
	Lines18GUI.java \
	main.java \
	Point.java \
	Stack.java

# default target definition
default: classes

classes: $(CLASSES:.java=.class)

run:
	$(CD) bin && java main

clean:
	$(RM) *.class
