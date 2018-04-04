package author.detector;

import java.io.File;
import java.io.IOException;
import static java.lang.Math.abs;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class GUIController {
    ArrayList<Author> authors = new ArrayList();
    int currentAuthorIndex = 0;
    String signatureHeaders = "Author Books AWL TTR HLR AWS SC\r\n";

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button file_ex;

    @FXML
    private TextArea authorSignatures;

    @FXML
    private TextField currentAuthor;

    @FXML
    private Label book_status;

    @FXML
    private TextArea mysteryResults;

    @FXML
    void change_author(ActionEvent event) {
        String name = currentAuthor.getText();
        currentAuthorIndex = findIndex(name);
        if(currentAuthorIndex == -1){
            Author newAuthor = new Author();
            newAuthor.setName(name);
            authors.add(newAuthor);
            currentAuthorIndex = authors.indexOf(newAuthor);
        }
    }

    @FXML
    void determine_mystery_author(ActionEvent event) {
        File myFile = chooseFile();
        Book myBook = processBook(myFile);
        mysteryResults.setText(signatureHeaders+scoreBook(myBook));
    }

    @FXML
    void display_signatures(ActionEvent event) {
        String body = "";
        for(Author x : authors){
            body += x.getName() + x.features2String();
        }
        authorSignatures.setText(signatureHeaders+body);
    }

    @FXML
    void load_signatures(ActionEvent event) {
        File myFile = chooseFile();
        Path path = Paths.get(myFile.getAbsolutePath());
        String alllines = "";
        String fileName = "";
        
        try{
            alllines = new String(Files.readAllBytes(path));             
            fileName = myFile.getName();
        } catch(IOException e){
        }  
        ArrayList<String> lines = new ArrayList<>(Arrays.asList(alllines.split("\r\n")));
        for(String line : lines){
            Author newGuy;
            ArrayList<String> singleLine = new ArrayList<>(Arrays.asList(line.split(" ")));
            newGuy = new Author(singleLine);
            authors.add(newGuy);
        }
    }

    @FXML
    void save_signatures(ActionEvent event) {
        String body = "";
        for(Author x : authors){
            body += x.getName() + x.features2String();
        }
        saveFile("Author_Signatures",body);
    }

    @FXML
    void select_book(ActionEvent event) {
        File myFile = chooseFile();
        Book myBook = processBook(myFile);
        authors.get(currentAuthorIndex).addBook(myBook);
        
        
    }

    @FXML
    void initialize() {
        assert file_ex != null : "fx:id=\"file_ex\" was not injected: check your FXML file 'GUI.fxml'.";
        assert authorSignatures != null : "fx:id=\"authorSignatures\" was not injected: check your FXML file 'GUI.fxml'.";
        assert currentAuthor != null : "fx:id=\"currentAuthor\" was not injected: check your FXML file 'GUI.fxml'.";
        assert book_status != null : "fx:id=\"book_status\" was not injected: check your FXML file 'GUI.fxml'.";
        assert mysteryResults != null : "fx:id=\"mysteryResults\" was not injected: check your FXML file 'GUI.fxml'.";

    }
    
    private File chooseFile(){
        FileChooser fc = new FileChooser();
        fc.setTitle("View Files");
        fc.getExtensionFilters().addAll(
                new ExtensionFilter(
                    "Text Files",
                    "*.txt"),
                new ExtensionFilter(
                    "All Files",
                    "*.*"));
        fc.setInitialDirectory(
                new File(System.getProperty("user.home"))
//                new File("D:\\Users\\Willi\\Downloads\\Good to Delete")
        );
        File myFile = fc.showOpenDialog(null);
        return myFile;
    }

    private Book processBook(File myFile) { 
        Book blackBook = new Book();
        String alllines = "";
        String fileName = "";
        Path path = Paths.get(myFile.getAbsolutePath());
        
        try{
            alllines = new String(Files.readAllBytes(path), Charset.forName("UTF-8"));             
            fileName = myFile.getName();
            book_status.setText(fileName + " Read");
        } catch(IOException e){
            book_status.setText("Failure");
        }
        
        blackBook.setTitle(fileName);
        blackBook.setAllWords(alllines);
        return blackBook;
    }

    private int findIndex(String name) {
        ArrayList<String> names = new ArrayList();
        for(Author x : authors){
            if(x.getName().equals(name)){
                return authors.indexOf(x);
            }
        }
        return -1;
    }

    private void saveFile(String author_Signatures, String allText) {
        Path file = Paths.get(System.getProperty("user.home")+"\\author_Signatures.txt");
        try {
            Files.write(file, allText.getBytes());
        } catch (IOException ex) {
            Logger.getLogger(GUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String scoreBook(Book myBook) {
        Author mysteryAuthor = new Author();
        Author closesAuthor = new Author();
        String body = "";
        mysteryAuthor.setName("Unknown");
        mysteryAuthor.addBook(myBook);
        double diff = 9999.0;
        for(Author x : authors){
            if(diff>=calcDiff(x,mysteryAuthor)){
                diff = calcDiff(x,mysteryAuthor);
                closesAuthor = x;
            }
        }
        body = mysteryAuthor.getName() + mysteryAuthor.features2String()+closesAuthor.getName() + closesAuthor.features2String()+"Difference Score of: "+ diff + "\n" +myBook.getTitle();
        return body;
    }

    private double calcDiff(Author author1, Author author2) {
        Double diff = 0.0;
        ArrayList<Double> features1 = new  ArrayList();
        ArrayList<Double> features2 = new  ArrayList();
        ArrayList<Double> weights = new  ArrayList();
        weights.addAll(Arrays.asList(1.0,75.0,85.0,1.255,4.0));
        
        features1.add(author1.getAvgWordLength());
        features1.add(author1.getTypeTokenRatio());
        features1.add(author1.getHapaxLegoRatio());
        features1.add(author1.getAvgWordPerSent());
        features1.add(author1.getSentComplexity());
        
        features2.add(author2.getAvgWordLength());
        features2.add(author2.getTypeTokenRatio());
        features2.add(author2.getHapaxLegoRatio());
        features2.add(author2.getAvgWordPerSent());
        features2.add(author2.getSentComplexity());
        
        for(int x = 0;x<=weights.size()-1;x++){
            double first = features1.get(x);
            double second = features2.get(x);
            double weight = weights.get(x);
            diff += abs(first - second)*weight;
        }
        return diff;
        
    }
    
    
}

