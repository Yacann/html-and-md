package html2md.parser;

import html2md.markdown.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Html2Md {
    public ComplexMarkdown parse(String inputFilename) {
        try (
            BufferedReader reader = new BufferedReader(
            new FileReader(inputFilename, StandardCharsets.UTF_8))
        ) {
            StringBuilder sb = new StringBuilder();
            String line;
            do {
                line = reader.readLine();
                if (line != null && !line.isEmpty()) {
                    sb.append(line);
                }
            } while (line != null);
            return new Parser((CharSource) new StringSource(sb.toString())).parse();
        } catch (IOException e) {
            System.err.println("Invalid I/O");
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

    private static class Parser extends BaseParser {
        private enum TAG {
            PARAGRAPH,
            BOLD,
            ITALIC,
            UNDERLINE,
            STRIKE,
            ORDLIST,
            UNLIST,
            LITEM
        }

        public Parser(final CharSource source) {
            super(source);
        }

        public ComplexMarkdown parse() {
            List<Node> elements = new ArrayList<>();
            skipWhitespace();
            if (eof()) {
                throw error("html expected");
            }
            while(!eof()) {
                Deque<TAG> tagStack = new ArrayDeque<>();
                tagStack.addLast(getTag());
                elements.add(parseNode(tagStack));
                skipWhitespace();
            }
            return new ComplexMarkdown(elements, new Tags("", ""));
        }

        private ComplexMarkdown parseComplexMarkdown(Deque<TAG> tagStack) {
            List<Node> elements = new ArrayList<>();
            SimpleText str = parseSimpleText();
            if (str != null) {
                elements.add(str);
            }
            TAG tag = getTag();
            while (tag != tagStack.getLast()) {
                tagStack.addLast(tag);
                elements.add(parseNode(tagStack));
                str = parseSimpleText();
                if (str != null) {
                    elements.add(str);
                }
                tag = getTag();
            }
            tagStack.removeLast();
            return new ComplexMarkdown(elements, new Tags("", ""));
        }

        private Node parseNode(Deque<TAG> tagStack) {
            return switch(tagStack.getLast()) {
                case PARAGRAPH, BOLD, ITALIC, UNDERLINE, STRIKE -> parseAbstractText(tagStack);
                case ORDLIST, UNLIST -> parseAbstractList(tagStack);
                default -> throw error("unexpected tag: " + tagStack.getLast());
            };
        }
        
        private AbstractText parseAbstractText(Deque<TAG> tagStack) {
            List<AbstractText> text = new ArrayList<>();
            SimpleText str = parseSimpleText();
            if (str != null) {
                text.add(str);
            }
            TAG tag = getTag();
            while (tag != tagStack.getLast()) {
                tagStack.addLast(tag);
                text.add(parseAbstractText(tagStack));
                str = parseSimpleText();
                if (str != null) {
                    text.add(str);
                }
                tag = getTag();
            }
            tagStack.removeLast();
            return switch(tag) {
                case PARAGRAPH -> new Paragraph(text);
                case BOLD -> new Bold(text);
                case ITALIC -> new Italic(text);
                case UNDERLINE -> new Underline(text);
                case STRIKE -> new Strike(text);
                default -> throw error("unexpected type of text " + tag);
            };
        }

        private SimpleText parseSimpleText() {
            StringBuilder sb = new StringBuilder();
            while (getChar() != '<') {
                sb.append(take());
            }
            if (!sb.isEmpty()) {
                return new SimpleText(sb.toString());
            }
            return null;
        }

        private AbstractList parseAbstractList(Deque<TAG> tagStack) {
            List<ListItem> list = new ArrayList<>();
            skipWhitespace();
            TAG tag = getTag();
            while (tag != tagStack.getLast()) {
                tagStack.addLast(tag);
                list.add(parseListItem(tagStack));
                tag = getTag();
            }
            tagStack.removeLast();
            return switch(tag) {
                case ORDLIST -> new OrderedList(list);
                case UNLIST -> new UnorderedList(list);
                default -> throw error("unexpected type of list " + tag);
            };
        }

        private ListItem parseListItem(Deque<TAG> tagStack) {
            Deque<TAG> innerTagStack = new ArrayDeque<>();
            innerTagStack.addLast(tagStack.removeLast());
            return new ListItem(parseComplexMarkdown(innerTagStack), new Tags("", ""));
        }

        private void skipWhitespace() {
            while (Character.isWhitespace(getChar())) {
                take();
            }
        }

        private TAG getTag() {
            StringBuilder sb = new StringBuilder();
            expect('<');
            while (getChar() != '>') {
                sb.append(take());
            }
            expect('>');
            return switch(sb.toString()) {
                case "p", "/p" -> TAG.PARAGRAPH;
                case "b", "/b" -> TAG.BOLD;
                case "i", "/i" -> TAG.ITALIC;
                case "u", "/u" -> TAG.UNDERLINE;
                case "s", "/s" -> TAG.STRIKE;
                case "ol", "/ol" -> TAG.ORDLIST;
                case "ul", "/ul" -> TAG.UNLIST;
                case "li", "/li" -> TAG.LITEM;
                default -> throw error("unexpected html tag");
            };
        }
    }
}
