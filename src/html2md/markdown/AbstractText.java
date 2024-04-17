package html2md.markdown;

public abstract class AbstractText implements Node {
    protected Tags tags;

    public AbstractText(Tags tags) {
        this.tags = tags;
    }

    @Override
    public Tags getTags() {
        return tags;
    }
}
