package tech.codingclub;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Date;

public class WikipediaDownloader implements Runnable{

    public WikipediaDownloader() {
//        this.keyword = keyword;
    }
    public WikipediaDownloader(String keyword) {
        this.keyword = keyword;
    }
    private String keyword;
    private String result;

    public void run(){
        if(this.keyword==null || this.keyword.length()==0){
            return;
        }
        else{
            this.keyword = this.keyword.trim().replaceAll("[ ]+","_");
            String wikiUrl = getWikipediaUrlForQuery(this.keyword);
            String response = "";
            String imageUrl = null;
//            System.out.println("outside");
            try {
                String wikipediaResponse =  HttpURLConnectionExample.sendGet(wikiUrl);
//                System.out.println(wikipediaResponse);

                Document document = Jsoup.parse(wikipediaResponse,"https://en.wikipedia.org");
                Elements childElements = document.body().select(".mw-parser-output > *");
                int state = 0;
                for(Element childElement: childElements){

                    if(state == 0){
                        if(childElement.tagName().equals("table")){
                            state = 1;
//                            System.out.println(childElement.tagName());
                        }}else if(state==1){
                            if(childElement.tagName().equals("p")) {
                                state = 2;
                                response = childElement.text();
//                                System.out.println(response);
                                break;
                            }

                        }
                    }
                try{
                    imageUrl = document.body().select(".infobox img").get(0).attr("src");
                }catch(Exception ex){

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
//            this.result = response;
            WikiResult wikiResult = new WikiResult(this.keyword,response,imageUrl);
//            System.out.println(new Gson().toJson(wikiResult));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonResult = gson.toJson(wikiResult);
            System.out.println(jsonResult);
        }

    }

    private String getWikipediaUrlForQuery(String cleanKeyword) {
        return "https://en.wikipedia.org/wiki/"+cleanKeyword;
    }

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager(20);
        System.out.println("This is Tanay Srivastava");
        System.out.println("Running Wekipedia Downloader at "+new Date().toString());
        String arr[] = {"India","United States"};
        for(String keyword: arr){
            WikipediaDownloader wikipediaDownloader = (new WikipediaDownloader(keyword));
            taskManager.waitTillQueueIsFreeAndAddTask(wikipediaDownloader);
        }
    }
}
