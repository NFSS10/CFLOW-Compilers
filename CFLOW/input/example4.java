package input;

public class example4 {

   public static void main(String args[]) {
      //@BasicBlock a
	   String city[][]={ {"NewYork","Muscat","London"} , {"Cairo","Beijing","CapeTown"} };
		
		for (int i=0; i<city.length; i++)
		{
			//@BasicBlock b
			if(i == 0)
			{
				//@BasicBlock c
			}
			else
			{
				for (int j=0; j<city[i].length; j++)
				{
					//@BasicBlock d
				}
			}
		}
   }

}

