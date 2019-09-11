package cflow;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NFAState {
	
	private int stateID;
	private Map<String, ArrayList<NFAState>> nextState;
	private boolean accept;
	
	public NFAState()
	{
		this.setNextState(new HashMap <String, ArrayList<NFAState>> ());
		this.accept = false;
	}
	
	public NFAState(int id)
	{
		this.stateID = id;
		this.setNextState(new HashMap <String, ArrayList<NFAState>> ());
		this.accept = false;
	}
	
	public int getStateID() {
		return stateID;
	}

	public void setStateID(int stateID) {
		this.stateID = stateID;
	}
	
	public void setNextState(HashMap<String, ArrayList<NFAState>> hashMap) {
		this.nextState = hashMap;
	}
	
	public void createTransition(String c, NFAState state)
	{
		if(nextState.containsKey(c))
		{
			nextState.get(c).add(state);
		}
		else
		{
			ArrayList<NFAState> array = new ArrayList<NFAState>();
			array.add(state);
			nextState.put(c, array);
		}		
	}
	
	public ArrayList<NFAState> getAllTransitions(String input) 
	{	
		if (this.nextState.get(input) == null)	
		{	
			return new ArrayList<NFAState> ();	
		}
		else 								
		{	
			return this.nextState.get(input);	
		}
	}
	
	public boolean isAccept() {
		return this.accept;
	}
	
	public void setAcceptState(boolean accept) {
		this.accept = accept;
	}
}
