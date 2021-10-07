package nl.abnamro.app;

import nl.abnamro.app.config.WebSecurityConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;


/**
 * Run Application
 *
 * @author Reza Nojavan
 */
@SpringBootApplication
@Import(WebSecurityConfig.class)
public class Application {

    private final static Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        logger.info("String Application");
        SpringApplication.run(Application.class, args);
    }

}
