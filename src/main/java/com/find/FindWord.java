package com.find;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/*
 * This program implements word dictionary with positions stored. It implements something similar to the
 * first half of the following link:
 *
 * http://www.ardendertat.com/2011/12/20/programming-interview-questions-23-find-word-positions-in-text/
 *
 * The structure of the code flow is that a text file is read from the current running location and reads
 * in every line and looks at each word and stores it location/position within the line.
 *
 * The main data structure is google's hashtable implementation called 'Table' from Guava Java Library.
 *
 * author: Frank Giordano 10/24/2015
 */
public class FindWord {

    private static Table<String, Integer, ArrayList<Integer>> dictionary;
    private static String fileName = "t3.txt";

    private static void processFile() {

        int position = 0;
        int lineCount = 0;
        String line = "";

        dictionary = HashBasedTable.create();
        try {
            BufferedReader bReader = new BufferedReader(new FileReader(fileName));

            // while we loop through the file, read each line until there is nothing left to
            // read. this assumes we have carriage returns ending each text line.
            while ((line = bReader.readLine()) != null) {

                lineCount++;

                line = toLowerCase(line);
                String[] words = line.replaceAll("[^a-z0-9]", " ").split(" ");
                for (String word : words) {
                    if ("".equals(word))
                        continue;

                    int startIndex = 0;
                    while ((position = line.indexOf(word, startIndex)) != -1) {
                        startIndex = position + word.length();
                        // ignore finding the word text within another larger word
                        if (isWordEmbedded(position, word, line)) {
                            continue; // skip this position
                        }

                        // first time this word is seen
                        if (!dictionary.containsRow(word)) {
                            addWordPosition(position, lineCount, word);
                            break;
                        }

                        // first time adding a position for this word for this line
                        if (dictionary.containsRow(word) && dictionary.get(word , lineCount) == null) {
                            addWordPosition(position, lineCount, word);
                            break;
                        }

                        // at this point, it is obvious add position to existing word/line storage
                        ArrayList<Integer> positions = dictionary.get(word , lineCount);
                        // increase last known position to the end of the word so it starts searching there
                        int lastKnownPos = positions.get(positions.size() - 1) + word.length();
                        // search for the next word occurrence from current read in line file
                        position = line.indexOf(word, lastKnownPos);
                        if (position == -1)
                            break;
                        if (!positions.contains(Integer.valueOf(position))) {
                            positions.add(position);
                            startIndex = position + word.length();
                        }
                    }
                }
            }

            bReader.close();
        } catch (IOException e) {
            System.out.print("Error reading file. Error message = " + e.getMessage());
            System.exit(-1);
        }
    }

    private static void addWordPosition(int position, int lineCount, String word) {
        ArrayList<Integer> positions = new ArrayList<>();
        positions.add(position);
        dictionary.put(word, lineCount, positions);
    }

    private static boolean isWordEmbedded(int position, String word, String line) {
        if (position != 0 && position != line.length() - 1 && position + (word.length() - 1) != (line.length() - 1)) {
            boolean isAlphabeticRightSide = Character.isAlphabetic(line.charAt(position + (word.length())));
            boolean isAlphabeticLeftSide = Character.isAlphabetic(line.charAt(position - 1));
            if (isAlphabeticRightSide || isAlphabeticLeftSide) {
                return true;
            }
        }

        if (position == 0 && (position + (word.length()) < line.length())) {
            boolean isAlphabeticRightSide = Character.isAlphabetic(line.charAt(position + (word.length())));
            if (isAlphabeticRightSide)
                return true;
        }

        return false;
    }

    private static String toLowerCase(String input) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            Character charAt = input.charAt(i);
            if (Character.isAlphabetic(charAt)) {
                result.append(Character.toLowerCase(charAt));
            } else {
                result.append(charAt);
            }
        }
        return result.toString();
    }

    public static void print(String searchWord) {
        processFile();
        if (!dictionary.containsRow(searchWord)) {
            System.out.println("The following word " + searchWord + " was not found.");
        } else {
            for (Cell<String, Integer, ArrayList<Integer>> cell : dictionary.cellSet()) {
                if (cell.getRowKey().equals(searchWord)) {
                    foundMsg(searchWord, cell);
                }
            }
        }
    }

    private static void foundMsg(String searchWord, Cell<String, Integer, ArrayList<Integer>> cell) {
        StringBuilder message = new StringBuilder();
        message.append("The following word ");
        message.append("\"");
        message.append(searchWord);
        message.append("\"");
        message.append(" was found at line number ");
        message.append(cell.getColumnKey());
        message.append(" at position(s): ");
        message.append((cell.getValue().toString()));
        System.out.println(message);
    }

    public static void main(String args[]) {

        String searchWord = null;
        byte[] input;

        do {
            input = new byte[80];
            System.out.printf("Enter a word to search the following file %s for location info\n", fileName);
            System.out.print("> ");
            try {
                System.in.read(input);
            } catch (IOException e) {
                System.out.print("Error reading given input. Error message = " + e.getMessage());
                System.exit(-1);
            }
            searchWord = (new String(input, 0, input.length)).trim();
            if (searchWord.length() > 0) {
                FindWord.print(searchWord);
            } else {
                System.exit(0);
            }
        } while (searchWord.length() > 0);
    }

}