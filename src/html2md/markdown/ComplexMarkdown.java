package html2md.markdown;

import java.util.List;

public class ComplexMarkdown implements Node {
    protected List<Node> elements;
    protected Tags tags;

    public ComplexMarkdown(List<Node> elements, Tags tags) {
        this.elements = elements;
        this.tags = tags;
    }

    @Override
    public void toMarkdown(StringBuilder sb) {
        sb.append(tags.getOpen());
        for (Node elem : elements) {
            elem.toMarkdown(sb);;
        }
        sb.append(tags.getClose() + '\n');
    }

    @Override
    public Tags getTags() {
        return tags;
    }
}
