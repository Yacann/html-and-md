package html2md;

import html2md.markdown.ComplexMarkdown;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
 
public class MarkdownWriter {
    public void writeMarkdown(ComplexMarkdown markdown, String outputFilename) {
        try (
            BufferedWriter writer = new BufferedWriter(
            new FileWriter(outputFilename, StandardCharsets.UTF_8))
        ) {
            StringBuilder sb = new StringBuilder();
            markdown.toMarkdown(sb);
            writer.write(sb.toString());
        } catch (IOException e) {
            System.err.println("Invalid I/O");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}