package cflow;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class NFAtoDFA {
	private NFA nfa;
	private DFA dfa;
	private List<String> chars;
	public HashMap<Integer, Map<String, List<Integer>>> table;

	public NFAtoDFA(NFA nfa, List<String> chars)
	{
		this.nfa = nfa;
		this.chars = chars;
		this.table = new HashMap<Integer, Map<String, List<Integer>>>();
	}
	
	public void getClousure() throws Exception
	{
		for(int i = 0; i< nfa.getNFAStates().size();i++)
		{
			NFAState state = nfa.getNFAStates().get(i);
			for(int k = 0; k < chars.size();k++)
			{
				ArrayList<NFAState> transitions = state.getAllTransitions(chars.get(k));
				
				if(table.containsKey(state.getStateID()))
				{
					List<Integer> list = new ArrayList<Integer>();
					
					for(int j = 0; j < transitions.size(); j++)
						if(!list.contains(transitions.get(j).getStateID()))
							list.add(transitions.get(j).getStateID());
					
					table.get(state.getStateID()).put(chars.get(k), list);
				}
				else
				{
					HashMap<String, List<Integer>> map = new HashMap<String, List<Integer>>();
					List<Integer> list = new ArrayList<Integer>();
					
					for(int j = 0; j < transitions.size(); j++)
						if(!list.contains(transitions.get(j).getStateID()))
							list.add(transitions.get(j).getStateID());
					
					map.put(chars.get(k), list);
					
					table.put(state.getStateID(), map);
				}
			}
			
			// Epsilon clousure
			ArrayList<NFAState> epsilon = state.getAllTransitions("epsilon");
			
			if(table.get(state.getStateID()).containsKey("epsilon"))
			{
				List<Integer> list = table.get(state.getStateID()).get("epsilon");
					
				if(!list.contains(state.getStateID()))
					list.add(state.getStateID());
					
				while(epsilon.size() != 0)
				{
					list.add(epsilon.get(0).getStateID());
					ArrayList<NFAState> epsilonSon = epsilon.get(0).getAllTransitions("epsilon");
					for(int p = 0; p < epsilonSon.size();p++)
						epsilon.add(epsilonSon.get(p));
					epsilon.remove(0);
				}
			}
			else
			{
				Map<String, List<Integer>> map = table.get(state.getStateID());
				
				List<Integer> list = new ArrayList<Integer>();
					
				if(!list.contains(state.getStateID()))
					list.add(state.getStateID());
					
				while(epsilon.size() != 0)
				{
					if(!list.contains(epsilon.get(0).getStateID()))
						list.add(epsilon.get(0).getStateID());
					ArrayList<NFAState> epsilonSon = epsilon.get(0).getAllTransitions("epsilon");
					for(int p = 0; p < epsilonSon.size();p++)
						epsilon.add(epsilonSon.get(p));
					epsilon.remove(0);
				}
					
				map.put("epsilon", list);
					
				table.put(state.getStateID(), map);
			}
		}
	}
	
	public void constructDFA()
	{
		this.dfa = new DFA();
		NFAState state = this.nfa.getNFAStates().getFirst();
		
		Stack<DFAState> stack = new Stack<DFAState>();
		List<Integer> clousureFirst = this.table.get(state.getStateID()).get("epsilon");
		
		DFAState dfastate = new DFAState(clousureFirst);
		this.dfa.getDFAStates().addLast(dfastate);
		stack.push(dfastate);
		
		while(!stack.isEmpty())
		{
			DFAState stateAux = stack.pop();
			
			for(int k = 0; k < this.chars.size(); k++)
			{
				List<Integer> aux = new ArrayList<Integer>();
				for(int i = 0; i < stateAux.getStateID().size(); i++)
				{
					List<Integer> l = this.table.get(stateAux.getStateID().get(i)).get(this.chars.get(k));
					aux = appendList(aux, l);
				}
				
				List<Integer> newState = new ArrayList<Integer>();
				for(int i = 0; i < aux.size(); i++)
				{
					List<Integer> list = this.table.get(aux.get(i)).get("epsilon");
					newState = appendList(newState, list);
				}

				if(!newState.isEmpty())
				{
					DFAState dfanewstate = this.dfa.containsState(newState);
					if(dfanewstate == null)
					{
						dfanewstate = new DFAState(newState);
						try {
							stateAux.createTransition(this.chars.get(k), dfanewstate);
							
						} catch (Exception e) {
							e.printStackTrace();
						}
						this.dfa.getDFAStates().addLast(dfanewstate);
						stack.push(dfanewstate);
					}
					else
					{					
						try {
							stateAux.createTransition(this.chars.get(k), dfanewstate);
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				
			}
		}
		
		// Associate end states to the DFA
		for(int i = 0; i < this.nfa.getNFAStates().size();i++)
		{
			List<NFAState> nfaStates = this.nfa.getNFAStates();
			if(nfaStates.get(i).isAccept())
			{
				for(int k = 0; k < this.dfa.getDFAStates().size(); k++) {
					List<DFAState> dfaStates = this.dfa.getDFAStates();
					if(dfaStates.get(k).getStateID().contains(nfaStates.get(i).getStateID()))
						dfaStates.get(k).setAcceptState(true);
				}
			}
		}
	}
	
	public DFA getDFA()
	{
	      try
	      {
	    	  getClousure();
	      }
	      catch(Exception e)
	      {
	    	  System.out.println("Error found!");
	    	  System.out.println(e.getMessage());
	      }
	      
	      constructDFA();
	    		  
	      return this.dfa;
	}
	
	 public List<Integer> appendList(List<Integer> l1, List<Integer> l2)
	 {
		 for(int i = 0; i < l2.size();i++)
			 if(!l1.contains(l2.get(i)))
				 l1.add(l2.get(i));
		 
		 return l1;
	 }

}
