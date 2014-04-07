package com.example.ledgr.server;

import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.example.ledgr.Utils;
import com.example.ledgr.contentprovider.RentalContentProvider;
import com.example.ledgr.dataobjects.Item;
import com.example.ledgr.dataobjects.ItemPriceUnit;
import com.example.ledgr.dataobjects.Rental;
import com.example.ledgr.dataobjects.User;

/**
 * Created by ksutardji on 3/1/14.
 */
public class ServerComm {
	
    private static final String BASE_URL = "http://kevinsutardji.com:5000/v1";
    
    public static String createUser(User user) throws Exception {
        //instantiates httpclient to make request
        DefaultHttpClient httpclient = new DefaultHttpClient();

        //url with the post data
        HttpPost httpost = new HttpPost(BASE_URL+"/users");

        //convert parameters into JSON object
        JSONObject holder = user.objectToJson();

        //passes the results to a string builder/entity
        StringEntity se = new StringEntity(holder.toString());

        //sets the post request as the resulting string
        httpost.setEntity(se);
        //sets a request header so the page receving the request
        //will know what to do with it
        httpost.setHeader("Accept", "application/json");
        httpost.setHeader("Content-type", "application/json");

        HttpResponse response = httpclient.execute(httpost);
        
        HttpEntity entity = response.getEntity();
        String entityString = EntityUtils.toString(entity);
        JSONObject userJson  = new JSONObject(entityString);
        System.out.println("HTTP Response:" + response.toString());

        return userJson.getString("uid");
        
    }
    
    public static void createItem(Item item, String userid, Context context) throws Exception {
        //instantiates httpclient to make request
        DefaultHttpClient httpclient = new DefaultHttpClient();

        //url with the post data
        HttpPost httpost = new HttpPost(BASE_URL+"/users/" + userid + "/items");

        //convert parameters into JSON object
        JSONObject holder = item.objectToJson();
        //JSONArray imageArray = new JSONArray();
        ArrayList<String> imageStrings = Utils.getImageStringsForId(item.getItem_id(), context);
//        for (int i = 0; i < imageStrings.size(); i++) {
//        	imageArray.put(i, imageStrings.get(i));
//        }
        JSONArray array = new JSONArray(imageStrings);
        holder.put("pictures", array);
        
        //holder.put("pictures", imageStrings);
        System.out.println("TEST TEST TEST" + holder.toString());
        
        //passes the results to a string builder/entity
        StringEntity se = new StringEntity(holder.toString());

        //sets the post request as the resulting string
        httpost.setEntity(se);
        //sets a request header so the page receving the request
        //will know what to do with it
        httpost.setHeader("Accept", "application/json");
        httpost.setHeader("Content-type", "application/json");

        //Handles what is returned from the page
        ResponseHandler responseHandler = new BasicResponseHandler();
        HttpResponse response = httpclient.execute(httpost);
    }
    
    public static void updateItem(Item item, String userid) throws Exception {
        //instantiates httpclient to make request
        DefaultHttpClient httpclient = new DefaultHttpClient();

        //url with the put data
        HttpPut httpost = new HttpPut(BASE_URL+"/users/" + userid + "/items/" + item.getItem_id());

        //convert parameters into JSON object
        JSONObject holder = item.objectToJson();

        //passes the results to a string builder/entity
        StringEntity se = new StringEntity(holder.toString());

        //sets the post request as the resulting string
        httpost.setEntity(se);
        //sets a request header so the page receving the request
        //will know what to do with it
        httpost.setHeader("Accept", "application/json");
        httpost.setHeader("Content-type", "application/json");

        //Handles what is returned from the page
        ResponseHandler responseHandler = new BasicResponseHandler();
        HttpResponse response = httpclient.execute(httpost);
    }    
    
    public static void deleteItem(Item item, String userid) throws Exception {
        //instantiates httpclient to make request
        DefaultHttpClient httpclient = new DefaultHttpClient();

        //url with the put data
        HttpDelete httpDelete = new HttpDelete(BASE_URL+"/users/" + userid + "/items/" + item.getItem_id());

        //sets the post request as the resulting string
        //sets a request header so the page receving the request
        //will know what to do with it
        httpDelete.setHeader("Accept", "application/json");
        httpDelete.setHeader("Content-type", "application/json");

        //Handles what is returned from the page
        ResponseHandler responseHandler = new BasicResponseHandler();
        HttpResponse response = httpclient.execute(httpDelete);
    }
    
    //
    //RENTAL STUFF
    //
    public static void createRental(Rental rental, String userid) throws Exception {
        //instantiates httpclient to make request
        DefaultHttpClient httpclient = new DefaultHttpClient();

        //url with the post data
        HttpPost httpost = new HttpPost(BASE_URL+"/users/" + userid + "/rentals");

        //convert parameters into JSON object
        JSONObject holder = rental.objectToJson(userid);

        //passes the results to a string builder/entity
        StringEntity se = new StringEntity(holder.toString());

        //sets the post request as the resulting string
        httpost.setEntity(se);
        //sets a request header so the page receving the request
        //will know what to do with it
        httpost.setHeader("Accept", "application/json");
        httpost.setHeader("Content-type", "application/json");

        //Handles what is returned from the page
        ResponseHandler responseHandler = new BasicResponseHandler();
        HttpResponse response = httpclient.execute(httpost);
    }
    
