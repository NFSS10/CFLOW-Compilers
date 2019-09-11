package input;

public class example3 {

   public static void main(String args[]) {
      //@BasicBlock a
	   printMax(34, 3, 3, 2, 56.5);
	  //@BasicBlock e
   }

   public static void printMax( double... numbers) {
      if (numbers.length == 0) {
         //@BasicBlock b
         
      }

      double result = numbers[0];

      for (int i = 1; i <  numbers.length; i++)
	  {
		  //@BasicBlock c
		  if (numbers[i] >  result)
		  {
			  //@BasicBlock d
			  result = numbers[i];
		  }
	  }
   }
}