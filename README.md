# findwordpositions-hashtableguava

This program implements word dictionary with positions stored. It implements something similar to the
first half of the following link:
 
http://www.ardendertat.com/2011/12/20/programming-interview-questions-23-find-word-positions-in-text/

The structure of the code flow is that a given text file is read by each line and looks at each word within the line and stores it location/position.

The main data structure is google's hashtable implementation called 'Table' from Guava Java Library. 

NOTE: This problem stores the first character position of the word within the line number.
It does not store the occurence position of the entire string word.

For instance,

"test frank giordano" 

test located at line 1, position 0
frank located at line 1, position 5
etc

whereas all the words positions would be:
test located at position 0 and frank located at position 1 and giordano located at position 3

## Setup, Installation, and running the application:

1 - Install Java 8

2 - Install Maven 3.6.3 or higher

At project's root directory, perform the following commands:

3 - mvn clean install

4 - java -jar .\target\FindWord-1.0-SNAPSHOT-jar-with-dependencies.jar
