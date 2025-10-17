# Define the source and output directories
SRCDIR := src
BINDIR := bin
DISTDIR := dist
DATADIR := data
LIBDIR := lib

MODULES := javafx.controls,javafx.fxml,commons.math3

# Define the target for building the used JAR file
USED_JAR := $(DISTDIR)/GM1Groupe9_Used.jar

# Define the target for building the archive JAR file
ARCHIVE_JAR := $(DISTDIR)/GM1Groupe9_Archive.jar

# Define the target for building the executable JAR file
EXECUTABLE_JAR := $(DISTDIR)/GM1Groupe1_Executable.jar

# Define the Java compiler and flags
JC := javac 
JAR := jar

JFLAGS := -Xlint:unchecked --module-path $(LIBDIR) --add-modules $(MODULES) -d $(BINDIR)
JARFLAGS := --create --module-path=$(LIBDIR) --verbose --manifest MANIFEST.MF --file $(USED_JAR)

# Find all .java files in the source directory and its subdirectories
SOURCES := $(shell find $(SRCDIR) -name '*.java')

# Define the default target for building the .class files and the used JAR file
all: $(DISTDIR) $(USED_JAR) $(EXECUTABLE_JAR) | $(LIBDIR)

$(USED_JAR): $(BINDIR) $(SOURCES:$(SRCDIR)/%.java=$(BINDIR)/%.class) MANIFEST.MF
	$(JAR) $(JARFLAGS) -C $(BINDIR) .

$(EXECUTABLE_JAR): $(USED_JAR) MANIFEST.MF | $(LIBDIR)
	$(JAR) --create --manifest MANIFEST.MF --file $(EXECUTABLE_JAR) -C $(BINDIR) . -C $(LIBDIR) .


# Define the target for building the archive JAR file
archive: clean $(ARCHIVE_JAR)

$(ARCHIVE_JAR): $(DISTDIR)
	@jar cvfm GM1Groupe9_Archive.jar MANIFEST.MF .
	@mv GM1Groupe9_Archive.jar $(ARCHIVE_JAR)

# Define the rule for compiling .java files into .class files
$(BINDIR)/%.class: $(SRCDIR)/%.java | $(BINDIR)
	$(JC) $(JFLAGS) -sourcepath $(SRCDIR) $< -d $(BINDIR)

# Define the rule for creating the output directory
$(BINDIR):
	mkdir -p $(BINDIR)

# Define the rule for creating the distribution directory
$(DISTDIR):
	mkdir -p $(DISTDIR)

# Define the target for cleaning the output directory and the distribution directory
clean:
	cp $(DATADIR)/lena.jpg $(DATADIR)/lion2.jpg $(DATADIR)/lion.jpg $(DATADIR)/chat.jpg $(DATADIR)/femmes.jpg $(BINDIR)/
	rm -rf $(DATADIR)/*
	cp $(BINDIR)/lena.jpg $(BINDIR)/lion2.jpg $(BINDIR)/lion.jpg $(BINDIR)/chat.jpg $(BINDIR)/femmes.jpg $(DATADIR)/
	rm -rf $(BINDIR)
	rm -rf $(DISTDIR)

run:
	@java --add-exports java.base/sun.nio.ch=ALL-UNNAMED --module-path $(LIBDIR) --add-modules $(MODULES) -jar $(USED_JAR)

.PHONY: all clean run archive



