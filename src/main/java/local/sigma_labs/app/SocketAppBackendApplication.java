package local.sigma_labs.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.ExecutionException;
@SpringBootApplication
public class SocketAppBackendApplication {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        SpringApplication.run(SocketAppBackendApplication.class, args);

    }
}
