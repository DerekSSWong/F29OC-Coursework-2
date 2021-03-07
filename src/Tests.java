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
	void specifyJobTest() {
		JobDispatcher dispatcher = new JobDispatcher();
		
		for (int i = 0; i < 5; i++) {
			Thread computeThread = new Thread() {			
				public void run () {
					dispatcher.queueComputeThread();
				}
			};	
			computeThread.start();
		}
		
		for (int i = 0; i < 5; i++) {
			Thread storageThread = new Thread() {			
				public void run () {
					dispatcher.queueStorageThread();
				}
			};	
			storageThread.start();
		}
		
		dispatcher.specifyJob(1, 0);
		dispatcher.specifyJob(2, 2);
		dispatcher.specifyJob(2, 0);
		dispatcher.specifyJob(0, 3);
		
	}
	
	
	
}
