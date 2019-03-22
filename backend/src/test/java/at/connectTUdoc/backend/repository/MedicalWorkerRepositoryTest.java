package at.connectTUdoc.backend.repository;

import at.connectTUdoc.backend.dao.MedicalWorkerRepository;
import at.connectTUdoc.backend.model.MedicalWorker;
import at.connectTUdoc.backend.utils.TestUtilsMedConnect;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

/**
 * This class contains the test cases for the repository tests of medical worker database transactions
 */
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest(showSql = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class MedicalWorkerRepositoryTest {

    @Autowired
    private MedicalWorkerRepository workerRepository;

    private MedicalWorker medicalWorker;

    @Before
    public void setUP() {
        medicalWorker = TestUtilsMedConnect.getSingleMedicalWorker();
    }

    @Test
    public void createMedicalWorker_InsertMedicalWorkerIntoDatabase() {
        MedicalWorker createdMW = workerRepository.save(medicalWorker);

        assertEquals(createdMW, medicalWorker);
    }
}
