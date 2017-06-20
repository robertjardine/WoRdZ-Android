package gaiginja.wordz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Robert Jardine
 * Press letters to form a word. If the word is
 * a valid English word, points will be awarded.
 * Hints are availiable. They will show words that
 * can be made with the current letters given.
 *
 */

public class ChooseLetter extends AppCompatActivity {
    EditText userInput;
    TextView score;
    private ArrayList<String> dict = new ArrayList<>();
    private Set<String> possibleWords = new HashSet<>();
    private Map<Character, Integer> charMap = new HashMap<>();
    private ArrayList<Button> buttonArray = new ArrayList<>();
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    private static final String LETTERFREQ = "aaaaaaaaabbccddddeeeeeeeeeeeeffggghhiiiiiiiiijkllllmmnnnnnnooooooooppqrrrrrrssssttttttuuuuvvwwxyyz";
    private static final String FILENAME = "dictionary.txt";

    //Objects for each input button
    Button letter1, letter2, letter3, letter4,
        letter5, letter6, letter7, letter8, letter9, letter10,
        letter11, letter12, letter13, letter14, letter15, letter16,
        letter17, letter18, letter19, letter20, letter21, submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_letter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userInput = (EditText) findViewById(R.id.userInput);
        score = (TextView) findViewById(R.id.score);
        submitButton = (Button) findViewById(R.id.submit);
        submitButton.setBackgroundColor(Color.RED);

        //Try-with-resouces not supported by older versions of Android
        try {
            BufferedReader fileRead = new BufferedReader(new InputStreamReader(getAssets().open(FILENAME)));
            String line = fileRead.readLine();
            //Initialize Words in Dictionary
            while(line != null){
                dict.add(line);
                line = fileRead.readLine();
            }
            fileRead.close();
        } catch (FileNotFoundException fnfe) {
            Log.e("onCreate", "message=invalidFile fileNotFoundException=" + fnfe.getMessage());
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            Log.d("onCreate", "message=errorClosingStream ioException=" + ioe.getMessage());
            ioe.printStackTrace();
        }

        //Initilize Button objects
        //Keep track of buttons for later use by putting them in an ArrayList
        letter1 = (Button)findViewById(R.id.letter1);
        buttonArray.add(letter1);
        letter2 = (Button)findViewById(R.id.letter2);
        buttonArray.add(letter2);
        letter3 = (Button)findViewById(R.id.letter3);
        buttonArray.add(letter3);
        letter4 = (Button)findViewById(R.id.letter4);
        buttonArray.add(letter4);
        letter5 = (Button)findViewById(R.id.letter5);
        buttonArray.add(letter5);
        letter6 = (Button)findViewById(R.id.letter6);
        buttonArray.add(letter6);
        letter7 = (Button)findViewById(R.id.letter7);
        buttonArray.add(letter7);
        letter8 = (Button)findViewById(R.id.letter8);
        buttonArray.add(letter8);
        letter9 = (Button)findViewById(R.id.letter9);
        buttonArray.add(letter9);
        letter10 = (Button)findViewById(R.id.letter10);
        buttonArray.add(letter10);
        letter11 = (Button)findViewById(R.id.letter11);
        buttonArray.add(letter11);
        letter12 = (Button)findViewById(R.id.letter12);
        buttonArray.add(letter12);
        letter13 = (Button)findViewById(R.id.letter13);
        buttonArray.add(letter13);
        letter14 = (Button)findViewById(R.id.letter14);
        buttonArray.add(letter14);
        letter15 = (Button)findViewById(R.id.letter15);
        buttonArray.add(letter15);
        letter16 = (Button)findViewById(R.id.letter16);
        buttonArray.add(letter16);
        letter17 = (Button)findViewById(R.id.letter17);
        buttonArray.add(letter17);
        letter18 = (Button)findViewById(R.id.letter18);
        buttonArray.add(letter18);
        letter19 = (Button)findViewById(R.id.letter19);
        buttonArray.add(letter19);
        letter20 = (Button)findViewById(R.id.letter20);
        buttonArray.add(letter20);
        letter21 = (Button)findViewById(R.id.letter21);
        buttonArray.add(letter21);

        //Initialize the Text on the Letter Buttons
        initializeButtonText(buttonArray);

