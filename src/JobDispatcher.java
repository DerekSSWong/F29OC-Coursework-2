
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
// Note that you MUST not use any other `thread safe` classes than the two imported above
import java.util.Arrays;
import java.util.ArrayList;

public class JobDispatcher implements Dispatcher {
	
	//Declare your local variables and objects here
	private ReentrantLock lock = new ReentrantLock();
	
	private Condition computeWait = lock.newCondition();
	private Condition storageWait = lock.newCondition();
	
	private int ID = 0;
	private ArrayList<Job> jobList = new ArrayList<Job>();


	@Override
	public void specifyJob(int nComputeThreads, int nStorageThreads) {
		//Your code here
		lock.lock();
			int currentComputeThreads = lock.getWaitQueueLength(computeWait);
			int currentStorageThreads = lock.getWaitQueueLength(storageWait);
			boolean enoughCompute = (currentComputeThreads >= nComputeThreads);
			boolean enoughStorage = (currentStorageThreads >= nStorageThreads);
			Job job = new Job(ID, nComputeThreads, nStorageThreads, enoughCompute, enoughStorage);
			
			if (enoughCompute == enoughStorage == true) {
				signalCompute(nComputeThreads);
				signalStorage(nStorageThreads);
				System.out.println("Job " + ID + " completed");
			} 
			else {jobList.add(job);}
			
			ID += 1;
		lock.unlock();
	}

	@Override
	public void queueComputeThread() {
		//Your code here
		lock.lock();
			int currentCompute = lock.getWaitQueueLength(computeWait) + 1;
			int currentStorage = lock.getWaitQueueLength(storageWait);
			ArrayList<Job> readyToRun = inspectList(currentCompute, currentStorage);
			if (readyToRun.size() > 0) {
				for (Job i: readyToRun) {
					signalCompute(i.getComputeThreads() - 1);
					signalStorage(i.getStorageThreads());
					System.out.println("Job " + i.getID() + " completed");
					jobList.remove(i);
				}
			}
			else {
				try {
					computeWait.await();
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
		ArrayList<Job> readyToRun = inspectList(currentCompute, currentStorage);
		if (readyToRun.size() > 0) {
			for (Job i: readyToRun) {
				signalCompute(i.getComputeThreads());
				signalStorage(i.getStorageThreads() - 1);
				System.out.println("Job " + i.getID() + " completed");
				jobList.remove(i);
			}
		}
		else {
			try {
				storageWait.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	lock.unlock();
	}
	
	private void signalCompute(int signal) {
		for (int i = 0; i < signal; i++) {
			computeWait.signal();
		}
	}
	
	private void signalStorage(int signal) {
		for (int i = 0; i < signal; i++) {
			storageWait.signal();
		}
	}
	
	private ArrayList<Job> inspectList(int currentCompute, int currentStorage) {
		ArrayList<Job> result = new ArrayList<Job>();
			for (Job i : jobList) {
				if (currentCompute >= i.getComputeThreads() & currentStorage >= i.getStorageThreads()) {
					result.add(i);
					currentCompute -= i.getComputeThreads();
					currentStorage -= i.getStorageThreads();
				}
			}
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
