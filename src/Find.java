import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.google.common.collect.Table;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table.Cell;

/*
 * author: Frank Giordano 10/24/2015
 * program implements word dictionary with positions stored
 * implements first half of the following link
 * http://www.ardendertat.com/2011/12/20/programming-interview-questions-23-find-word-positions-in-text/
 * this program uses google's hashtable implementation - i.e. guava-10.0.jar
 */
public class Find {
	
	private static Table<String, Integer, ArrayList<Integer>> dictionary;
	private static ArrayList<Integer> wordPositionsInfo;
    private static String fileName = "/Users/FrankGiordano/Documents/workspace/FindWordPositionsByHashTableGuava/t3.txt";
	
    public static void main(String args[]) {
    
        String searchWord = null;
		byte[] input;
		
		do {
			input = new byte[80];
			System.out.println("Enter a word to search the following file for location info");
			System.out.println(fileName);
			System.out.print(":");
			try {
				System.in.read(input);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			searchWord = (new String(input, 0, input.length)).trim();
			if (searchWord.length() > 0)
		    {
		    	 processFileWordSearch(searchWord);
		    }
		} while (searchWord.length() > 0);
		
    }

	private static void processFileWordSearch(String searchWord) {
		
	     try {
	        	int posFound = 0;
	            int lineCount = 0;
	            String line = "";
	            dictionary = HashBasedTable.create();
	            wordPositionsInfo = new ArrayList<Integer>();

	            // create a reader which reads our file. 
	            BufferedReader bReader = new BufferedReader(new FileReader(fileName));

	            // while we loop through the file, read each line until there is nothing left to read.
	            // this assumes we have carriage returns ending each text line.
	            while ((line = bReader.readLine()) != null) {

	                 lineCount++;
	                 line = toLowerCase(line);
	                 String[] words = line.split(" ");
	                    
	                 for (String word: words) {
	                    	
	                    posFound = line.indexOf(word);
	                    if (dictionary.containsRow(word)) {
	                    	wordPositionsInfo = dictionary.get(word, lineCount);
	                    	if (wordPositionsInfo != null) {
	                    		// find the position of this word from the last saved known position
	                    		// within the array
	                    		posFound = line.indexOf(word, (wordPositionsInfo.lastIndexOf(wordPositionsInfo.size()-1) + word.length()));
	                    		wordPositionsInfo.add(posFound);
	                    	} 
	                    	else 
	                    	{
	                    		ArrayList<Integer> position = new ArrayList<Integer>();
	                    		position.add(posFound);
	                    		dictionary.put(word, lineCount, position);
	                    	}                 	
	                    }
	                    else
	                    {
	                    	ArrayList<Integer> position = new ArrayList<Integer>();
	                		position.add(posFound);
	                    	dictionary.put(word, lineCount, position);
	                    }
	                    			
	                 } 

	             }
	             // close the reader.
	             bReader.close();  
	             print(searchWord, dictionary);      
	       }
	       catch (IOException e) {
	                // we encountered an error with the file, print it to the user.
	                System.out.println("Error: " + e.toString());
	       }
	}

	private static String toLowerCase(String input) {

		    StringBuilder stringBuffer = new StringBuilder(input);

		    String result = "";
		    
		    for (int i = 0; i < stringBuffer.length(); i++) {
		        Character charAt = stringBuffer.charAt(i);
		        if (Character.isAlphabetic(charAt)){
		        	result = result + Character.toLowerCase(charAt);
		        }
		        else
		        {
		        	result = result + charAt;
		        }
		    }

		    return result;
	}

	private static void print(String searchWord, Table<String, Integer, ArrayList<Integer>> dictionary) {
		
		if (!dictionary.containsRow(searchWord)) {
			System.out.println(" The following word " + searchWord + " was not found.");
		}
		else
		{
	        for (Cell<String, Integer, ArrayList<Integer>> cell: dictionary.cellSet()) {
	        	if (cell.getRowKey().equals(searchWord)) {
	        		System.out.println("The following word " + cell.getRowKey() + 
								   " was found at line number " + cell.getColumnKey() + 
								   " at position(s) " + cell.getValue().toString());
	        	}
			}
		}
		
	}

}