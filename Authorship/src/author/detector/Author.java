/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package author.detector;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Willi
 */
public class Author {
    private String name;
    private List<Book> books;
    private double numBooks;
    private double avgWordLength;
    private double typeTokenRatio;
    private double hapaxLegoRatio;
    private double avgWordPerSent;
    private double sentComplexity; 
    DecimalFormat df = new DecimalFormat("######.####");

    public Author() {
        books = new ArrayList<>();
        this.numBooks = books.size();
        this.avgWordLength = 0.0;
        this.typeTokenRatio = 0.0;
        this.hapaxLegoRatio = 0.0;
        this.avgWordPerSent = 0.0;
        this.sentComplexity = 0.0;
    }

    public Author(String name, int numBooks,List<Book> books, double avgWordLength, double typeTokenRatio, double hapaxLegoRatio, double avgWordPerSent, double sentComplexity) {
        this.name = name;
        this.books = books;
        this.numBooks = numBooks;
        this.avgWordLength = avgWordLength;
        this.typeTokenRatio = typeTokenRatio;
        this.hapaxLegoRatio = hapaxLegoRatio;
        this.avgWordPerSent = avgWordPerSent;
        this.sentComplexity = sentComplexity;
    }
    
    public Author(ArrayList<String> line) {
        this.name = line.get(0);
        this.books = new ArrayList<>();
        this.numBooks = Double.parseDouble(line.get(1));
        this.avgWordLength = Double.parseDouble(line.get(2));
        this.typeTokenRatio = Double.parseDouble(line.get(3));
        this.hapaxLegoRatio = Double.parseDouble(line.get(4));
        this.avgWordPerSent = Double.parseDouble(line.get(5));
        this.sentComplexity = Double.parseDouble(line.get(6));
    }

    public List<Book> getBooks() {
        return books;
    }
    
    public double getBookNum(){
        return numBooks;
    }

    public String getName() {
        return name;
    }

    public double getAvgWordLength() {
        return avgWordLength;
    }

    public double getAvgWordPerSent() {
        return avgWordPerSent;
    }

    public double getTypeTokenRatio() {
        return typeTokenRatio;
    }

    public double getHapaxLegoRatio() {
        return hapaxLegoRatio;
    }

    public double getSentComplexity() {
        return sentComplexity;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void addBook(Book myBook){
        this.books.add(myBook);
        double oldNumber = numBooks;
        this.numBooks += 1.0;
            avgWordLength = ((avgWordLength * oldNumber) + myBook.calcAvgWordLength()) / numBooks;
            typeTokenRatio = ((typeTokenRatio * oldNumber) + myBook.calcTypeTokenRatio()) / numBooks;
            hapaxLegoRatio = ((hapaxLegoRatio * oldNumber) + myBook.calcHapaxLegoRatio()) / numBooks;
            avgWordPerSent = ((avgWordPerSent * oldNumber) + myBook.calcAvgWordsPerSent()) / numBooks;
            sentComplexity = ((sentComplexity * oldNumber) + myBook.calcSentComplexity()) / numBooks;
    }
    
    public String features2String(){
        return " " + numBooks + " " + df.format(avgWordLength) + " " + df.format(typeTokenRatio) + " " + df.format(hapaxLegoRatio) + " " + df.format(avgWordPerSent) + " " + df.format(sentComplexity) + "\r\n";
    }
    
//    public void calcSignature(){
//        if(numBooks!=0){ 
//            for(Book currentBook : books){
//                avgWordLength += currentBook.calcAvgWordLength();
//                typeTokenRatio += currentBook.calcTypeTokenRatio();
//                hapaxLegoRatio += currentBook.calcHapaxLegoRatio();
//                avgWordPerSent += currentBook.calcAvgWordsPerSent();
//                sentComplexity += currentBook.calcSentComplexity();
//            }
//            avgWordLength = avgWordLength/numBooks;
//            typeTokenRatio = typeTokenRatio/numBooks;
//            hapaxLegoRatio = hapaxLegoRatio/numBooks;
//            avgWordPerSent = avgWordPerSent/numBooks;
//            sentComplexity = sentComplexity/numBooks;
//        }
//    }
    
    
    
    
}
