package old.tweet.crawl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.lang.Object;
//import com.google.common.io.Files;

import twitter4j.Query;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

class TwitterUserTweetCollection {
  public static int api_call_count = 0;

  public void collect(String username, String outputFile) throws Exception {

    ConfigurationBuilder cb = new ConfigurationBuilder();
    cb.setOAuthConsumerKey("0ApP0Ag26EDQktamLPC5c7xMz");
    cb.setOAuthConsumerSecret("vUGFj0sJUSCUtjQkZuxG8ZE7hyRbFTAaAmw3kPHoILBJuQEVt5");
    cb.setOAuthAccessToken("2941079587-CPIhw45mjnOmbzrkgI5Tj9uAIzenWIG3OZju5hO");
    cb.setOAuthAccessTokenSecret("RL6cAHH5en4qUDC1MaSXpRzodhWh5VgTHAy6Hp5NLlq92");

    Twitter twitter = new TwitterFactory(cb.build()).getInstance();

    long maxId = 1;
    List<Status> statuses = new ArrayList<Status>();
    int max_status = 10000;

    while (statuses.size() < max_status) {
      try {
        int size = statuses.size();

        Query query = new Query(username);

        query.lang("en");

        //query.setSince("2006-01-01");

        //query.setUntil("2013-12-28");
        query.setMaxId(maxId);

        statuses.addAll(twitter.search(query).getTweets());
        maxId = writeToFile(outputFile, statuses);
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
  }

  public long writeToFile(String outputFile, List<Status> statuses) throws Exception {
    BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile, true));
    long maxId = 1;
    for (Status s : statuses) {
      if (s.getLang().equals("en")) {
        bw.write(s.getText().replace("\n", " ").replace(",", " "));
        bw.newLine();
      }
      maxId = s.getId();
    }
    bw.close();
    return maxId;
  }
}

public class OldTweetCrawl {
  private static String inputFile = "oldharvey.txt";
  private static String outputFile = "user_tweets_fpl_from_twitter.csv";

  public static void main(String[] args) throws Exception {
    HashSet<String> users = new HashSet<String>();
    BufferedReader reader = new BufferedReader(new FileReader(inputFile));
    List<String> userInfos = new ArrayList<String>();
    //Files.readLines(new File(inputFile), Charset.defaultCharset());
    String line = reader.readLine();
    while (line != null) {
        userInfos.add(line);
        line = reader.readLine();
    }
    for (String userInfo : userInfos) {
      users.add(userInfo.split(",")[0]);
    }
    TwitterUserTweetCollection tutc = new TwitterUserTweetCollection();
    for (String user : users) {
      System.out.println(user);
      tutc.collect(user, outputFile);
    }
  }
}
