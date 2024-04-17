package html2md.markdown;

import java.util.List;

public class Bold extends ComplexText {
    public Bold(List<AbstractText> text) {
        super(text, new Tags("**", "**"));
    }
}
