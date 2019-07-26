/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package javaapplication1;


//import java.io.UnsupportedEncodingException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import twitter4j.conf.ConfigurationBuilder;

public class irma_stream {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws TwitterException {
        try
        {
            // twitter keys
        	ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
            .setOAuthConsumerKey("dWSlNEN2BqwzrEv33yAhT2BQ5")
            .setOAuthConsumerSecret("CkKrhjHNETlw3lTm8kvsH0XmuBF9bQroRwALPx0Y4Ab2M3eYEJ")
            .setOAuthAccessToken("705918314955018240-5Izemd3NOI1guDpu21R9R3FWft9RSLB")
            .setOAuthAccessTokenSecret("3qvhFPv6YFx2KXoFvNPyMzrDcHoUkrjIW6vobYJkuIYV8");
         TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
              
             StringBuilder sb = new StringBuilder();
             /*sb.append("user Screen Name");
             sb.append(',');
             sb.append("Tweet");
             sb.append('\n');*/
           StatusListener listener = new StatusListener() {

            public void onStatus(Status status) {
                try{
                    FileWriter pw = new FileWriter("irma_data345.csv",true);                // output file

                    //String txt = status.getText();
                    String regexp =  "RT " + ".*";                      // Regular exp to check Retweets
                    //String regexp2 = ".*" + "@" + ".*";                 // Regular exp to check whether tweet has mentions or not
                    if(status.getLang().equalsIgnoreCase("en"))
                    {
                    //if(status.getText().matches(regexp2)){              // if tweet has mentions
                    if(!status.getText().matches(regexp)){              // if tweet is not a retweet
                        System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());  
                         pw.append(status.getUser().getScreenName());           // getting username
                         pw.append(',');
                         pw.append("\"" + status.getText() +"\"");				// getting tweet
                         pw.append(',');
                         //pw.append("\"" + status.getId() +"\"");				// getting tweetID
                         //pw.append(',');
                         pw.append("\"" + status.getCreatedAt() +"\"");			// getting date and time
                         pw.append(',');
                         pw.append("\"" + status.getGeoLocation() +"\"");		// getting geolocation
                         pw.append('\n');
                    //}
            //System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
                }
                }
                pw.flush();
                pw.close();
            }catch (IOException ex) {    
                    ex.printStackTrace();
                }
            }     

            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
            System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
           }

        public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
            System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
        }

        public void onScrubGeo(long userId, long upToStatusId) {
            System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
        }

        public void onException(Exception ex) {
            ex.printStackTrace();
        }

              @Override
              public void onStallWarning(StallWarning sw) {
                  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
              }

    };

    
    String keywords[] = new String[400];                                            // saving keywords in a keywords arrays ---- limit 400 keywords
    String line="";
    int i= 0;
    BufferedReader br = new BufferedReader(new FileReader("irmakeywords.txt"));
    while((line=br.readLine())!= null) {
        keywords[i] = line;
        i++;
    }
    //String keywords[] = {"France", "Germany", "USA", "India"};
    FilterQuery fq = new FilterQuery();                                             // filtering tweets
    fq.track(keywords);

    twitterStream.addListener(listener);
    twitterStream.filter(fq);      
}
        catch(Exception e) {
            System.out.println(e);
        }
}}
    
    
