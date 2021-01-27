

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
// Note that you MUST not use any other `thread safe` classes than the two imported above

public class JobDispatcher implements Dispatcher {
	
	//Declare your local variables and objects here


	@Override
	public void specifyJob(int nComputeThreads, int nStorageThreads) {
		//Your code here
	}

	@Override
	public void queueComputeThread() {
		//Your code here
	}

	@Override
	public void queueStorageThread() {
		//Your code here
	}

	

	class PrivateClass1 {
		//Add any private classes that you need
	}

}
