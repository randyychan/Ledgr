package com.example.ledgr.temboo;

import android.os.AsyncTask;

import com.example.ledgr.dataobjects.Item;
import com.example.ledgr.temboo.TembooFacebook.PostToFacebook;
import com.temboo.Library.Facebook.Publishing.CreateEvent;
import com.temboo.Library.Facebook.Publishing.CreateEvent.CreateEventInputSet;
import com.temboo.Library.Facebook.Publishing.CreateEvent.CreateEventResultSet;
import com.temboo.core.TembooException;
import com.temboo.core.TembooSession;

public class TembooCalendar {
	public static final String CLIENT_ID = "223450131228-4jik7ikv7n3ahomr995aj5ut8rrv1kkq.apps.googleusercontent.com";
	public static final String CLIENT_SECRET = "6ufHgtwAiX-O4TiPH1VlpsOF";
	public static final String REFRESH_TOKEN = "1/hYJuMvMc6KzRKohqWBh9xLx4TwsCTpGXGFQrHxcZceg";
	public static final String APP_NAME = "myFirstApp";
	public static final String APP_KEY = "21daa4189b4046a0bb32c6f0d21ce263";
	
	public class PostToCalendar extends AsyncTask<Item, Void, Void> {
	     protected Void doInBackground(Item... items) {
	    	 Item item = items[0];
	    	 TembooSession session;
	 		try {
	 		// Instantiate the Choreo, using a previously instantiated TembooSession object, eg:
	 			session = new TembooSession("randyychan", "APP_NAME", "APP_KEY");
	 			CreateEvent createEventChoreo = new CreateEvent(session);

	 			// Get an InputSet object for the choreo
	 			CreateEventInputSet createEventInputs = createEventChoreo.newInputSet();

//	 			// Set inputs
//	 			createEventInputs.set
//	 			createEventInputs.set_EndDate("");
//	 			createEventInputs.set_StartDate("");
//	 			createEventInputs.set_CalendarID("");
//	 			createEventInputs.set_RefreshToken("1/hYJuMvMc6KzRKohqWBh9xLx4TwsCTpGXGFQrHxcZceg");
//	 			createEventInputs.set_EndTime("");
//	 			createEventInputs.set_EventTitle("");
//	 			createEventInputs.set_StartTime("");
//	 			createEventInputs.set_ClientID("223450131228-4jik7ikv7n3ahomr995aj5ut8rrv1kkq.apps.googleusercontent.com");

	 		// Execute Choreo
	 		CreateEventResultSet createEventResults = createEventChoreo.execute(createEventInputs);

	 		} catch (TembooException e) {
	 			// TODO Auto-generated catch block
	 			e.printStackTrace();
	 		}
	         return null;
	     }



	     protected void onPostExecute(Void result) {
	     }
	 }
	
	public void postItem(Item item) {
		// Instantiate the Choreo, using a previously instantiated TembooSession object, eg:
		//new PostToFacebook().execute(new Item[] {item});
	}
	
	
}
