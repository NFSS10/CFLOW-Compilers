package cflow;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DFAState implements Serializable {
 
    private static final long serialVersionUID = -55857686305273843L;
	private List<Integer> stateID;
	private Map<String, DFAState> nextState;
	private boolean accept;
	
	public DFAState()
	{
		this.setNextState(new HashMap <String, DFAState> ());
		this.accept = false;
	}
	
	public DFAState(List<Integer> ids)
	{
		this.stateID = ids;
		this.setNextState(new HashMap <String, DFAState> ());
		this.accept = false;
	}
	
	public Map<String, DFAState> getNextStates()
	{
		return this.nextState;
	}
	
	public List<Integer> getStateID() {
		return stateID;
	}

	public void setStateID(ArrayList<Integer> stateID) {
		this.stateID = stateID;
	}
	
	public void setNextState(HashMap<String, DFAState> hashMap) {
		this.nextState = hashMap;
	}
	
	public void createTransition(String c, DFAState state) throws Exception
	{
		if(nextState.containsKey(c))
		{
			if(nextState.get(c) != state)
				throw new Exception("Error found in the convertion from the nfa to dfa");
		}
		else
		{
			nextState.put(c, state);
		}		
	}
	
	public DFAState getTransition(String input) 
	{	
		if (this.nextState.get(input) == null)	
		{	
			return null;	
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
	
	@Override
	public boolean equals(Object obj) {
	    if (obj == null) {
	        return false;
	    }
	    if (!DFAState.class.isAssignableFrom(obj.getClass())) {
	        return false;
	    }
	    final DFAState other = (DFAState) obj;
	    
	    boolean equals = true;
        if (other.getStateID() != null && this.getStateID() != null)
        {
          if (other.getStateID().size() != other.getStateID().size())
              equals = false;
          else
              for (int i = 0; i < other.getStateID().size(); i++) 
              {
                  if (!this.getStateID().contains(other.getStateID().get(i))) {
                      equals = false;
                      break;
                  }                 
              }
        }
        else
        {
          equals = false;
        }
	    return equals;
	}
}
