package com.dont.mapamental.robots;

import com.dont.mapamental.models.Content;
import com.dont.mapamental.models.Robot;
import com.dont.mapamental.utils.Utils;

import java.util.Arrays;
import java.util.List;

public class InputRobot extends Robot {

    private final List<String> prefixes = Arrays.asList("Who is", "What is", "The history of");

    public InputRobot(Content content) {
        super(content);
    }

    @Override
    public void start() {
        Utils.log("iniciando robô de input");
        String searchTerm = Utils.input("Digite o termo de pesquisa");
        /*int prefixIndex = Utils.parseInt(Utils.input("Digite o prefixo: " + prefixes.stream().map(p -> prefixes.indexOf(p) + " -> " + p).collect(Collectors.joining(", "))));
        while (prefixIndex < 0 || prefixIndex >= prefixes.size()) {
            prefixIndex = Utils.parseInt(Utils.input("Digite o prefixo: " + prefixes.stream().map(p -> prefixes.indexOf(p) + " -> " + p).collect(Collectors.joining(", "))));
        }
        String prefix = prefixes.get(prefixIndex);
        this.content.setPrefix(prefix);*/
        this.content.setSearchTerm(searchTerm);
    }

}
