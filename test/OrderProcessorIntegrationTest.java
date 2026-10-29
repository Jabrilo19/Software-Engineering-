import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OrderProcessorIntegrationTest {

    static class InMemoryInventoryService implements InventoryService {
        private final Map<String, Integer> inventory = new HashMap<>();

        public void addStock(String itemId, int quantity) {
            inventory.put(itemId, quantity);
        }

        public int getStock(String itemId) {
            return inventory.getOrDefault(itemId, 0);
        }

        @Override
        public boolean checkStock(String itemId) {
            return inventory.getOrDefault(itemId, 0) > 0;
        }

        @Override
        public boolean reserveItem(String itemId) {
            int currentStock = inventory.getOrDefault(itemId, 0);
            if (currentStock > 0) {
                inventory.put(itemId, currentStock - 1);
                return true;
            }
            return false;
        }
    }

    @Test
    public void testOrderProcessorWithRealInventory() {
        InMemoryInventoryService realInventory = new InMemoryInventoryService();
        realInventory.addStock("laptop", 5);

        OrderProcessor processor = new OrderProcessor(realInventory);

        boolean result = processor.processOrder("laptop", 1);

        Assertions.assertTrue(result);
        Assertions.assertEquals(4, realInventory.getStock("laptop"));
    }
}
