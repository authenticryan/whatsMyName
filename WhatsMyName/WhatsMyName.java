import java.io.File;
import edu.duke.*;
import org.apache.commons.csv.*;
/**
 * Write a description of WhatsMyName here.
 * 
 * @author Ryan Dsouza 
 * @version 1.0
 * Signed off by Ryan as Stable and free of defects
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

    public void getTotalBirths(){
        FileResource fr = new FileResource();
        totalBirths(fr);
    }

    public void testTotalBirths() {
        FileResource fr = new FileResource("../Data Sets/us_babynames_test/yob2012short.csv");
        System.out.println("Running totalBirths()\n");
        totalBirths(fr);
        System.out.println("\nExpected output - Total Births - 73. Female Births - 40, Male Births - 33");
    }

    public void totalNames(FileResource fr) {
        int totalFemaleNames = 0, totalMaleNames = 0, totalNames = 0;

        for (CSVRecord record : fr.getCSVParser(false)) {

            if (record.get(1).equals("F")){
                totalFemaleNames++;
            } else {
                totalMaleNames++;
            }   
        }
        totalNames = totalFemaleNames + totalMaleNames;
        System.out.println("Total Births is " + totalNames +
            "\nTotal Female Births is " + totalFemaleNames +
            "\nTotal Male Births is " + totalMaleNames);
    }

    public void getTotalNames() {
        FileResource fr = new FileResource();
        totalNames(fr);
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
        System.out.println("\nRunning test for Olivia, Female");
        testOutcome = yearOfHighestRank("Olivia", "F");
        System.out.println("Expected Outome -> 2014. Returned -> " + testOutcome);        

        // test for non existing name
        System.out.println("\nRunning test for Ryan, Male");
        testOutcome = yearOfHighestRank("Ryan", "M");
        System.out.println("Expected Outome -> -1. Returned -> " + testOutcome);

    }

    // returns average rank of the name in the dataset. If name not found, returns -1
    public double getAverageRank(String searchName, String searchGender) {
        double averageRank = 0;
        int cumulativeRank = 0, numberOfRanks = 0;

        DirectoryResource dr = new DirectoryResource();

        for (File f : dr.selectedFiles()){
            FileResource fr = new FileResource(f);
            int currentRank = getRankLogic(fr, searchName, searchGender);
            if (currentRank != -1) {
                cumulativeRank += currentRank;
                numberOfRanks += 1;
            }
        }
        if(numberOfRanks > 0) 
            averageRank = (double)cumulativeRank / numberOfRanks;       
        else 
            averageRank = -1;

        return averageRank; 
    }

    public void testGetAverageRank(){
        System.out.println("Beginning test of getAverageRank()\n");

        // test for Female
        System.out.println("Running test for Sophia, Female");
        double testAverageRank = getAverageRank("Sophia", "F");
        System.out.println("Expected Outome -> 1.66. Returned -> " + testAverageRank);

        // test for male
        System.out.println("\nRunning test for Jacob, Female");
        testAverageRank = getAverageRank("Jacob", "M");
        System.out.println("Expected Outome -> 2.66. Returned -> " + testAverageRank);

        // test for non existent name
        System.out.println("\nRunning test for Ryan, Male");
        testAverageRank = getAverageRank("Ryan", "M");
        System.out.println("Expected Outome -> -1. Returned -> " + testAverageRank);
    }

    // returns total cumulative births ranked higher than a specific name. Returns -1 if no name is found
    public int getTotalBirthsRankedHigher(int searchYear, String searchName, String searchGender) {
        FileResource fr = new FileResource("../Data Sets/us_babynames_by_year/yob"+searchYear+".csv");

        // for testing only. !!NOT IN PRODUCTION!!
        // FileResource fr = new FileResource("../Data Sets/us_babynames_test/yob"+searchYear+"short.csv");
        int nameRanking = getRankLogic(fr, searchName, searchGender);
        if (nameRanking == -1)
            return -1;

        int birthsRankedHigher = 0, index = 0;        
        for (CSVRecord record : fr.getCSVParser(false)) {
            if(record.get(1).equalsIgnoreCase(searchGender)) {
                index++;
                if(index < nameRanking) 
                    birthsRankedHigher += Integer.parseInt(record.get(2));
                else 
                    return birthsRankedHigher;
            }
        }
        return birthsRankedHigher;
    }

    public void testGetTotalBirthsRankedHigher() {
        System.out.println("Testing function getTotalBirthsRankedHigher()\n");

        // Testing Male
        System.out.println("\nTesting Ethan, Male in 2012");
        int testBirthsRankedHigher = getTotalBirthsRankedHigher(2012, "Ethan", "M");
        System.out.println("Expected -> 15. Received -> " + testBirthsRankedHigher);

        // Testing Male
        System.out.println("\nTesting Olivai, Female in 2012");
        testBirthsRankedHigher = getTotalBirthsRankedHigher(2012, "Olivia", "F");
        System.out.println("Expected -> 27. Received -> " + testBirthsRankedHigher);

        // Testing Wrong Name
        System.out.println("\nTesting Ryan, Male in 2012");
        testBirthsRankedHigher = getTotalBirthsRankedHigher(2012, "Ryan", "M");
        System.out.println("Expected -> -1. Received -> " + testBirthsRankedHigher);

    }
}