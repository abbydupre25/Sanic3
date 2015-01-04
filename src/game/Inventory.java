package game;

import game.item.Item;
import game.player.Player;
import game.util.ItemLoader;

import java.util.ArrayList;

public class Inventory {
	private ArrayList<Item> items;
	private Player owner;
	
	public Inventory() {
		items = new ArrayList<Item>();
	}
	
	public void add(Item item) {
		item.setOnMap(false);
		items.add(item);
	}
	
	public void addFromFile(String filePath) {
		add(ItemLoader.getItem(filePath));
	}
	
	public ArrayList<Item> getItems() {
		return items;
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}
	
	public void drop(int index) {
		Item i = items.get(index);
		i.setPos(owner.getPos().clone());
		i.setOnMap(true);
		owner.getRequests().add(i);
		items.remove(i);
	}
	
	public void remove(String name, int quantity) {
		for(int i=0; i<items.size() && quantity>0; i++){
			if(name.equals(items.get(i).getName())){
				items.remove(i);
				quantity--;
			}
		}
		if(quantity>0){
			// There should be a better way to handle this...
			System.out.println("Insufficient " + name + " in inventory");
		}
	}
	
	public boolean contains(String key) {
		for(Item i : items){
			if(key.equals(i.getName())){
				return true;
			}
		}
		return false;
	}
}
