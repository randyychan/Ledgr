package com.example.ledgr.dataobjects;

import java.io.Serializable;

import org.json.JSONObject;


import org.json.JSONObject;

/**
 * @author ksutardji
 *
 */
public class User implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1805473932151576228L;
	private String uid;
    private String pwdhash;
    private String gcmId;
    private String facebookId;
    private String firstName;


    private String lastName;
    private String emailAddress;
    private String phoneNumber;
    private String city;
    private double longitude;
    private double latitude;

    /**
     * @param uid
     * @param pwdhash
     * @param facebookId
     * @param gcmId
     * @param firstName
     * @param lastName
     * @param emailAddress
     * @param phoneNumber
     * @param city
     * @param longitude
     * @param latitude
     */
    public User(String uid, String pwdhash, String facebookId, String gcmId, String firstName,
                String lastName, String emailAddress, String phoneNumber,
                String city, double longitude, double latitude) {
        this.uid = uid;
        this.pwdhash = pwdhash;
        this.facebookId = facebookId;
        this.gcmId = gcmId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    public User(String facebookId, String firstname) {
    	this.uid = "";
        this.pwdhash = "";
        this.facebookId = facebookId;
        this.gcmId = "";
        this.firstName = firstname;
        this.lastName = "";
        this.emailAddress = "";
        this.phoneNumber = "";
        this.city = "";
        this.latitude = 0;
        this.longitude = 0;
    }


    public JSONObject objectToJson() throws Exception {
        JSONObject userJson = new JSONObject();
        userJson.put("uid", this.uid);
        userJson.put("pwdhash", this.uid);
        userJson.put("facebook_id", this.facebookId);
        userJson.put("gcm_id", this.gcmId);
        userJson.put("first_name", this.firstName);
        userJson.put("last_name", this.lastName);
        userJson.put("email_address", this.emailAddress);
        userJson.put("phone_number", this.phoneNumber);
        userJson.put("city", this.city);
        userJson.put("latitude", this.latitude);
        userJson.put("longitude", this.longitude);

        return userJson;
    }


    /**
     * @return the uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * @param uid the uid to set
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPwdhash() {
        return pwdhash;
    }

    public void setPwdhash(String pwdhash) {
        this.pwdhash = pwdhash;
    }

    /**
     * @return the facebookId
     */
    public String getFacebookId() {
        return facebookId;
    }

    /**
     * @param facebookId the facebookId to set
     */
    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    /**
     * @return the gcmId
     */
    public String getGcmId() {
        return gcmId;
    }

    /**
     * @param gcmId the gcmId to set
     */
    public void setGcmId(String gcmId) {
        this.gcmId = gcmId;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the emailAddress
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * @param emailAddress the emailAddress to set
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * @return the phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @param phoneNumber the phoneNumber to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    /**
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

}

