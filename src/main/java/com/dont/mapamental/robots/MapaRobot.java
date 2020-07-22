package com.dont.mapamental.robots;

import com.dont.mapamental.models.Content;
import com.dont.mapamental.models.Robot;
import com.dont.mapamental.models.Sentence;
import com.dont.mapamental.utils.Utils;
import org.apache.batik.gvt.renderer.ImageRenderer;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.lang3.text.WordUtils;

import java.awt.*;
import java.io.*;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class MapaRobot extends Robot {
    public MapaRobot(Content content) {
        super(content);
    }

    /**
     * Todo: refazer usando html e css, usando svg os textos não ficam alinhados (ou mudar os templates?)
     *
     * @throws Exception
     */
    @Override
    public void start() throws Exception {
        saveSvg();
        convertToPng();
        new File(Utils.MAIN_DIRECTORY + "\\" + content.getSearchTerm() + ".svg").delete();
    }

    private void convertToPng() throws Exception {
        TranscoderInput transcoderInput = new TranscoderInput(Paths.get(Utils.MAIN_DIRECTORY + "\\" + content.getSearchTerm() + ".svg").toUri().toURL().toString());
        OutputStream outputStream = new FileOutputStream(Utils.MAIN_DIRECTORY + "\\" + content.getSearchTerm() + ".png");
        TranscoderOutput transcoderOutput = new TranscoderOutput(outputStream);
        PNGTranscoder pngTranscoder = prepareTranscoder();
        pngTranscoder.transcode(transcoderInput, transcoderOutput);
        outputStream.flush();
        outputStream.close();
    }

    private void saveSvg() throws IOException {
        String filePath = Utils.getRandomFile("templates");
        BufferedReader reader = new BufferedReader(new InputStreamReader(MapaRobot.class.getClassLoader().getResourceAsStream(filePath)));
        PrintWriter writer = new PrintWriter(new BufferedOutputStream(new FileOutputStream(Utils.MAIN_DIRECTORY + "\\" + content.getSearchTerm() + ".svg")));
        Map<String, String> replacer = prepareReplacer();
        String string;
        while ((string = reader.readLine()) != null) {
            for (Map.Entry<String, String> entry : replacer.entrySet()) {
                string = string.replace(entry.getKey(), entry.getValue());
            }
            writer.println(string);
        }
        writer.close();
        reader.close();
    }

    private PNGTranscoder prepareTranscoder() {
        PNGTranscoder my_converter = new PNGTranscoder() {
            @Override
            protected ImageRenderer createRenderer() {
                ImageRenderer r = super.createRenderer();

                RenderingHints rh = r.getRenderingHints();

                rh.add(new RenderingHints(RenderingHints.KEY_ALPHA_INTERPOLATION,
                        RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY));
                rh.add(new RenderingHints(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BICUBIC));

                rh.add(new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON));

                rh.add(new RenderingHints(RenderingHints.KEY_COLOR_RENDERING,
                        RenderingHints.VALUE_COLOR_RENDER_QUALITY));
                rh.add(new RenderingHints(RenderingHints.KEY_DITHERING,
                        RenderingHints.VALUE_DITHER_DISABLE));

                rh.add(new RenderingHints(RenderingHints.KEY_RENDERING,
                        RenderingHints.VALUE_RENDER_QUALITY));

                rh.add(new RenderingHints(RenderingHints.KEY_STROKE_CONTROL,
                        RenderingHints.VALUE_STROKE_PURE));

                rh.add(new RenderingHints(RenderingHints.KEY_FRACTIONALMETRICS,
                        RenderingHints.VALUE_FRACTIONALMETRICS_ON));
                rh.add(new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_OFF));

                r.setRenderingHints(rh);

                return r;
            }
        };
        my_converter.addTranscodingHint(PNGTranscoder.KEY_PIXEL_UNIT_TO_MILLIMETER, 0.084672F);
        my_converter.addTranscodingHint(PNGTranscoder.KEY_BACKGROUND_COLOR, Color.WHITE);
        my_converter.addTranscodingHint(PNGTranscoder.KEY_WIDTH, 1200f);
        my_converter.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, 1200f);
        return my_converter;
    }

    private Map<String, String> prepareReplacer() {
        Map<String, String> replacer = new HashMap<>();
        replacer.put("#TITLE", content.getSearchTerm().toUpperCase());
        for (int i = 0; i < content.getSentences().size(); i++) {
            Sentence sentence = content.getSentences().get(i);
            replacer.put("#TEXT" + i, substring(sentence.getText()));
            replacer.put("#TEXTO" + i, substring(sentence.getText()));
            replacer.put("#KEYWORD" + i, WordUtils.capitalizeFully(sentence.getKeywords().get(0)));
        }
        return replacer;
    }

    private String substring(String text) {
        List<String> words = Arrays.asList(text.split(" "));
        List<List<String>> wordsList = getPages(words, 4);
        return wordsList.stream().map(list -> "<tspan  x=\"-0.2em\" dy=\"0.8em\">" + String.join(" ", list) + "</tspan>").collect(Collectors.joining(" "));
    }

    private <T> List<List<T>> getPages(Collection<T> c, Integer pageSize) { // créditos a https://stackoverflow.com/users/2813377/pscuderi
        List<T> list = new ArrayList<T>(c);
        if (pageSize == null || pageSize <= 0 || pageSize > list.size()) pageSize = list.size();
        int numPages = (int) Math.ceil((double) list.size() / (double) pageSize);
        List<List<T>> pages = new ArrayList<List<T>>(numPages);
        for (int pageNum = 0; pageNum < numPages; )
            pages.add(list.subList(pageNum * pageSize, Math.min(++pageNum * pageSize, list.size())));
        return pages;
    }

}
