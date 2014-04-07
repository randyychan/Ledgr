package com.example.ledgr.dataobjects;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.json.JSONObject;

import com.example.ledgr.alarmandnotification.Alarm;

public class Rental implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	LocalDate dueDate;
	User renter, owner;
	Item item;
	protected String title, description;
	protected ItemPriceUnit item_price_unit;
	protected double price;
	protected String item_id;
	protected String rental_id;
	protected int quality;
	protected boolean pending;
	protected int picture_count;
	
	public int getPicture_count() {
		return picture_count;
	}

	public void setPicture_count(int picture_count) {
		this.picture_count = picture_count;
	}

	public boolean isPending() {
		return pending;
	}

	public void setPending(boolean pending) {
		this.pending = pending;
	}

	public String getRental_id() {
		return rental_id;
	}

	public void setRental_id(String rental_id) {
		this.rental_id = rental_id;
	}

	public int getQuality() {
		return quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}

	protected int rental_alarm_id;

	public int getRental_alarm_id() {
		return rental_alarm_id;
	}

	public void setRental_alarm_id(int rental_alarm_id) {
		this.rental_alarm_id = rental_alarm_id;
	}

	public Rental(Item item, LocalDate dueDate, User renter, User owner) {
		this.dueDate = dueDate;
		this.renter = renter;
		this.item = item;
		this.owner = owner;
		this.title = item.getTitle();
		this.description = item.getDescription();
		this.item_price_unit = item.getItem_price_unit();
		this.price = item.getPrice();
		this.item_id = item.getItem_id();
		this.picture_count = item.getPicture_count();
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public User getRenter() {
		return renter;
	}

	public void setRenter(User renter) {
		this.renter = renter;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ItemPriceUnit getItem_price_unit() {
		return item_price_unit;
	}

	public void setItem_price_unit(ItemPriceUnit item_price_unit) {
		this.item_price_unit = item_price_unit;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getItem_id() {
		return item_id;
	}

	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}
	
    public JSONObject objectToJson(String uuid) throws Exception {
    	
        JSONObject itemJson = new JSONObject();
        itemJson.put("id", this.rental_id);
        itemJson.put("item_id", this.item_id);
        itemJson.put("owner_id", uuid);
        itemJson.put("renter_id", "1030980015");//this.renter.getFacebookId());
        itemJson.put("due_date_day", this.getDueDate().getDayOfMonth());
        itemJson.put("due_date_month", this.getDueDate().getMonthOfYear());
        itemJson.put("due_date_year", this.getDueDate().getYear());

        return itemJson;
    }

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String str = " owner_id " + owner.getUid() +
				     " owner_name " + owner.getFirstName() +
				     " renter_id " + renter.getUid() +
				     " renter_name " + renter.getFirstName() +
				     " title " + title +
				     " description " + description;
	
		
		return str;
	}
	
    

}
