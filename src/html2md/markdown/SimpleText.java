package html2md.markdown;

public class SimpleText extends AbstractText {
    private String text;

    public SimpleText(String text) {
        super(new Tags("", ""));
        this.text = text;
    }

    @Override
    public void toMarkdown(StringBuilder sb) {
        sb.append(text);
    }
}
