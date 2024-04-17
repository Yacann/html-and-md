package html2md.markdown;

import java.util.List;

public class Underline extends ComplexText {
    public Underline(List<AbstractText> text) {
        super(text, new Tags("<u>", "</u>"));
    }
}