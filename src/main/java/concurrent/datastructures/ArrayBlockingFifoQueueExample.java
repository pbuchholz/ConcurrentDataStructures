package concurrent.datastructures;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ArrayBlockingFifoQueueExample {

	private static final Logger log = Logger.getLogger(ArrayBlockingFifoQueueExample.class.getName());

	private ExecutorService executorService;
	private ArrayBlockingFifoQueue<Integer> arrayBlockingQueue = new ArrayBlockingFifoQueue<>(Integer.class);
	private AtomicInteger currentHigh = new AtomicInteger(0);

	private static class StartupArguments {
		int producerCount;
		int consumerCount;
		int numberOfConcurrentThreads;

		@Override
		public String toString() {
			return "StartupArguments (ProducerCount <" + producerCount + ">, ConsumerCount <" + consumerCount
					+ ">, NumberOfConcurrentThreads <" + numberOfConcurrentThreads + ">)";
		}
	}

	public static void main(String... args) throws InterruptedException {
		if (Objects.isNull(args)) {
			throw new IllegalArgumentException("Programming arguments missing.");
		}

		StartupArguments startupArgs = new StartupArguments();
		startupArgs.producerCount = Integer.valueOf(args[0]);
		startupArgs.consumerCount = Integer.valueOf(args[1]);
		startupArgs.numberOfConcurrentThreads = Integer.valueOf(args[2]);
		log.log(Level.INFO, () -> startupArgs.toString());

		ArrayBlockingFifoQueueExample example = new ArrayBlockingFifoQueueExample();
		example.startupExecutorService(startupArgs);
		example.submitProducers(startupArgs);
		example.submitConsumers(startupArgs);
		example.waitForShutdown();
	}

	private void waitForShutdown() {
		this.executorService.shutdownNow();
		while (!this.executorService.isTerminated()) {
			log.log(Level.INFO, () -> "Waiting for ExecutorService termination.");
		}
	}

	private void startupExecutorService(StartupArguments startupArgs) {
		this.executorService = Executors.newFixedThreadPool(startupArgs.numberOfConcurrentThreads, new ThreadFactory() {

			private AtomicInteger threadNumber = new AtomicInteger(1);

			@Override
			public Thread newThread(Runnable r) {
				Thread thread = new Thread(r);
				thread.setName("ExecutorService-Thread-" + threadNumber.getAndIncrement());
				return thread;
			}
		});
	}

	private void submitProducers(StartupArguments startupArgs) {
		for (int i = 0; i < startupArgs.producerCount; i++) {
			executorService.submit(() -> {
				arrayBlockingQueue.put(currentHigh.incrementAndGet());
			});
		}
	}

	private void submitConsumers(StartupArguments startupArgs) {
		try {
			for (int i = 0; i < startupArgs.consumerCount; i++) {
				Thread.sleep(100);
				executorService.submit(() -> {
					try {
						Integer current = arrayBlockingQueue.take();
						log.log(Level.FINE, () -> "Processing element <" + current + ">.");
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}