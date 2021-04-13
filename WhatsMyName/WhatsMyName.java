import java.io.File;
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
    
    // returns rank of a given name, from a given FileResource. Returns -1 if no name is availabe in that year
    public int getRankLogic(FileResource fr, String searchName, String searchGender) {
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
    
    // returns rank of a given name, from a given year. Returns -1 if no name is availabe in that year
    public int getRank(int searchYear, String searchName, String searchGender) {
        FileResource fr = new FileResource("../Data Sets/us_babynames_by_year/yob"+searchYear+".csv");
        return getRankLogic(fr, searchName, searchGender);
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
    
    public void whatIsNameInYear(String birthName, int birthYear, int newYear, String gender) {
        int rankAtBirthYear = getRank(birthYear, birthName, gender);
        String nameAtNewYear = null;
        if (rankAtBirthYear != -1) {
            nameAtNewYear = getName(newYear, rankAtBirthYear, gender);
            if(!nameAtNewYear.equals("No Name Found"))
                System.out.println(birthName + " born in " + birthYear + " would be " + nameAtNewYear + " if he was born in " + newYear);
            else 
                System.out.println("Rank Out of Bounds");
        } else {
            System.out.println("Birth Name not available in BirthYear Dataset");
        }
    }
    
    public void testWhatIsNameInYear() {
        whatIsNameInYear("Ryan", 1997, 1930, "M");
        System.out.println("Expected Raymond\n");
        
        whatIsNameInYear("Jennifer", 1997, 1930, "F");
        System.out.println("Expected Joyce\n");
        
        
        whatIsNameInYear("Not A name", 1997, 1930, "M");
        System.out.println("Expected error indicating birth name was not available in birth year dataset\n");
                
        whatIsNameInYear("Zykerria", 1997, 1930, "F");
        System.out.println("Expected error indicating similar name not available in the new year dataset\n");
    }
    
    // return the year, where searchName was ranked highest. Returns -1 if searchName was not found in any files. Lower the rank, the better.
    public int yearOfHighestRank(String searchName, String searchGender) {
        int lowestRank = -1, yearOfHighestRank = 0;
        String fileWithHighestRank = null;
        
        DirectoryResource dr = new DirectoryResource();
        for(File f : dr.selectedFiles()) {
            FileResource fr = new FileResource(f);
            int currentRank = getRankLogic(fr, searchName, searchGender);
            if (currentRank != -1) {
                if (lowestRank == -1) {
                    lowestRank = currentRank;
                    fileWithHighestRank = f.getName();
                } 
                if (currentRank < lowestRank) {
                    lowestRank = currentRank;
                    fileWithHighestRank = f.getName();
                }
            }
        }
        
        if (fileWithHighestRank != null) 
            yearOfHighestRank = Integer.parseInt(fileWithHighestRank.substring(3,7));
        else 
            yearOfHighestRank = -1;
            
        return yearOfHighestRank;
    }
    
    public void testYearOfHighestRank() {
        System.out.println("Plesae open files yob2012short.csv till yob2014short.csv from whatsMyName\\Data Sets\\us_babynames_test");
        
        // test for male
        System.out.println("Running test for Mason, Male");
        int testOutcome = yearOfHighestRank("Mason", "M");
        System.out.println("Expected Outome -> 2012. Returned -> " + testOutcome);

        // test for female
        System.out.println("Running test for Olivia, Female");
        testOutcome = yearOfHighestRank("Olivia", "F");
        System.out.println("Expected Outome -> 2014. Returned -> " + testOutcome);        
        
        // test for non existing name
        System.out.println("Running test for Ryan, Male");
        testOutcome = yearOfHighestRank("Ryan", "M");
        System.out.println("Expected Outome -> -1. Returned -> " + testOutcome);
        
    }
}
