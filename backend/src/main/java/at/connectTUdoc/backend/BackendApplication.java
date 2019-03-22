package at.connectTUdoc.backend;

import at.connectTUdoc.backend.dao.MedicalWorkerRepository;
import at.connectTUdoc.backend.dao.OfficeRepository;
import at.connectTUdoc.backend.helper.MedConnectConstants;
import at.connectTUdoc.backend.property.FileStorageProperties;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.time.format.DateTimeFormatter;

/**
 * This class represents the entry point of the application.
 */
@SpringBootApplication
@EnableConfigurationProperties({
        FileStorageProperties.class
})
public class BackendApplication {

    private static final Logger LOG = LoggerFactory.getLogger(BackendApplication.class);

    /**
     * Entry point of the application
     *
     * @param args all optional arguments to set
     */
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);

        try {
            // This requires a firebase.serviceAccountKey.json file with a valid firebase key to work
            InputStream serviceAccount = new ClassPathResource("/firebase.serviceAccountKey.json").getInputStream();

            FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl(MedConnectConstants.FIREBASE_URL)
                .build();

            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e.getCause());
        }
    }

    // Test data generation
    @Bean
    public CommandLineRunner demo(OfficeRepository officeRepository, MedicalWorkerRepository medicalWorkerRepository) {
        DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern(MedConnectConstants.JSON_FORMAT_LOCALDATETIME);


        return (args) -> {
      /*      officeRepository.deleteAll();
            medicalWorkerRepository.deleteAll();

            // Mayer
            officeRepository.save(DataGeneration.generateOffice("Praxis Dr. Mayer", "office@hugo.at", "Zieglergasse", "7", "7", "1",
                    "Vienna", "Vienna", "Austria", LocalDateTime.parse("2017-02-11 11:11", dtfDate), LocalDateTime.parse("2017-02-11 11:11", dtfDate)));
            // Dagobert
            officeRepository.save(DataGeneration.generateOffice("Praxis Dr. Hubert", "dagobert@duck.eh", "Löwengasse", "5", "7", "7",
                    "Vienna", "Vienna", "Austria", LocalDateTime.parse("2018-01-07 07:00", dtfDate), LocalDateTime.parse("2018-01-07 08:00", dtfDate)));
            // Düsentrieb
            officeRepository.save(DataGeneration.generateOffice("Praxis Dr. Berger", "psycho@farma.fa", "Herzgasse", "10", "6", "6",
                    "Vienna", "Vienna", "Austria", LocalDateTime.parse("2018-03-12 14:15", dtfDate), LocalDateTime.parse("2018-03-12 16:15", dtfDate)));
            // Ironman
            officeRepository.save(DataGeneration.generateOffice("Praxis Dr. Müller", "iron@man.dc", "Karlsplatz", "3", "1", "1",
                    "Vienna", "Vienna", "Austria", LocalDateTime.parse("2018-09-09 09:00", dtfDate), LocalDateTime.parse("2018-09-09 09:30", dtfDate)));
            // Graf Dracula
            medicalWorkerRepository.save(DataGeneration.generateMedicalWorker("Marcus", "Mayer", "Vamp", "ire", MedWorkerType.DOCTOR, "Human", "graf@dracu.la"));
            medicalWorkerRepository.save(DataGeneration.generateMedicalWorker("Max", "Muster", "Ass", "tent", MedWorkerType.ASSISTANT, "Technican", "max.muster@dracu.la"));
            medicalWorkerRepository.save(DataGeneration.generateMedicalWorker("Berta", "Hadel", "Dr", "MSc", MedWorkerType.DOCTOR, "Dentist", "berta.hadel@dracu.la"));
            medicalWorkerRepository.save(DataGeneration.generateMedicalWorker("Mist", "Eria", "Dipl.Ing", "", MedWorkerType.ASSISTANT, "Common Allergic", "mist.eria@dracu.la"));
            medicalWorkerRepository.save(DataGeneration.generateMedicalWorker("Manuel", "Meister", "", "", MedWorkerType.ASSISTANT, "Terror", "manuel.meister@dracu.la"));
            medicalWorkerRepository.save(DataGeneration.generateMedicalWorker("Eva", "Jung", "", "BSc", MedWorkerType.ASSISTANT, "Psychology", "eva.jung@dracu.la")); */
        };

    }

}
