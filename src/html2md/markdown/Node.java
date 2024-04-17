package html2md.markdown;

public interface Node {
    void toMarkdown(StringBuilder sb);

    Tags getTags();
}
