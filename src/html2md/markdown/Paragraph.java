package html2md.markdown;

import java.util.List;

public class Paragraph extends ComplexText {
    public Paragraph(List<AbstractText> text) {
        super(text, new Tags("", ""));
    }

    @Override
    public void toMarkdown(StringBuilder sb) {
        super.toMarkdown(sb);
        sb.append('\n');
    }
}
