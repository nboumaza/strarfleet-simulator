# Problem Statement
Starfleet Academy requires that all cadets take a course on programming robotic mine clearing vessels. You have been selected to teach this course.

The mine clearing vessels are very simple vehicles that move forward at a constant rate, can fire volleys of photon torpedoes, and can make some very simple navigational changes.

Cadets at the academy are given an initial layout of the cuboid of space they are to clear and submit a “script” of firing patterns and moves. You, as their instructor, are to give them feedback on how well their scripts clear the space of mines.

Please refer to for the complete description: 
	https://drive.google.com/file/d/0B8ogWxgo3PyKbm52UnBzQjFROFU/view?usp=sharing
 

# Solution Overview

	TODO... will enclose a high level UML very soon


# Prerequisites to Building

* Java 8
* Maven  

# Building the Artifact 

Once you clone the source of this repo, use the following command to build 
	
	mvn clean install


# Running the Simulator
	usage:  java Simulator <fieldFilePathName> <scriptFilePathName>
	where:
	    <fieldFilePathName> is the absolute file path of the field input file
	    <scriptFilePathName> is the absolute file path of the script input file
	    
