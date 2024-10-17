import com.primaryredtools.ToolRental;
import com.primaryredtools.models.RentalAgreement;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class DemonstrationScenariosTest {

    ToolRental toolRental;

    @Before
    public void setup() {
        toolRental = new ToolRental();
    }

    @Test
    public void runTest1() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> {
            toolRental.checkout(
                    "JAKR",
                    5,
                    101,
                    LocalDate.of(2015, 9, 3));
        });
        assertEquals("The discount percentage must be between 0 and 100.",iae.getMessage());
    }

    @Test
    public void runTest2() {
        RentalAgreement rentalAgreement = toolRental.checkout(
                "LADW",
                3,
                10,
                LocalDate.of(2020, 7, 2)
        );

        assertEquals(LocalDate.of(2020, 7, 5), rentalAgreement.getDueDate());
        assertEquals(3, rentalAgreement.calculateChargeDays());
        assertEquals(BigDecimal.valueOf(1.99), rentalAgreement.getDailyRentalCharge());
        assertEquals(BigDecimal.valueOf(1.99 * 3), rentalAgreement.getPreDiscountCharge());
        assertEquals(10, rentalAgreement.getDiscountPercentage());
        assertEquals(BigDecimal.valueOf(0.60).setScale(2, RoundingMode.HALF_UP),
                rentalAgreement.getDiscountAmount());
        assertEquals(BigDecimal.valueOf(5.37), rentalAgreement.getFinalCharge());
    }

    @Test
    public void runTest3() {
        RentalAgreement rentalAgreement = toolRental.checkout(
                "CHNS",
                5,
                25,
                LocalDate.of(2015, 9, 2)
        );

        assertEquals(LocalDate.of(2015, 9, 7), rentalAgreement.getDueDate());
        assertEquals(3, rentalAgreement.calculateChargeDays());
        assertEquals(BigDecimal.valueOf(1.49), rentalAgreement.getDailyRentalCharge());
        assertEquals(BigDecimal.valueOf(1.49 * 3), rentalAgreement.getPreDiscountCharge());
        assertEquals(25, rentalAgreement.getDiscountPercentage());
        assertEquals(BigDecimal.valueOf(1.12).setScale(2, RoundingMode.HALF_UP),
                rentalAgreement.getDiscountAmount());
        assertEquals(BigDecimal.valueOf(3.35), rentalAgreement.getFinalCharge());
    }

    @Test
    public void runTest4() {
        RentalAgreement rentalAgreement = toolRental.checkout(
                "JAKR",
                9,
                0,
                LocalDate.of(2015, 7, 2)
        );

        assertEquals(LocalDate.of(2015, 7, 11), rentalAgreement.getDueDate());
        assertEquals(6, rentalAgreement.calculateChargeDays());
        assertEquals(BigDecimal.valueOf(2.99), rentalAgreement.getDailyRentalCharge());
        assertEquals(BigDecimal.valueOf(2.99 * 6), rentalAgreement.getPreDiscountCharge());
        assertEquals(0, rentalAgreement.getDiscountPercentage());
        assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
                rentalAgreement.getDiscountAmount());
        assertEquals(BigDecimal.valueOf(17.94), rentalAgreement.getFinalCharge());
    }

    @Test
    public void runTest5() {
        RentalAgreement rentalAgreement = toolRental.checkout(
                "JAKR",
                4,
                50,
                LocalDate.of(2020, 7, 2)
        );

        assertEquals(LocalDate.of(2020, 7, 6), rentalAgreement.getDueDate());
        assertEquals(2, rentalAgreement.calculateChargeDays());
        assertEquals(BigDecimal.valueOf(2.99), rentalAgreement.getDailyRentalCharge());
        assertEquals(BigDecimal.valueOf(2.99 * 2), rentalAgreement.getPreDiscountCharge());
        assertEquals(50, rentalAgreement.getDiscountPercentage());
        assertEquals(BigDecimal.valueOf(2.99).setScale(2, RoundingMode.HALF_UP),
                rentalAgreement.getDiscountAmount());
        assertEquals(BigDecimal.valueOf(2.99), rentalAgreement.getFinalCharge());
    }


}
