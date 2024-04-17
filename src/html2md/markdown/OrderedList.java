package html2md.markdown;

import java.util.List;

public class OrderedList extends AbstractList {
    public OrderedList(List<ListItem> list) {
        super(list, new Tags("", ""));
    }

    @Override
    public void toMarkdown(StringBuilder sb) {
        sb.append('\n');
        for (int i = 1; i <= list.size(); i++) {
            sb.append(i + " ");
            list.get(i - 1).toMarkdown(sb);
        }
    }
}
