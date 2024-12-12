package v1;

import java.util.Properties;

public class TestProdCons {
    public static void main(String[] args) {
        try {
            Properties properties = new Properties();
            properties.loadFromXML(
                TestProdCons.class.getClassLoader().getResourceAsStream("options.xml")
            );

            int nProd = Integer.parseInt(properties.getProperty("nProd")); 
            int nCons = Integer.parseInt(properties.getProperty("nCons")); 
            int bufSz = Integer.parseInt(properties.getProperty("bufSz")); 
            int prodTime = Integer.parseInt(properties.getProperty("prodTime"));
            int consTime = Integer.parseInt(properties.getProperty("consTime"));
            int minProd = Integer.parseInt(properties.getProperty("minProd")); 
            int maxProd = Integer.parseInt(properties.getProperty("maxProd")); 

            IProdConsBuffer buffer = new ProdConsBuffer(bufSz);

            for (int i = 0; i < nProd; i++) {
                new Producer(buffer, minProd, maxProd, prodTime).start();
            }

            for (int i = 0; i < nCons; i++) {
                new Consumer(buffer, consTime).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}