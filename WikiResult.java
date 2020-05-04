package tech.codingclub;

public class WikiResult {

    private String query;
    private String text_result;
    private String image_url;

    public WikiResult(String query, String text_result, String image_url) {
        this.query = query;
        this.text_result = text_result;
        this.image_url = image_url;
    }

    public static void main(String[] args) {

    }
}
