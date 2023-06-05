import java.util.List;

public class Order {
// Store 클래스는 4번 필수 요구 사항

    List<Item> orderItems; // 주문 상품 목록
    int orderCnt; // 총 수량
    double totalPrice; // 총 가격

    // getter() , setter()
    public List<Item> getOrderItems() {
        return orderItems;
    }

    public int getOrderCnt() {
        return orderCnt;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setOrderItems(List<Item> orderItems) {
        this.orderItems = orderItems;
    }

    public void setOrderCnt(int orderCnt) {
        this.orderCnt = orderCnt;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
