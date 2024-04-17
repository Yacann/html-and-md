package html2md.markdown;

import java.util.List;

public class Strike extends ComplexText {
    public Strike(List<AbstractText> text) {
        super(text, new Tags("**", "**"));
    }
}