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
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    private Document LoadRandomDocument() throws IOException {
        String source;
        try {
            source = LoadRandomSource();
        } catch (Exception e)
        {
            throw new IOException();
        }
        return Jsoup.parse(source);
    }

    private int ExtractInt( final String url, final String after, final String before ) {
        int index = url.indexOf(after);
        int end_index = url.indexOf(before, index);
        if (index == -1 || end_index == -1)
            return 0;
        String id = url.substring(index + after.length(), end_index);
        return Integer.parseInt(id);
    }

    private int ExtractInt( final String url, final String after ) {
        int index = url.indexOf(after);
        if (index == -1)
            return 0;
        String id = url.substring(index + after.length());
        return Integer.parseInt(id);
    }


    private int ExtractId( String url ) {
        return ExtractInt(url, "i=");
    }

    private int ExtractVote( String url ) {
        return ExtractInt(url, "current=");
    }

    private ArrayList<Element> LoadCountainers( Document a ) {
        Elements res = a.select(".sub_container");
        ArrayList<Element> ret= new ArrayList<Element>();
        Iterator<Element> i = res.iterator();
        while (i.hasNext())
           ret.add(i.next());
        return ret;
    }

    class CantParse extends Exception {

    }

    private int GetVote( Elements votes, int id ) {
        assert(id >= 0 && id < 3);
        String num = "one";
        if (id == 1)
            num = "two";
        else if (id == 2)
            num = "three";
        String select = "#vote" + num;
        Elements el = votes.select(select);
        if (el.size() == 0)
            return 0;
        return ExtractVote(el.first().attr("name"));
    }

    private ParsedObject ParseContainer( Element a ) throws CantParse {
        ParsedObject ret = new ParsedObject();
        Element title = a.select("a.sub_title").first();
        ret.title = title.html();
        ret.id = ExtractId(title.attr("href"));
        ret.img = a.select(".image img").attr("src");
        Elements votes = a.select(".vote_container .vote_button");
        for (int i = 0; i < 3; i++)
            ret.votes_count[i] = GetVote(votes, i);
        ret.comments_count = Integer.parseInt(a.select(".comments_button").html());

        if (ret.img.length() == 0)
            throw new CantParse();
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
            try {
                ret.add(ParseContainer(i.next()));
            } catch (CantParse e) {

            }
        return ret;
    }
}
