package v7;

public class TestProdCons {
    public static void main(String[] args) {
        // Configurações do teste
        int bufferSize = 5; // Tamanho do buffer
        int maxConsumers = 3; // Número máximo de consumidores
        int numProducers = 2; // Número de produtores
        int numTasksPerProducer = 5; // Tarefas por produtor
        int prodTime = 1000; // Tempo entre produções (ms)

        // Criando o TaskExecutor
        TaskExecutor taskExecutor = new TaskExecutor(bufferSize, maxConsumers);

        // Criando e iniciando os produtores
        Thread[] producers = new Thread[numProducers];
        for (int i = 0; i < numProducers; i++) {
            producers[i] = new Producer(taskExecutor, numTasksPerProducer, prodTime);
            producers[i].start();
        }

        // Esperando todos os produtores terminarem
        for (Thread producer : producers) {
            try {
                producer.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Encerrando o TaskExecutor
        taskExecutor.shutdown();

        System.out.println("Application finished.");
    }
}