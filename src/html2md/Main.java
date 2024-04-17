package html2md;

import html2md.parser.Html2Md;

public class Main {
    public static void main(String[] args) {
        Html2Md parser = new Html2Md();
        MarkdownWriter writer = new MarkdownWriter();
        writer.writeMarkdown(parser.parse("input.txt"), "output.txt"); 
    }
}
