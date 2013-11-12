package net.exsul.IWSMT;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class ContentLoader {
    private final String USER_AGENT = "EXSUL IWSMT.COM Android Client";
    private final String RANDOM_STR = "http://iwastesomuchtime.com/random.php";

    private String LoadRandomSource() throws Exception {
        // http://www.mkyong.com/java/how-to-send-http-request-getpost-in-java/
        URL url = new URL(RANDOM_STR);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();

        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        in.close();

        return response.toString();
    }

    private Document LoadRandomDocument() throws IOException {
        return Jsoup.connect(RANDOM_STR).get();
    }

    private int ExtractInt( final String url, final String after, final String before ) {
        int index = url.indexOf(after);
        int end_index = url.indexOf(before, index);
        if (index == -1 || end_index == -1)
            return 0;
        String id = url.substring(index, end_index);
        return Integer.parseInt(id);
    }

    private int ExtractId( String url ) {
        return ExtractInt(url, "i=", "\"");
    }

    private int ExtractVote( String url ) {
        return ExtractInt(url, "current=", "\"");
    }

    private ArrayList<Element> LoadCountainers( Document a ) {
        Elements res = a.select(".sub_container");
        ArrayList<Element> ret= new ArrayList<Element>();
        Iterator<Element> i = res.iterator();
        while (i.hasNext())
           ret.add(i.next());
        return ret;
    }

    private ParsedObject ParseContainer( Element a ) {
        ParsedObject ret = new ParsedObject();
        Element title = a.select("a.sub_title").first();
        ret.title = title.html();
        ret.id = ExtractId(title.attr("href"));
        ret.img = a.select(".image > img").attr("src");
        Elements votes = a.select(".vote_container > #vote_button");
        for (int i = 0; i < 3; i++)
            ret.votes_count[i] = ExtractVote(votes.get(i).attr("name"));
        ret.comments_count = Integer.parseInt(a.select(".comments_button").html());

        return ret;
    }

    public ArrayList<ParsedObject> LoadRandom() {
        Document a;
        try {
            a = LoadRandomDocument();
        } catch (IOException e)
        {
            return null;
        }
        ArrayList<Element> b = LoadCountainers(a);
        Iterator<Element> i = b.iterator();

        ArrayList<ParsedObject> ret = new ArrayList<ParsedObject>(b.size());
        while (i.hasNext())
            ret.add(ParseContainer(i.next()));
        return ret;
    }
}
