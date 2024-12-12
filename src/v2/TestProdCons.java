package v2;

import java.util.Properties;

public class TestProdCons {
    public static void main(String[] args) {
        try {
            Properties properties = new Properties();
            properties.loadFromXML(
                TestProdCons.class.getClassLoader().getResourceAsStream("options.xml")
            );

            // Extract the properties
            int nProd = Integer.parseInt(properties.getProperty("nProd")); 
            int nCons = Integer.parseInt(properties.getProperty("nCons")); 
            int bufSz = Integer.parseInt(properties.getProperty("bufSz")); 
            int prodTime = Integer.parseInt(properties.getProperty("prodTime"));
            int consTime = Integer.parseInt(properties.getProperty("consTime"));
            int minProd = Integer.parseInt(properties.getProperty("minProd")); 
            int maxProd = Integer.parseInt(properties.getProperty("maxProd")); 

            IProdConsBuffer buffer = new ProdConsBuffer(bufSz);

            Thread[] producers = new Thread[nProd];
            for (int i = 0; i < nProd; i++) {
                producers[i] = new Producer(buffer, minProd, maxProd, prodTime);
                producers[i].start();
            }
            
            Thread[] consumers = new Thread[nCons];
            for (int i = 0; i < nCons; i++) {
                consumers[i] = new Consumer(buffer, consTime);
                consumers[i].start();
            }

            for (Thread producer : producers) {
                producer.join();
            }

            for (Thread consumer : consumers) {
                consumer.join();
            }

            System.out.println("All producers and consumers have finished. Application finished.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}