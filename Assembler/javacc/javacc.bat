@echo off
java -Xmx4096M -Xms4096M -classpath "lib\javacc.jar" javacc asm.jj
pause