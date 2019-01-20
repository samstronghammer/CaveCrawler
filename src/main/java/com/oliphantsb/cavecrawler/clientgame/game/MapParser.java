package com.oliphantsb.cavecrawler.clientgame.game;

import com.oliphantsb.cavecrawler.clientgame.enums.Direction;
import com.oliphantsb.cavecrawler.clientgame.enums.ItemType;
import com.oliphantsb.cavecrawler.clientgame.enums.UnitType;
import com.oliphantsb.cavecrawler.clientgame.Util;
import com.oliphantsb.cavecrawler.clientgame.enums.ResourceType;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MapParser {

    private MapParser() {}

    public static Unit parseMap(String filename, List<Unit> units, Board board) throws IOException {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(MapParser.class.getClassLoader().getResourceAsStream(Util.getResourceLocation(filename, ResourceType.MAP))))) {
            String name = br.readLine();
            String descrip = br.readLine();
            int numLines = Integer.parseInt(br.readLine());
            Unit player = null;
            for (int i = 0; i < numLines; i++) {
                board.addRow();
                String next = br.readLine();
                for (int j = 0; j < next.length(); j++) {
                    char c = next.charAt(j);
                    switch (c) {
                        case '#':
                            board.addSquare(null, false, false, i, j);
                            break;
                        case '.':
                            board.addSquare(null, true, false, i, j);
                            break;
                        case '_':
                            board.addSquare(null, true, true, i, j);
                            break;
                        case '$': {
                            BoardSquare sq = board.addSquare(null, true, false, i, j);
                            Item gold = new Item(ItemType.GOLD, 1, "gold");
                            sq.addItem(gold);
                            break;
                        }
                        case '@': {
                            BoardSquare sq = new BoardSquare(true, false, i, j);
                            player = new Unit(UnitType.fromAscii(c), sq);
                            sq.setUnit(player);
                            board.addSquare(sq);
                            break;
                        }
                        default: {
                            BoardSquare sq = new BoardSquare(true, false, i, j);
                            Unit newUnit = new Unit(UnitType.fromAscii(c), sq);
                            sq.setUnit(newUnit);
                            board.addSquare(sq);
                            units.add(newUnit);
                            break;
                        }
                    }
                }
            }
            for (Unit u : units) {
                String next = br.readLine();
                assert next.length() == 1;
                u.setDirection(Direction.fromAscii(next.charAt(0)));
            }
            numLines = Integer.parseInt(br.readLine());
            for (int i = 0; i < numLines; i++) {
                String line = br.readLine();
                String[] parts = line.split(",");
                Item item = new Item(ItemType.fromAscii(parts[2].charAt(0)), Integer.parseInt(parts[3]), parts[4]);
                board.addItem(item, Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
            }
            board.initializeConnections();
            return player;
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("'" + filename + "'" + " was not found.");
        } catch (IOException ioe) {
            throw new IOException("'" + filename + "'" + " caused IO exception.");
        }
    }

    public static String[] getNameAndDescription(String filename) throws IOException {
        String[] strings = new String[2];
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(MapParser.class.getClassLoader().getResourceAsStream(Util.getResourceLocation(filename, ResourceType.MAP))))) {
            strings[0] = br.readLine();
            strings[1] = br.readLine();
            return strings;
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("'" + filename + "'" + " was not found.");
        } catch (IOException ioe) {
            throw new IOException("'" + filename + "'" + " caused IO exception.");
        }
    }

    public static List<String> getStringOnEachLine(String filename, ResourceType type) throws IOException {
        List<String> strings = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(MapParser.class.getClassLoader().getResourceAsStream(Util.getResourceLocation(filename, type))))) {
            String line = br.readLine();
            while (line != null) {
                strings.add(line);
                line = br.readLine();
            }
            return strings;
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("'" + filename + "'" + " was not found.");
        } catch (IOException ioe) {
            throw new IOException("'" + filename + "'" + " caused IO exception.");
        }
    }

}