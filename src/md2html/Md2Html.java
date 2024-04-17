package md2html;

import java.io.IOException;


public class Md2Html {
    public static void main(String[] args) {
        Md2HtmlParser parser = new Md2HtmlParser();
        try {
            parser.parseHtml(args[0], args[1]);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}