package app;

import app.repository.TeammateRepository;
import app.service.DatabaseLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

import static java.lang.System.out;

@SpringBootApplication
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    TeammateRepository tmr;

    @Autowired(required = false)
    DatabaseLoader dataBaseloader;

    @PostConstruct
    void getRoster(){
        out.println("Roster");
        tmr.findAll().forEach(tm -> log.info(tm.toString()));
    }
}
