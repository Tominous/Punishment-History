package me.fautor.punishmenthistory.dependencies.litebans.command;

import litebans.api.Database;
import me.fautor.punishmenthistory.Main;
import me.fautor.punishmenthistory.dependencies.managers.MenuManager;
import me.fautor.punishmenthistory.enums.manager.Statics;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import sun.misc.IOUtils;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static me.fautor.punishmenthistory.enums.Dependencies.d;
import static me.fautor.punishmenthistory.enums.manager.Statics.*;
import static me.fautor.punishmenthistory.utils.Color.message;

/**
 * History.class was created by David(Fautor)
 * Please read History's LICENSE.md file to know your rights
 *
 * @author Fautor
 **/

public class History implements CommandExecutor {

    private Main main;

    public History(Main main) {
        this.main = main;
    }

    public static Map<String, Long> lastUsage = new HashMap<>();
    private final int cooldown = 4;

    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("You must run this command in-game.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("phistory.access")) {

            message(player, "&cInsufficient permissions.");
            return true;

        }

        long lastUsed = 0;
        if (lastUsage.containsKey(player.getName())) {
            lastUsed = lastUsage.get(player.getName());
        }

        int cdmillis = cooldown * 1000;

        if (!(System.currentTimeMillis() - lastUsed > cdmillis)) {
            int timeleft = (int) (cooldown - ((System.currentTimeMillis() - lastUsed) / 1000));
            message(player, "&cTry again in " + timeleft + " seconds!");
            return true;
        }

        if (args.length < 1) {

            message(player, "&cUsage: /history <player>");
            return true;

        }

        if (args.length == 1) {
            MenuManager.checking_bans.remove(player);
            MenuManager.checking_warnings.remove(player);
            MenuManager.checking_mutes.remove(player);

            new Thread(() -> {

                String uuid = getUUID(args[0]);

                final String query = "SELECT * FROM {history} WHERE uuid=?";

                Player target = Bukkit.getPlayer(UUID.fromString(uuid));

                try (PreparedStatement st = Database.get().prepareStatement(query)) {
                    st.setString(1, uuid);
                    try (ResultSet rs = st.executeQuery()) {
                        while (rs.next()) {

                            MenuManager.storage.put(player, target);
                            player.openInventory(main.litebansMenu.menu(target));
                            message(player, "&aOpening " + target.getName() + "'s Punishment History.");
                        }
                    }
                } catch (SQLException exc) {
                    exc.printStackTrace();
                }

            }).start();

        }

        if (args.length > 1) message(player, a().substring(0, d().length()));

        return false;
    }

    public String getUUID(String playerName) {
        try {
            String url = "https://api.mojang.com/users/profiles/minecraft/" + playerName;

            String UUIDJson = IOUtils.toString(new URL(url));

            JSONObject UUIDObject = (JSONObject) JSONValue.parseWithException(UUIDJson);

            String uuid = UUIDObject.get("id").toString();

            uuid = java.util.UUID.fromString(
                    uuid.replaceFirst("(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5")).toString();

            return uuid;
        } catch (Exception e) {
            return "Not found";
        }
    }
}
