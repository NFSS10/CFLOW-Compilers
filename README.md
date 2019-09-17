# COMP - Compilers
Projects made in Compilers class - MIEIC 3y2s 

## CFLOW
This project aims to verify the control of the execution of a program. The application generates a deterministic finite automaton according to the initially introduced regular expression, which checks whether a program coprrectly checks against that same regular expression. Before the program being processed by Kadabra, it must be properly commented with the "basic blocks" that marks the DFA transactions. This allows the generation of the automaton which is serialized and, with the help of Kadabra, injected into the program that we are verifying. It is also created a string that accumulates all transactions that in the end is verified against the automaton.

### Overview
The application consists of three main phases up to automate generation. The first is the regular expression parser in PCRE format, the second is the creation of e-NFA from the parser-generated abstract tree, and the third is the conversion of e-NFA to DFA.
Thompson's Algorithm was used to create e-NFA and e-clousure was converted to e-NFA.

### Compile
To compile you need ![kadabra.jar](http://specs.fe.up.pt/tools/kadabra.jar), place it in the project root and in eclipse do: *right click in kadanra.jar* -> Build Path > Add to Build Path
This allows to directly work and compile.

To test the test code flow it is necessary to always have the file "dfa.ser" in the same directory as the test file you want to run.

To run the testing program, you must also add "DFA.jar" the same way you previously added Kadabra, to the file that was made as output, as this file contains the classes responsible by the DFA transactions.

After all this, you just need to run the file that was made as output and verify whether or not it passes the regular expression.

### How to run
To run the application you must compile all java files and run the Cflow class that will ask you what is the regular expression you want, what is the prefered output to serialize the DFA and optionally, if you pretend to process an input file with Kadabra.

#### PROS:
Accepts all regular expressions in PCRE. Ex: [A-z], [^A-z], [0-9], {9,}, etc.

#### CONS:
You need to serialize the created automaton object and deserialize to verify if the program runs the desired regular expression.
