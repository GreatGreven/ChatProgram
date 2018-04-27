package ChatServer;

import java.util.ArrayList;
import java.util.LinkedList;
/**
 * Class works a general pool of thread.
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

	public RunOnThreadN(int nbrOfThreads) {
		this.n = nbrOfThreads;
	}

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
								execute(runnable);
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

	public synchronized void stop() {
		if (threads != null) {
			for (int i = 0; i < n; i++) {
				execute(new StopThread());
			}
			threads.clear();
			threads = null;
		}
	}

	public synchronized void execute(Runnable task) {
		tasks.put(task);
	}
	
	private class StopThread implements Runnable {
		public void run() {
			Thread.currentThread().interrupt();
		}
		public String toString() {
			return "Closing down " + Thread.currentThread();
		}
	}
	
	private class Buffer<T> {
		private LinkedList<T> buffer = new LinkedList<T>();
		
		public synchronized void put(T obj) {
			buffer.addLast(obj);
			notifyAll();
		}
		
		public synchronized T get() throws InterruptedException {
			while(buffer.isEmpty()) {
				wait();
			}
			return buffer.removeFirst();
		}
		
		public int size() {
			return buffer.size();
		}
	}
}
