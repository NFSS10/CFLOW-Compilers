

package input;


public class example4 {
    public static String flow = "";
    public static void main(String[] args) {
        // @BasicBlock a
        flow+="a";
        String[][] city = new String[][]{ new String[]{ "NewYork" , "Muscat" , "London" } , new String[]{ "Cairo" , "Beijing" , "CapeTown" } };
        for (int i = 0; i < (city.length); i++) {
            // @BasicBlock b
            flow+="b";
            if (i == 0) {
                // @BasicBlock c
                flow+="c";
            }else {
                for (int j = 0; j < (city[i].length); j++) {
                    // @BasicBlock d
                    flow+="d";
                }
            }
        }
        cflow.DFA dfa = null;
        dfa = cflow.SerializeDFA.deserialize("dfa.ser");
        Boolean passed = dfa.check(flow);
        if(passed)
        System.out.println("PASSOU");
        else
        System.out.println("NAO PASSOU");
    }
}

