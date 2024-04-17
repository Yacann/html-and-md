package md2html;

import java.util.List;


public class HtmlText implements Html {
    private List<HtmlText> list;
    private String openTag;
    private String closeTag;
    private int length;

    private static class Text extends HtmlText {
        private final String text;
        private int length = 0;

        public Text(String str) {
            text = str;
            length = str.length();
        }

        @Override
        public void toHtml(StringBuilder sb) {
            sb.append(text);
        }

        @Override
        public int getLength() {
            return length;
        }
    }

    public HtmlText() {
    }

    public HtmlText(List<HtmlText> list, String openTag, String closeTag) {
        this.list = list;
        this.openTag = openTag;
        this.closeTag = closeTag;
        length = list.stream().mapToInt(HtmlText::getLength).sum();
    }
    
    public HtmlText(List<HtmlText> list, String openTag, String closeTag, int extraLen) {
        this(list, openTag, closeTag);
        length += extraLen;
    }

    public HtmlText(HtmlText item, String openTag, String closeTag) {
        this(List.of(item), openTag, closeTag);
    }

    public HtmlText(String str, String openTag, String closeTag) {
        this(new Text(str), openTag, closeTag);
    }

    public HtmlText(String str, String openTag, String closeTag, int extraLen) {
        this(new Text(str), openTag, closeTag);
        length += extraLen;
    }

    public HtmlText(String str) {
        this(str, "", "");
    }

    public HtmlText(String str, int extraLen) {
        this(str);
        length += extraLen;
    }

    public String getOpenTag() {
        return openTag;
    }

    public String getCloseTag() {
        return closeTag;
    }

    public int getLength() {
        return length;
    }

    public void toHtml(StringBuilder sb) {
        sb.append(openTag);
        for (Html item : list) {
            item.toHtml(sb);
        }
        sb.append(closeTag);
    }
}
