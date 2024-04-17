package html2md.markdown;

public class Tags {
    private final String open;
    private final String close;

    public Tags(String open, String close) {
        this.open = open;
        this.close = close;
    }

    public String getOpen() {
        return open;
    }

    public String getClose() {
        return close;
    }
}