

package input;


public class example3 {
    public static String flow = "";
    public static void main(String[] args) {
        // @BasicBlock a
        flow+="a";
        example3.printMax(34, 3, 3, 2, 56.5);
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

    public static void printMax(double... numbers) {
        if ((numbers.length) == 0) {
            // @BasicBlock b
            flow+="b";
        }
        double result = numbers[0];
        for (int i = 1; i < (numbers.length); i++) {
            // @BasicBlock c
            flow+="c";
            if ((numbers[i]) > result) {
                // @BasicBlock d
                flow+="d";
                result = numbers[i];
            }
        }
    }
}

