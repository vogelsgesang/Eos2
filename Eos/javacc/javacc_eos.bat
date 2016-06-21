@echo off
java -Xmx4096M -Xms4096M -classpath "lib\javacc.jar" javacc eos_de.jj
java -Xmx4096M -Xms4096M -classpath "lib\javacc.jar" javacc eos_en.jj
pause