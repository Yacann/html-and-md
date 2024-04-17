package html2md.markdown;

import java.util.List;

public class Header extends ComplexMarkdown {
    public Header(ComplexMarkdown content, Tags tags) {
        super(List.of(content), tags);
    }
}
