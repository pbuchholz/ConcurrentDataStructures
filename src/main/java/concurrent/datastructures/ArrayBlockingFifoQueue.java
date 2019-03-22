package concurrent.datastructures;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Simple queue which uses an {@link Array} as backing data structure which is
 * redimensioned dynamically if needed. The elements taken from the queue are
 * retured in first in first out order. Take() blocks if no element is available
 * and returned as soon as one becomes available.
 * 
 * @author Philipp Buchholz
 */
public class ArrayBlockingFifoQueue<E> {

	private static Logger log = Logger.getLogger(ArrayBlockingFifoQueue.class.getName());

	private final static int CAPACITY = 10;
	private final static int EMPTY = -1;

	private Class<E> clazz;
	private E[] elements;
	private int pos = 0;

	/* Private internal lock. */
	private Object lock = new Object();

	public ArrayBlockingFifoQueue(Class<E> clazz) {
		this.clazz = clazz;
		this.elements = newArrayInstance(CAPACITY);
	}

	@SuppressWarnings("unchecked")
	private E[] newArrayInstance(int capacity) {
		return (E[]) Array.newInstance(this.clazz, capacity);
	}

	/**
	 * Puts a new element of type E to the queue.
	 * 
	 * @param element
	 */
	public void put(E element) {
		synchronized (lock) {
			if (pos == elements.length) {
				log.log(Level.INFO, () -> "Redimension backing array.");
				elements = Arrays.copyOf(elements, //
						elements.length + CAPACITY);
			}

			if (Objects.isNull(element)) {
				throw new IllegalArgumentException("Element to put is null");
			}

			log.log(Level.INFO, () -> "Putting element to <" + pos + ">.");
			elements[pos++] = element;
			lock.notifyAll();
		}
	}

	/**
	 * Return <code>true</code> if the queue is empty otherwise <code>false</code>.
	 * 
	 * @return
	 */
	private boolean isEmpty() {
		return elements.length == EMPTY || //
				pos == EMPTY || //
				Stream.of(elements).allMatch(Objects::isNull);
	}

	/**
	 * Takes the next element from the queue is one is available blocks otherwise
	 * till one becomes available.
	 * 
	 * @return
	 */
	public E take() throws InterruptedException {
		synchronized (lock) {
			/* Wait for element becoming available. */
			while (isEmpty()) {
				try {
					log.log(Level.INFO, () -> "Waiting for element to become available.");
					lock.wait();
				} catch(InterruptedException e) {
					Thread.currentThread().interrupt();
					throw e;
				}
			}
			return this.doTake();
		}
	}

	private E doTake() {
		if (!Thread.holdsLock(lock)) {
			throw new IllegalMonitorStateException();
		}

		log.log(Level.INFO, () -> "Available elements <" + Arrays.toString(elements) + ">.");
		log.log(Level.INFO, () -> "Taking element <" + elements[0] + ">.");

		E element = elements[0];
		if (Objects.isNull(element)) {
			throw new IllegalArgumentException("Element taken is null.");
		}
		elements = Arrays.copyOfRange(elements, 1, --pos);
		return element;
	}

}
