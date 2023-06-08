import java.text.SimpleDateFormat;
import java.util.*;

public class ShakeShackBurgerApplication {
	private static MenuContext menuContext;

	public static void main(String[] args) {
		menuContext = new MenuContext();
		displayMainMenu();
	}

	private static void displayMainMenu() {
		System.out.println("SHAKESHACK BURGER 에 오신걸 환영합니다.");
		System.out.println("아래 메뉴판을 보시고 메뉴를 골라 입력해주세요.\n");
		// 메인 페이지에서 0번 관리자 페이지인거 명시 안함.

		System.out.println("[ SHAKESHACK MENU ]");
		List<Menu> mainMenus = menuContext.getMenus("Main");
		int nextNum = printMenu(mainMenus, 1);

		System.out.println("[ ORDER MENU ]");
		List<Menu> orderMenus = menuContext.getMenus("Order");
		nextNum=printMenu(orderMenus, nextNum);
		System.out.println(nextNum+". recent orders |   완료된 최근주문 3개와 현재 대기중인 주문들을 보여줍니다");
		handleMainMenuInput();
	}

	private static int printMenu(List<Menu> menus, int num) {
		for (int i=0; i<menus.size(); i++) {
			System.out.println(num++ + ". " + menus.get(i).name + "   | " + menus.get(i).description);
		}
		return num;
	}

	private static void handleMainMenuInput() {
		Scanner scanner = new Scanner(System.in);
		int input = scanner.nextInt();
		switch (input) {
			case 0: // 메인페이지에서 0번 관리자 페이지인거 명시 안함.
				displayAdminMenu();
				break;
			case 1:
				displayBurgersMenu();
				break;
			case 2:
				displayFrozenCustardMenu();
				break;
			case 3:
				displayDrinksMenu();
				break;
			case 4:
				displayBeerMenu();
				break;
			case 5:
				displayOrderMenu();
				break;
			case 6:
				handleCancelMenuInput();
				break;
			case 7:
				RecentOrder();
				break;
			default:
				System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
				handleMainMenuInput();
				break;
		}
	}

	// 관리자 페이지 로드
	private static void displayAdminMenu() {
		Scanner scanner = new Scanner(System.in);

		System.out.println("이곳은 관리자 페이지입니다.");

		System.out.println("1. 대기주문 목록");
		System.out.println("2. 완료주문 목록");
		System.out.println("3. 상품 생성");
		System.out.println("4. 상품 삭제");
		System.out.print("항목을 선택하세요: ");

		int input = scanner.nextInt();
		switch (input) {
			case 1:
				displayWaitingOrder();
				break;
			case 2:
				printCompletedOrder();
				break;
			case 3:
				createItem();
				break;
			case 4:
				deleteItem();
				break;
			case 5:
				displayMainMenu();
			default:
				System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
				displayAdminMenu();
				break;
		}// switch() of the end
	}// displayAdminMenu() of the end

	// 모든 주문 상세 출력
	private static void printOrders(List<Order> orders) {
		for (int i=0; i<orders.size(); i++) {
			printOrder(orders.get(i));
			System.out.println();
		}// for() of the end
	}// printOrders() of the end

	private static void printRecentOrders(List<Order> orders) { //
		int tempNum=orders.size()-3;
		if (tempNum < 0){
			tempNum =0;
		}
		for (int i = tempNum; i < orders.size(); i++) {
			printOrder(orders.get(i));
			System.out.println();
		}
	}
	// 선택한 주문 내역 출력
	private static void printOrder(Order selectedOrder) {
		int num = selectedOrder.getOrderNum();
		System.out.println("대기 번호 : " + num);
		System.out.println("주문 상품 목록 : ");
		printMenuItems(selectedOrder.orderItems);
		System.out.println("주문 총 가격 : " + selectedOrder.getTotalPrice());
		System.out.println("요청 사항: " + selectedOrder.getRequestContent());

		// 날짜는 ISO 8601 형식으로 ex)2016-10-27T17:13:40+00:00
		Date date = selectedOrder.getOrderDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss:XXX");
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
		String dateString = sdf.format(date);
		System.out.println("주문 일시: " + dateString);
	}// printOrder() of the end


	// 1. 대기 중인 주문 조회 및 완료 화면
	private static void displayWaitingOrder() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("========================================");
		System.out.println("대기 중인 주문 목록입니다.\n");

