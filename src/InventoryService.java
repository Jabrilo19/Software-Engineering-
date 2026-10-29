public interface InventoryService {

    boolean checkStock(String itemId);

    boolean reserveItem(String itemId);
}