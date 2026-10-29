public class OrderProcessor {

    private final InventoryService inventoryService;

    public OrderProcessor(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public boolean processOrder(String itemId, int quantity) {
        if (quantity <= 0) {
            return false;
        }

        if (inventoryService.checkStock(itemId)) {
            inventoryService.reserveItem(itemId);
            return true;
        }

        return false;
    }
}
