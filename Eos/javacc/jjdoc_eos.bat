@echo off
java -classpath "lib\javacc.jar" jjdoc eos_de.jj
java -classpath "lib\javacc.jar" jjdoc eos_en.jj
pause