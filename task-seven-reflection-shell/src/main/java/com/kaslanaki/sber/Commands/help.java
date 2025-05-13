package com.kaslanaki.sber.Commands;

import com.kaslanaki.sber.Command;

public class help implements Command {

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public void execute(String[] args) {
    }

    @Override
    public String getHelp() {
        return "вывод списĸа доступных ĸоманд или справĸи по ĸонĸретной ĸоманде";
    }
}
