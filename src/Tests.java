import static org.junit.jupiter.api.Assertions.*;



/**
 * @author mikec
 *
 */
class Tests {

	@org.junit.jupiter.api.Test
	//Example Test
	void test() {
		JobDispatcher dispatcher = new JobDispatcher();
		
		//Specify job for 3 Compute threads and 0 Storage threads
		dispatcher.specifyJob(3, 0);
		
		//But start only one Compute thread:
		Thread computeThread = new Thread() {			
			public void run () {
				dispatcher.queueComputeThread();
			}
		};	
		computeThread.start();
		
		
		
		//Wait for set time and assume that execution has finished:
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace(); };

		
		//The single Compute thread should be blocked waiting: 
		assertEquals(Thread.State.WAITING, computeThread.getState()); 
	}
	
	/**
	 * 
	 * 
	 * 
	 * Write your other tests here
	 * 
	 * 
	 * 
	 */
	@org.junit.jupiter.api.Test
	void UR1Test() {
		JobDispatcher dispatcher = new JobDispatcher();
		
		//Create compute threads
		for (int i = 0; i < 4; i++) {
			Thread computeThread = new Thread() {			
				public void run () {
					dispatcher.queueComputeThread();
				}
			};	
			computeThread.start();
		}
		
		//Create storage threads
		for (int i = 0; i < 0; i++) {
			Thread storageThread = new Thread() {			
				public void run () {
					dispatcher.queueStorageThread();
				}
			};	
			storageThread.start();
		}
		
		//Create job
		dispatcher.specifyJob(4, 0);
		
		//Wait for set time and assume that execution has finished:
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace(); };
		
		assertEquals(0, dispatcher.getWaitingCompute());
		assertEquals(0, dispatcher.getWaitingStorage());
		assertEquals(0, dispatcher.getJobListSize());
		
	}
	
	@org.junit.jupiter.api.Test
	void UR2Test() {
		JobDispatcher dispatcher = new JobDispatcher();
		
		//Create compute threads
		for (int i = 0; i < 5; i++) {
			Thread computeThread = new Thread() {			
				public void run () {
					dispatcher.queueComputeThread();
				}
			};	
			computeThread.start();
		}
		
		//Create storage threads
		for (int i = 0; i < 5; i++) {
			Thread storageThread = new Thread() {			
				public void run () {
					dispatcher.queueStorageThread();
				}
			};	
			storageThread.start();
		}
		
		//Specify jobs
		dispatcher.specifyJob(3, 1);
		dispatcher.specifyJob(2, 1);
		dispatcher.specifyJob(0, 3);
		dispatcher.specifyJob(6, 6); //This one is supposed to be left hanging
		
		//Wait for set time and assume that execution has finished:
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace(); };
		
		assertEquals(0, dispatcher.getWaitingCompute());
		assertEquals(0, dispatcher.getWaitingStorage());
		assertEquals(1, dispatcher.getJobListSize());
	}
	
	@org.junit.jupiter.api.Test
	void UR3Test() {
		JobDispatcher dispatcher = new JobDispatcher();
		
		//Create compute threads
		for (int i = 0; i < 2; i++) {
			Thread computeThread = new Thread() {			
				public void run () {
					dispatcher.queueComputeThread();
				}
			};	
			computeThread.start();
		}
		
		//Create storage threads
		for (int i = 0; i < 1; i++) {
			Thread storageThread = new Thread() {			
				public void run () {
					dispatcher.queueStorageThread();
				}
			};	
			storageThread.start();
		}
		
		//Specify some jobs
		dispatcher.specifyJob(3, 1);
		dispatcher.specifyJob(2, 1);
				
		//Create more compute threads
		for (int i = 0; i < 3; i++) {
			Thread computeThread = new Thread() {			
				public void run () {
					dispatcher.queueComputeThread();
				}
			};	
			computeThread.start();
		}
		
		//Create more storage threads
		for (int i = 0; i < 4; i++) {
			Thread storageThread = new Thread() {			
				public void run () {
					dispatcher.queueStorageThread();
				}
			};	
			storageThread.start();
		}
		
		//Specify more jobs
		dispatcher.specifyJob(0, 3);
		dispatcher.specifyJob(6, 6); //This one is supposed to be left hanging
		
		//Wait for set time and assume that execution has finished:
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace(); };
		
		assertEquals(0, dispatcher.getWaitingCompute());
		assertEquals(0, dispatcher.getWaitingStorage());
		assertEquals(1, dispatcher.getJobListSize());
	}
	
}
