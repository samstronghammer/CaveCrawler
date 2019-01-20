package com.oliphantsb.cavecrawler.clientgame.game;

import com.oliphantsb.cavecrawler.clientgame.Util;
import com.oliphantsb.cavecrawler.clientgame.enums.Direction;
import com.oliphantsb.cavecrawler.clientgame.enums.ItemType;
import com.oliphantsb.cavecrawler.clientgame.enums.UnitType;
import com.oliphantsb.cavecrawler.clientgame.graph.AStarSearch;

import java.util.List;
import java.util.Scanner;

public class GameLogic {

    public static boolean handleCommand(String s, Board board, Unit player, List<Unit> units, Scanner sc) {
        s = s == null ? "" : s;
        s = s.toLowerCase();
        if (s.equals("quit") || s.equals("exit")) {
            return true;
        } else {
            if (s.length() == 1 && Util.moveCommand.indexOf(s.charAt(0)) != -1) {
                char move = s.charAt(0);
                if (move != '.') {
                    Direction d = Direction.fromUser(move);
                    GameLogic.movePlayer(board, player, units, d);
                }
                for (Unit m : units) {
                    m.addTurn(player.getSpeed());
                    if (GameLogic.moveMonster(m, board, player)) {
                        return true;
                    }
                    GameLogic.equipMonster(m);
                }
            } else {
                switch (s) {
                    case "help":
                        Util.message(Util.s_help);
                        break;
                    case ",":
                        List<Item> items = player.getSquare().getItems();
                        if (items.size() == 0) {
                            Util.message(Util.s_nothing_to_pick_up);
                        } else {
                            for (Item i : items) {
                                player.pickUp(i);
                                Util.message("Picked up " + i.toString());
                            }
                        }
                        break;
                    case "i":
                        if (player.getInventory().size() == 0) {
                            Util.message(Util.s_empty_inventory);
                        } else {
                            for (Item i : player.getInventory()) {
                                Util.message(i.toString());
                            }
                        }
                        break;
                    case "u":
                        if (player.getWeapons().size() == 0) {
                            Util.message(Util.s_no_weapons);
                        } else {
                            Item weapon = Util.userPickFromList(player.getWeapons(), sc);
                            player.equipWeapon(weapon);
                            Util.message(Util.equipMessage(weapon, ItemType.WEAPON));
                        }
                        break;
                    case "p":
                        if (player.getArmor().size() == 0) {
                            Util.message(Util.s_no_armor);
                        } else {
                            Item armor = Util.userPickFromList(player.getArmor(), sc);
                            player.equipArmor(armor);
                            Util.message(Util.equipMessage(armor, ItemType.ARMOR));
                        }
                        break;
                    case "?":
                        Util.message(Util.s_char_help_string);
                        Util.message(Util.getHelp(sc.next()));
                        break;
                    default:
                        Util.message(Util.s_invalid_command);
                        break;
                }
            }
            return false;
        }
    }

    private static void movePlayer(Board board, Unit player, List<Unit> units, Direction d) {
        BoardSquare nextSquare = board.destination(player, d);
        player.setDirection(d);
        if (nextSquare.isOpen()) {
            if (nextSquare.getUnit() == null) {
                player.move(nextSquare);
            } else {
                Unit unit = nextSquare.getUnit();
                boolean dead = unit.takeDamage(player.getAttack());
                if (dead) {
                    units.remove(unit);
                    nextSquare.setUnit(null);
                    for (Item i : unit.getInventory()) {
                        unit.drop(i, nextSquare);
                    }
                    Util.message(Util.deathMessage(player, unit));
                } else {
                    Util.message(Util.attackMessage(player, unit));
                }
            }
        } else {
            Util.message(Util.s_bump_wall);
        }
    }

    public static void equipMonster(Unit m) {
        List<Item> items = m.getSquare().getItems();
        for (Item i : items) {
            m.pickUp(i);
        }
        for (Item i : m.getInventory()) {
            if (i.getItemType() == ItemType.ARMOR && i.getItemValue() > m.getEquippedArmorValue()) {
                m.equipArmor(i);
            } else if (i.getItemType() == ItemType.WEAPON && i.getItemValue() > m.getEquippedWeaponValue()) {
                m.equipWeapon(i);
            }
        }
    }

