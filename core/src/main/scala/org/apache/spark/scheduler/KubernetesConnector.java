package org.apache.spark.scheduler;


import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.client.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.apache.log4j.Logger;

/**
 * Created by D064878 on 29.08.2017.
 */
public class KubernetesConnector {
    public static final KubernetesConnector connector = new KubernetesConnector();
    public static HashMap<Integer,String> hostnames = new HashMap<>();
    Logger log = Logger.getLogger(getClass().getName());
    public KubernetesConnector()
    {
        start();
    }

    public void start()
    {

        Config config = new ConfigBuilder().build();
        config.setMasterUrl("https://10.125.0.132");
        config.setCaCertFile("/var/run/secrets/kubernetes.io/serviceaccount/ca.crt");
        String token = readFile("/var/run/secrets/kubernetes.io/serviceaccount/token");
        if (!token.equals("")) {
            config.setOauthToken(token);
        }
        else {
                //TODO
        }
        KubernetesClient client = new DefaultKubernetesClient(config);
        PodList pods = client.pods().inNamespace("kubernetes-hdfs").list();
        for (Pod pod : pods.getItems()) {
            //TODO --> Integer muss IP sein
            hostnames.put(new Integer(0),pod.getSpec().getHostname());
        }
    }
    public String readFile(String path) {
    String result = "";
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                result += sCurrentLine;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}

