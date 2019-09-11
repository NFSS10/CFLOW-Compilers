

// default package (CtPackage.TOP_LEVEL_PACKAGE_NAME in Spoon= unnamed package)



public class example1 {
    public static String flow = "";
    public static void main(String[] args) {
        // @BasicBlock a
        flow+="a";
        int i = 0;
        while (i < 10) {
            // @BasicBlock b
            flow+="b";
            i++;
        } 
        // @BasicBlock c
        flow+="c";
        cflow.DFA dfa = null;
        dfa = cflow.SerializeDFA.deserialize("dfa.ser");
        Boolean passed = dfa.check(flow);
        if(passed)
        System.out.println("PASSOU");
        else
        System.out.println("NAO PASSOU");
    }
}

