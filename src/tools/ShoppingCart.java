//package tools;
//
//import entity.Product;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class ShoppingCart {
//    private List<Product> items;
//
//    public ShoppingCart() {
//        items = new ArrayList<>();
//    }
//
//    public void addItem(Product product) {
//        items.add(product);
//        product.decreaseQuantity(1); // Уменьшаем количество продукта на 1
//        System.out.println(product.getName() + " добавлен в корзину.");
//    }
//    
//
//    
//    public void deleteLastItem() {
//        if (!items.isEmpty()) {
//            Product lastItem = items.remove(items.size() - 1);
//            System.out.println(lastItem.getName() + " удален из корзины.");
//        } else {
//            System.out.println("Корзина пуста.");
//        }
//    }
//
//    public double getTotal() {
//        double total = 0;
//        for (Product item : items) {
//            total += item.getPrice();
//        }
//        double roundedTotalPrice = Math.round(total*100.0)/100.0;
//        return roundedTotalPrice;
//    }
//
//
//    
//    public void displayItems() {
//        System.out.println("Товары в корзине:");
//
//        // Создаем мапу для подсчета количества каждого товара
//        Map<Product, Integer> itemCounts = new HashMap<>();
//
//        // Подсчитываем количество каждого товара
//        for (Product item : items) {
//            if (itemCounts.containsKey(item)) {
//                itemCounts.put(item, itemCounts.get(item) + 1);
//            } else {
//                itemCounts.put(item, 1);
//            }
//        }
//
//        // Выводим информацию о товарах
//        for (Map.Entry<Product, Integer> entry : itemCounts.entrySet()) {
//            Product product = entry.getKey();
//            int count = entry.getValue();
//            System.out.println(product.getName() + " (" + count + " шт.) - $" + product.getPrice());
//        }
//    }
//
//    
//
//    public void clear() {
//        items.clear();
//        System.out.println("Корзина очищена.");
//    }
//    
//    public List<Product> getAllItems() {
//        return items;
//    }    
//
//    public Map<String, Integer> getProductCounts() {
//        Map<String, Integer> productCounts = new HashMap<>();
//
//        // Подсчитываем количество каждого товара
//        for (Product item : items) {
//            String productName = item.getName();
//            int count = productCounts.getOrDefault(productName, 0);
//            productCounts.put(productName, count + 1);
//        }
//
//        return productCounts;
//    }
//  
//    
//}
