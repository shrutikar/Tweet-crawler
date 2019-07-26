/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package harveycollect;


//import java.io.UnsupportedEncodingException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.FilterQuery;
import twitter4j.Query;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import twitter4j.conf.ConfigurationBuilder;

public class harveycollect {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws TwitterException {
        
    	int api_call_count=0;
    	
    	try
        {
            // twitter keys
          ConfigurationBuilder cb = new ConfigurationBuilder();
              cb.setDebugEnabled(true)
                 .setOAuthConsumerKey("0ApP0Ag26EDQktamLPC5c7xMz")
                 .setOAuthConsumerSecret("vUGFj0sJUSCUtjQkZuxG8ZE7hyRbFTAaAmw3kPHoILBJuQEVt5")
                 .setOAuthAccessToken("2941079587-CPIhw45mjnOmbzrkgI5Tj9uAIzenWIG3OZju5hO")
                 .setOAuthAccessTokenSecret("RL6cAHH5en4qUDC1MaSXpRzodhWh5VgTHAy6Hp5NLlq92");
           Twitter twitter = new TwitterFactory(cb.build()).getInstance();
           
             StringBuilder sb = new StringBuilder();
             /*sb.append("user Screen Name");
             sb.append(',');
             sb.append("Tweet");
             sb.append('\n');*/
           StatusListener listener = new StatusListener() {

            public void onStatus(Status status) {
                try{
                    FileWriter pw = new FileWriter("harvey_data.csv",true);                // output file

                    //String txt = status.getText();
                    String regexp =  "RT " + ".*";                      // Regular exp to check Retweets
                    //String regexp2 = ".*" + "@" + ".*";                 // Regular exp to check whether tweet has mentions or not
                    if(status.getLang().equalsIgnoreCase("en"))
                    {
                    //if(status.getText().matches(regexp2)){              // if tweet has mentions
                    if(!status.getText().matches(regexp)){              // if tweet is not a retweet
                        System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
                       
                        
                        
                        
                        
                        
                        
                        
                        
                        long maxId = 1;
                        List<Status> statuses = new ArrayList<Status>();
                        int max_status = 10000;

                        while (statuses.size() < max_status) {
                          try {
                            int size = statuses.size();

                            Query query = new Query("TRUMP");

                            query.lang("en");

                            query.setSince("2006-01-01");

                            query.setUntil("2013-12-28");
                            query.setMaxId(maxId);

                            statuses.addAll(twitter.search(query).getTweets());
                           // maxId = writeToFile(outputFile, statuses);
                            pw.append(status.getUser().getScreenName());           // getting username
                            pw.append(',');
                            pw.append("\"" + status.getText() +"\"");				// getting tweet
                            pw.append(',');
                            pw.append("\"" + status.getCreatedAt() +"\"");			// getting date and time
                            pw.append(',');
                            pw.append("\"" + status.getGeoLocation() +"\"");		// getting geolocation
                            pw.append('\n');
                            statuses = new ArrayList<Status>();
                            api_call_count++;
                            if (statuses.size() == size)
                              break;
                          } catch (TwitterException e) {
                            e.printStackTrace();
                            break;
                          }
                          if (api_call_count > 800) {
                            Thread.sleep(900000);
                            api_call_count = 0;
                          }
                        }
                        
                        
                        
                        
                        
                        
                        
                  
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
    BufferedReader br = new BufferedReader(new FileReader("HarveyOldKeywords.txt"));
    while((line=br.readLine())!= null) {
        keywords[i] = line;
        i++;
    }
    //String keywords[] = {"France", "Germany", "USA", "India"};
    
    


    
    
    
    
    
    
    
    
    
    
    
    FilterQuery fq = new FilterQuery();                                             // filtering tweets
    fq.track(keywords);

    twitter.addListener(listener);
    twitter.filter(fq);      
}
        catch(Exception e) {
            System.out.println(e);
        }
}}
    
    
