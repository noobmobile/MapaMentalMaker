package com.dont.mapamental.robots;

import com.dont.mapamental.models.Content;
import com.dont.mapamental.models.Robot;
import com.dont.mapamental.models.Sentence;
import com.dont.mapamental.utils.Utils;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.v1.Customsearch;
import com.google.api.services.customsearch.v1.model.Result;
import com.google.api.services.customsearch.v1.model.Search;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ImageRobot extends Robot {
    private String googleKey;
    private String googleCx;

    public ImageRobot(Content content, String googleKey, String googleCx) {
        super(content);
        this.googleKey = googleKey;
        this.googleCx = googleCx;
    }

    @Override
    public void start() throws Exception {
        Utils.log("iniciando robô de imagens");
        fetchImagesOfAllSentences();
        downloadAllImages();
    }

    private void downloadAllImages() {
        for (int i = 0; i < content.getSentences().size(); i++) {
            Sentence sentence = content.getSentences().get(i);
            for (int j = 0; j < sentence.getImages().size(); j++) {
                String imageLink = sentence.getImages().get(j);
                try {
                    if (content.getAlreayDownloadedLinks().contains(imageLink))
                        throw new Exception("imagem já baixada");
                    downloadImage(imageLink, i + "-" + j + " - original.png");
                    Utils.log("imagem " + i + "-" + j + " baixada com sucesso: " + imageLink + "(" + sentence.getGoogleQuery() + ")");
                    content.getAlreayDownloadedLinks().add(imageLink);
                } catch (Exception e) {
                    Utils.log("erro ao baixar imagem " + imageLink + ": " + e.getMessage());
                }

            }
        }
    }

    private void downloadImage(String imageLink, String path) throws Exception {
        URL url = new URL(imageLink);
        BufferedImage image = ImageIO.read(url);
        ImageIO.write(image, "png", new File(Utils.MAIN_DIRECTORY + "\\content\\" + path));
    }

    private void fetchImagesOfAllSentences() throws Exception {
        for (int i = 0; i < content.getSentences().size(); i++) {
            Sentence sentence = content.getSentences().get(i);
            String query = i == 0
                    ? content.getSearchTerm() : content.getSearchTerm() + " " + sentence.getKeywords().get(0);
            Utils.log("pesquisando imagens do google relacionadas a: " + query);
            sentence.setImages(fetchGoogleAndReturnImagesLinks(query));
            sentence.setGoogleQuery(query);
        }
    }

    private final List<String> blacklist = Arrays.asList("cloudfront", "amazonaws", "logo");

    private List<String> fetchGoogleAndReturnImagesLinks(String query) throws Exception {
        HttpRequestInitializer httpRequest = http -> {
        };
        Search search = new Customsearch.Builder(new NetHttpTransport(), new JacksonFactory(), httpRequest)
                .setApplicationName("video-maker").build()
                .cse()
                .siterestrict()
                .list()
                .setImgType("CLIPART")
                .setKey(googleKey)
                .setQ(query)
                .setNum(4)
                .setCx(googleCx)
                .setSearchType("image")
                .execute();
        return search.getItems().stream().map(Result::getLink)
                .filter(link -> !blacklist.contains(link))
                .collect(Collectors.toList());
    }
}
