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
	
	
	
}
