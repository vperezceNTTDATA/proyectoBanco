package proyecto.banco.bancoDemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BancoDemoApplication {
	public static void main(String[] args) {
		try {
			SpringApplication.run(BancoDemoApplication.class, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
