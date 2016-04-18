package com.example.koshvocabapp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class AppScope extends Application 
{
	private static AppScope singleton;
	private static Context context;
	private static BufferedReader dictionaryReader;
	private static BufferedReader lastWordReader;
	private static BufferedWriter lastWordwriter;
	private String currentLine = null;
	private String[] wordTokens = null;
	private static String currentWord;
	private static String currentMeaning;
	private static String lastSpelledWord;
	public static final String DICTIONARY_FILE_ON_PHONE = "dict_new.txt";
	public static final String DICTIONARY_FILE_IN_APK = "dict.txt";
	public static final String LAST_WORD_FILE = "currentWord_new.txt";
	private File dictFile, lastWordFile;

	public AppScope getInstance(){
		return singleton;
	}

	/*
	 * Load the last word user spelled and move the index to that word in the dictionary file
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		singleton = this;
		context = getApplicationContext();
		// Transfer the dictionary words to the phone storage from APK
		String line = "Kosha=Now it works";
		int temp = 1;
		String tempLast="dummy";
		BufferedWriter phoneWriter;
		/*
		 * If the dictionary file does not exist on phone, create from APK
		 */
		
		// open the file on phone to overwrite with dictionary file in APK
		try {
			File file = new File(context.getExternalFilesDir(null),DICTIONARY_FILE_ON_PHONE);
			dictionaryReader = new BufferedReader(new InputStreamReader(getAssets().open(DICTIONARY_FILE_IN_APK)));
			phoneWriter = new BufferedWriter(new FileWriter(file));
			while((line = dictionaryReader.readLine()) != null)
			{

				line = line + "\n";
				if(temp == 1)
				{
					String[] wordTokens = line.split("=");
					tempLast = wordTokens[0];
				}
				phoneWriter.write(line);
			}
			phoneWriter.close();
			dictionaryReader.close();
		} catch ( IOException e ) {
			System.out.println("Error creating the file - " + DICTIONARY_FILE_ON_PHONE + " - "+ e.toString());
		}
		// Create the file for last word on phone
		try {
			// open the file on phone to overwrite with dictionary file in APK
			File file = new File(context.getExternalFilesDir(null),LAST_WORD_FILE);
			phoneWriter = new BufferedWriter(new FileWriter(file));

			phoneWriter.write(tempLast);
			phoneWriter.close();
		} catch ( IOException e ) {
			System.out.println("Error creating the file - " + LAST_WORD_FILE + " - "+ e.toString());
		}
	}

	public static Context getAppContext() {
		return AppScope.context;
	}

	/*
	 * Write the last word user spelled to the file
	 */
	public void onDestroy() {
		writeLastSpelledWordToPhone(this.lastSpelledWord);
	}

	//************************** START - Dictionary File methods *****************************  

	/*
	 * Seek up to the last spelled word
	 */
	public void seekUptoLastSpelledWord() throws Exception
	{
		while(lastSpelledWord.compareToIgnoreCase(currentWord)!=0)
		{
			advanceDictionaryWord();
		}
	}

	/*
	 * Get the next word from the file and update the current word variables
	 */
	public void advanceDictionaryWord() throws Exception
	{
		try{
			if ((currentLine = dictionaryReader.readLine()) != null) {
				wordTokens = currentLine.split("=");
				currentWord = wordTokens[0];
				currentMeaning = wordTokens[1];
			}else
			{
				// eof reached. Show the message
				throw new Exception("All words are over !");
			}
		}catch (Exception e){
			System.out.println("Error - " + e.toString());
		}
	}


	/*
	 * Return latest dictionary word
	 */
	public String getCurrentDictionaryWord() throws Exception
	{
		return this.currentWord;
	}

	/*
	 * Return latest word's meaning
	 */
	public String getCurrentWordMeaning() throws Exception
	{
		return this.currentMeaning;
	}

	/*
	 * Open the dictionary file for playing game
	 * TODO: make the file opening and closing generic
	 */
	public void openDictionaryFileOnPhone()
	{
		try
		{
			dictFile = new File(context.getExternalFilesDir(null),DICTIONARY_FILE_ON_PHONE);
			dictionaryReader = new BufferedReader(new FileReader(dictFile));
		}catch (Exception e)
		{
			System.out.println("Error opening the file - " + DICTIONARY_FILE_ON_PHONE + " - "+ e.toString());
		}
	}


	/*
	 * Close the dictionary file
	 * TODO: make the file opening and closing generic
	 */
	public void closeDictionaryFileOnPhone()
	{
		try
		{
			dictionaryReader.close();
		}catch (Exception e)
		{
			System.out.println("Error closing the file - " + e.toString());
		}
	}

	/*
	 * TODO: make the file opening and closing generic
	 */
	// reopen dictionary file
	public void reopenDictionaryFileOnPhone()
	{
		closeDictionaryFileOnPhone();
		openDictionaryFileOnPhone();
	}

	/*
	 ************************** START - Last spelled word File methods *****************************  
	 */

	/*
	 * Open the last spelled word file
	 * TODO: make the file opening and closing generic
	 */
	public void openLastWordFile()
	{
		try
		{
			lastWordFile = new File(context.getExternalFilesDir(null),LAST_WORD_FILE);
			lastWordReader = new BufferedReader(new FileReader(LAST_WORD_FILE));
		}catch (Exception e)
		{
			System.out.println("Error opening the file - " + LAST_WORD_FILE + " - "+ e.toString());
		}
	}

	/*
	 * write the lastspelled word to the file
	 * TODO: make the file opening and closing generic
	 */
	public void writeLastSpelledWordToPhone(String lastSpelledWord)
	{
		// update the current word
		try
		{
			BufferedWriter phoneWriter;
			// open the file on phone to overwrite with dictionary file in APK
			File file = new File(context.getExternalFilesDir(null),LAST_WORD_FILE);
			phoneWriter = new BufferedWriter(new FileWriter(file));
			phoneWriter.write(this.lastSpelledWord);
			phoneWriter.close();
		}catch (Exception e)
		{
			System.out.println("Error writing the last word to file - " + LAST_WORD_FILE + " - "+ e.toString());
		}
	}

	/*
	 * Close the last spelled word file
	 * TODO: make the file opening and closing generic
	 */
	public void closeLastWordFile()
	{
		try
		{
			lastWordReader.close();
		}catch (Exception e)
		{
			System.out.println("Error closing the file - " + e.toString());
		}
	}

	/*
	 * TODO: make the file opening and closing generic
	 */
	// reopen the last spelled word file
	public void reopenLastWordFile()
	{
		closeLastWordFile();
		openLastWordFile();
	}

	/*
	 * Get the last spelled word from file
	 */
	public String getLastSpelledWordFromFileOnPhone() throws Exception
	{
		try{
			this.lastSpelledWord = lastWordReader.readLine();
		}catch (Exception e){
			System.out.println("Error reading last spelled words from the file - " + e.toString());
		}
		return this.lastSpelledWord;
	}

	/*
	 * Get the last spelled word
	 */
	public String getLastSpelledWord() throws Exception
	{
		return this.lastSpelledWord;
	}

	/*
	 * Get the last spelled word
	 */
	public void setLastSpelledWord(String word) throws Exception
	{
		this.lastSpelledWord = this.getCurrentDictionaryWord();
	}

} // End of class
