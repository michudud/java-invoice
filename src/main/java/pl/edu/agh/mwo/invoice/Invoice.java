package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {

    public static int lastNumber = 0;

    private final int invoiceNumber;

    private Map<Product, Integer> products = new HashMap<Product, Integer>();

    public Invoice(){
        this.invoiceNumber = ++lastNumber;
    }
    public int getNumber(){
        return invoiceNumber;
    }

    public void addProduct(Product product) {
        if(product == null) throw new IllegalArgumentException();
        if (!products.containsKey(product)) {
            products.put(product, 1);
        } else {
            products.put(product, products.get(product) + 1);
        }
    }

    public void addProduct(Product product, Integer quantity) {
        if(quantity <= 0 || product == null) throw new IllegalArgumentException();
        if(!products.containsKey(product)) {
            products.put(product, quantity);
        }else{
            products.put(product,products.get(product)+quantity);
        }
    }

    public Map<Product, Integer> getProducts(){
        return products;
    }

    public BigDecimal getNetTotal() {
        BigDecimal totalNet = BigDecimal.ZERO;
        for (Product product : products.keySet()) {
            BigDecimal quantity = new BigDecimal(products.get(product));
            totalNet = totalNet.add(product.getPrice().multiply(quantity));
        }
        return totalNet;
    }

    public BigDecimal getTaxTotal() {
        return getGrossTotal().subtract(getNetTotal());
    }

    public BigDecimal getGrossTotal() {
        BigDecimal totalGross = BigDecimal.ZERO;
        for (Product product : products.keySet()) {
            BigDecimal quantity = new BigDecimal(products.get(product));
            totalGross = totalGross.add(product.getPriceWithTax().multiply(quantity));
        }
        return totalGross;
    }

    public void printInvoice() {
        int numberOfproducts = 0;
        System.out.println("Invocie Number: " + invoiceNumber);

        for (Map.Entry<Product, Integer> product : products.entrySet()) {
            numberOfproducts += product.getValue();
            Product productInf = product.getKey();
            System.out.println(productInf.getName() + " " + product.getValue() + " " + productInf.getPrice());
        }

        System.out.println("Number of positions: " + products.size());
        System.out.println("Number of products: " + numberOfproducts);
    }
}