    public static void updateRental(Rental rental, String userid) throws Exception {
        //instantiates httpclient to make request
        DefaultHttpClient httpclient = new DefaultHttpClient();

        //url with the put data
        HttpPut httpost = new HttpPut(BASE_URL+"/users/" + userid + "/rentals/" + rental.getRental_id());

        //convert parameters into JSON object
        JSONObject holder = rental.objectToJson(userid);

        //passes the results to a string builder/entity
        StringEntity se = new StringEntity(holder.toString());
        //sets the post request as the resulting string
        httpost.setEntity(se);
        //sets a request header so the page receving the request
        //will know what to do with it
        httpost.setHeader("Accept", "application/json");
        httpost.setHeader("Content-type", "application/json");

        //Handles what is returned from the page
        ResponseHandler responseHandler = new BasicResponseHandler();
        HttpResponse response = httpclient.execute(httpost);
    }    
    
    public static void deleteRental(Rental rental, String userid) throws Exception {
        //instantiates httpclient to make request
        DefaultHttpClient httpclient = new DefaultHttpClient();

        //url with the put data
        HttpDelete httpDelete = new HttpDelete(BASE_URL+"/users/" + userid + "/rentals/" + rental.getRental_id());

        //sets the post request as the resulting string
        //sets a request header so the page receving the request
        //will know what to do with it
        httpDelete.setHeader("Accept", "application/json");
        httpDelete.setHeader("Content-type", "application/json");

        //Handles what is returned from the page
        ResponseHandler responseHandler = new BasicResponseHandler();
        HttpResponse response = httpclient.execute(httpDelete);
    }
    
    public static Rental getRental(Rental rental, String userid) throws Exception {
        //instantiates httpclient to make request
        DefaultHttpClient httpclient = new DefaultHttpClient();
        System.out.println("RANDY RENTAL USERS" + userid);
        //url with the put data
        HttpGet httpGet = new HttpGet(BASE_URL+"/users/" + userid + "/rentals/" + rental.getRental_id());
        
        //Handles what is returned from the page
        HttpResponse response = httpclient.execute(httpGet);
        
        //response.getstatuscode check for 200.
        
        HttpEntity entity = response.getEntity();
        String entityString = EntityUtils.toString(entity);
        JSONObject rentalJson  = new JSONObject(entityString);

        
        System.out.println("RANDY KEVIN " + entityString);
        System.out.println("RANDY KEVIN " + rental.getRental_id());
        
        //process response here! probably needs to return a Rental
        return processJSONtoRental(rentalJson);
    }
    
    private static Rental processJSONtoRental(JSONObject json) {
    	String item_id=null, title=null, description=null, renter_id=null, renter_name=null, owner_id=null, owner_name=null;
    	String owner_last_name=null, owner_uid = null, renter_uid= null, renter_last_name = null;
    	int due_date_day=15, due_date_month=6, due_date_year=2000, quality=3, price_unit=1;
    	double price =0;
    	int picture_count=0;
    	try {
			JSONObject itemJson = json.getJSONObject("item");
			item_id = itemJson.getString("id");
	        title = itemJson.getString(RentalContentProvider.C_NAME);
	        description = itemJson.getString(RentalContentProvider.C_DESCRIPTION);
	        price = itemJson.getDouble("price");
	        System.out.println("TEST TEST TEST " + price);
	        quality = 3;//json.getInt(RentalContentProvider.C_QUALITY);
	        price_unit = 2;//json.getInt(RentalContentProvider.C_PRICE_UNIT);
	        picture_count = json.getInt("photo_count");
	        System.out.println("pcount sever comm 1" + picture_count);
	        JSONObject userJson = json.getJSONObject("owner");
	        owner_id = userJson.getString("facebook_id");
	        owner_name = userJson.getString("first_name");
	        
	        //TODO: add places for last name and uid in the sql table
	        owner_last_name = userJson.getString("last_name");
	        owner_uid = userJson.getString("uid");
	        
	        JSONObject renterJson = json.getJSONObject("renter");
	        renter_id = renterJson.getString("facebook_id");
	        renter_name = renterJson.getString("first_name");
	        
	        //TODO: add places for last name and uid in the sql table
	        renter_last_name = renterJson.getString("last_name");
	        renter_uid = renterJson.getString("uid");
	        // this will be null from the get 
	        
	        due_date_day = json.getInt(RentalContentProvider.C_DUEDAY);
	        due_date_month = json.getInt(RentalContentProvider.C_DUEMONTH);
	        due_date_year = json.getInt(RentalContentProvider.C_DUEYEAR);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
        Item rentalItem = new Item(title, description);
        rentalItem.setPrice(price);
        rentalItem.setQuality_rating(quality);
        rentalItem.setItem_price_unit(ItemPriceUnit.MONTH);
        rentalItem.setItem_id(item_id);
        rentalItem.setPicture_count(picture_count);
        User owner = new User(owner_id, owner_name);
        System.out.println("Get Rental owner is = " + owner_id + owner_name);
        owner.setUid(owner_uid);
        User renter = new User(renter_id, renter_name);
        renter.setUid(renter_uid);
		LocalDate dueDate = new LocalDate(due_date_year, due_date_month, due_date_day);
		
    	return new Rental(rentalItem, dueDate , renter, owner);
    }
    
}