        //Initialize point values for each letter
        initializeLetterValues();
    }

    /**
     * Method called when a letter from the pool is clicked on.
     * Image will become transparent and associated char will be added to answer.
     * @param v - Holds details of the object that was clicked.
     */
    public void letterClicked(View v) {
        Button buttonClicked = (Button) findViewById(v.getId());
        String nextChar = (String) buttonClicked.getText();
        Log.d("letterClicked", "nextChar=" + nextChar);
        //Add the character associated to the button clicked
        String newInput = userInput.getText() + nextChar;
        userInput.setText(newInput);
        Log.d("letterClicked", "newInput=" + userInput.getText());
        //Change the color to green when the first letter is entered
        if( userInput.length() == 1 ){
            submitButton.setBackgroundColor(Color.GREEN);
        }

        // Remove letter Once it ss chosen
        buttonClicked.setVisibility(View.GONE);
    }

    /**
     * Check if the entered input is valid.
     * If it is, then award points. Else, put the letters back.
     */
    public void submitClicked(View v) {
        String submitInput = String.format("%s", userInput.getText());
        Log.d("submitClicked", "submitInput=" + submitInput);
        if(inDict(submitInput)){
            String currScoreString = (String)score.getText();
            Log.d("submitClicked", "currScore=" + currScoreString);
            int currScore = Integer.parseInt(currScoreString);
            int newScore = getPoints(submitInput) + currScore;
            score.setText(String.format("%d", newScore));
            Log.d("submitClicked", "newScore=" + score.getText());
        } else {
            for(int i=0; i<submitInput.length(); i++){
                Log.d("submitClicked", "message=revertCharacter BEGIN LOOP");
                for(Button button : buttonArray) {
                    if( (button.getText().equals(String.format("%s", submitInput.charAt(i))))
                        && (button.getVisibility() != View.VISIBLE)){
                        Log.d("submitClicked", "message=revertCharacter character=" + submitInput.charAt(i));
                        button.setVisibility(View.VISIBLE);
                        //Only one button will come back per matching letter
                        break;
                    }
                }
            }
        }
        userInput.setText("");
        submitButton.setBackgroundColor(Color.RED);
    }

    /**
     * Reset all variables for a new game.
     * @param v - Holds the properties of the restart button
     */
    public void restart(View v){
        initializeButtonText(buttonArray);
        for(Button button : buttonArray) {
            if(button.getVisibility() != View.VISIBLE){
                button.setVisibility(View.VISIBLE);
            }
        }
        score.setText("0");
        userInput.setText("");
        submitButton.setBackgroundColor(Color.RED);
    }

    /**
     * Displays all valid words that can be
     * created with the remaining letters.
     * @param v - Holds properties of the Hint button.
     */
    public void showHint(View v) {
        String remainingLetters = "";
        for(Button button : buttonArray){
            if(button.getVisibility() == View.VISIBLE){
                remainingLetters = remainingLetters + button.getText();
            }
        }
        Log.d("showHint", "remainingLetters=" + remainingLetters);
        getAllValidWords(remainingLetters, "");
        List<String> possibleWordsList = new ArrayList<>(possibleWords);
        Collections.sort(possibleWordsList);
        Log.d("showHint", "possibleWords=" + possibleWordsList);

        AlertDialog.Builder hint = new AlertDialog.Builder(this);
        hint.setPositiveButton("GOT IT!",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface text, int num) {
                        //Just close dialogue when pressed
                    }
                });

        StringBuilder message = new StringBuilder("");
        for(String word : possibleWordsList) {
            message.append(word);
            message.append("\n");
        }
        if(message.toString().equals("")){
            hint.setMessage("NO MATCHES FOUND!");
        } else {
            hint.setMessage(message.toString());
        }
        AlertDialog hintMessage = hint.create();
        hintMessage.show();

        possibleWords.clear();
    }

    /**
     * Initialize the letters that will be displayed on the buttons in the pool of letters
     * @param buttonArray - array of button objects that will hold a single letter on the screen
     */
    private void initializeButtonText(ArrayList<Button> buttonArray){
        for(Button button : buttonArray){
            Random randNum = new Random();
            //Add random letter from LETTERFREQ to current button
            int stringIndex = randNum.nextInt(LETTERFREQ.length()-1);
            button.setText(String.format("%s", LETTERFREQ.charAt(stringIndex)));
        }
    }

    /**
     * Assign a point value to all letters.
     * This information is stored in the Hashmap variable "charMap"
     * Will be used when adding points after a successful word is entered.
     */
    private void initializeLetterValues() {
        for(int i=0; i<ALPHABET.length(); i++){
            switch(ALPHABET.charAt(i)) {
                case 'a':
                    charMap.put('a', 1);
                case 'b':
                    charMap.put('b', 3);
                case 'c':
                    charMap.put('c', 3);
                case 'd':
                    charMap.put('d', 2);
                case 'e':
                    charMap.put('e', 1);
                case 'f':
                    charMap.put('f', 4);
                case 'g':
                    charMap.put('g', 2);
                case 'h':
                    charMap.put('h', 4);
                case 'i':
                    charMap.put('i', 1);
                case 'j':
                    charMap.put('j', 8);
                case 'k':
                    charMap.put('k', 5);
                case 'l':
                    charMap.put('l', 1);
                case 'm':
                    charMap.put('m', 3);
                case 'n':
                    charMap.put('n', 1);
                case 'o':
                    charMap.put('o', 1);
                case 'p':
                    charMap.put('p', 3);
                case 'q':
                    charMap.put('q', 10);
                case 'r':
                    charMap.put('r', 1);
                case 's':
                    charMap.put('s', 1);
                case 't':
                    charMap.put('t', 1);
                case 'u':
                    charMap.put('u', 1);
                case 'v':
                    charMap.put('v', 4);
                case 'w':
                    charMap.put('w', 4);
                case 'x':
                    charMap.put('x', 8);
                case 'y':
                    charMap.put('y', 4);
                case 'z':
                    charMap.put('z', 10);
            }
        }
    }

    /**
     * Check if the entered word is a valid English word
     * @param userInput - Word entered by the user
     * @return - If the word is valid.
     */
    private boolean inDict(String userInput){
        return dict.contains(userInput);
    }

    /**
     * Binary Search of the dictionary for a certain string.
     * Used to improve the execution time of the Hint search.
     * @param word - Current String being checked
     * @return - boolean: Whether a prefix is found in the dictionary.
     */
    private boolean binarySearch(String word){
        int max = dict.size() - 1;
        int min = 0;
        int currIndex;
        boolean result = false;
        while(min <= max) {
            currIndex = (min + max) / 2;
            if (dict.get(currIndex).startsWith(word)) {
                result = true;
                break;
            } else if (dict.get(currIndex).compareTo(word) < 0) {
                min = currIndex + 1;
            } else if(dict.get(currIndex).compareTo(word) > 0){
                max = currIndex - 1;
            } else {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * Use the pre-determined points per letter to
     * calculate the value of the new word entered.
     * @param input - String to analyze.
     * @return - Point value of the word.
     */
    private int getPoints(String input){
        int result = 0;
        for(int i=0; i<input.length(); i++){
            int letterValue = charMap.get(input.charAt(i));
            result += letterValue;
        }
        Log.d("getPoints", "totalPointsAdded=" + result);
        return result;
    }

    private void getAllValidWords(String letterPool, String currWord) {
        //Add to possibleWords when valid word
        if (letterPool.equals("")) {
           // No action to be done.
        } else if(currWord.equals("")){
            //Will run only the initial time the method is called.
            for (int i = 0; i < letterPool.length(); i++) {
                //Get the individual letters that will become the first letter of a word
                String curr = letterPool.substring(i, i+1);
                //Delete the single letter from letterPool
                String newLetterPool = (letterPool.substring(0, i) + letterPool.substring(i+1));
                if(inDict(curr)){
                    possibleWords.add(curr);
                }
                boolean prefixInDic = binarySearch(curr);
                if(prefixInDic){
                    //If the prefix isn't found, don't continue this recursive path.
                    getAllValidWords(newLetterPool, curr);
                }
            }
        } else {
            //Every time we add a letter to currWord, delete from letterPool
            for(int i=0; i<letterPool.length(); i++){
                String curr = currWord + letterPool.substring(i, i+1);
                String newLetterPool = (letterPool.substring(0, i) + letterPool.substring(i+1));
                if(inDict(curr)){
                    possibleWords.add(curr);
                }
                boolean prefixInDic = binarySearch(curr);
                if(prefixInDic){
                    //If the prefix isn't found, don't continue this recursive path.
                    getAllValidWords(newLetterPool, curr);
                }
            }
        }
    }
}
