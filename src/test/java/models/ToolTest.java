package models;

import com.primaryredtools.models.Charge;
import com.primaryredtools.models.Tool;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class ToolTest {
    @Test
    public void checkToString() {
        Tool tool = Tool.builder()
                .toolCode("HAMM")
                .toolType("Hammer")
                .brand("M.C. Hammer")
                .charge(Charge.builder()
                        .holidayCharge(true)
                        .weekendCharge(true)
                        .weekdayCharge(true)
                        .dailyCharge(BigDecimal.valueOf(0.02))
                        .toolType("Hammer")
                        .build())
                .build();

        String expected
                = "HAMM       Hammer           M.C. Hammer          0.02  Yes             Yes             Yes            ";
        assertEquals(expected.length(), tool.toString().length());
        assertEquals(expected, tool.toString());
    }

    @Test
    public void checkToString2() {
        Tool tool = Tool.builder()
                .toolCode("HAMM")
                .toolType("Hammer")
                .brand("M.C. Hammer")
                .charge(Charge.builder()
                        .holidayCharge(false)
                        .weekendCharge(false)
                        .weekdayCharge(false)
                        .dailyCharge(BigDecimal.valueOf(0.02))
                        .toolType("Hammer")
                        .build())
                .build();

        String expected
                = "HAMM       Hammer           M.C. Hammer          0.02  No              No              No             ";
        assertEquals(expected.length(), tool.toString().length());
        assertEquals(expected, tool.toString());
    }
}
