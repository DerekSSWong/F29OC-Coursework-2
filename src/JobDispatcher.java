
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
// Note that you MUST not use any other `thread safe` classes than the two imported above
import java.util.ArrayList;

/**
 * My solution is to store individual jobs as objects in an arraylist,
 *  and whenever a new job is specified, or a worker thread is queued,
 *  the code will check if any current jobs are ready to be completed
 * @author dsw6
 *
 */
public class JobDispatcher implements Dispatcher {
	
	//Declare your local variables and objects here
	private ReentrantLock lock = new ReentrantLock();
	
	private Condition computeWait = lock.newCondition();
	private Condition storageWait = lock.newCondition();
	
	private int ID = 0; //Unique key for jobs
	public ArrayList<Job> jobList = new ArrayList<Job>();
	
	public int waitingCompute = 0;
	public int waitingStorage = 0;


	@Override
	public void specifyJob(int nComputeThreads, int nStorageThreads) {
		//Your code here
		lock.lock();
		
			//Creates a new instance of the Job class
			int currentComputeThreads = lock.getWaitQueueLength(computeWait);
			int currentStorageThreads = lock.getWaitQueueLength(storageWait);
			boolean enoughCompute = (currentComputeThreads >= nComputeThreads);
			boolean enoughStorage = (currentStorageThreads >= nStorageThreads);
			Job job = new Job(ID, nComputeThreads, nStorageThreads, enoughCompute, enoughStorage);
			System.out.println("(SJ) Job " + ID + "(" + job.getComputeThreads() + "," + job.getStorageThreads() + ")" +" added");
			
			//Checks if the new job is doable with the current amount of worker threads
			//If so, signal the threads needed and finish the job
			//If not, add the Job instance to the jobList
			if (enoughCompute & enoughStorage) {
				signalCompute(nComputeThreads);
				signalStorage(nStorageThreads);
				System.out.println("(SJ) Job " + ID + "(" + job.getComputeThreads() + "," + job.getStorageThreads() + ")" +" completed");
			} 
			else {
				jobList.add(job);
				System.out.println("(SJ) Job " + ID + "(" + job.getComputeThreads() + "," + job.getStorageThreads() + ")" +" queued");
			}
			
			ID += 1; //Increment ID for the next job
		lock.unlock();
	}

	@Override
	public void queueComputeThread() {
		//Your code here
		lock.lock();
		
			int currentCompute = lock.getWaitQueueLength(computeWait) + 1;
			int currentStorage = lock.getWaitQueueLength(storageWait);
			System.out.println("Compute +1 = " + currentCompute);
			ArrayList<Job> readyToRun = inspectList(currentCompute, currentStorage);
			
			//If there are doable jobs, remove them from the jobList and unlocks
			//If not, go into waiting
			if (readyToRun.size() > 0) {
				for (Job i: readyToRun) {
					signalCompute(i.getComputeThreads() - 1);
					signalStorage(i.getStorageThreads());
					System.out.println("(CT) Job " + i.getID() + "(" + i.getComputeThreads() + "," + i.getStorageThreads() + ")" + " completed");
					jobList.remove(i);
				}
			}
			else {
				try {
					waitingCompute += 1;
					computeWait.await();
					waitingCompute -= 1;
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		lock.unlock();
		
	}

	@Override
	public void queueStorageThread() {
		//Your code here
		lock.lock();
		int currentCompute = lock.getWaitQueueLength(computeWait);
		int currentStorage = lock.getWaitQueueLength(storageWait) + 1;
		System.out.println("Storage +1 = " + currentStorage);
		ArrayList<Job> readyToRun = inspectList(currentCompute, currentStorage);
		if (readyToRun.size() > 0) {
			for (Job i: readyToRun) {
				signalCompute(i.getComputeThreads());
				signalStorage(i.getStorageThreads() - 1);
				System.out.println("(ST) Job " + i.getID() + "(" + i.getComputeThreads() + "," + i.getStorageThreads() + ")" + " completed");
				jobList.remove(i);
			}
		}
		else {
			try {
				waitingStorage += 1;
				storageWait.await();
				waitingStorage -= 1;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	lock.unlock();
	}
	
	//Signals a specified amount of compute threads
	private void signalCompute(int signal) {
		lock.lock();
		for (int i = 0; i < signal; i++) {
			computeWait.signal();
		}
		lock.unlock();
	}
	
	//Signals a specified amount of storage threads
	private void signalStorage(int signal) {
		lock.lock();
		for (int i = 0; i < signal; i++) {
			storageWait.signal();
		}
		lock.unlock();
	}
	
	//Iterates through the jobList and returns a combination of jobs which can be completed with the current amount of threads
	private ArrayList<Job> inspectList(int currentCompute, int currentStorage) {
		lock.lock();
			ArrayList<Job> result = new ArrayList<Job>();
				for (Job i : jobList) {
					if ((currentCompute >= i.getComputeThreads()) & (currentStorage >= i.getStorageThreads())) {
						result.add(i);
						currentCompute -= i.getComputeThreads();
						currentStorage -= i.getStorageThreads();
					}
				}
		lock.unlock();
		return result;
	}
	
	//Add any private classes that you need
	class Job {
		
		private int ID;
		private int nComputeThreads;
		private int nStorageThreads;
		private boolean enoughCompute;
		private boolean enoughStorage;
		
		public Job(int ID, int nComputeThreads, int nStorageThreads, boolean enoughCompute, boolean enoughStorage) {
			this.ID = ID;
			this.nComputeThreads = nComputeThreads;
			this.nStorageThreads = nStorageThreads;
			this.enoughCompute = enoughCompute;
			this.enoughStorage = enoughStorage;
		}
		
		public void setEnoughCompute(boolean enough) {
			enoughCompute = enough;
		}
		
		public void setEnoughStorage(boolean enough) {
			enoughStorage = enough;
		}
		
		public int getID() {
			return ID;
		}
		
		public int getComputeThreads() {
			return nComputeThreads;
		}
		
		public int getStorageThreads() {
			return nStorageThreads;
		}
		
		public boolean getEnoughCompute() {
			return enoughCompute;
		}
		
		public boolean getEnoughStorage() {
			return enoughStorage;
		}
	}

}
