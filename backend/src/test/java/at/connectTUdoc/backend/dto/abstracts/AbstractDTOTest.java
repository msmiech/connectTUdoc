package at.connectTUdoc.backend.dto.abstracts;

import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * This test class contains the general objects used by the test classes
 */
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public abstract class AbstractDTOTest {
    // could not autowire?
    protected final ModelMapper modelMapper = new ModelMapper();

}
