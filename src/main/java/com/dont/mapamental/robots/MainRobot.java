package com.dont.mapamental.robots;

import com.dont.mapamental.models.Content;
import com.dont.mapamental.models.Robot;
import com.dont.mapamental.utils.Utils;

public class MainRobot extends Robot {

    private String input;
    private MapaRobot mapaRobot;
    private ImageRobot imageRobot;
    private InputRobot inputRobot;
    private TextRobot textRobot;

    public MainRobot(String algorithmiaKey, String watsonKey, String googleKey, String googleCx) {
        this(algorithmiaKey, watsonKey, googleKey, googleCx, null);
    }

    public MainRobot(String algorithmiaKey, String watsonKey, String googleKey, String googleCx, String input) {
        super(new Content());
        this.inputRobot = new InputRobot(content);
        this.textRobot = new TextRobot(content, algorithmiaKey, watsonKey);
        this.imageRobot = new ImageRobot(content, googleKey, googleCx);
        this.mapaRobot = new MapaRobot(content);
        this.input = input;
        if (input != null) content.setSearchTerm(input);
    }


    @Override
    public void start() throws Exception {
        Utils.log("iniciando robô principal");
        if (input == null) inputRobot.start();
        textRobot.start();
        //imageRobot.start();
        mapaRobot.start();
    }
}
