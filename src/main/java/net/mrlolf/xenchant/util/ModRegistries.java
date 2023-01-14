package net.mrlolf.xenchant.util;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.mrlolf.xenchant.command.EnchantCommand;

public class ModRegistries {
    public static void registerModStuff(){
        registerCommands();
    }

    private static void registerCommands(){
        CommandRegistrationCallback.EVENT.register(EnchantCommand::register);
    }
}
