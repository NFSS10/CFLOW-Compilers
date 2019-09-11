package cflow;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import weaver.gui.KadabraLauncher;

public class Cflow {
	
	private static Stack<NFA> NFA_Stack = new Stack<NFA> ();
	private static int stateID = 0;
	private static List<String> chars = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
	private static String laraDir = "Cflow.lara";

	 public static void main(String args [])
	 {
		 RegexParser parse = new RegexParser(System.in);
		 SimpleNode root = null;
	    
		 System.out.println("Starting...");
		 System.out.print("Enter a regular expression in PCRE format : ");
		 createDFA(root);	  
	 }

	private static void createDFA(SimpleNode root) 
	{
		try
		{
			root = RegexParser.check();
		}
		catch (Exception e)
		{
			System.out.println("Regular expression is not in PCRE format!");
			System.out.println(e.getMessage());
	        RegexParser.ReInit(System.in);
	        System.exit(-1);
		}
		catch (Error e)
		{
	        System.out.println("ERROR_FOUND");
	        System.out.println(e.getMessage()); 
	        System.exit(-1);
		}
		
		try
		{
			Scanner sc = new Scanner(System.in);
			
			System.out.println("Enter the directory of the java file to be outputed: ");
			System.out.print(">> ");
			String outputDir = sc.nextLine();
			
			// Create dir
			File theDir = new File(outputDir);
			
			if (!theDir.exists()) 
			{
				try{
					 theDir.mkdir();
				} 
				catch(SecurityException se){
					System.out.println("Error creating the output directory...");
					System.out.println("Exiting");
					return;
				}   
			}
			
			// DFA create
			SimpleNode son = (SimpleNode) root.children[0];
			NFA finalNFA = createNFA(son);
		      
			NFAtoDFA d = new NFAtoDFA(finalNFA, chars);
		      
			DFA dfa = d.getDFA();
			
			String result = null;
			Boolean firstTime = true;
			
			System.out.println("Do you want to generate a new java file?(y/n)");
			System.out.print(">> ");
			
			do{
				if(!firstTime)
				{
					System.out.println("Wrong input. Please choose again...(y/n)");
					System.out.print(">> ");
				}
				
				result = sc.nextLine().toUpperCase();
				firstTime = false;
			}
			while(!result.equals("Y") && !result.equals("N"));
			
			if(result.equals("Y"))
			{
				System.out.println("Enter the directory of the input java file: ");
				System.out.print(">> ");
				String inputDir = sc.nextLine();
		
				String[] kadabraArgs = {laraDir, "-p", inputDir, "-o", outputDir, "-b", "2"};
				KadabraLauncher.execute(kadabraArgs);
			}
			
			String serializeDir = outputDir + "\\dfa.ser";
			try {
				SerializeDFA.serialize(dfa, serializeDir);
			} catch (Exception e) {
				System.out.println("There was a problem trying to serialize the dfa...");
				System.out.println("Exiting");
				return;
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(-1);
		}
	}

	 public static NFA createNFA(SimpleNode node)
	 {
		 // Clears the stack of NFA's
		 NFA_Stack.clear();
		 
		 makeNFA(node);
		 NFA finalNFA = NFA_Stack.pop();
		 finalNFA.getNFAStates().get(finalNFA.getNFAStates().size() - 1).setAcceptState(true);
	      
		 return finalNFA;
	 }
	 
	 // Recursive method that goes through the tree
	 public static void makeNFA(SimpleNode node)
	 { 
		 switch(node.toString())
		 {
		 case "AND":
			 concatenation(node);
			 break;
		 case "OR":
			 reunion(node);
			 break;
		 case "Mult":
			 mult(node);
			 break;
		 case "Char":
			 symbol(node);
			 break;
		 case "Any":
			 anything(node);
		 case "Bracket":
			 bracket(node);
			 break;
		 case "BracketC":
			 bracketCarret(node);
			 break;
		 default:
			 break;
		 }
	 }
	 
	 public static void concatenation(SimpleNode node)
	 {
		 for(int i = 0; i < node.jjtGetNumChildren(); i++)
		 {
			 SimpleNode n = (SimpleNode)node.jjtGetChild(i);
		        if (n != null) {
		        	makeNFA(n);
		        	
		        	if(i != 0)
		        	{
		        		// Pops the previous nfa's
		        		NFA nfa2 = NFA_Stack.pop();
		        		NFA nfa1 = NFA_Stack.pop();
		        		
		        		nfa1.getNFAStates().getLast().createTransition("epsilon", nfa2.getNFAStates().getFirst());
		        		
		        		for (NFAState s : nfa2.getNFAStates()) 
		        			nfa1.getNFAStates().addLast(s); 

		        		NFA_Stack.push(nfa1);
		        	}
		        }
		 }
	 }
	 
	 public static void reunion(SimpleNode node)
	 {
		 for(int i = 0; i < node.jjtGetNumChildren(); i++)
		 {
			 SimpleNode n = (SimpleNode)node.jjtGetChild(i);
		        if (n != null) {
		        	makeNFA(n);
		        	
		        	if(i != 0)
		        	{
		        		// Pops the previous nfa's
		        		NFA nfa2 = NFA_Stack.pop();
		        		NFA nfa1 = NFA_Stack.pop();
		        		
		        		NFAState start = new NFAState(stateID++);
		        		NFAState end = new NFAState(stateID++);
		        		
		        		start.createTransition("epsilon", nfa1.getNFAStates().getFirst());
		        		start.createTransition("epsilon", nfa2.getNFAStates().getFirst());

		        		nfa1.getNFAStates().getLast().createTransition("epsilon", end);
		        		nfa2.getNFAStates().getLast().createTransition("epsilon", end);
		        		
		        		// Adds the created states to the nfa's
		        		nfa1.getNFAStates().addFirst(start);
		        		nfa2.getNFAStates().addLast(end);
		        		
		        		for (NFAState state : nfa2.getNFAStates())
		        			nfa1.getNFAStates().addLast(state);

		        		NFA_Stack.push(nfa1);
		        	}
		        }
		 }
	 }
	 
	 public static void bracket(SimpleNode node)
	 {
		 SimpleNode bracketElems = (SimpleNode)node.children[0];
		 
		 String firstElement = Character.toString(bracketElems.val.charAt(0));
		 String lastElement = Character.toString(bracketElems.val.charAt(2));
		 
		 NFA nfa = new NFA();
		 NFAState start = new NFAState(stateID++);
		 NFAState end = new NFAState(stateID++);
		 
		 Boolean first = true;
		 for(int i = 0; i < chars.size(); i++)
		 { 
			 if(chars.get(i).equals(lastElement))
			 {
				start.createTransition(chars.get(i), end);
				break;
			 }
			 
			 if(chars.get(i).equals(firstElement) && first)
			 {
				 first = false;
				 start.createTransition(chars.get(i), end);
				 
			 }
			 else if (first == false)
			 {
				 start.createTransition(chars.get(i), end);
			 }
		 }
		 
		 LinkedList<NFAState> states = new LinkedList<NFAState>();
		 states.add(start);
		 states.add(end);
		 nfa.setNFA(states);
		 
		 NFA_Stack.push(nfa);
	 }
	 
	 public static void bracketCarret(SimpleNode node)
	 {
		 SimpleNode bracketElems = (SimpleNode)node.children[0];
		 
		 int firstElement = (int) bracketElems.val.charAt(0);
		 int lastElement = (int) bracketElems.val.charAt(2);
		 
		 NFA nfa = new NFA();
		 NFAState start = new NFAState(stateID++);
		 NFAState end = new NFAState(stateID++);
		 
		 for(int i = 0; i < chars.size(); i++)
		 { 
			 int asciiChar = (int) chars.get(i).charAt(0);
			 
			 if(asciiChar < firstElement || asciiChar > lastElement)
				 start.createTransition(chars.get(i), end);
		 }
		 
		 LinkedList<NFAState> states = new LinkedList<NFAState>();
		 states.add(start);
		 states.add(end);
		 nfa.setNFA(states);
		 
		 NFA_Stack.push(nfa);
	 }
	 
	 public static void mult(SimpleNode node)
	 {
		 switch(node.val)
		 {
		 case "*":
			 star(node);
			 break;
		 case "+":
			 plus(node);
			 break;
		 case "?":
			 interrogation(node);
			 break;
		default:
			 keys(node);
			 break;
		 }
	 }
	 
	 public static void star(SimpleNode node)
	 {
		 // Recursive method to create the nfa
		 makeNFA((SimpleNode)node.children[0]);
		 
		 // Pops the previous nfa
		 NFA nfa = NFA_Stack.pop();
		 
		 // Creates two states to connect the epsilon transitions
		 NFAState start = new NFAState(stateID++);
		 NFAState end = new NFAState(stateID++);
		 
		 // Create transition to the end state
		 start.createTransition("epsilon", end);
		 
		 // Create transition from the start to the first from the previous nfa
		 start.createTransition("epsilon", nfa.getNFAStates().getFirst());
		
		 // Create transition from the last state from the previous nfa to the end state
		 nfa.getNFAStates().getLast().createTransition("epsilon", end);
			 
		 // Create transition from the last state from the previous nfa to the first state
		 nfa.getNFAStates().getLast().createTransition("epsilon", nfa.getNFAStates().getFirst());
			
		 // Adds the created states to the nfa
		 nfa.getNFAStates().addFirst(start);
		 nfa.getNFAStates().addLast(end);
			
		 NFA_Stack.push(nfa);
	 }
	 
	 public static void plus(SimpleNode node)
	 {
		// Recursive method to create the nfa
		makeNFA((SimpleNode)node.children[0]);
		star(node);
		
		// Pops the previous nfa's
		NFA nfa1 = NFA_Stack.pop();
		NFA nfa2 = NFA_Stack.pop();
				 
		// Creates two states to connect the epsilon transitions
		NFAState start = new NFAState(stateID++);
		NFAState end = new NFAState(stateID++);
				 
		// Create transition from the start to the first from the previous nfa
		start.createTransition("epsilon", nfa1.getNFAStates().getFirst());

		// Create transition from the last state from the previous nfa to the end state
		nfa1.getNFAStates().getLast().createTransition("epsilon", end);
		
		// Create transition from the end state from the previous nfa to the first from nfa2
		end.createTransition("epsilon", nfa2.getNFAStates().getFirst());
					
		// Adds the created states to the nfa
		nfa1.getNFAStates().addFirst(start);
		nfa1.getNFAStates().addLast(end);
		
		for (NFAState state : nfa2.getNFAStates())
			nfa1.getNFAStates().addLast(state);
					
		NFA_Stack.push(nfa1);
	 }
	 
	 public static void interrogation(SimpleNode node)
	 {
		 // Recursive method to create the nfa
		 makeNFA((SimpleNode)node.children[0]);
		 
		 // Pops the previous nfa
		 NFA nfa = NFA_Stack.pop();
		 
		 // Creates two states to connect the epsilon transitions
		 NFAState start = new NFAState(stateID++);
		 NFAState end = new NFAState(stateID++);
		 
		 // Create transition from the start to the end
		 start.createTransition("epsilon", end);
		 
		 // Create transition from the start to the first from the previous nfa
		 start.createTransition("epsilon", nfa.getNFAStates().getFirst());
		
		 // Create transition from the last to the end
		 nfa.getNFAStates().getLast().createTransition("epsilon", end);
			
		// Adds the created states to the nfa
		 nfa.getNFAStates().addFirst(start);
		 nfa.getNFAStates().addLast(end);
			
		 NFA_Stack.push(nfa);
	 }
	 
	 public static void keys(SimpleNode node)
	 {
		 String withoutKey = node.val.substring(1, node.val.length()-1);
		 String[] array = withoutKey.split(",", -1);
		 
		 if(array.length == 1 || array[0].equals(array[1]))
		 {
			 Integer value = Integer.parseInt(array[0]);
			 
			 if(value == 0)
			 {
				 NFA nfa = new NFA();
				 NFAState start = new NFAState(stateID++);
				 NFAState end = new NFAState(stateID++);
				
				 start.createTransition("epsilon", end);
				 
				 LinkedList<NFAState> states = new LinkedList<NFAState>();
				 states.add(start);
				 states.add(end);
				 nfa.setNFA(states);
				 
				 NFA_Stack.push(nfa);
			 }
			 else
			 {
				 for(int i = 0; i < value; i++)
				 {
					 // Recursive method to create the nfa
					 makeNFA((SimpleNode)node.children[0]);
					 
					 if(i != 0)
					 {
						 // Pops the previous nfa's
						 NFA nfa1 = NFA_Stack.pop();
						 NFA nfa2 = NFA_Stack.pop();
										 
						 // Creates two states to connect the epsilon transitions
						 NFAState start = new NFAState(stateID++);
						 NFAState end = new NFAState(stateID++);
										 
						 // Create transition from the start to the first from the previous nfa
						 start.createTransition("epsilon", nfa1.getNFAStates().getFirst());
				
						 // Create transition from the last state from the previous nfa to the end state
						 nfa1.getNFAStates().getLast().createTransition("epsilon", end);
								
						 // Create transition from the end state from the previous nfa to the first from nfa2
						 end.createTransition("epsilon", nfa2.getNFAStates().getFirst());
											
						 // Adds the created states to the nfa
						 nfa1.getNFAStates().addFirst(start);
						 nfa1.getNFAStates().addLast(end);
								
						 for (NFAState state : nfa2.getNFAStates())
							 nfa1.getNFAStates().addLast(state);
											
						 NFA_Stack.push(nfa1);
					 }
				 }
			 }
		 }
		 else if(!array[0].equals("") && !array[1].equals(""))
		 {
			 Integer minValue = Integer.parseInt(array[0]);
			 Integer maxValue = Integer.parseInt(array[1]);
			 
			 for(int i = 0; i < minValue; i++)
			 {
				 // Recursive method to create the nfa
				 makeNFA((SimpleNode)node.children[0]);
				 
				 if(i != 0)
				 {
					 // Pops the previous nfa's
					 NFA nfa1 = NFA_Stack.pop();
					 NFA nfa2 = NFA_Stack.pop();
									 
					 // Creates two states to connect the epsilon transitions
					 NFAState start = new NFAState(stateID++);
					 NFAState end = new NFAState(stateID++);
									 
					 // Create transition from the start to the first from the previous nfa
					 start.createTransition("epsilon", nfa1.getNFAStates().getFirst());
			
					 // Create transition from the last state from the previous nfa to the end state
					 nfa1.getNFAStates().getLast().createTransition("epsilon", end);
							
					 // Create transition from the end state from the previous nfa to the first from nfa2
					 end.createTransition("epsilon", nfa2.getNFAStates().getFirst());
										
					 // Adds the created states to the nfa
					 nfa1.getNFAStates().addFirst(start);
					 nfa1.getNFAStates().addLast(end);
							
					 for (NFAState state : nfa2.getNFAStates())
						 nfa1.getNFAStates().addLast(state);
										
					 NFA_Stack.push(nfa1);
				 }
			 }
			 
			 for(int i = 0; i < (maxValue - minValue); i++)
			 {
				 // Recursive method to create the nfa
				 makeNFA((SimpleNode)node.children[0]);
				 
				 if(i != 0)
				 {
					 // Pops the previous nfa's
					 NFA nfa1 = NFA_Stack.pop();
					 NFA nfa2 = NFA_Stack.pop();
									 
					 // Creates two states to connect the epsilon transitions
					 NFAState start = new NFAState(stateID++);
					 NFAState end = new NFAState(stateID++);
									 
					 // Create transition from the start to the first from the previous nfa
					 start.createTransition("epsilon", nfa1.getNFAStates().getFirst());
					 
					 // Create transition from the last state from the previous nfa to the end state
					 nfa1.getNFAStates().getLast().createTransition("epsilon", end);
							
					 // Create transition from the end state from the previous nfa to the first from nfa2
					 end.createTransition("epsilon", nfa2.getNFAStates().getFirst());
					 
					 // Create transition from the start to the end
					 start.createTransition("epsilon", nfa2.getNFAStates().getLast());
										
					 // Adds the created states to the nfa
					 nfa1.getNFAStates().addFirst(start);
					 nfa1.getNFAStates().addLast(end);
							
					 for (NFAState state : nfa2.getNFAStates())
						 nfa1.getNFAStates().addLast(state);
										
					 NFA_Stack.push(nfa1);
				 }
			 }
			 
			 if(minValue != 0)
			 {
				 ////// Put NFA's together
				 
				 // Pops the previous nfa's
				 NFA nfa1 = NFA_Stack.pop();
				 NFA nfa2 = NFA_Stack.pop();
				 
				 nfa2.getNFAStates().getLast().createTransition("epsilon", nfa1.getNFAStates().getFirst());
				 nfa2.getNFAStates().getLast().createTransition("epsilon", nfa1.getNFAStates().getLast());
				 
				 for (NFAState state : nfa1.getNFAStates())
					 nfa2.getNFAStates().addLast(state);
				 
				 NFA_Stack.push(nfa2);
			 }
			 
		 } 
		 else if(!array[0].equals("") && array[1].equals(""))
		 {
			Integer value = Integer.parseInt(array[0]);
				
			if(value == 0)
			{
				star(node);
			}
			else
			{
				star(node);

				for(int i = 0; i < value; i++)
				{
					// Recursive method to create the nfa
					makeNFA((SimpleNode)node.children[0]);
					
					// Pops the previous nfa's
					NFA nfa1 = NFA_Stack.pop();
					NFA nfa2 = NFA_Stack.pop();
								 
					// Creates two states to connect the epsilon transitions
					NFAState start = new NFAState(stateID++);
					NFAState end = new NFAState(stateID++);
								 
					// Create transition from the start to the first from the previous nfa
					start.createTransition("epsilon", nfa1.getNFAStates().getFirst());
		
					// Create transition from the last state from the previous nfa to the end state
					nfa1.getNFAStates().getLast().createTransition("epsilon", end);
						
					// Create transition from the end state from the previous nfa to the first from nfa2
					end.createTransition("epsilon", nfa2.getNFAStates().getFirst());
									
					// Adds the created states to the nfa
					nfa1.getNFAStates().addFirst(start);
					nfa1.getNFAStates().addLast(end);
						
					for (NFAState state : nfa2.getNFAStates())
						nfa1.getNFAStates().addLast(state);
									
					NFA_Stack.push(nfa1);
				}
			}
		 }
	 }
	 
	 // Stopping method 
	 public static void symbol(SimpleNode node)
	 {
		 NFA nfa = new NFA();
		 NFAState start = new NFAState(stateID++);
		 NFAState end = new NFAState(stateID++);

		 start.createTransition(node.val, end);
		 LinkedList<NFAState> states = new LinkedList<NFAState>();
		 states.add(start);
		 states.add(end);
		 nfa.setNFA(states);
		 
		 NFA_Stack.push(nfa);
	 }
	 
	 public static void anything(SimpleNode node)
	 {
		 NFA nfa = new NFA();
		 NFAState start = new NFAState(stateID++);
		 NFAState end = new NFAState(stateID++);
		 
		 for(int i = 0; i < chars.size(); i++)
			 start.createTransition(chars.get(i), end);
		 
		 LinkedList<NFAState> states = new LinkedList<NFAState>();
		 states.add(start);
		 states.add(end);
		 nfa.setNFA(states);
		 
		 NFA_Stack.push(nfa);
	 }

}
