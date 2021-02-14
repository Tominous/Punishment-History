package me.fautor.punishmenthistory;

import me.fautor.punishmenthistory.enums.Dependencies;
import me.fautor.punishmenthistory.enums.manager.Statics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;


import static me.fautor.punishmenthistory.utils.Color.system;

/**
 * Register.class was created by David(Fautor)
 * Please read History's LICENSE.md file to know your rights
 *
 * @author Fautor
 **/

public class Register {

    private static Register instance;

    private Main main;

    public Register(Main main) {
        this.main = main;
        instance = this;

        registerCommands();
        registerListeners();
        registerSchedulers();
        registerMisc();

        Statics.disablePlugin(main);
        enabled();
        Dependencies.getBooleans();
    }

    /**
     * Registers the in-game commands.
     */
    public void registerCommands() {
        if (Dependencies.LITEBANS.isEnabled()) {
            main.getCommand("history").setExecutor(new me.fautor.punishmenthistory.dependencies.litebans.command.History(main));
        }
        if (Dependencies.ADVANCEDBAN.isEnabled()) {
            main.getCommand("history").setExecutor(new me.fautor.punishmenthistory.dependencies.advancedban.command.History(main));
        }
    }

    /**
     * Registers the in-game windows/events.
     */
    public void registerListeners() {
        if (Dependencies.LITEBANS.isEnabled()) {
            PluginManager p = Bukkit.getPluginManager();
            p.registerEvents(new me.fautor.punishmenthistory.dependencies.litebans.Menu(main), main);
            p.registerEvents(new me.fautor.punishmenthistory.dependencies.litebans.windows.WindowListener(main), main);
        }
        if (Dependencies.ADVANCEDBAN.isEnabled()) {
            PluginManager p = Bukkit.getPluginManager();
            p.registerEvents(new me.fautor.punishmenthistory.dependencies.advancedban.Menu(main), main);
            p.registerEvents(new me.fautor.punishmenthistory.dependencies.advancedban.windows.WindowListener(main), main);
        }
    }

    /**
     * Registers the in-game schedulers.
     */
    private void registerSchedulers() {

    }

    /**
     * Registers all misc.
     */
    private void registerMisc() {

    }

    /**
     * This will tell the console that
     * the main is simply enabling.
     * If it corrupts, it will give
     * them an error.
     */
    private void enabled() {
        try {
            system("&6---------------------------");
            system("&6[ Punishment History b1.0 ]");
            system("&6[  Author:                ]");
            system("&6[   - Fautor              ]");
            system("&6[  Build: 1.0             ]");
            system("&6[  Contact(s):            ]");
            system("&6[   - Telegram: @fautor   ]");
            system("&6[   - Twitter: @fautorr   ]");
            system("&6[   - Discord: fautor#0001]");
            system("&6[  Dependency Enabled:    ]");
            for (Dependencies dependencies : Dependencies.values()) {
                if (dependencies.isEnabled())
                system("&6[   - " + dependencies.getName() + " ]");
            }
            system("&6---------------------------");
        } catch(Exception e) {
            system("&4[!!! THERE IS AN ERROR !!!]");
            e.printStackTrace();
        }
    }


    /**
     * This will tell the console that
     * the main is simply disabling.
     */
    public void disabled() {
        try {
            system("&4---------------------------");
            system("&4[ Punishment History b1.0 ]");
            system("&4[  Author:                ]");
            system("&4[   - Fautor              ]");
            system("&4---------------------------");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    //Get any instances from this class around the world!
    public static Register getInstance() {
        return instance;
    }

}
