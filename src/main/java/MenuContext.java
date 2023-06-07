import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class MenuContext {
	private Map<String, List<Menu>> menus;
	private Map<String, List<Item>> menuItems;
	private List<Item> cart;
	private double totalPrice;
	private int orderNumber;
	private List<Order> waitingOrders; // add - 대기 주문 상품
	private List<Order> completedOrders; // add - 완료된 주문 상품
	private String requestContent;

	public MenuContext() {
		menus = new HashMap<>();
		menuItems = new HashMap<>();
		cart = new ArrayList<>();
		totalPrice = 0.0;
		orderNumber = 0;
		waitingOrders = new ArrayList<>();
		completedOrders = new ArrayList<>();

		initializeMenuItems();
	}

	private void initializeMenuItems() {
		List<Menu> mainMenus = new ArrayList<>();
		mainMenus.add(new Menu("Burgers", "앵거스 비프 통살을 다져만든 버거"));
		mainMenus.add(new Menu("Forzen Custard", "매장에서 신선하게 만드는 아이스크림"));
		mainMenus.add(new Menu("Drinks", "매장에서 직접 만드는 음료"));
		mainMenus.add(new Menu("Beer", "뉴욕 브루클린 브루어리에서 양조한 맥주"));

		List<Menu> orderMenus = new ArrayList<>();
		orderMenus.add(new Menu("Order", "장바구니를 확인 후 주문합니다."));
		orderMenus.add(new Menu("Cancel", "진행중인 주문을 취소합니다."));

		menus.put("Main", mainMenus);
		menus.put("Order", orderMenus);

		List<Item> burgersMenus = new ArrayList<>();
		burgersMenus.add(new Item("ShackBurger", 6.9, "토마토, 양상추, 쉑소스가 토핑된 치즈버거"));
		burgersMenus.add(new Item("SmokeShack", 8.9, "베이컨, 체리 페퍼에 쉑소스가 토핑된 치즈버거"));
		burgersMenus.add(new Item("Shroom Burger", 9.4, "몬스터 치즈와 체다 치즈로 속을 채운 베지테리안 버거"));
		burgersMenus.add(new Item("Cheeseburger", 6.9, "포테이토 번과 비프패티, 치즈가 토핑된 치즈버거"));
		burgersMenus.add(new Item("Hamburger", 5.4, "비프패티를 기반으로 야채가 들어간 기본버거"));

		List<Item> frozenCustardMenu = new ArrayList<>();
		frozenCustardMenu.add(new Item("Vanilla Ice cream", 1.4, "It's a basic"));
		frozenCustardMenu.add(new Item("Chocolate peanuts butter Ice cream", 1.0, "Delish thing"));

		List<Item> drinksMenu = new ArrayList<>();
		drinksMenu.add(new Item("Coke", 1.5, "Coca Cola"));
		drinksMenu.add(new Item("Canada Dry", 1.5, "It's Ginger Ale"));

		List<Item> beerMenu = new ArrayList<>();
		beerMenu.add(new Item("Cass", 4.0, "Origin beer in Korea"));
		beerMenu.add(new Item("Draft Beer", 4.0, "Every like it"));

		menuItems.put("Burgers", burgersMenus);
		menuItems.put("Frozen Custard", frozenCustardMenu);
		menuItems.put("Drinks", drinksMenu);
		menuItems.put("Beer", beerMenu);
	}

	public List<Menu> getMenus(String key) {
		return menus.get(key);
	}

	public List<Item> getMenuItems(String key) {
		return menuItems.get(key);
	}

	// 상품 생성 리스트 추가
	public void addItemToMenu(String menu, Item item) {
		List<Item> menuList = menuItems.get(menu);

		if (menuList == null) {
			menuList = new ArrayList<>();
			menuItems.put(menu, menuList);
		}

		menuList.add(item);
	}

	// 상품 삭제 리스트 추가
	public void deleteItemFromMenu(String menu, Item item) {
		List<Item> menuList = menuItems.get(menu);

		if (menuList != null) {
			menuList.remove(item);
		}
	}

	public List<Order> getWaitingOrders() {
		return waitingOrders;
	}

	public List<Order> getCompletedOrders() {
		return completedOrders;
	}

	public List<Item> getCart() {
		return cart;
	}

	public int getOrderNumber() {
		return orderNumber;
	}

	public void addToCart(Item menuItem) {
		cart.add(menuItem);
		totalPrice += menuItem.price;
	}

	public void displayCart() {
		for (Item item : cart) {
			System.out.println(item.name + "   | " + item.price + " | " + item.description);
		}
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public int generateOrderNumber() {
		orderNumber++;
		return orderNumber;
	}

	public void resetCart() {
		cart.clear();
		totalPrice = 0.0;
	}

	public void addToWaitingOrder(Order order){
		// 주문 완료 후에 실행
		waitingOrders.add(order);
	}

	public void addToCompleteOrder(Order order) {
		// 관리자 페이지에서 주문 처리 완료 후에 싫행
		completedOrders.add(order);
	}
}