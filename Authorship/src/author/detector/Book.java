/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package author.detector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 *
 * @author Willi
 */
public class Book {
    
    private String title;
    private String allWords;
    private String sentenceTerminator = "[?!\\.]+";
    private String wordTerminator = "[\\s\\.,?!;:\"()_]+";
    private String phraseTerminator = "[\\.,?!;:]+";
    

    public Book() {
    }

    public Book(String title, String allWords) {
        this.title = title;
        this.allWords = allWords.toLowerCase().replace("\n", " ").replace("\t", " ").replace("\u201C","\"").replace("\u201D", "\"").replace("--", ",");
    }

    @Override
    public String toString() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAllWords(String allWords) {
        allWords = allWords.toLowerCase().replace("\n", " ").replace("\t", " ").replace("\u201C","\"").replace("\u201D", "\"").replace("--", ",");
        this.allWords = allWords;
    }

    public String getAllWords() {
        return allWords;
    }

    public String getTitle() {
        return title;
    }
    
    public double calcAvgWordLength(){
        String[] words = allWords.split(wordTerminator);
        double sum = 0.0;
        for(String word:words){
            sum += word.length();
        }
        return sum/words.length;
    }
    
    public double calcTypeTokenRatio(){
        String[] words = allWords.split(wordTerminator);
        double numWords = words.length;
        Set<String> temp = new HashSet<String>(Arrays.asList(words));
        String[] set = temp.toArray(new String[temp.size()]);
        double setLength = set.length;
        return setLength/numWords;
    }
    
    public double calcHapaxLegoRatio(){
        ArrayList<String> words = new ArrayList<>(Arrays.asList(allWords.split(wordTerminator)));
        Collections.sort(words);
        ArrayList<String> singleOccWords = new ArrayList();
        double numWords = words.size();
        String previousWord = "";
        String currentWord = "";
        String nextWord = "";
        for(int x = 0;x<numWords-1;x++){
            if(x!=0){
                previousWord = words.get(x-1);
            } else{
                previousWord = "";
            }
            
            currentWord = words.get(x);
            if(x!=numWords){
                nextWord = words.get(x+1);
            } else{
                nextWord = "";
            }
            
            if(!previousWord.equals(currentWord)&!currentWord.equals(nextWord)){
                singleOccWords.add(words.get(x));
            }
        }
        double numSOWords = singleOccWords.size();
        return numSOWords/numWords;
    }
    
    public double calcAvgWordsPerSent(){
        ArrayList<String> sentences = new ArrayList<>(Arrays.asList(allWords.split(sentenceTerminator)));
        double sum = 0.0;
        for(String sentence:sentences){
            String[] words = sentence.split(wordTerminator);
            sum += words.length;
        }
        return sum/sentences.size();
    }
    
    public double calcSentComplexity(){
        ArrayList<String> sentences = new ArrayList<>(Arrays.asList(allWords.split(sentenceTerminator)));
        double sum = 0.0;
        for(String sentence:sentences){
            String[] phrases = sentence.split(phraseTerminator);
            sum += phrases.length;
        }
        return sum/sentences.size();
    }
   
    
}
