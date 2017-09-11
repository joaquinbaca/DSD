#!/bin/bash

javac *.java

java -cp . -Djava.security.policy=server.policy Cliente

