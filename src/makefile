JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
        $(JC) $(JFLAGS) $*.java

CLASSES = \
        Score.java \
        WordApp.java \
        WordDictionary.java \
        WordPanel.java \
        WordRecord.java \
        ControlMatch.java \
        ControlThread.java \ 
        ThreadReset.java \
        ThreadEnd.java \

default: classes

classes: $(CLASSES:.java=.class)

clean:
        $(RM) *.class

run:
		java WordApp "10" "5" "example_dict.txt"
