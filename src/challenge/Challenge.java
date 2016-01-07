package challenge;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashSet;

public class Challenge
{
    private static final String USAGE = "usage: java challenge.Challenge file_name";

    public static final void main(String[] args) throws Exception
    {
	if (args.length != 1)
	{
	    System.out.println(USAGE);
	    System.exit(1);
	}

	String fileName = args[0];
	HashSet<String> wordSet = getWordSet(fileName);

	ArrayList<String> longestCompoundWordList = new ArrayList<String>();
	longestCompoundWordList.add("");

	for (String word : wordSet)
	{
	    if (getCompoundWord(wordSet, word) != null)
	    {
		// check length and keep or discard
		String currentLongWord = longestCompoundWordList.get(0);
		if (word.length() == currentLongWord.length())
		{
		    longestCompoundWordList.add(word);
		}
		else if (word.length() > currentLongWord.length())
		{
		    longestCompoundWordList.clear();
		    longestCompoundWordList.add(word);
		}
		// else word is small and ignore

	    }
	}

	for (String word : longestCompoundWordList)
	{
	    System.out.println(word + ":" + getCompoundWord(wordSet, word));
	}

    }

    private static ArrayList<String> getCompoundWord(HashSet<String> wordSet, String word)
    {
	ArrayList<ArrayList<String>> comboList = getComboList(wordSet, word);

	for (ArrayList<String> combo : comboList)
	{
	    // discard one word sets
	    if (combo.size() == 1)
	    {
		continue;
	    }

	    // made it this far - now validate all parts
	    boolean keepGoing = true;
	    for (String subword : combo)
	    {
		if (!wordSet.contains(subword))
		{
		    keepGoing = false;
		    break;
		}

	    }
	    if (!keepGoing)
	    {
		continue;
	    }

	    // word contains one or more valid compound forms
	    return combo;

	}
	return null;
    }

    private static ArrayList<ArrayList<String>> getComboList(HashSet<String> wordSet, String word)
    {
	ArrayList<ArrayList<String>> comboList = new ArrayList<ArrayList<String>>();

	ArrayList<String> baseCombo = new ArrayList<String>(); // may need to
							       // discard later
	baseCombo.add(word);
	comboList.add(baseCombo);

	for (int startLength = 2; startLength <= word.length() - 2; startLength++)
	{
	    String start = word.substring(0, startLength);

	    // if start is not a known word, skip to next possibility
	    if (!wordSet.contains(start))
	    {
		continue;
	    }

	    String remainder = word.substring(startLength);

	    // recursive call
	    ArrayList<ArrayList<String>> nestedComboList = getComboList(wordSet, remainder);
	    for (ArrayList<String> nestedCombo : nestedComboList)
	    {
		nestedCombo.add(0, start);
		comboList.add(nestedCombo);
	    }

	}
	return comboList;
    }

    private static HashSet<String> getWordSet(String fileName) throws IOException
    {
	FileReader fileReader = new FileReader(fileName);
	LineNumberReader lineReader = new LineNumberReader(fileReader);

	HashSet<String> wordSet = new HashSet<String>();
	while (lineReader.ready())
	{
	    String line = lineReader.readLine().trim();
	    if (line.length() == 0)
	    {
		continue;
	    }
	    wordSet.add(line);
	}

	lineReader.close();
	fileReader.close();

	return wordSet;
    }
}
