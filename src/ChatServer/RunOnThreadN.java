package ChatServer;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Class works a general pool of thread that can handle any runnable task.
 */
/**
 * 
 * @author Eric Grevillius
 *
 */
public class RunOnThreadN {
	private Buffer<Runnable> tasks = new Buffer<Runnable>();
	private ArrayList<Thread> threads;
	private int n;

	/**
	 * Constructor sets the incoming integer its own variable 'n' which is a
	 * representation of the highest amount of threads.
	 * 
	 * @param nbrOfThreads
	 *            Integer representing the number of threads to create.
	 */
	public RunOnThreadN(int nbrOfThreads) {
		this.n = nbrOfThreads;
	}

	/**
	 * Method starts all the threads in the pool only if they aren't started.
	 */
	public synchronized void start() {
		Thread thread;
		if (threads == null) {
			threads = new ArrayList<Thread>();
			for (int i = 0; i < n; i++) {
				thread = new Thread() {
					public void run() {
						Runnable runnable;
						while (!Thread.interrupted()) {
							try {
								runnable = tasks.get();
								runnable.run();
							} catch (InterruptedException e) {
								try {
									join();
								} catch (InterruptedException e1) {
								}
							}
						}
					}
				};
				thread.start();
				threads.add(thread);
			}
		}
	}

	/**
	 * Stops all threads in the pool and then clears the pool of all threads.
	 */
	public synchronized void stop() {
		if (threads != null) {
			for (int i = 0; i < n; i++) {
				execute(new StopThread());
			}
			threads.clear();
			threads = null;
		}
	}

	/**
	 * Adds a task in the buffer for the tread-pool to execute
	 * 
	 * @param task
	 *            Runnable to but added in the buffer.
	 */
	public synchronized void execute(Runnable task) {
		tasks.put(task);
	}

	/**
	 * A Runnable that is put in the buffer when to stop the thread-pool.
	 */
	/**
	 * 
	 * @author Eric Grevillius
	 *
	 */
	private class StopThread implements Runnable {
		public void run() {
			Thread.currentThread().interrupt();
		}

		public String toString() {
			return "Closing down " + Thread.currentThread();
		}
	}

	/**
	 * 
	 * @author Eric Grevillius
	 *
	 *
	 *         Class is a buffer for class <T>.
	 * @param <T>
	 *            Class to be specified when used.
	 */
	private class Buffer<T> {
		private LinkedList<T> buffer = new LinkedList<T>();

		/**
		 * Adds given object of the given type to the the buffer.
		 * 
		 * @param obj
		 *            Object of the given type.
		 */
		public synchronized void put(T obj) {
			buffer.addLast(obj);
			notifyAll();
		}

		/**
		 * Returns an object of the given type.
		 * 
		 * @return Object of given type.
		 * @throws InterruptedException
		 */
		public synchronized T get() throws InterruptedException {
			while (buffer.isEmpty()) {
				wait();
			}
			return buffer.removeFirst();
		}
	}
}
