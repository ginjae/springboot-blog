package me.kimjaemin.springbootblog;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class CommonUtil {

    public String markdownToHtml(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

    public String markdownToText(String markdown) {
        String html = markdownToHtml(markdown);
        return Jsoup.parse(html).text();
    }

    public String previewImage(String markdown) {
        String html = markdownToHtml(markdown);
        Document document = Jsoup.parse(html);
        Elements elements = document.getElementsByTag("img");
        if (!elements.isEmpty()) {
            return elements.get(0).attr("src");
        }
        return "";
    }

}
