package me.xxfreakdevxx.de.game.inventory;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import me.xxfreakdevxx.de.game.MouseInput;
import me.xxfreakdevxx.de.game.SquareCraft;
import me.xxfreakdevxx.de.game.object.Material;

public class Inventory {
	
	public boolean showInventory = false;
	private int inventory_slots_per_row = 0;
	private int space_between_slots = 4;
	private int slot_size = 36;
	private int inventory_lines = 6;
	public int width = 0;
	public int height = 0;
	public int x = 0;
	public int y = 0;
	private int distance_to_border_x = 20;
	private int distance_to_border_y = 20;
	private int maximal_stack_size = 999;
	public Slot[] slots = null;
	private Color inventory_color = new Color(0f,0.4f,1f,0.8f);
	private TrashSlot trash = null;
	private Cursor cursor = new Cursor();
	
	public Inventory() {
		this.inventory_slots_per_row = 14;
		this.inventory_lines = 4;
		setup();
	}
	public Inventory(int inventory_slots_per_row, int inventory_lines) {
		this.inventory_slots_per_row = inventory_slots_per_row;
		this.inventory_lines = inventory_lines;
		setup();
	}
	
	public void setup() {
		slots = new Slot[inventory_lines*inventory_slots_per_row];
		width = (inventory_slots_per_row*slot_size) + ((inventory_slots_per_row+(distance_to_border_x)) *space_between_slots);
		height = (inventory_lines*slot_size)        + ((inventory_lines+1)         *space_between_slots);
		x = SquareCraft.windowWidth-width-20;
		int id = 0;
		for(int l = 0; l != inventory_lines; l++) {
			for(int r = 0; r != inventory_slots_per_row; r++) {
//				slots[id] = new Slot(this, id, new ItemStack(Material.values()[SquareCraft.randomInteger(0, Material.values().length-1)]));
				slots[id] = new Slot(this, id, new ItemStack(Material.AIR));
				slots[id].setItemStack(slots[id].getItemStack().setAmount(SquareCraft.randomInteger(0, maximal_stack_size)));
				id++;
			}
		}
		trash = new TrashSlot(this);
	}
	
	public void clicked(Point p) {
		if(isOnInventory(p)) {
			for(Slot s : slots) {
				s.isClicked(p);
			}
		}
	}
	
	public boolean isOnInventory(Point p) {
		if(p.x >= x && p.x <= x+width && p.y >= y && p.y <= y+height) return true;
		else return false;
	}
	
	public void tick() {
		x = SquareCraft.windowWidth-width-distance_to_border_x;
		y = distance_to_border_y;
		for(int i = 0; i != slots.length; i++) {
			Slot slot = slots[i];
			if(slot != null) slot.tick();
		}
		trash.tick();
		cursor.tick();
	}
	
	public void render(Graphics g) {
		if(showInventory) {
			g.setColor(inventory_color);
			g.fillRoundRect((int)(x), (int)(y), width, height, 10, 10);
			for(int i = 0; i != slots.length; i++) {
				Slot slot = slots[i];
				if(slot != null) slot.render(g);
				else System.out.println("Slot ID: "+i+" == null");
			}
			trash.render(g);
			cursor.render(g);
		}
	}
	
	public boolean addItem(ItemStack item) {
		for(Slot s : slots) {
			if(s.getItemStack().getMaterial() == Material.AIR) {
				s.setItemStack(item); 
				return true;
			}
		}
		return false;
	}
	
	public class Slot {
		
		protected int id = 0;
		protected ItemStack item = new ItemStack(Material.AIR);
		protected boolean render_box = false;
		protected boolean render_slot_id = false;
		protected boolean isLooked = false;
		protected int x = 0;
		protected int y = 0;
		protected Color slot_color = new Color(0f,1f,0f,0.3f);
		protected Inventory inventory = null;
		
		public Slot(Inventory inventory, int id, ItemStack item) {
			this.inventory = inventory;
			this.id = id;
			this.item = item;
		}
		public Slot(Inventory inventory, int id) {
			this.inventory = inventory;
			this.id = id;
			this.item = new ItemStack(Material.AIR);
		}
		
