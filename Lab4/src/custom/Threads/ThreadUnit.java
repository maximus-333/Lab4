package custom.Threads;





class ThreadUnit implements Runnable{
	private Thread thr;
	private String name;
	private ThreadController master;
	private int offset;
	
	ThreadUnit(ThreadController owner, String name, int offset){
		this.name = name;
		thr = new Thread(this, this.name);
		master = owner;
		this.offset = offset;
		
		thr.start();
	}
	
	private byte processData() {
		//corners can't be local min/max
		if(offset == master.dataInput.length - 1
		|| offset == 0)
		{
			return 0;
		}
	
		if(master.dataInput[offset] > master.dataInput[offset - 1] 
		&& master.dataInput[offset] > master.dataInput[offset + 1])
		{
			return 1;
		}
		else if(master.dataInput[offset] < master.dataInput[offset - 1] 
			 && master.dataInput[offset] < master.dataInput[offset + 1])
		{
			return -1;
		}
		
		return 0;
	}
	
	@Override
	public void run() {
		master.acceptData(offset, processData());
	}

}
