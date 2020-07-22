package com.dont.mapamental.robots;

import com.algorithmia.Algorithmia;
import com.algorithmia.AlgorithmiaClient;
import com.algorithmia.algo.Algorithm;
import com.dont.mapamental.models.Content;
import com.dont.mapamental.models.Robot;
import com.dont.mapamental.models.Sentence;
import com.dont.mapamental.utils.Utils;
import com.google.gson.JsonObject;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.natural_language_understanding.v1.model.Features;
import com.ibm.watson.natural_language_understanding.v1.model.KeywordsOptions;
import com.ibm.watson.natural_language_understanding.v1.model.KeywordsResult;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class TextRobot extends Robot {

    private final NaturalLanguageUnderstanding naturalLanguageUnderstanding;
    private final AlgorithmiaClient algorithmiaClient;

    public TextRobot(Content content, String algorithmiaKey, String watsonKey) {
        super(content);
        this.naturalLanguageUnderstanding = new NaturalLanguageUnderstanding("2018-04-05", "https://gateway.watsonplatform.net/natural-language-understanding/api/", new IamAuthenticator(watsonKey));
        this.algorithmiaClient = Algorithmia.client(algorithmiaKey);
    }

    @Override
    public void start() throws Exception {
        Utils.log("iniciando robô de texto");
        fetchFromWikipedia();
        sanitizeContent();
        breakContentIntoSentences();
        limitMaximumSentences();
        fetchKeywordsFromSentences();
        Utils.log("robô de texto finalizado");
    }

    private void fetchKeywordsFromSentences() {
        for (Sentence sentence : content.getSentences()) {
            Utils.log("sentença: " + sentence.getText());
            fetchWatsonKeywords(sentence);
            Utils.log("keywords: " + String.join(", ", sentence.getKeywords()));
        }
    }

    private void fetchWatsonKeywords(Sentence sentence) {
        KeywordsOptions keywordsOptions = new KeywordsOptions.Builder().build();
        Features features = new Features.Builder().keywords(keywordsOptions).build();
        AnalyzeOptions builder = new AnalyzeOptions.Builder().features(features).language(Utils.LANGUAGE).text(sentence.getText()).build();
        List<String> keywords = naturalLanguageUnderstanding.analyze(builder).execute().getResult().getKeywords().stream().map(KeywordsResult::getText).collect(Collectors.toList());
        sentence.setKeywords(keywords);
    }

    private void limitMaximumSentences() {
        if (content.getSentences().size() <= content.getMaxSentences()) return;
        content.setSentences(content.getSentences().subList(0, content.getMaxSentences()));
    }

    private void breakContentIntoSentences() throws Exception {
        InputStream inputStream = TextRobot.class.getClassLoader().getResourceAsStream("en-sent.bin");
        SentenceModel sentenceModel = new SentenceModel(inputStream);
        SentenceDetectorME sentenceDetector = new SentenceDetectorME(sentenceModel);
        content.setSentences(Arrays.stream(sentenceDetector.sentDetect(content.getSanitizedContent())).map(Sentence::new).collect(Collectors.toList()));
    }

    private void sanitizeContent() {
        String withoutBlanklinesAndMarkdown = Utils.removeBlanklinesandMarkdown(content.getOriginalContent());
        String withoutDatesInParentheses = Utils.removeDatesInParentheses(withoutBlanklinesAndMarkdown);
        content.setSanitizedContent(withoutDatesInParentheses);
    }

    private void fetchFromWikipedia() throws Exception {
        Utils.log("fetching conteúdo da wikipédia");
        Algorithm algorithm = algorithmiaClient.algo("web/WikipediaParser/0.1.2");
        algorithm.setTimeout(300L, TimeUnit.SECONDS);

        JsonObject searchJson = new JsonObject();
        searchJson.addProperty("articleName", content.getSearchTerm());
        searchJson.addProperty("lang", Utils.LANGUAGE);
        JSONObject jsonParser = new JSONObject(algorithm.pipe(searchJson).asJsonString());
        content.setOriginalContent(jsonParser.getString("content"));

        Utils.log("fetching completo");
    }
}
