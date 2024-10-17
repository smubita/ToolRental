import com.primaryredtools.ToolRental;
import com.primaryredtools.models.Holiday;
import com.primaryredtools.models.Tool;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static org.junit.Assert.*;

public class ToolRentalTest {

    ToolRental toolRental;

    @Before
    public void setup() {
        toolRental = new ToolRental();
    }

    @Test
    public void loadRentalClass() {
        assertNotNull(toolRental.getConfiguration());
        assertNotNull(toolRental.getTools());
        assertNotNull(toolRental.getCharges());
        assertNotNull(toolRental.getHolidays());

        assertEquals(4, toolRental.getTools().size());
        assertEquals(3, toolRental.getCharges().size());
        assertEquals(2, toolRental.getHolidays().size());

        Tool tool = toolRental.getTools().get("JAKR");
        assertNotNull(tool);
        assertNotNull(tool.getCharge());
        assertEquals("Jackhammer", tool.getToolType());
        assertEquals("Ridgid", tool.getBrand());

        assertEquals("Jackhammer", tool.getCharge().getToolType());
        assertEquals(BigDecimal.valueOf(2.99), tool.getCharge().getDailyCharge());
        assertFalse(tool.getCharge().hasHolidayCharge());
        assertTrue(tool.getCharge().hasWeekdayCharge());

        Optional<Holiday> holiday = toolRental.getHolidays().stream()
                .filter(h -> h.getName().equals("Labor Day")).findFirst();
        assertFalse(holiday.isEmpty());
        assertEquals(Month.SEPTEMBER, holiday.get().getMonth());
        assertNotNull(holiday.get().getDayList());
        assertEquals(1, holiday.get().getDayList().size());
        assertTrue(holiday.get().getDayList().contains(DayOfWeek.MONDAY));
        assertEquals(Integer.valueOf(0), holiday.get().getDay());

    }

    @Test
    public void checkoutNullTool() {
        assertThrows(NullPointerException.class, () -> {
            toolRental.checkout(null, 1, 1, LocalDate.now());
        });
    }

    @Test
    public void checkoutIncorrectTool() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> {
            toolRental.checkout("purple", 1, 1, LocalDate.now());
        });
        assertEquals("Tool purple is not in our current inventory.", iae.getMessage());
    }

    @Test
    public void checkoutWrongPercentages() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> {
            toolRental.checkout("LADW", 1, -4, LocalDate.now());
        });
        assertEquals("The discount percentage must be between 0 and 100.", iae.getMessage());

        iae = assertThrows(IllegalArgumentException.class, () -> {
            toolRental.checkout("CHNS", 101, -4, LocalDate.now());
        });

        assertEquals("The discount percentage must be between 0 and 100.",iae.getMessage());
    }

    @Test
    public void checkWrongDayCount() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> {
            toolRental.checkout("JAKD", -1, 1, LocalDate.now());
        });
        assertEquals("The rental day should be greater than or equal to one.", iae.getMessage());
    }

    public void checkoutNullDate() {
        assertThrows(NullPointerException.class, () -> {
            toolRental.checkout("CHNS", 1, 1, null);
        });
    }
}