		int a = id;
		int b = 0;
		public void tick() {
			a=id;
			b = 0;
			while(a >= inventory_slots_per_row) {
				a-=inventory_slots_per_row;
				b++;
			}
			this.x = a * slot_size + ((a+1)*space_between_slots) + inventory.x;
			this.y = b * slot_size + ((b+1)*space_between_slots) + inventory.y;
		}
		
		public void render(Graphics g) {
			if(render_box) {
				g.setColor(slot_color);
				g.fillRect((int)(x), (int)(y), slot_size, slot_size);				
			}
			g.setColor(new Color(0f,0f,0f,0.3f));
			g.drawRect(x, y, slot_size, slot_size);
			g.setColor(Color.black);
			if(item != null) {
				g.drawImage(item.getMaterial().getTexture(), (int)(x)+1, (int)(y)+1, slot_size-1, slot_size-1, null);
				if(item.getAmount() != 0)g.drawString(""+item.getAmount(), x+2, y+slot_size-2);
			}
			if(render_slot_id) g.drawString(""+id, x, y+10);
		}
		
		public void isClicked(Point p) {
			if((p.x > x && p.x < (x+slot_size)) && (p.y > y && p.y < (y+slot_size))) {
				
				if(cursor.item.getMaterial() == Material.AIR && 
						this.item.getMaterial() != Material.AIR) {
					//Cursor: Kein Item
					//Slot: Hat Item
					
					cursor.setItemStack(this.item);
					this.item = new ItemStack(Material.AIR);
					
				}else if(cursor.item.getMaterial() != Material.AIR &&
						this.item.getMaterial() == Material.AIR) {
					//Cursor: Hat Item
					//Slot: Kein Item
					
					this.item = cursor.getItemStack();					
					cursor.setItemStack(new ItemStack(Material.AIR));
				
				}else if(cursor.item.getMaterial() != Material.AIR && 
						this.item.getMaterial() != Material.AIR) {
					//Cursor: Hat Item
					//Slot: Hat Item
					
					ItemStack item = cursor.getItemStack();
					cursor.setItemStack(this.item);
					this.item = item;
				}
			}
		}
		
		public ItemStack getItemStack() {
			return item;
		}
		public void setItemStack(ItemStack item) {
			this.item = item;
		}
		
	}
	public class TrashSlot extends Slot {
		@Override
		public void tick() {
			x = inventory.x+inventory.width;
			y = inventory.y+distance_to_border_y+(2*height);
			super.tick();
		}
		
		public TrashSlot(Inventory inventory) {
			super(inventory, -1, null);
			this.inventory = inventory;
		}
		
		@Override
		public void render(Graphics g) {
			g.setColor(new Color(1f,0f,0f,0.8f));
			g.fillRoundRect(x, y, slot_size, slot_size, 15, 15);
			g.setColor(Color.WHITE);
			g.drawLine(x+10, y+10, x-10+slot_size, y-10+slot_size);
			g.drawLine(x+10, y+slot_size-10, x+slot_size-10, y+10);
			g.setColor(Color.black);
			if(render_slot_id) g.drawString("trash", x, y);
			super.render(g);
		}
	}
	public class Cursor {
		private ItemStack item = new ItemStack(Material.AIR);
		public int x = 0;
		public int y = 0;
		public int size = 20;
		
		public Cursor() { }
		
		public void tick() {
			x = (int)(MouseInput.getInstance().x_unconverted);
			y = (int)(MouseInput.getInstance().y_unconverted);
		}
		
		public void render(Graphics g) {
			if(item.getMaterial() != Material.AIR) {
				g.setColor(Color.BLACK);
				g.drawRect(x-(size/2), y-(size/2), size, size);
				g.drawImage(item.getMaterial().getTexture(), x-(size/2), y-(size/2), size, size, null);
			}
		}
		
		public ItemStack getItemStack() {
			return item;
		}
		public void setItemStack(ItemStack item) {
			this.item = item;
		}
		public void resetItemStack() {
			
		}
	}
}
