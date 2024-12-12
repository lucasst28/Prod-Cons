package v7;

public class TestProdCons {
    public static void main(String[] args) {

        int bufferSize = 5; 
        int maxConsumers = 3; 
        int numProducers = 2;
        int numTasksPerProducer = 5;
        int prodTime = 1000; 

        TaskExecutor taskExecutor = new TaskExecutor(bufferSize, maxConsumers);

        Thread[] producers = new Thread[numProducers];
        for (int i = 0; i < numProducers; i++) {
            producers[i] = new Producer(taskExecutor, numTasksPerProducer, prodTime);
            producers[i].start();
        }

        for (Thread producer : producers) {
            try {
                producer.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        taskExecutor.shutdown();

        System.out.println("Application finished.");
    }
}