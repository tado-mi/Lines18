# variable for java compiler
JC = javac

# damage control
.SUFFIXES: .java .class

# target for creating .class from .java in format:
#	.original_extention.target_extention:
#		rule
.java.class:
	$(JC) $*.java
	
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

clean:
	$(RM) *.class
	
