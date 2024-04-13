package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;

public class FuelCanister extends Product {

    private final BigDecimal excise = new BigDecimal("5.56");

    private Clock clock = Clock.systemDefaultZone();
    public FuelCanister(String name, BigDecimal price) {
        super(name, price, new BigDecimal("0.23"));
    }
    @Override
    public BigDecimal getPriceWithTax() {
        LocalDate todayDate = LocalDate.now(clock);
        LocalDate motherInLawDay = LocalDate.of(todayDate.getYear(), 03, 05);

        if(todayDate.equals(motherInLawDay)){
            return getPrice().multiply(getTaxPercent()).add(getPrice());
        }else{
            return getPrice().add(excise).multiply(getTaxPercent()).add(getPrice()).add(excise);
        }
    }
}
