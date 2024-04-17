package html2md.markdown;

import java.util.List;

abstract class ComplexText extends AbstractText {
    List<AbstractText> text;

    public ComplexText(List<AbstractText> text, Tags tags) {
        super(tags);
        this.text = text;
    }

    @Override
    public void toMarkdown(StringBuilder sb) {
        sb.append(tags.getOpen());
        for (AbstractText elem : text) {
            elem.toMarkdown(sb);
        }
        sb.append(tags.getClose());
    }
}
