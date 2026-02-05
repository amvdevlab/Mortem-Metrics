package com.na.postmortemproject.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TextReducerService {

    private static final double APPROX_CHARS_PER_TOKEN = 4.0;
    private static final Pattern MULTI_WHITESPACE = Pattern.compile("[ \\t]+");
    private static final Pattern MULTI_NEWLINES = Pattern.compile("\n{3,}");
    private static final Pattern PARAGRAPH_BREAK = Pattern.compile("\n\n");

    @Value("${postmortem.text-reducer.compression-level:light}")
    private String compressionLevel;

    @Value("${postmortem.text-reducer.max-tokens:900000}")
    private int maxTokens;

    // Compress text: remove excessive whitespace and deduplicate consecutive lines.
    // Behavior depends on postmortem.text-reducer.compression-level (none, light, aggressive).
    public String compress(String rawText) {
        if (rawText == null || rawText.isEmpty()) return "";
        String level = compressionLevel == null ? "light" : compressionLevel.toLowerCase();
        switch (level) {
            case "none":
                return rawText;
            case "aggressive":
                return deduplicateLines(collapseWhitespaceAggressive(rawText));
            case "light":
            default:
                return deduplicateLines(collapseWhitespaceLight(rawText));
        }
    }
    
    // Approximate token count for Gemini context limits (~4 chars per token).
    public int approximateTokenCount(String text) {
        if (text == null || text.isEmpty()) return 0;
        return (int) Math.ceil(text.length() / APPROX_CHARS_PER_TOKEN);
    }


    // If compressed text exceeds max tokens, split into chunks at line/paragraph boundaries.
    // Otherwise returns a single-element list.
    public List<String> chunkIfNeeded(String rawText) {
        String compressed = compress(rawText);
        int tokens = approximateTokenCount(compressed);
        if (tokens <= maxTokens) {
            return List.of(compressed);
        }
        List<String> chunks = new ArrayList<>();
        String[] paragraphs = PARAGRAPH_BREAK.split(compressed);
        StringBuilder current = new StringBuilder();
        int currentTokens = 0;
        for (String para : paragraphs) {
            int paraTokens = approximateTokenCount(para);
            if (paraTokens > maxTokens) {
                if (current.length() > 0) {
                    chunks.add(current.toString().trim());
                    current = new StringBuilder();
                    currentTokens = 0;
                }
                List<String> subChunks = chunkByLines(para, maxTokens);
                chunks.addAll(subChunks);
                continue;
            }
            if (currentTokens + paraTokens > maxTokens && current.length() > 0) {
                chunks.add(current.toString().trim());
                current = new StringBuilder();
                currentTokens = 0;
            }
            current.append(para).append("\n\n");
            currentTokens += paraTokens;
        }
        if (current.length() > 0) {
            chunks.add(current.toString().trim());
        }
        return chunks;
    }

    private List<String> chunkByLines(String text, int tokenLimit) {
        List<String> result = new ArrayList<>();
        String[] lines = text.split("\n");
        StringBuilder current = new StringBuilder();
        int currentTokens = 0;
        for (String line : lines) {
            int lineTokens = approximateTokenCount(line);
            if (currentTokens + lineTokens > tokenLimit && current.length() > 0) {
                result.add(current.toString().trim());
                current = new StringBuilder();
                currentTokens = 0;
            }
            current.append(line).append("\n");
            currentTokens += lineTokens;
        }
        if (current.length() > 0) {
            result.add(current.toString().trim());
        }
        return result;
    }

    private String collapseWhitespaceLight(String text) {
        return MULTI_WHITESPACE.matcher(text).replaceAll(" ")
                .lines()
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .collect(Collectors.joining("\n"));
    }

    private String collapseWhitespaceAggressive(String text) {
        return MULTI_NEWLINES.matcher(
                MULTI_WHITESPACE.matcher(text).replaceAll(" ")
        ).replaceAll("\n\n")
                .lines()
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .collect(Collectors.joining("\n"));
    }

    private String deduplicateLines(String text) {
        if (text == null || text.isEmpty()) return "";
        String prev = null;
        List<String> out = new ArrayList<>();
        for (String line : text.split("\n")) {
            if (!line.equals(prev)) {
                out.add(line);
                prev = line;
            }
        }
        return String.join("\n", out);
    }
}
