package html2md.markdown;

import java.util.List;

public class ListItem extends ComplexMarkdown {
    public ListItem(ComplexMarkdown item, Tags tags) {
        super(List.of(item), tags);
    }
}
