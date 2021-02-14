package me.fautor.punishmenthistory;

import me.fautor.punishmenthistory.configuration.ConfigFile;
import me.fautor.punishmenthistory.dependencies.managers.DependencyManager;
import me.fautor.punishmenthistory.utils.Pages;
import me.fautor.punishmenthistory.dependencies.managers.MenuManager;
import org.bukkit.plugin.java.JavaPlugin;

import static me.fautor.punishmenthistory.utils.Color.system;

/**
 * Main.class was created by David(Fautor)
 * Please read History's LICENSE.md file to know your rights
 *
 * @author Fautor
 **/

public class Main extends JavaPlugin {

    private static Main main;

    public me.fautor.punishmenthistory.dependencies.litebans.Menu litebansMenu;
    public me.fautor.punishmenthistory.dependencies.advancedban.Menu advancedbanMenu;
    public MenuManager menuManager;
    public Pages pages;

    @Override
    public void onEnable() {
        main = this;

        new ConfigFile().load(this);

        new DependencyManager(this);
        new Register(this);

        litebansMenu = new me.fautor.punishmenthistory.dependencies.litebans.Menu(this);
        advancedbanMenu = new me.fautor.punishmenthistory.dependencies.advancedban.Menu(this);
        menuManager = new MenuManager(this);
        pages = new Pages(this);

    }

    @Override
    public void onDisable() {
//        new Register(this).disabled();
    }

    public static Main getInstance() { return main; }

}
