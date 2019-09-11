package cflow;
import java.util.LinkedList;

public class NFA {
	private LinkedList<NFAState> NFA;
	
	public NFA()
	{
		this.NFA = new LinkedList<NFAState> ();
		this.NFA.clear();
	}
	
	public LinkedList<NFAState> getNFAStates() 
	{
		return this.NFA;
	}

	public void setNFA(LinkedList<NFAState> NFA) 
	{
		this.NFA = NFA;
	}

}
