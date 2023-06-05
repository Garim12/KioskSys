import java.util.List;

class Menu {
	String name;
	String description;
	List<Item> items; // 2번 필수 요구 사항

	// 생성자 메서드 영역
	Menu(String name, String description) {
		this.name = name;
		this.description = description;
	}

	// getter() , setter()
	public void setItems(List<Item> items) {
		this.items = items;
	}

	public List<Item> getItems() {
		return items;
	}


}