		System.out.println("[ 대기 주문 목록 ]");
		List<Order> waitingOrders = menuContext.getWaitingOrders();
		if(waitingOrders.isEmpty()){
			System.out.println("대기 중인 주문이 없습니다.");
			System.out.println("========================================");
			displayMainMenu();
		}else {
			printOrders(waitingOrders);

			System.out.println("완료할 주문 대기 번호를 입력해주세요.");
			handleWaitingOrders(waitingOrders); // 주문 완료 처리할 메서드
		}
	}

	// 주문 완료 처리할 메서드
	private static void handleWaitingOrders(List<Order> orders) {
		Scanner scanner = new Scanner(System.in);
		int input = scanner.nextInt(); // 선택한 주문 번호
		Order selectedOrder = new Order(); // 선택한 주문
		boolean hasSelectedOrder = false; // 입력한 대기 번호에 맞는 주문이 있는지 여부

		for (Order order : orders) {
			if (order.getOrderNum() == input) {
				System.out.println("처리할 주문 존재함.");
				selectedOrder = order;
				hasSelectedOrder = true;
			}// if() of the end
		}// for() of the end

		if (hasSelectedOrder) { // 입력받은 번호에 맞는 주문이 있을 경우
			confirmCompleteOrder(selectedOrder);
		} else { // 입력받은 번호에 맞는 주문이 없을 경우
			System.out.println("해당 번호에 맞는 주문이 없습니다.");
			System.out.println("다시 입력해주세요.");
			handleWaitingOrders(orders);
		}// if~else() of the end

	}// handleWaitingOrders() of the end

	// 주문 완료 처리
	private static void confirmCompleteOrder(Order selectedOrder) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("아래 주문을 완료 처리 하시겠습니까?\n");

		printOrder(selectedOrder);

		System.out.println("1. 완료      2. 메뉴판");
		int confirm = scanner.nextInt();
		if(confirm==1){
			//menuContext.addToCompleteOrder(selectedOrder);
			setCompleteOrder(selectedOrder); // 주문 완료 리스트에 넣기
			resetWaitingOrder(selectedOrder); // 대기 주문 리스트에서 빼기
			System.out.println("해당 주문을 완료 처리 하였습니다.");
			System.out.println("========================================");
			displayMainMenu();
		}else if(confirm==2){
			System.out.println("========================================");
			displayMainMenu();
		}else {
			System.out.println("잘못된 입력입니다.");
			confirmCompleteOrder(selectedOrder);
		}// if~else() of the end
	}// completeOrder() of the end

	// 주문 완료 리스트에 넣기
	private static void setCompleteOrder(Order selectedOrder) {
		Order order = new Order();
		Date now = new Date();

		// List의 깊은 복사
		List<Item> it = new ArrayList<>();
		for(Item its : selectedOrder.getOrderItems()){
			it.add(its);
		}

		order.setOrderItems(it);
		order.setTotalPrice(selectedOrder.getTotalPrice());
		order.setRequestContent(selectedOrder.getRequestContent());
		order.setOrderDate(selectedOrder.getOrderDate()); // 주문 일시
		order.setCompleteDate(now); // 완료 주문 일시
		order.setOrderNum(selectedOrder.OrderNum);
		menuContext.addToCompleteOrder(order);
	}// setCompleteOrder() of the end

	// 주문 완료 처리된 주문은 대기 리스트에서 제외
	private static void resetWaitingOrder(Order selectedOrder) {
		menuContext.getWaitingOrders().remove(selectedOrder);
	}// resetWaitingOrder() of the end


	// 2. 주문 완료 목록 출력
	private static void printCompletedOrder() {
		Scanner scanner = new Scanner(System.in);

		System.out.println("========================================");
		System.out.println("처리 완료된 주문 목록입니다.\n");

		System.out.println("[ 완료 주문 목록 ]");
		printOrders(menuContext.getCompletedOrders());
		System.out.println("========================================");

		System.out.println("1. 메뉴판");
		int input = scanner.nextInt();
		if(input==1){
			System.out.println("========================================");
			displayMainMenu();
		}else {
			System.out.println("잘못된 입력입니다.");
			System.out.println("========================================");
			printCompletedOrder();
		}// if~else() of the end
	}// printCompletedOrder() of the end

	// 3. 상품 삭제
	private static void deleteItem() {
		Scanner scanner = new Scanner(System.in);

		System.out.println("삭제할 상품 정보를 입력해주세요.");
		System.out.print("메뉴: ");
		int menu = scanner.nextInt();
		scanner.nextLine();

		System.out.print("이름: ");
		String name = scanner.nextLine();

		if (menu >= 1 && menu <= 4) {
			boolean isMenuExist = findDeleteMenu(menu, name);
			if (isMenuExist) {
				System.out.println("상품이 삭제되었습니다.\n");
				displayMainMenu();
			}
			else {
				System.out.println("해당 상품이 존재하지 않습니다.\n");
				deleteItem();
			}
		} else {
			System.out.println("잘못된 메뉴입니다.");
			deleteItem();
		}
	}

	public static boolean findDeleteMenu(int menu, String name) {
		ArrayList<String> menuNames = new ArrayList<>(Arrays.asList("Burgers", "Frozen Custard", "Drinks", "Beer"));
		for (Item item : menuContext.getMenuItems(menuNames.get(menu - 1))) {
			if (item.getName().equals(name)) {
				if (!menuContext.getMenuItems(menuNames.get(menu - 1)).isEmpty()) {
					menuContext.deleteItemFromMenu(menuNames.get(menu - 1), item);
					return true;
				}
				else return false;
			}
		}
		return false;
	}

	// 4. 상품 생성
	private static void createItem() {
		Scanner scanner = new Scanner(System.in);

		System.out.println("새로운 상품 정보를 입력해주세요.");
		System.out.print("메뉴: ");
		int menu = scanner.nextInt();
		scanner.nextLine();
		System.out.print("이름: ");
		String name = scanner.nextLine();
		System.out.print("설명: ");
		String description = scanner.nextLine();
		System.out.print("가격: ");
		Double price = scanner.nextDouble();

		// 새로운 상품 생성
		Item newItem = new Item(name, price, description);

		switch (menu) {
			case 1:
				menuContext.addItemToMenu("Burgers", newItem);
				break;
			case 2:
				menuContext.addItemToMenu("Frozen Custard", newItem);
				break;
			case 3:
				menuContext.addItemToMenu("Drinks", newItem);
				break;
			case 4:
				menuContext.addItemToMenu("Beer", newItem);
				break;
			default:
				System.out.println("잘못된 메뉴입니다.");
				break;
		}

		System.out.println("새로운 상품이 생성되었습니다.");
		displayMainMenu();
	}
	private  static void RecentOrder(){
		System.out.println("========================================");
		System.out.println("대기 중인 주문 목록입니다.\n");

		System.out.println("[ 대기 주문 목록 ]");
		List<Order> waitingOrders = menuContext.getWaitingOrders();
		if(waitingOrders.isEmpty()){
			System.out.println("대기 중인 주문이 없습니다.");
			System.out.println("========================================");
		}else {
			printOrders(waitingOrders);
		}
		System.out.println("[ 최근주문완료 목록 ]");
		printRecentOrders(menuContext.getCompletedOrders());
		displayMainMenu();
	}

	private static void displayBurgersMenu() {
		System.out.println("SHAKESHACK BURGER 에 오신걸 환영합니다.");
		System.out.println("아래 상품메뉴판을 보시고 상품을 골라 입력해주세요.\n");

		System.out.println("[ Burgers MENU ]");
		List<Item> burgerItems = menuContext.getMenuItems("Burgers");
		printMenuItems(burgerItems);

		handleMenuItemInput(burgerItems);
	}

	private static void handleMenuItemInput(List<Item> items) {
		Scanner scanner = new Scanner(System.in);
		int input = scanner.nextInt();
		if (input > 0 && input <= items.size()) {
			input--;
			Item selectedItem = items.get(input);
			displayConfirmation(selectedItem);
		} else {
			System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
			handleMenuItemInput(items);
		}
	}

	private static void printMenuItems(List<Item> items) {
		for (int i=0; i<items.size(); i++) {
			int num = i + 1;
			System.out.println(num + ". " + items.get(i).name + "   | " + items.get(i).price + " | " + items.get(i).description);
		}
	}

	private static void displayFrozenCustardMenu() {
		System.out.println("SHAKESHACK BURGER 에 오신걸 환영합니다.");
		System.out.println("아래 상품메뉴판을 보시고 상품을 골라 입력해주세요.\n");

		System.out.println("[ Frozen Custard MENU ]");
		List<Item> frozenCustardItems = menuContext.getMenuItems("Frozen Custard");
		printMenuItems(frozenCustardItems);

		handleMenuItemInput(frozenCustardItems);
	}

	private static void displayDrinksMenu() {
		System.out.println("SHAKESHACK BURGER 에 오신걸 환영합니다.");
		System.out.println("아래 상품메뉴판을 보시고 상품을 골라 입력해주세요.\n");

		System.out.println("[ Drinks MENU ]");
		List<Item> drinkItems = menuContext.getMenuItems("Drinks");
		printMenuItems(drinkItems);

		handleMenuItemInput(drinkItems);
	}

	private static void displayBeerMenu() {
		System.out.println("SHAKESHACK BURGER 에 오신걸 환영합니다.");
		System.out.println("아래 상품메뉴판을 보시고 상품을 골라 입력해주세요.\n");

		System.out.println("[ Beer MENU ]");
		List<Item> beerItems = menuContext.getMenuItems("Beer");
		printMenuItems(beerItems);

		handleMenuItemInput(beerItems);
	}

	private static void displayConfirmation(Item menuItem) {
		System.out.println(menuItem.name + "   | " + menuItem.price + " | " + menuItem.description);
		System.out.println("위 메뉴를 장바구니에 추가하시겠습니까?");
		System.out.println("1. 확인        2. 취소");

		handleConfirmationInput(menuItem);
	}

	private static void handleConfirmationInput(Item menuItem) {
		Scanner scanner = new Scanner(System.in);
		int input = scanner.nextInt();
		if (input == 1) {
			menuContext.addToCart(menuItem);
			System.out.println("장바구니에 추가되었습니다.");
			displayMainMenu();
		} else if (input == 2) {
			displayMainMenu();
		} else {
			System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
			handleConfirmationInput(menuItem);
		}
	}

	private static void displayOrderMenu() {
		System.out.println("아래와 같이 주문 하시겠습니까?\n");
		menuContext.displayCart();

		System.out.println("[ Total ]");
		System.out.println("W " + menuContext.getTotalPrice() + "\n");
		System.out.println("1. 주문      2. 메뉴판");

		handleOrderMenuInput();
	}

	private static void handleOrderMenuInput() {
		Scanner scanner = new Scanner(System.in);
		int input = scanner.nextInt();
		if (input == 1) {
			displayOrderComplete();
		} else if (input == 2) {
			displayMainMenu();
		} else {
			System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
			handleOrderMenuInput();
		}
	}

	// 주문시 요청 사항 입력받기
	private static void displayOrderComplete() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("주문시 요청사항 메시지 : ");
		String request = scanner.nextLine();

		int orderNumber = menuContext.generateOrderNumber();
		System.out.println("주문이 완료되었습니다!\n");
		System.out.println("대기번호는 [ " + orderNumber + " ] 번 입니다.");
		setWaitingOrder(request);

		resetCartAndDisplayMainMenu();
	}

	// 주문한 내역 대기 주문 리스트에 입력하기
	private static void setWaitingOrder(String request) {
		Order order = new Order();
		Date now = new Date();

		// List의 깊은 복사
		List<Item> it = new ArrayList<>();
		for(Item its : menuContext.getCart()){
			it.add(its);
		}

		order.setOrderItems(it);
		order.setTotalPrice(menuContext.getTotalPrice());
		order.setRequestContent(request); //요청 사항
		order.setOrderDate(now);
		order.generateOrderCnt();
		order.setOrderNum(menuContext.getOrderNumber());
		menuContext.addToWaitingOrder(order);
	}// setWaitingOrder() of the end

	private static void resetCartAndDisplayMainMenu() {
		menuContext.resetCart();
		System.out.println("(3초후 메뉴판으로 돌아갑니다.)");
		try {
			Thread.sleep(3000); // 3초 대기
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		displayMainMenu();
	}

	private static void handleCancelMenuInput() {
		System.out.println("주문을 취소하시겠습니까?");
		System.out.println("1. 확인        2. 취소");

		handleCancelConfirmationInput();
	}

	private static void handleCancelConfirmationInput() {
		Scanner scanner = new Scanner(System.in);
		int input = scanner.nextInt();
		if (input == 1) {
			menuContext.resetCart();
			System.out.println("주문이 취소되었습니다.");
			displayMainMenu();
		} else if (input == 2) {
			displayMainMenu();
		} else {
			System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
			handleCancelConfirmationInput();
		}
	}
}

