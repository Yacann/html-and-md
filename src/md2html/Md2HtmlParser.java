package md2html;

import java.util.List;
import java.util.ArrayList;
import java.util.Deque;
import java.util.ArrayDeque;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;


public class Md2HtmlParser {
    private static final List<String> MARKS = List.of("*", "_", "`", "**", "__", "--", "```");
    private static final Map<Character, String> SPECIAL = Map.of(
        '<', "&lt;",
        '>', "&gt;",
        '&', "&amp;"
    );

    private boolean formattingMode = true;

    public Md2HtmlParser() {
    }

    public void parseHtml(final String inputFilename, final String outputFilename) throws IOException {
        try (BufferedReader reader = new BufferedReader(
            new FileReader(inputFilename, StandardCharsets.UTF_8));
            BufferedWriter writer = new BufferedWriter(
            new FileWriter(outputFilename, StandardCharsets.UTF_8))
        ) {
            StringBuilder sb = new StringBuilder();
            String line = "";
            while (line != null) {
                line =  reader.readLine();
                if (line != null && !line.isEmpty()) {
                    sb.append(line).append('\n');
                } else if (sb.length() > 0) {
                    sb.delete(sb.length() - 1, sb.length());
                    HtmlBlock block = parseHtmlBlock(sb);
                    sb.setLength(0);
                    block.toHtml(sb);
                    writer.write(sb + "\n");
                    sb.setLength(0);
                }
            }
        }
    }

    public HtmlBlock parseHtmlBlock(StringBuilder sb) {
        int h = 0;
        while (h < sb.length() && sb.charAt(h) == '#') {
            h++;
        }
        String tag;
        if (h == 0 || h == sb.length() || !Character.isWhitespace(sb.charAt(h))) {
            tag = "p";
            h = 0;
        } else {
            tag = "h";
            h++;
        }
        Deque<String> markStack = new ArrayDeque<String>();
        markStack.push("");
        return new HtmlBlock(parseHtmlText(sb, h, markStack), "<" + tag + ">", "</" + tag + ">");
    }

    private HtmlText parseHtmlText(StringBuilder sb, int start, Deque<String> markStack) {
        List<HtmlText> list = new ArrayList<HtmlText>();
        String mark;
        int i = start;
        int j = start;
        while (j < sb.length()) {
            if (MARKS.contains("" + sb.charAt(j))) {
                if (j + 2 < sb.length() && MARKS.contains(sb.substring(j, j + 3))) {
                    formattingMode = !formattingMode;
                    mark = sb.substring(j, j + 3);
                } else if (j + 1 < sb.length() && MARKS.contains(sb.substring(j, j + 2)) && formattingMode) {
                    mark = sb.substring(j, j + 2);
                } else if (formattingMode) {
                    mark = sb.substring(j, j + 1);
                } else {
                    j++;
                    continue;
                }
            } else {
                if (SPECIAL.containsKey(sb.charAt(j))) {
                    String special = SPECIAL.get(sb.charAt(j));
                    list.add(new HtmlText(sb.substring(i, j) + special, 1 - special.length()));
                    i = j + 1;
                } else if (sb.charAt(j) == '\\') {
                    if (j + 1 < sb.length() && MARKS.contains("" + sb.charAt(j + 1))) {
                        list.add(new HtmlText(sb.substring(i, j) + sb.charAt(j + 1), 1));
                        j++;
                        i = j + 1;
                    }
                }
                j++;
                continue;
            }

            list.add(new HtmlText(sb.substring(i, j)));
            if (!markStack.contains(mark)) {
                markStack.push(mark);
                HtmlText item = parseHtmlText(sb, j + mark.length(), markStack);
                j += item.getLength();
                i = j;
                list.add(item);
            } else {
                if (markStack.getLast().equals(mark)) {
                    List<String> tags = parseHtmlTag(markStack.pop());
                    return new HtmlText(list, tags.get(0), tags.get(1), 2 * mark.length());
                }
                mark = markStack.pop();
                return new HtmlText(list, mark, "", mark.length());
            }
        }

        list.add(new HtmlText(sb.substring(i)));
        mark = markStack.pop();
        return new HtmlText(list, mark, "", mark.length());
    }

    private List<String> parseHtmlTag(String mark) {
        return switch (mark) {
            case "*", "_" -> List.of("<em>", "</em>");
            case "`" -> List.of("<code>", "</code>");
            case "**", "__" -> List.of("<strong>", "</strong>");
            case "--" -> List.of("<s>", "</s>");
            case "```" -> List.of("<pre>", "</pre>");
            default -> List.of("", "");
        };
    }
}
