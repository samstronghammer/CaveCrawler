package com.oliphantsb.cavecrawler.clientgame;

import com.oliphantsb.cavecrawler.clientgame.enums.ResourceType;
import com.oliphantsb.cavecrawler.clientgame.enums.VisibilitySetting;
import com.oliphantsb.cavecrawler.clientgame.game.MapParser;
import com.oliphantsb.cavecrawler.clientgame.game.Game;
import com.oliphantsb.cavecrawler.clientgame.game.GraphicalGame;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Main {

    public static void main(String[] args) throws IOException {
        OptionParser parser = new OptionParser();
        parser.accepts("complex");
        parser.accepts("text");
        parser.accepts("hard");
        OptionSet options = parser.parse(args);
        List<String> maps =
                MapParser.getStringOnEachLine(options.has("complex") ? "complexmaps.txt" : "simplemaps.txt", ResourceType.INFORMATION);
        VisibilitySetting visibility = options.has("hard") ? VisibilitySetting.HARD : VisibilitySetting.EASY;
        if (options.has("text")) {
            runText(visibility, maps);
        } else {
            runGraphical(visibility, maps);
        }
        Util.println("Exited successfully.");
    }

    private static void runGraphical(VisibilitySetting visibility, List<String> boardfiles) throws IOException {
        JFrame frame = new JFrame("Cave Crawler");
        setUpJFrame(frame);
        String answer = getGraphicalDialogAnswer(frame, boardfiles);
        while(answer != null) {
            new GraphicalGame(null, answer, frame).run(visibility);
            setUpJFrame(frame);
            answer = getGraphicalDialogAnswer(frame, boardfiles);
        }
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }

    private static void runText(VisibilitySetting visibility, List<String> boardfiles) throws IOException {
        Scanner sc = new Scanner(System.in);
        String answer = Util.userPickStringFromList(boardfiles, sc);
        while (answer != null) {
            new Game(null, answer, sc).run(visibility);
            answer = Util.userPickStringFromList(boardfiles, sc);
        }
        Util.message("Goodbye!");
        sc.close();
    }

    private static void setUpJFrame(JFrame frame) {
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.add(new JLabel("HELLO"));
        frame.setVisible(true);
    }

    private static String getGraphicalDialogAnswer(JFrame frame, List<String> allMaps) throws IOException {
        int delta = 10;
        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> list = new JList<>(model);
        list.setFont(new Font(list.getFont().getName(), Font.PLAIN, Util.fontSize));
        JScrollPane listScroller = new JScrollPane(list);
        for (String s : allMaps) {
            String[] namedescrip = MapParser.getNameAndDescription(s);
            String together = namedescrip[0] + ": " + namedescrip[1];
            model.addElement(together);
        }
        JOptionPane pane = new JOptionPane("Are you ready to fight the monsters?",
                JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        pane.add(listScroller);
        list.addListSelectionListener(e -> pane.setValue(e.getFirstIndex() + delta));
        JDialog dialog = pane.createDialog(frame, "Level Selection");
        dialog.setVisible(true);
        int selectedValue = (int) pane.getValue();
        if (selectedValue < delta) {
            return null;
        } else {
            return allMaps.get(selectedValue - delta);
        }
    }
}
