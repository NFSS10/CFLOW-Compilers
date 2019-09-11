package input;

public class example2 {

	public static void main(String[] args) {
		//@BasicBlock a
		int i = 0;
		for(i = 0; i < 10; i++){//@BasicBlock b
			
		 	if(i % 2 == 0){
				//@BasicBlock c
			}else{
				//@BasicBlock d
			}
		}
		//@BasicBlock e
	}

}
