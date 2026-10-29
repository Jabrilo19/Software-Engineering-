import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class OrderProcessorUnitTest {

    @Test
    public void testProcessOrder_InStock() {
        InventoryService mockInventory = Mockito.mock(InventoryService.class);
        Mockito.when(mockInventory.checkStock("item123")).thenReturn(true);

        OrderProcessor orderProcessor = new OrderProcessor(mockInventory);

        boolean result = orderProcessor.processOrder("item123", 1);
        Assertions.assertTrue(result);
    }

    @Test
    public void testProcessOrder_OutOfStock() {
        InventoryService mockInventory = Mockito.mock(InventoryService.class);
        Mockito.when(mockInventory.checkStock("item123")).thenReturn(false);

        OrderProcessor orderProcessor = new OrderProcessor(mockInventory);

        boolean result = orderProcessor.processOrder("item123", 1);
        Assertions.assertFalse(result);
    }
}