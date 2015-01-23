package game;

import game.item.Item;
import game.player.Player;
import game.util.ItemLoader;

import java.util.ArrayList;
import java.util.HashMap;

public class Inventory {
	private ArrayList<Item> items;
	private Player owner;
	private HashMap<String, GearSlot> gearSlots;
	private int rangs; // le currency
	
	public Inventory(Player player) {
		items = new ArrayList<Item>();
		this.owner = player;
		gearSlots = new HashMap<String, GearSlot>();
		for(int i=0; i<Defines.gearTypes.length; i++){
			gearSlots.put(Defines.gearTypes[i], new GearSlot());
		}
		rangs = 200;
	}
	
	public class GearSlot {
		private Item gear;
		private boolean occupied = false;
		
		public boolean isOccupied() {
			return occupied;
		}
		
		public void setGear(Item gear) {
			this.gear = gear;
			occupied = true;
		}
		
		public Item getGear() {
			return gear;
		}
		
		public void empty() {
			gear = null;
			occupied = false;
		}
	}
	
	public HashMap<String, GearSlot> getGearSlots() {
		return gearSlots;
	}
	
	public void add(Item item) {
		item.setOnMap(false);
		items.add(item);
	}
	
	/** Pass String CURRENCY_TYPE to also work with currency */
	// TODO this could cause poor performance on high quantities
	public void addFromFile(String filePath, int quantity) {
		if(filePath.equals(Defines.CURRENCY_NAME)){
			rangs += quantity;
		} else {
			for(int i=0; i<quantity; i++){
				addFromFile(filePath);
			}
		}
	}
	
	public void addFromFile(String filePath){
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
		i.actionOnDrop();
		owner.getRequests().add(i);
		items.remove(i);
	}
	
	public void remove(String name, int quantity) {
		if(name.equals(Defines.CURRENCY_NAME)){
			rangs -= quantity;
		} else {
			for(int i=0; i<items.size() && quantity>0; i++){
				if(name.equals(items.get(i).getName())){
					items.remove(i);
					quantity--;
				}
			}
			if(quantity>0){
				System.out.println("Payment failed for " + name);
			}
		}
	}
	
	public int count(String key) {
		if(key.equals(Defines.CURRENCY_NAME)){
			return rangs;
		}
		int count = 0;
		for(Item i : items){
			if(key.equals(i.getName())){
				count++;
			}
		}
		return count;
	}
	
	public boolean contains(String key) {
		for(Item i : items){
			if(key.equals(i.getName())){
				return true;
			}
		}
		return false;
	}
	
	public void equip(int index) {
		Item item = items.get(index);
		String type = item.getGearType();
		if(!type.equals("") && gearSlots.containsKey(type)) {
			getOwner().equip(item);
			items.remove(item);
			GearSlot gearSlot = gearSlots.get(type);
			if(gearSlot.isOccupied()){
				items.add(gearSlot.getGear()); // Return item to inventory
				getOwner().unequip(item);
				System.out.println("unequipped " + item.getName());
			}
			gearSlot.setGear(item);
			System.out.println("equipped " + item.getName());
		} else if (type.equals("")) {
			System.out.println("Cannot equip this item - no gear type");
		} else {
			System.out.println("No gear slot available for gear type " + type);
		}
	}
	
	public void unequip(String key) {
		if(gearSlots.containsKey(key)){
			GearSlot gearSlot = gearSlots.get(key);
			Item item = gearSlot.getGear();
			getOwner().unequip(item);
			gearSlot.empty();
			items.add(item);
			System.out.println("unequipped " + item.getName());
		}
	}

	public int getRangs() {
		return rangs;
	}

	public void setRangs(int rangs) {
		this.rangs = rangs;
	}
}
