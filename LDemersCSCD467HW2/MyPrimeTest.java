
public class MyPrimeTest {
	public static void main(String[] args) throws InterruptedException {
		if (args.length < 3) {
			System.out.println("Usage: MyPrimeTest numThread low high \n");
			return;
		}
		int nthreads = Integer.parseInt(args[0]);
		int low = Integer.parseInt(args[1]);
		int high = Integer.parseInt(args[2]);
		Counter c = new Counter();
		
		//test of serial code
		long start = System.currentTimeMillis();
		int numPrimeSerial = SerialPrime.numSerailPrimes(low, high);
		long end = System.currentTimeMillis();
		long timeCostSer = end - start;
		
		//test of concurrent code
		start = System.currentTimeMillis();
		int updateLow = low, threadSplitter = high / nthreads, updateHigh = threadSplitter; //threadSplitter splits the boundaries, allowing each thread to work on the same amount of the whole
		ThreadPrime[] pool = new ThreadPrime[nthreads];

		for (int i = 0; i < nthreads; i++) {
			pool[i] = new ThreadPrime(updateLow, updateHigh, c);
			updateLow = updateHigh + 1;
			updateHigh += threadSplitter;
			pool[i].start();
		}
		for (int i = 0; i < nthreads; i++) {
			pool[i].join();
		}
		
		end = System.currentTimeMillis();
		long timeCostCon = end - start;
		
		// **************************************
		System.out.println("Time cost of serial code: " + timeCostSer + " ms.");
		
		System.out.println("Time cost of parallel code: " + timeCostCon + " ms.");
		System.out.format("The speedup ration is by using concurrent programming: %5.2f. %n", (double)timeCostSer / timeCostCon);
		
		System.out.println("Number prime found by serial code is: " + numPrimeSerial);
		System.out.println("Number prime found by parallel code is " + c.total());
	}
}
