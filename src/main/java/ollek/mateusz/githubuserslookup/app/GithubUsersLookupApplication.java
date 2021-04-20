package ollek.mateusz.githubuserslookup.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ollek.mateusz.githubuserslookup.rest")
public class GithubUsersLookupApplication {

    public static void main(String[] args) {
        SpringApplication.run(GithubUsersLookupApplication.class, args);
    }

}
