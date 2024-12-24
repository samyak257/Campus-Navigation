# Variables
JUNIT_JAR=../junit5.jar
SRC_FILES=$(wildcard *.java)

# Targets
compile: Backend.java Frontend.java WebApp.java
	javac Backend.java
	javac Frontend.java
	javac -cp .:../junit5.jar WebApp.java

runServer: compile
	@echo "Running server..."
	sudo java WebApp 80

compileTests: BackendTests.java Backend.java
	javac Backend.java
	javac -cp .:../junit5.jar BackendTests.java

runTests: compileTests
	@echo "Running tests..."
	java -jar $(JUNIT_JAR) -cp . -c BackendTests


clean:
	@echo "Cleaning up class files..."
	rm -f *.class

%.class: %.java
	@echo "Compiling $<..."
	javac -cp $(JUNIT_JAR) $<
