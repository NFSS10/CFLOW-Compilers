

package input;


public class example2 {
    public static String flow = "";
    public static void main(String[] args) {
        // @BasicBlock a
        flow+="a";
        int i = 0;
        for (i = 0; i < 10; i++) {
            // @BasicBlock b
            flow+="b";
            if ((i % 2) == 0) {
                // @BasicBlock c
                flow+="c";
            }else {
                // @BasicBlock d
                flow+="d";
            }
        }
        // @BasicBlock e
        flow+="e";
        cflow.DFA dfa = null;
        dfa = cflow.SerializeDFA.deserialize("dfa.ser");
        Boolean passed = dfa.check(flow);
        if(passed)
        System.out.println("PASSOU");
        else
        System.out.println("NAO PASSOU");
    }
}

