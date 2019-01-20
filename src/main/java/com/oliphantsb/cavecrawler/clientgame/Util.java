package com.oliphantsb.cavecrawler.clientgame;

import com.oliphantsb.cavecrawler.clientgame.enums.ItemType;
import com.oliphantsb.cavecrawler.clientgame.enums.ResourceType;
import com.oliphantsb.cavecrawler.clientgame.enums.UnitType;
import com.oliphantsb.cavecrawler.clientgame.game.Item;
import com.oliphantsb.cavecrawler.clientgame.game.Unit;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Util {

    public static final String s_invalid_command = "Uh oh! That was an invalid command. Type help and press enter if you have questions.";
    public static final String s_bump_wall = "Ouch! You bumped your head on the wall.";
    public static final String s_char_wrong = "Oops! Please only input one character.";
    public static final String s_nothing_to_pick_up = "Nothing to pick up here :/";
    public static final String s_empty_inventory = "You have nothing in your inventory.";
    public static final String s_no_weapons = "You have no weapons.";
    public static final String s_no_armor = "You have no armor.";
    public static final String s_win = "Congratulations! You beat the level!";
    public static final String s_scroller_info = "This is where events are displayed.";
    public static final String s_stats_info = "Here you can read your current stats.";
    public static final String s_char_help_string = "What character do you have questions about?";
    public static final String s_zombie_bump = "You hear a squish as a zombie bumps into a wall. Disgruntled moaning ensues.";
    public static final String s_end_opens = "You hear a door creak open.";
    public static final String s_help = "Welcome to CaveCrawler :)\n" +
            "Any command you wish to issue can be done by pressing a key then pressing enter.\n" +
            "Here is a full list of commands:\n" +
            "w: move up\n" +
            "a: move left\n" +
            "s: move down\n" +
            "d: move right\n" +
            ".: stay in place for a turn\n" +
            ",: pick up items beneath you\n" +
            "i: examine your inventory\n" +
            "u: use a weapon from your inventory\n" +
            "p: put on armor from your inventory\n" +
            "t: toggle direction mode on/off\n" +
            "?: get information about a map symbol\n" +
            "If you want to attack an enemy, just try to walk into them.\n" +
            "They can attack you too though, so watch out! Good luck!";

    public static String s_graphical_help = "Welcome to CaveCrawler :)\n" +
            "Any command you wish to issue can be done by pressing a key.\n" +
            "Here is a full list of commands:\n" +
            "up arrow: move up\n" +
            "left arrow: move left\n" +
            "down arrow: move down\n" +
            "right arrow: move right\n" +
            ".: stay in place for a turn\n" +
            ",: pick up items beneath you\n" +
            "i: examine your inventory\n" +
            "u: use a weapon from your inventory\n" +
            "p: put on armor from your inventory\n" +
            "If you want to attack an enemy, just try to walk into them.\n" +
            "They can attack you too though, so watch out! Good luck!";

    public static final Icon wall_icon = getIcon("wallicon.png");
    public static final Icon dark_wall_icon = getIcon("darkwallicon.png");
    public static final Icon floor_icon = getIcon("flooricon.png");
    public static final Icon dark_floor_icon = getIcon("darkflooricon.png");
    public static final Icon closed_exit_icon = getIcon("closedexiticon.png");
    public static final Icon dark_closed_exit_icon = getIcon("darkclosedexiticon.png");
    public static final Icon open_exit_icon = getIcon("openexiticon.png");
    public static final Icon dark_open_exit_icon = getIcon("darkopenexiticon.png");

    public static final String moveCommand = "WwAaSsDd.";
    public static final String otherCommand = ",iupt?";

    private static final double scale = 1;
    public static final int iconSize = (int) (32 * scale);
    public static final int fontSize = (int) (12 * scale);
    public static final int topPartHeight = (int) (150 * scale);

    public static final int standardTurnLength = 24;

    private static DefaultListModel<String> listModel = null;
    private static JList<String> jlist = null;

    private Util(){}

    public static void setJList(JList<String> list, DefaultListModel<String> model) {
        jlist = list;
        listModel = model;
    }

    public static Icon getIcon(String fileName) {
        return new ImageIcon(new ImageIcon(Unit.class.getClassLoader().getResource(getResourceLocation(fileName, ResourceType.SPRITE)))
                .getImage().getScaledInstance(Util.iconSize, Util.iconSize, Image.SCALE_DEFAULT));
    }

    public static void message(String s) {
        if (listModel == null) {
            System.out.println(s);
        } else {
            String[] strings = s.split("\n");
            for (String str : strings) {
                listModel.addElement(str);
            }
            jlist.ensureIndexIsVisible(listModel.size() - 1);
        }
    }

    public static void println(String s) { System.out.println(s); }
    public static void println(int i) { System.out.println(Integer.toString(i)); }

    public static void printchar(char c) { System.out.print(c); }

    public static void printloc(char c, int row, int col) {
        System.out.print(String.format("\033[%d;%dH%s", row, col, c));
    }

    public static String deathMessage(Unit attacker, Unit defender) {
        if (attacker.getType() == UnitType.PLAYER) {
            return "You strike the " + defender.getName() + " and it crumbles to dust!";
        } else if (defender.getType() == UnitType.PLAYER){
            return "The " + attacker.getName() + " strikes you. You can tell its fatal. The world slowly loses focus and you know no more.";
        } else {
            return "The " + attacker.getName() + " strikes and kills the " + defender.getName() + "!";
        }
    }

    public static String attackMessage(Unit attacker, Unit defender) {
        if (attacker.getType() == UnitType.PLAYER) {
            return "You strike the " + defender.getName() + ".";
        } else if (defender.getType() == UnitType.PLAYER){
            return "The " + attacker.getName() + " strikes you.";
        } else {
            return "The " + attacker.getName() + " strikes the " + defender.getName() + ".";
        }
    }

    public static String equipMessage(Item i, ItemType type) {
        switch (type) {
            case WEAPON:
                return i == null ? "You are now wielding your bare hands." : "You are now wielding a " + i.toString();
            case ARMOR:
                return i == null ? "You are now wearing no armor." : "You are now wearing a " + i.toString();
            default:
                return "ERROR";
        }
    }

    public static String getHelp(String s) {
        if (s.equals("#") || s.equals(".") || s.equals("_") || s.equals("0")) {
            switch (s.charAt(0)) {
                case '#':
                    return "That's a wall. Can't walk through there!";
                case '.':
                    return "That's an open space. Feel free to walk through.";
                case '_':
                    return "That's the closed exit. It will open after you collect all the gold.";
                case 'O':
                    return "That's the open exit. You win if you get there.";
            }
        } else {
            if (s.length() == 1) {
                char c = s.charAt(0);
                UnitType unit = UnitType.fromAscii(c);
                if (unit != UnitType.ERROR) {
                    if (unit == UnitType.PLAYER) {
                        return "That's you :)";
                    } else {
                        return "That's a monster. More specifically, a " + unit.getName();
                    }
                } else {
                    ItemType item = ItemType.fromAscii(c);
                    if (item != ItemType.ERROR) {
                        switch (item) {
                            case GOLD:
                                return "That's an item. More specifically, some gold.";
                            case ARMOR:
                                return "That's an item. More specifically, a piece of armor.";
                            case WEAPON:
                                return "That's an item. More specifically, a weapon.";
                        }
                    }
                }
            } else {
                switch (s) {
                    case "event list":
                        return "That is your information feed. It tells you what's happening.";
                    case "statistics":
                        return "Those are your current stats. Find and use good weapons and armor to improve your stats.";
                }
            }
        }
        return "Hmm I don't think that character is in the game.";
    }

    public static Item userPickFromList(List<Item> list, Scanner sc) {
        Map<Integer, Item> map = new HashMap<>();
        int i = 0;
        for (Item item : list) {
            map.put(i, item);
            i++;
        }
        map.put(i, null);
        int choice;
        if (listModel == null) {
            i = 0;
            for (Item item : list) {
                Util.message(Integer.toString(i) + ": " + item.toString());
                i++;
            }
            Util.message(Integer.toString(i) + ": " + "None");
            choice = Integer.parseInt(sc.next());
        } else {
            String[] options = new String[list.size() + 1];
            int j;
            for (j = 0; j < list.size(); j++) {
                options[j] = list.get(j).toString();
            }
            options[j] = "None";
            choice = JOptionPane.showOptionDialog(null, "What do you wish to equip?",
                    "Choose", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, options, options[0]);
        }
        return map.get(choice);
    }

    public static String userPickStringFromList(List<String> list, Scanner sc) {
        Map<Integer, String> map = new HashMap<>();
        int i = 0;
        for (String item : list) {
            map.put(i, item);
            i++;
        }
        map.put(i, null);
        int choice;
        if (listModel == null) {
            i = 0;
            for (String item : list) {
                Util.message(Integer.toString(i) + ": " + item);
                i++;
            }
            Util.message(Integer.toString(i) + ": " + "Exit");
            choice = Integer.parseInt(sc.next());
        } else {
            String[] options = new String[list.size() + 1];
            int j;
            for (j = 0; j < list.size(); j++) {
                options[j] = list.get(j);
            }
            options[j] = "Exit";
            choice = JOptionPane.showOptionDialog(null, "What do you wish to choose?",
                    "Choose", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, options, options[0]);
        }
        return map.get(choice);
    }

    public static String getResourceLocation(String filename, ResourceType type) {
        return type.getPrefix() + filename;
    }

    public static Map<Integer, String> getKeyHandlerMap() {
        Map<Integer, String> map = new HashMap<>();
        map.put(37, "a");
        map.put(38, "w");
        map.put(39, "d");
        map.put(40, "s");
        map.put(46, ".");
        map.put(44, ",");
        map.put(73, "i");
        map.put(85, "u");
        map.put(80, "p");
        return map;
    }
}
