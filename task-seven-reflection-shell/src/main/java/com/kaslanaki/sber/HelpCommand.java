package com.kaslanaki.sber;

@CommandAnno(name = "help", description = "вывод списĸа доступных ĸоманд или справĸи по ĸонĸретной ĸоманде")
public class HelpCommand implements Command {
    private final Command[] commands;

    public HelpCommand() {
        this.commands = new Command[0];
    }

//    public HelpCommand(Command[] commands) {
//        this.commands = commands;
//    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public void execute(String[] args) {
        for (Command command : commands) {
            System.out.println(command.getName() + ": " + command.getHelp());
        }
    }

    @Override
    public String getHelp() {
        return "вывод списĸа доступных ĸоманд или справĸи по ĸонĸретной ĸоманде";
    }
}
