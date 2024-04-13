package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.mockito.*;
import pl.edu.agh.mwo.invoice.Invoice;
import pl.edu.agh.mwo.invoice.product.*;

import static org.mockito.Mockito.doReturn;


public class InvoiceTest {

    private Invoice invoice;

    private final static LocalDate LOCAL_DATE = LocalDate.of(LocalDate.now().getYear(), 03, 05);

    @InjectMocks
    private FuelCanister fuelCanisterOnMotherInLawDay = new FuelCanister("95", new BigDecimal("10"));

    @Mock
    private Clock clock;

    private Clock fixedClock;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);

        fixedClock = Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        doReturn(fixedClock.instant()).when(clock).instant();
        doReturn(fixedClock.getZone()).when(clock).getZone();
    }
    @Before
    public void createEmptyInvoiceForTheTest() {
        invoice = new Invoice();
    }

    @Test
    public void testEmptyInvoiceHasEmptySubtotal() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testEmptyInvoiceHasEmptyTaxAmount() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getTaxTotal()));
    }

    @Test
    public void testEmptyInvoiceHasEmptyTotal() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test
    public void testInvoiceSubtotalWithTwoDifferentProducts() {
        Product onions = new TaxFreeProduct("Warzywa", new BigDecimal("10"));
        Product apples = new TaxFreeProduct("Owoce", new BigDecimal("10"));
        invoice.addProduct(onions);
        invoice.addProduct(apples);
        Assert.assertThat(new BigDecimal("20"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceSubtotalWithManySameProducts() {
        Product onions = new TaxFreeProduct("Warzywa", BigDecimal.valueOf(10));
        invoice.addProduct(onions, 100);
        Assert.assertThat(new BigDecimal("1000"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceHasTheSameSubtotalAndTotalIfTaxIsZero() {
        Product taxFreeProduct = new TaxFreeProduct("Warzywa", new BigDecimal("199.99"));
        invoice.addProduct(taxFreeProduct);
        Assert.assertThat(invoice.getNetTotal(), Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test
    public void testInvoiceHasProperSubtotalForManyProducts() {
        invoice.addProduct(new TaxFreeProduct("Owoce", new BigDecimal("200")));
        invoice.addProduct(new DairyProduct("Maslanka", new BigDecimal("100")));
        invoice.addProduct(new OtherProduct("Wino", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("310"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceHasProperTaxValueForManyProduct() {
        // tax: 0
        invoice.addProduct(new TaxFreeProduct("Pampersy", new BigDecimal("200")));
        // tax: 8
        invoice.addProduct(new DairyProduct("Kefir", new BigDecimal("100")));
        // tax: 2.30
        invoice.addProduct(new OtherProduct("Piwko", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("10.30"), Matchers.comparesEqualTo(invoice.getTaxTotal()));
    }

    @Test
    public void testInvoiceHasProperTotalValueForManyProduct() {
        // price with tax: 200
        invoice.addProduct(new TaxFreeProduct("Maskotki", new BigDecimal("200")));
        // price with tax: 108
        invoice.addProduct(new DairyProduct("Maslo", new BigDecimal("100")));
        // price with tax: 12.30
        invoice.addProduct(new OtherProduct("Chipsy", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("320.30"), Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test
    public void testInvoiceHasPropoerSubtotalWithQuantityMoreThanOne() {
        // 2x kubek - price: 10
        invoice.addProduct(new TaxFreeProduct("Kubek", new BigDecimal("5")), 2);
        // 3x kozi serek - price: 30
        invoice.addProduct(new DairyProduct("Kozi Serek", new BigDecimal("10")), 3);
        // 1000x pinezka - price: 10
        invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
        Assert.assertThat(new BigDecimal("50"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceHasPropoerTotalWithQuantityMoreThanOne() {
        // 2x chleb - price with tax: 10
        invoice.addProduct(new TaxFreeProduct("Chleb", new BigDecimal("5")), 2);
        // 3x chedar - price with tax: 32.40
        invoice.addProduct(new DairyProduct("Chedar", new BigDecimal("10")), 3);
        // 1000x pinezka - price with tax: 12.30
        invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
        Assert.assertThat(new BigDecimal("54.70"), Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvoiceWithZeroQuantity() {
        invoice.addProduct(new TaxFreeProduct("Tablet", new BigDecimal("1678")), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvoiceWithNegativeQuantity() {
        invoice.addProduct(new DairyProduct("Zsiadle mleko", new BigDecimal("5.55")), -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddingNullProduct() {
        invoice.addProduct(null);
    }

    @Test
    public void testInvoiceHasNumber(){
        Invoice invoice = new Invoice();
        int number = invoice.getNumber();
    }

    @Test
    public void testInvoiceNumberIsGreaterThan0(){
        Assert.assertTrue(new Invoice().getNumber() > 0);
    }

    @Test
    public void testInvoiceOfSecondInvoiceNumberIsGreaterThanPreviousOne(){
        int n1 = new Invoice().getNumber();
        int n2 = new Invoice().getNumber();
        Assert.assertThat(n2, Matchers.greaterThan(n1));
    }

    @Test
    public void testAddingDuplicate(){
        Invoice invoice = new Invoice();
        Product product = new OtherProduct("testProduct", new BigDecimal("10"));

        invoice.addProduct(product);
        invoice.addProduct(product, 2);
        invoice.addProduct(product, 3);

        Assert.assertEquals(6, (int) invoice.getProducts().get(product));
        Assert.assertEquals(new BigDecimal("60"), invoice.getNetTotal());
    }

    @Test
    public void testCheckBottleOfWinePrice(){
        Invoice invoice = new Invoice();
        Product wine = new BottleOfWine("red", new BigDecimal("10"));
        invoice.addProduct(wine);

        BigDecimal correctPrice = new BigDecimal("15.56").multiply(new BigDecimal("0.23")).add(new BigDecimal("15.56"));

        Assert.assertEquals(correctPrice, invoice.getGrossTotal());
    }

    @Test
    public void testFuelPriceOnMotherInLawDay(){
        Invoice invoice = new Invoice();
        invoice.addProduct(fuelCanisterOnMotherInLawDay);
        BigDecimal correctPrice = new BigDecimal("12.30");

        Assert.assertEquals(correctPrice, invoice.getGrossTotal());
    }

    @Test
    public void testFuelPriceOnNormalDay(){
        Invoice invoice = new Invoice();
        FuelCanister fuel = new FuelCanister("95", new BigDecimal("10"));
        invoice.addProduct(fuel);
        BigDecimal correctPrice = new BigDecimal("15.56").multiply(new BigDecimal("0.23")).add(new BigDecimal("15.56"));

        Assert.assertEquals(correctPrice, invoice.getGrossTotal());
    }
}
