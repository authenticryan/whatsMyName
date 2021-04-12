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
        System.out.println("Running totalBirths()\n");
        totalBirths(fr);
        System.out.println("\nExpected output - Total Births - 73. Female Births - 40, Male Births - 33");
    }
    
    // returns rank of a given name, from a given year. Returns -1 if no name is availabe in that year
    public int getRank(int searchYear, String searchName, String searchGender) {
        FileResource fr = new FileResource("../Data Sets/us_babynames_by_year/yob"+searchYear+".csv");
        int index = 0;
        for (CSVRecord record : fr.getCSVParser(false)) {
            if(record.get(1).equalsIgnoreCase(searchGender)) {
                index++;
                if(record.get(0).equalsIgnoreCase(searchName)) {
                    return index;
                }
            }
        }
        
        return -1;
    }
    
    public void testGetRank() {
        int testRank = getRank(1930, "Myriam", "F");
        System.out.println("Expected result for Myrian, Female in 1930 -> 3934. getRank() Function Returned -> " + testRank);
        
        testRank = getRank(1930, "Zoma", "F");
        System.out.println("Expected result for Zoma, Female in 1930 -> 5248. getRank() Function Returned -> " + testRank);
        
        
        testRank = getRank(1930, "Robert", "M");
        System.out.println("Expected result for Robert, Male in 1930 -> 1. getRank() Function Returned -> " + testRank);
        
        testRank = getRank(1930, "Json", "M");
        System.out.println("Expected result for Json, Male in 1930 -> -1. getRank() Function Returned -> " + testRank);

    }
    
    // return name of the individual, at corresponding ranking in Gender 
    public String getName(int searchYear, int searchRank, String searchGender){
        FileResource fr = new FileResource("../Data Sets/us_babynames_by_year/yob"+searchYear+".csv");
        int index = 0;
        
        for (CSVRecord record : fr.getCSVParser(false)) {
            if(record.get(1).equalsIgnoreCase(searchGender)) {
                index++;
                if(index == searchRank)
                    return record.get(0);
            }
        }
        return "No Name Found";
    }
    
    public void testGetName() {
        String testName;
        
        System.out.println("Beginning testing getName()\n");
        
        testName = getName(1930, 12, "F");
        System.out.println("Expected Virginia, Female at 12th Rank. Received -> " + testName);
        
        
        testName = getName(1930, 5248, "F");
        System.out.println("Expected Zoma, Female at 5248th Rank. Received -> " + testName);
        
        
        testName = getName(1930, 12, "M");
        System.out.println("Expected Paul, Male at 12th Rank. Received -> " + testName);
        
    }
}
