package custom.Threads;


import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;


public class ThreadController {
	float dataInput[];
	SortedSet<Integer> posMax, posMin;	//Won't have duplicates, they only store indexes
	
	
	private ThreadUnit slaveThreads[];
	private String commonThreadName;
	
	int threadsAtWork;	//To track number of unprocessed points
	
	public ThreadController(String threadGroupName){
		commonThreadName = threadGroupName;
		
		posMax = new TreeSet<Integer>();
		posMin = new TreeSet<Integer>();
	}
	
	//Controls threads
	public void startJob(float[] dataArr) {
		dataInput = dataArr;
		
		threadsAtWork = dataInput.length;
		
		posMax.clear();
		posMin.clear();
		
		//Create array of work units
		slaveThreads = new ThreadUnit[dataInput.length];
		//Initialize each one with its job
		for(int i = 0; i < dataInput.length; i++)
		{
			slaveThreads[i] = new ThreadUnit(this, commonThreadName + i, i);
		}
	}
	

	
	//Monitor method, only one process can use it
	synchronized void acceptData(int pos, byte result) {
		//Adds index to corresponding set.
		//They are sorted on insertion because of container choice
		switch (result){
			case (1):{
				posMax.add(Integer.valueOf(pos));
				break;
			}
			case (-1):{
				posMin.add(Integer.valueOf(pos));
				break;
			}
		}
		
		threadsAtWork--;
	}
	
	
	//Both return indexes of local max/min points(0 to N)
	public int[] getLocalMax() {
		if(threadsAtWork != 0)
		{
			return null;
		}
		
		//No easy way to convert Integer[] to int[]...
		Integer[] rawArr = posMax.toArray(new Integer[0]);
		int[] intArr = new int[rawArr.length];
		for(int i = 0;i < intArr.length;i++)
		{
			intArr[i] = rawArr[i].intValue();
		}
		
		return intArr;
	}
	
	public int[] getLocalMin() {
		if(threadsAtWork != 0)
		{
			return null;
		}
		
		//No easy way to convert Integer[] to int[]...
		Integer[] rawArr = posMin.toArray(new Integer[0]);
		int[] intArr = new int[rawArr.length];
		for(int i = 0;i < intArr.length;i++)
		{
			intArr[i] = rawArr[i].intValue();
		}
		
		return intArr;
	}
}
