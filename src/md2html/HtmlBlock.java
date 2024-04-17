package md2html;

import java.util.List;


public class HtmlBlock implements Html {
    private final List<HtmlText> list;
    private final String openTag;
    private final String closeTag;

    public HtmlBlock(List<HtmlText> list, String openTag, String closeTag) {
        this.list = list;
        this.openTag = openTag;
        this.closeTag = closeTag;
    }

    public HtmlBlock(HtmlText item, String openTag, String closeTag) {
        this(List.of(item), openTag, closeTag);
    }
    
    public void toHtml(StringBuilder sb) {
        sb.append(openTag);
        for (HtmlText item : list) {
            item.toHtml(sb);
        }
        sb.append(closeTag);
    }
    
    public int getLength() {
        return 0;
    }
}
