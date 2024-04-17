package html2md.markdown;

import java.util.List;

public abstract class AbstractList implements Node {
    protected List<ListItem> list;
    protected Tags tags;

    public AbstractList(List<ListItem> list, Tags tags) {
        this.list = list;
        this.tags = tags;
    }

    @Override
    public Tags getTags() {
        return tags;
    }
}
