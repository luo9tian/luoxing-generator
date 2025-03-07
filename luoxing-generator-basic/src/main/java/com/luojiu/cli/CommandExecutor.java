package com.luojiu.cli;

import com.luojiu.cli.command.ConfigCommand;
import com.luojiu.cli.command.GenerateCommand;
import com.luojiu.cli.command.ListCommand;
import picocli.CommandLine;
import picocli.CommandLine.*;

@Command(name = "luojiu",mixinStandardHelpOptions = true)
public class CommandExecutor implements Runnable{

    private final CommandLine commandLine;

    {
        commandLine=new CommandLine(this)
                .addSubcommand(new GenerateCommand())
                .addSubcommand(new ListCommand())
                .addSubcommand(new ConfigCommand());
    }
    @Override
    public void run() {
        System.out.println("请输入命令，或者输入--help获取命令提示");
    }
    public Integer doExecutor(String[] args){
        return commandLine.execute(args);
    }
}
