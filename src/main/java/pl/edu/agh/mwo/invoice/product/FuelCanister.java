package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FuelCanister extends Product {

    private final BigDecimal excise = new BigDecimal("5.56");
    public FuelCanister(String name, BigDecimal price) {
        super(name, price, new BigDecimal("0.23"));
    }
    @Override
    public BigDecimal getPriceWithTax() {
        LocalDate todayDate = LocalDate.now();
        LocalDate motherInLawDay = LocalDate.of(todayDate.getYear(), 3, 5);

        if(todayDate.equals(motherInLawDay)){
            return getPrice().multiply(getTaxPercent());
        }else{
            return getPrice().add(excise).multiply(getTaxPercent());
        }
    }
}
