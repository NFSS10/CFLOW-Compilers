package cflow;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class DFA implements Serializable {
 
    private static final long serialVersionUID = -55857686305273843L;
	private LinkedList<DFAState> DFA;
	
	public DFA()
	{
		DFA = new LinkedList<DFAState> ();
		DFA.clear();
	}
	
	public LinkedList<DFAState> getDFAStates() 
	{
		return DFA;
	}

	public void setNFA(LinkedList<DFAState> DFA) 
	{
		this.DFA = DFA;
	}
	
	public boolean addState(DFAState state)
	{
		if(this.DFA.contains(state))
		{
			return false;
		}
		else
		{
			this.DFA.addLast(state);
			return true;
		}
	}
	
	public DFAState containsState(List<Integer> l)
	{
		for(int i = 0; i < getDFAStates().size();i++)
			if(getDFAStates().get(i).equals(new DFAState(l)))
				return getDFAStates().get(i);
		
		return null;
	}
	
	public boolean check(String input)
	{
		DFAState state = this.DFA.getFirst();
		
		for(int i = 0; i < input.length(); i++)
		{
			state = state.getTransition(Character.toString(input.charAt(i)));
			
			if(state == null)
				return false;
		}
		
		if(state.isAccept())
			return true;
		else
			return false;
	}
}