    private static boolean moveMonster(Unit m, Board board, Unit player) {
        assert m.getType() != UnitType.PLAYER;
        boolean dead = false;
        while (m.getMoveUnits() >= m.getSpeed() && !dead) {
            m.subtractMoveUnits();
            switch (m.getType()) {
                case ZOMBIE:
                    dead = moveZombie(m, board, player);
                    break;
                case SKELETON:
                    dead = moveSkeleton(m, board, player);
                    break;
                case SLUG:
                    dead = moveSlug(m, player);
                    break;
                default:
                    dead = true;
                    break;
            }
        }
        return dead;
    }

    private static boolean moveZombie(Unit m, Board board, Unit player) {
        assert m.getType() == UnitType.ZOMBIE;
        int deltaRow = player.getSquare().getRow() - m.getSquare().getRow();
        int deltaCol = player.getSquare().getCol() - m.getSquare().getCol();
        BoardSquare nextSquare;
        if (Math.abs(deltaRow) > Math.abs(deltaCol)) {
            if (deltaRow > 0) {
                nextSquare = board.destination(m, Direction.DOWN);
            } else {
                nextSquare = board.destination(m, Direction.UP);
            }
        } else {
            if (deltaCol > 0) {
                nextSquare = board.destination(m, Direction.RIGHT);
            } else {
                nextSquare = board.destination(m, Direction.LEFT);
            }
        }
        if (nextSquare.isOpen()) {
            if (nextSquare.getUnit() == null) {
                m.move(nextSquare);
            } else if (nextSquare.getUnit().getType() == UnitType.PLAYER) {
                return attackPlayer(m, player);
            }
        } else {
            if (Math.random() < 0.3) {
                Util.message(Util.s_zombie_bump);
            }
        }
        return false;
    }

    private static boolean moveSkeleton(Unit m, Board board, Unit player) {
        assert m.getType() == UnitType.SKELETON;
        boolean canSee = false;
        Direction playerDir = null;
        for (Direction d : Direction.values()) {
            if (d != m.getDirection().getReverse() && d != Direction.ERROR) {
                BoardSquare step = board.destination(m, d);
                while (step.isOpen() && step.getUnit() == null) {
                    step = board.destination(step, d);
                }
                if (step.isOpen() && step.getUnit() != null && step.getUnit().getType() == UnitType.PLAYER) {
                    canSee = true;
                    playerDir = d;
                    break;
                }
            }
        }
        if (canSee) {
            m.setDirection(playerDir);
            BoardSquare nextSquare = board.destination(m, m.getDirection());
            if (nextSquare.getUnit() == null) {
                m.move(nextSquare);
            } else {
                assert nextSquare.getUnit().getType() == UnitType.PLAYER;
                return attackPlayer(m, player);
            }
        } else {
            BoardSquare nextSquare = board.destination(m, m.getDirection());
            if (nextSquare.isOpen() && nextSquare.getUnit() == null) {
                m.move(nextSquare);
            } else {
                m.setDirection(m.getDirection().getReverse());
            }
        }
        return false;
    }

    public static boolean moveSlug(Unit m, Unit player) {
        assert m.getType() == UnitType.SLUG;
        AStarSearch<BoardSquare, BoardConnection> search = new AStarSearch<>(m.getSquare(), player.getSquare());
        List<BoardConnection> path = search.findSortestDistance();
        if (path.size() > 0) {
            BoardSquare next = path.get(0).getV1();
            if (next.getUnit() == null) {
                m.move(next);
            } else if (next.getUnit().getType() == UnitType.PLAYER) {
                return attackPlayer(m, player);
            }
        }
        return false;
    }

    public static boolean attackPlayer(Unit m, Unit player) {
        boolean dead = player.takeDamage(m.getAttack());
        if (dead) {
            player.getSquare().setUnit(null);
            for (Item i : player.getInventory()) {
                player.drop(i, player.getSquare());
            }
            Util.message(Util.deathMessage(m, player));
        } else {
            Util.message(Util.attackMessage(m, player));
        }
        return dead;
    }

    public static String getStatsString(Unit player, long startTime) {
        String stats = player.getStatsString();
        return stats + ", " + getTimerString(startTime);
    }

    private static String getTimerString(long startTime) {
        long timePassed = System.currentTimeMillis() - startTime;
        return String.format("Timer: %d", timePassed / 1000);
    }
}
