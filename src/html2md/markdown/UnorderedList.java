package html2md.markdown;

import java.util.List;

public class UnorderedList extends AbstractList {
    public UnorderedList(List<ListItem> list) {
        super(list, new Tags("* ", ""));
    }
    
    @Override
    public void toMarkdown(StringBuilder sb) {
        sb.append('\n');
        for (ListItem elem : list) {
            sb.append(tags.getOpen());
            elem.toMarkdown(sb);
        }
    }
}
