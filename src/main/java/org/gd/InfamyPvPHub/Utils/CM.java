package org.gd.InfamyPvPHub.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class CM {

    final static String path = "plugins/InfamyPvPHub/interndata/bossbars.yml";
    static File file = new File(path);
    public static FileConfiguration config = YamlConfiguration.loadConfiguration(file);

    public static boolean enabled = false;
    public static boolean random = true;
    public static int interval = 60;
    public static int show = 20;
    public static String colorcodes;
    public static List<List<String>> messages;
    public static List<List<String>> rawmessages;
    public static String noperm;
    public static List<String> players;
    public static boolean whitelist = false;
    public static List<String> worlds;
    //public String configVersion = Main.getInstance().getDescription().getVersion();

    public static void createConfig() {
        //if (newConfig() == false)
        //    return;

        config.addDefault("InfamyPvPHub.Enabled", true);
        config.addDefault("InfamyPvPHub.Random", false);
        config.addDefault("InfamyPvPHub.Interval", 250);
        config.addDefault("InfamyPvPHub.Show", 100);
        config.addDefault("InfamyPvPHub.ColorCodes", "'0123456789abcdef'");
        config.addDefault("InfamyPvPHub.NoPermission", "&cNo Permission!");
        config.addDefault("InfamyPvPHub.Whitelist", false);

        List<List<String>> exampleList = new ArrayList<List<String>>();
        
        List<String> msg1 = new ArrayList<>();
        msg1.add("&bYo &5%player%&b, wazzup?");
        msg1.add("100");
        exampleList.add(msg1);

        List<String> msg2 = new ArrayList<>();
        msg2.add("&aInfamyPvPHub - BossBar plugin by &bGlobal Development");
        msg2.add("60");
        exampleList.add(msg2);
        
        List<String> msg3 = new ArrayList<>();
        msg3.add("%rdm_color%Now %rdm_color%supports %rdm_color%custom %rdm_color%random %rdm_color%colors");
        msg3.add("30");
        exampleList.add(msg3);
        
        config.addDefault("InfamyPvPHub.Messages", exampleList);

        List<String> playersList = new ArrayList<>();
        playersList.add("testPlayer");
        playersList.add("examplePlayer");
        playersList.add("your name");
        config.addDefault("InfamyPvPHub.IgnorePlayers", playersList);

        List<String> worldList = new ArrayList<>();
        worldList.add("world");
        worldList.add("world_nether");
        worldList.add("ExampleWorld");
        config.addDefault("InfamyPvPHub.WhitelistedWorlds", worldList);

        //config.set("InfamyPvPHub.configVersion", Main.getInstance().getDescription().getVersion());

        config.options().copyDefaults(true);

        save();
    }
    
        @SuppressWarnings("unchecked")
        public static void readConfig() {
        enabled = config.getBoolean("InfamyPvPHub.Enabled");
        random = config.getBoolean("InfamyPvPHub.Random");
        interval = config.getInt("InfamyPvPHub.Interval");
        show = config.getInt("InfamyPvPHub.Show");
        noperm = ChatColor.translateAlternateColorCodes('&', config.getString("InfamyPvPHub.NoPermission"));
        colorcodes = config.getString("InfamyPvPHub.ColorCodes");
        rawmessages = (List<List<String>>) config.getList("InfamyPvPHub.Messages");
        messages = getMsgs();
        players = config.getStringList("InfamyPvPHub.IgnorePlayers");
        whitelist = config.getBoolean("InfamyPvPHub.Whitelist");
        worlds = config.getStringList("InfamyPvPHub.WhitelistedWorlds");
    }

    public static void save() {
        try {
            config.save(path);
        } catch (IOException e) {
            System.out.println("InfamyPvPHub | Error 'createConfig' on " + path);
        }
    }
    
    @SuppressWarnings("unchecked")
        public static List<List<String>> getMsgs() {
            List<List<String>> output = new ArrayList<List<String>>();
            List<List<String>> msgs = (List<List<String>>) config.getList("InfamyPvPHub.Messages");
            for (List<String> msg:msgs) {
                    List<String> a = new ArrayList<>();
                    a.add(ChatColor.translateAlternateColorCodes('&', msg.get(0)));
                    a.add(msg.get(1));
                    output.add(a);
            }
            for (List<String> msg:rawmessages) {
                    Bukkit.broadcastMessage(msg.get(0));
            }
                return output;
    }
}