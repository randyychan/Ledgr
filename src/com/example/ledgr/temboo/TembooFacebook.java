package com.example.ledgr.temboo;

import android.os.AsyncTask;

import com.example.ledgr.dataobjects.Item;
import com.temboo.Library.Facebook.Publishing.SetStatus;
import com.temboo.Library.Facebook.Publishing.SetStatus.SetStatusInputSet;
import com.temboo.Library.Facebook.Publishing.SetStatus.SetStatusResultSet;
import com.temboo.core.TembooException;
import com.temboo.core.TembooSession;

public class TembooFacebook {
	public static final String ACCESS_TOKEN = "CAAH851owX1wBAFNB88gDasofjxbnICLkwWLVZBdgKcuVsX34yqTZAifiU0CiI9cBAI8g3fIVSQU5ZBcySyJdT98ZBKFLumu7mzxZBUynGTl7ZAsqqG93ijSCKGlcM0hsWDW0f27v7irLuZAdk47z0mSCGVQ7ETlTZAtZAJLPRqpacf8PtdOdZBst2S";
	public static final String APP_NAME = "myFirstApp";
	public static final String APP_KEY = "21daa4189b4046a0bb32c6f0d21ce263";
	
	public class PostToFacebook extends AsyncTask<Item, Void, Void> {
	     protected Void doInBackground(Item... items) {
	    	 Item item = items[0];
	    	 TembooSession session;
	 		try {
	 			session = new TembooSession("randyychan", APP_NAME, APP_KEY);
	 			SetStatus setStatusChoreo = new SetStatus(session);

	 			// Get an InputSet object for the choreo
	 			SetStatusInputSet setStatusInputs = setStatusChoreo.newInputSet();

	 			String message = "Lending out my " + item.getTitle() + " to everyone!\n" +
	 							 "Check it out at my Ledgr profile!\n" +
	 							 "\n" +
	 							 item.getDescription() + "\n" +
	 							 "\n" +
	 							 "http://kevinsutardji.com/ledger/media/images/" + item.getItem_id() + "/" + item.getItem_id() + "_0.jpg";
	 			
	 			// Set inputs
	 			setStatusInputs.set_AccessToken(ACCESS_TOKEN);
	 			setStatusInputs.set_Message(message);
	 			SetStatusResultSet setStatusResults = setStatusChoreo.execute(setStatusInputs);

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
		new PostToFacebook().execute(new Item[] {item});
	}
	
	
}
