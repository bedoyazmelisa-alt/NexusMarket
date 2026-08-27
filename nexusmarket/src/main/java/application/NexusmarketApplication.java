package application;
 
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.mongodb.autoconfigure.MongoAutoConfiguration;
 
/**
* Application entry point. DataSource/JPA/Mongo auto-configurations are
* excluded while the persistence adapters are not implemented yet; re-enable
* them when the repositories are wired to MySQL/MongoDB.
*/
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
        MongoAutoConfiguration.class
})
public class NexusmarketApplication {
 
	public static void main(String[] args) {
		SpringApplication.run(NexusmarketApplication.class, args);
	}
}