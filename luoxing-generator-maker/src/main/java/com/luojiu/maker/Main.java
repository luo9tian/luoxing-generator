package com.luojiu.maker;

import com.luojiu.maker.cli.CommandExecutor;

public class Main {

    public static void main(String[] args) {
        args=new String[]{"generate","-l","-a","-o"};
//        args=new String[]{"config"};
//        args=new String[]{"list"};
//        args=new String[]{"--help"};
        CommandExecutor commandExecutor=new CommandExecutor();
        commandExecutor.doExecutor(args);
    }
}
