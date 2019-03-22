package at.connectTUdoc.backend.repository;

import at.connectTUdoc.backend.dao.MedicineRepository;
import at.connectTUdoc.backend.model.Medicine;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

/**
 * This class contains the test cases for the repository tests for the specific class and the database transactions
 */
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest(showSql = false)
public class MedicineRepositoryTest {
    @Autowired
    private MedicineRepository medicineRepository;

    @Test
    public void findById_shouldReturnOneMedicine() {
      Medicine medicine = medicineRepository.findById("1329497").orElse(null);
      Assert.assertEquals(medicine.getId(), "1329497");
      Assert.assertEquals(medicine.getName(), "Seractil forte 400mg-Filmtabletten");
    }

    @Test
    public void findById_shouldFindNoMedicine() {
        Medicine medicine = medicineRepository.findById("1329496").orElse(null);
        Assert.assertNull(medicine);
    }

}
