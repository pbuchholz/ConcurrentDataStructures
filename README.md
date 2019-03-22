# ConcurrentDataStructures
Contains some tests and examples for concurrent data structures in java.

## ArrayBlockingFifoQueue and ArrayBlockingFifoQueueExample
Contains a queue which uses an array as backing data structure which is dynamically redimensioned if needed. Elements are taken out of the queue in first in first out order. This queue uses wait and notifyAll synchronization on an internal object lock.
If no elements are available in the queue callig take() will block until a new element becomes available.

The Array BlockingFifoQueueExample uses an ExecutorService to submit consumers and producers of the queue.
The count of consumers, producers and threads available in the underlying thread pool can be controlled by program arguments.
First argument is the number of producers next comes the number of consumers and the last argument is the number of threads for the internal thread pool.

__Example:__
java -cp datastructures-0.0.1-SNAPSHOT.jar concurrent.datastructures.ArrayBlockingFifoQueueExample 100 100 20 -Djava.util.logging.file=logging.properties

__Example output:__
INFO: Putting element to <94>.
März 22, 2019 11:04:35 VORM. concurrent.datastructures.ArrayBlockingFifoQueue put
INFO: Putting element to <95>.
März 22, 2019 11:04:35 VORM. concurrent.datastructures.ArrayBlockingFifoQueue put
INFO: Putting element to <96>.
März 22, 2019 11:04:35 VORM. concurrent.datastructures.ArrayBlockingFifoQueue doTake
INFO: Available elements <[7, 6, 19, 25, 18, 28, 17, 30, 16, 32, 33, 15, 5, 36, 37, 14, 13, 12, 11, 10, 9, 4, 3, 2, 43, 46, 48, 41, 40, 52, 53, 39, 54, 38, 35, 58, 34, 31, 29, 61, 63, 26, 27, 65, 66, 67, 24, 23, 71, 72, 73, 74, 75, 76, 77, 22, 79, 80, 81, 82, 83, 21, 84, 78, 70, 88, 89, 69, 68, 64, 93, 62, 60, 59, 57, 56, 55, 100, 51, 50, null, null, null, null]>.
März 22, 2019 11:04:35 VORM. concurrent.datastructures.ArrayBlockingFifoQueue doTake
INFO: Taking element <7>.
März 22, 2019 11:04:35 VORM. concurrent.datastructures.ArrayBlockingFifoQueue doTake
INFO: Available elements <[6, 19, 25, 18, 28, 17, 30, 16, 32, 33, 15, 5, 36, 37, 14, 13, 12, 11, 10, 9, 4, 3, 2, 43, 46, 48, 41, 40, 52, 53, 39, 54, 38, 35, 58, 34, 31, 29, 61, 63, 26, 27, 65, 66, 67, 24, 23, 71, 72, 73, 74, 75, 76, 77, 22, 79, 80, 81, 82, 83, 21, 84, 78, 70, 88, 89, 69, 68, 64, 93, 62, 60, 59, 57, 56, 55, 100, 51, 50, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null]>.
März 22, 2019 11:04:35 VORM. concurrent.datastructures.ArrayBlockingFifoQueue doTake
INFO: Taking element <6>.
März 22, 2019 11:04:36 VORM. concurrent.datastructures.ArrayBlockingFifoQueue doTake
INFO: Available elements <[19, 25, 18, 28, 17, 30, 16, 32, 33, 15, 5, 36, 37, 14, 13, 12, 11, 10, 9, 4, 3, 2, 43, 46, 48, 41, 40, 52, 53, 39, 54, 38, 35, 58, 34, 31, 29, 61, 63, 26, 27, 65, 66, 67, 24, 23, 71, 72, 73, 74, 75, 76, 77, 22, 79, 80, 81, 82, 83, 21, 84, 78, 70, 88, 89, 69, 68, 64, 93, 62, 60, 59, 57, 56, 55, 100, 51, 50, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null]>.
März 22, 2019 11:04:36 VORM. concurrent.datastructures.ArrayBlockingFifoQueue doTake
INFO: Taking element <19>.
März 22, 2019 11:04:36 VORM. concurrent.datastructures.ArrayBlockingFifoQueue doTake
INFO: Available elements <[25, 18, 28, 17, 30, 16, 32, 33, 15, 5, 36, 37, 14, 13, 12, 11, 10, 9, 4, 3, 2, 43, 46, 48, 41, 40, 52, 53, 39, 54, 38, 35, 58, 34, 31, 29, 61, 63, 26, 27, 65, 66, 67, 24, 23, 71, 72, 73, 74, 75, 76, 77, 22, 79, 80, 81, 82, 83, 21, 84, 78, 70, 88, 89, 69, 68, 64, 93, 62, 60, 59, 57, 56, 55, 100, 51, 50, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null]>.

This output shows how the backing array is redimensioned and how the elements are put into and taken out of the queue.
