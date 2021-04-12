import edu.duke.*;
import org.apache.commons.csv.*;
/**
 * Write a description of WhatsMyName here.
 * 
 * @author Ryan Dsouza 
 * @version 0.1
 */


public class WhatsMyName {
    public void totalBirths(FileResource fr) {
        int totalFemaleBirths = 0, totalMaleBirths = 0, totalBirths = 0;

        for (CSVRecord record : fr.getCSVParser(false)) {
            
            if (record.get(1).equals("F")){
                totalFemaleBirths += Integer.parseInt(record.get(2));
            } else {
                totalMaleBirths += Integer.parseInt(record.get(2));
            }   
        }
        totalBirths = totalFemaleBirths + totalMaleBirths;
        System.out.println("Total Births is " + totalBirths +
                            "\nTotal Female Births is " + totalFemaleBirths +
                            "\nTotal Male Births is " + totalMaleBirths);
    }    
    
    public void testTotalBirths() {
        FileResource fr = new FileResource("../Data Sets/us_babynames_test/yob2012short.csv");
        totalBirths(fr);
    }
}
