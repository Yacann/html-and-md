package html2md.markdown;

import java.util.List;

public class Italic extends ComplexText {
    public Italic(List<AbstractText> text) {
        super(text, new Tags("_", "_"));
    }
}
