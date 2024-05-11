package dk.sdu.mmmi.cbse.consuming_cs_rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ConsumingCsRestApplication {

	public static void main(String[] args) {
		RestTemplate restTemplate = new RestTemplate();
		String url = "http://localhost:8080/score";
		Integer score = restTemplate.getForObject(url, Integer.class);
        if (score != null) {
            System.out.println("Score: " + score);
        }
    }

}
