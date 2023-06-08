public class Item extends Menu {

	Double price;

	public Item(String name, Double price, String description) {
		super(name, description);
		this.price = price;
	}
}
