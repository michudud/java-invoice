package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
    private HashMap<Product, Integer> products = new HashMap<>();

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

    public BigDecimal getSubtotal() {
        BigDecimal subTotal = BigDecimal.ZERO;

            for (Map.Entry<Product, Integer> entry : products.entrySet()) {
                Product product = entry.getKey();
                Integer qty = entry.getValue();
                subTotal = subTotal.add(product.getPrice().multiply(BigDecimal.valueOf(qty)));
            }

        return subTotal;
    }

    public BigDecimal getTax() {
        BigDecimal tax = BigDecimal.ZERO;

            for (Map.Entry<Product, Integer> entry : products.entrySet()) {
                Product product = entry.getKey();
                Integer qty = entry.getValue();
                tax = tax.add(product.getPrice().multiply(product.getTaxPercent()).multiply(BigDecimal.valueOf(qty)));
            }
        //}
        return tax;
    }

    public BigDecimal getTotal() {
        BigDecimal total = BigDecimal.ZERO;

            for (Map.Entry<Product, Integer> entry : products.entrySet()) {
                Product product = entry.getKey();
                Integer qty = entry.getValue();
                total = total.add(product.getPriceWithTax().multiply(BigDecimal.valueOf(qty)));
            }

        return total;
    }
}
