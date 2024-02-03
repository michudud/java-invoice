package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
    private HashMap<Product, Integer> products;

    public void addProduct(Product product) {
        if(!products.containsKey(product)) {
            this.products.put(product, 1);
        }else{
            this.products.put(product,products.get(product)+1);
        }
    }

    public void addProduct(Product product, Integer quantity) {
        if(!products.containsKey(product)) {
            this.products.put(product, quantity);
        }else{
            this.products.put(product,products.get(product)+quantity);
        }
    }

    public BigDecimal getSubtotal() {
        BigDecimal subTotal = BigDecimal.ZERO;

        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            Product product = entry.getKey();
            Integer qty = entry.getValue();
            subTotal.add(product.getPrice().multiply(BigDecimal.valueOf(qty)));
        }

        return subTotal;
    }

    public BigDecimal getTax() {
        BigDecimal tax = BigDecimal.ZERO;

        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            Product product = entry.getKey();
            Integer qty = entry.getValue();
            tax.add(product.getPrice().multiply(product.getTaxPercent()).multiply(BigDecimal.valueOf(qty)));
        }

        return tax;
    }

    public BigDecimal getTotal() {
        BigDecimal total = BigDecimal.ZERO;

        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            Product product = entry.getKey();
            Integer qty = entry.getValue();
            total.add(product.getPriceWithTax().multiply(BigDecimal.valueOf(qty)));
        }

        return total;
    }
}
