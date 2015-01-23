# Make TokenXML
#
# Makes the project and optionally makes the tests. Class files are placed 
# in the bin directory and output jars will be in the out directory.
#
# @version 0.2
# @author Matthew Maynes

#
# The version should be updated per each new release build. The output jar
# will be labeled with the current version
#
VERSION = $(shell if [ -e VERSION ]; then cat VERSION; else echo "0.0.0"; fi)

#
# The name of this framework. This is used for compiling the jar outputs for the project
#
NAME = token-xml

#
# Compilers
#
JC = javac
JDOC = javadoc
JAR = jar

#
# Key Directories
#
SRC = src
BIN = bin
OUT = out
DOC = doc

#
# Compiler Flags
#
JFLAGS = -sourcepath $(SRC)
JDOCFLAGS = -sourcepath $(SRC)
JTESTFLAGS = -classpath $(LIB)/hamcrest-core-1.3.jar:$(LIB)/junit-4.11.jar -sourcepath $(SRC)

#
# The location of the java packages
#
PACKAGES = emex/xml
TESTPACKAGES = test/emex test/emex/xml

#
# The default directive is used when only make is called. It 
# class all of the other directives
#
default: classes

#
# Cleans the project and compiles all directives in the make file
# except for the test directive
#
release: clean classes javadoc jar

#
# Compiles all of the classes in the project
#
classes:
	$(JC) $(JFLAGS) -d $(BIN) $(foreach package, $(PACKAGES), $(SRC)/$(package)/*.java) 
	cp $(SCRIPTS) $(BIN)

#
# Compiles the project verbosely with all of the java compiler messages
#
verbose:
	$(JC) -version -Xlint $(JFLAGS) -d $(BIN) $(foreach package, $(PACKAGES), $(SRC)/$(package)/*.java)
	$(JC) -version -Xlint $(JTESTFLAGS) -d $(BIN) $(foreach package, $(TESTPACKAGES), $(SRC)/$(package)/*.java)
	cp $(SCRIPTS) $(BIN)


#
# Compiles and outputs all of the javadoc into the API folder
#
javadoc:
	$(JDOC) $(JDOCFLAGS) -d $(DOC) $(foreach package, $(PACKAGES), $(SRC)/$(package)/*.java)

#
# Compiles all of the tests in the project
#
tests:
	$(JC) $(JTESTFLAGS) -d $(BIN) $(foreach package, $(TESTPACKAGES), $(SRC)/$(package)/*.java)
	cp $(SCRIPTS) $(BIN)

#
# Jars the project into a convenient jar output
#
jar: 
	cd $(BIN) && $(JAR) cvf ../$(OUT)/$(NAME)-$(VERSION).jar $(foreach package, $(PACKAGES), $(package)/*.class)


#
# Cleans the project
#
clean: 
	cd $(BIN) && rm -f $(foreach package, $(PACKAGES), $(package)/*.class)
	find . -name *~ -exec rm -rf {} \;
