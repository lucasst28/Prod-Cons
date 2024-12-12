package v6;

import java.util.Properties;

public class TestProdCons {
    public static void main(String[] args) {
        try {
            Properties properties = new Properties();
            properties.loadFromXML(
                TestProdCons.class.getClassLoader().getResourceAsStream("v6/options.xml")
            );

            int nProd = Integer.parseInt(properties.getProperty("nProd"));
            int nCons = Integer.parseInt(properties.getProperty("nCons"));
            int bufSz = Integer.parseInt(properties.getProperty("bufSz"));
            int prodTime = Integer.parseInt(properties.getProperty("prodTime"));
            int consTime = Integer.parseInt(properties.getProperty("consTime"));
            int minProd = Integer.parseInt(properties.getProperty("minProd"));
            int maxProd = Integer.parseInt(properties.getProperty("maxProd"));
            int consumersRequired = Integer.parseInt(properties.getProperty("consumersRequired"));

            // Create the shared buffer
            IProdConsBuffer buffer = new ProdConsBuffer(bufSz);

            // Start producers
            Thread[] producers = new Thread[nProd];
            for (int i = 0; i < nProd; i++) {
                producers[i] = new Producer(buffer, minProd, maxProd, prodTime, consumersRequired);
                producers[i].start();
            }

            // Start consumers
            Thread[] consumers = new Thread[nCons];
            for (int i = 0; i < nCons; i++) {
                consumers[i] = new Consumer(buffer, consTime);
                consumers[i].start();
            }

            // Wait for all producers to finish
            for (Thread producer : producers) {
                producer.join();
            }

            // Wait for all consumers to finish
            for (Thread consumer : consumers) {
                consumer.join();
            }

            System.out.println("Application finished.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}