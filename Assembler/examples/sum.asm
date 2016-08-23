//sum 1 to 10
data
i: 0
sum: 0

code  
  LOADI 10
  STORE i
  LOADI 0
  STORE sum
  JMP cond
  
body:
  LOAD i
  INC  
  STORE i
  ADD sum
  STORE sum
cond: 
  LOAD i
  CMP 0
  JMPNE body
  HLT  