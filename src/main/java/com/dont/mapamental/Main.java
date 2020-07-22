package com.dont.mapamental;

import com.dont.mapamental.robots.MainRobot;
import com.dont.mapamental.utils.Utils;

public class Main {

    public static void main(String[] args) {
        try {
            MainRobot mainRobot = new MainRobot("simjB4BCm6peRlyDmBvra6PfJ4J1",
                    "Q5i7t9haAKAwBP8NrTHva-awurSd69wgf8QUJiuYMn0v",
                    "AIzaSyBaz9KmzwbgkFBey7M3MRJDWVMSL6VVzEk", "000875240919289923638:hfv9pjshjfw");
            mainRobot.start();
            Utils.end("finalizado");
        } catch (Exception e) {
            e.printStackTrace();
            Utils.end("erro: " + e.getMessage());
        }
    }

}
