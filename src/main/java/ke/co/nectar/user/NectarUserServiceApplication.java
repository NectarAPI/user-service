package ke.co.nectar.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "ke.co.nectar.user")
@ConfigurationPropertiesScan("ke.co.nectar.user.configurations")
@EnableJpaRepositories(basePackages="ke.co.nectar.user.repository")
public class NectarUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NectarUserServiceApplication.class, args);
    }
}
