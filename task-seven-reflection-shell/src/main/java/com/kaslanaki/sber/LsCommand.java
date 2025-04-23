package com.kaslanaki.sber;

@CommandAnno(name = "ls", description = "вывод содержимого теĸущей диреĸтории")
public class LsCommand implements Command {
    @Override
    public String getName() {
        return "ls";
    }

    @Override
    public void execute(String[] args) {
        System.out.println(System.getProperty("user.dir"));
    }

    @Override
    public String getHelp() {
        return "вывод содержимого теĸущей диреĸтории";
    }
}
