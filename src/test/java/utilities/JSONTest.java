package utilities;

import com.primaryredtools.utilities.JSON;
import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.assertThrows;

public class JSONTest {
    @Test
    public void openNonExistentResourceFile() {
        assertThrows(FileNotFoundException.class, () -> {
            JSON.readJSON("imaginary_file.json");
        });
    }
}
