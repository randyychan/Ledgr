package com.example.ledgr.dataobjects;

import java.io.Serializable;
import java.text.DecimalFormat;

import org.joda.time.DateTime;
import org.json.JSONObject;

public class Item implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected String id;
	protected String title;
	protected String description;
	protected double price;
	protected boolean rented;
	protected String owner_id;
	protected int quality_rating;
	protected int picture_count;
	
	protected ItemState item_state;
	protected ItemPriceUnit item_price_unit;
	
	
	
    public int getPicture_count() {
		return picture_count;
	}

	public void setPicture_count(int picture_count) {
		this.picture_count = picture_count;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOwner_id() {
		return owner_id;
	}

	public void setOwner_id(String owner_id) {
		this.owner_id = owner_id;
	}

	public JSONObject objectToJson() throws Exception {
    	boolean is_online = item_state == ItemState.NOT_FOR_RENT? false : true;
    	
        JSONObject itemJson = new JSONObject();
        itemJson.put("id", this.id);
        itemJson.put("name", this.title);
        itemJson.put("description", this.description);
        itemJson.put("price", this.price);
        itemJson.put("owner_id", "");
        itemJson.put("is_rented", this.rented);
        itemJson.put("is_online", is_online);
        itemJson.put("category", "");

        return itemJson;
    }
	
	public int getQuality_rating() {
		return quality_rating;
	}

	public void setQuality_rating(int quality_rating) {
		this.quality_rating = quality_rating;
	}

	public boolean isRented() {
		return rented;
	}

	public void setRented(boolean rented) {
		this.rented = rented;
	}

	public Item(String title, String description) {
		this.title = title;
		this.description = description;
		item_state = ItemState.NOT_FOR_RENT;
		price = 0;
		this.rented = false;
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

	public ItemState getItem_state() {
		return item_state;
	}

	public void setItem_state(ItemState item_state) {
		this.item_state = item_state;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getPriceString() {
		if (price == 0) {
			return "FREE";
		} else {
			DecimalFormat df = new DecimalFormat("0.00");
			return "$" + df.format(price);
		}
	}

	public String getItem_id() {
		return id;
	}

	public void setItem_id(String item_id) {
		this.id = item_id;
	}

	public ItemPriceUnit getItem_price_unit() {
		return item_price_unit;
	}

	public void setItem_price_unit(ItemPriceUnit item_price_unit) {
		this.item_price_unit = item_price_unit;
	}

}
