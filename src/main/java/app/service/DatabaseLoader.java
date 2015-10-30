package app.service;

import app.domain.Teammate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import app.repository.TeammateRepository;

import javax.annotation.PostConstruct;



@Service
@Profile("!production")
public class DatabaseLoader {

    private final TeammateRepository teammateRepository;

    @Autowired
    public DatabaseLoader(TeammateRepository teammateRepository) {
        this.teammateRepository = teammateRepository;
    }

    @PostConstruct
    private void initDatabase() {
        teammateRepository.deleteAll();

        Teammate roy = new Teammate("Roy", "Clarkson");
        roy.setPosition("1st base");
        teammateRepository.save(roy);

        Teammate phil = new Teammate("Phil", "Webb");
        phil.setPosition("pitcher");
        teammateRepository.save(phil);
    }
}
