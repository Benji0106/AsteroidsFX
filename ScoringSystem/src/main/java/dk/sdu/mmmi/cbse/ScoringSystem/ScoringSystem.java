package dk.sdu.mmmi.cbse.ScoringSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ScoringSystem {

	private int totalScore = 0;

	public static void main(String[] args) {
		SpringApplication.run(ScoringSystem.class, args);
	}

	@GetMapping("/score")
	public int getScore(@RequestParam(value = "score", defaultValue = "0") int score) {
		totalScore += score;
		return totalScore;
	}

}
