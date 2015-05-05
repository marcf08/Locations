import java.io.*; //Import declaration
import java.util.*; //Import declaration
/**
 * Locations will look up and display geographical information to the user.
 * Assignment: Project 5
 * @author Marcus Flores (Unity ID: mflores)
 * Version 1.0
 * Date 3.28.2015
 */
public class Locations {
/**Class constant for rounding**/
    public static final double RND = 100.0;
/**Class constant for n umber of states plus D.C.*/
    public static final int NUM_STATES = 51; //
/** An array of state abbreviations */
    public static final String[] STATES = {"AK", "AL", "AR", "AZ", "CA", "CO", "CT", "DC",
                                           "DE", "FL", "GA", "HI", "IA", "ID", "IL", "IN", "KS",
                                           "KY", "LA", "MA", "MD", "ME", "MI", "MN", "MO",
                                           "MS", "MT", "NC", "ND", "NE", "NH", "NJ", "NM",
                                           "NV", "NY", "OH", "OK", "OR", "PA", "RI", "SC", "SD",
                                           "TN", "TX", "UT", "VA", "VT", "WA", "WI", "WV", "WY"};
/**
 * @param args an array of command line arguments
 * We put the information for reading a file from the command line here.
 * Since the command line is an array of String elements, we can check for an 
 * input file using array methods. If we don't find exactly one argument
 * (if the array is not exactly one element in length) we can show the proper
 * program usage and exit.  
 *
 */
    public static void main(String [] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Locations filename");
            System.exit(1);
        }
        File inputFile = new File(args[0]);
        if (inputFile.canRead()) {
            readInputFile(inputFile);  //Pass the file to the readInputFile() method
        } else {
            System.out.println("Unable to access input file: " + args[0]);
            System.exit(1);
        }
    }
/**
 * readInputFile will read input from the file of locations.
 * @param input the input file passed from the introMenu() method
 */
    public static void readInputFile(File input) {
        try {
            int count = 0; //Count the number of lines for use in an array
            Scanner fileScannerCount = new Scanner(input); //Scanner strictly for counting locations
            fileScannerCount.nextLine(); //Omit the first line of file for the count
            Scanner fileScannerProcess = new Scanner(input); //Scanner for adding to array
            fileScannerProcess.nextLine(); //Omit the first line of the file of the input file
            while (fileScannerCount.hasNextLine()) {
                String lines = fileScannerCount.nextLine();
                count++;  //Now we know how many locations we have           
            }
            fileScannerCount.close(); //This scanner is finished
            Location[] locations = new Location[count]; //Create an array of location objects
            int i = 0; //The array starts at 0
            //Since the length of locations is the same length as the lines in the file,
            //we don't need to worry about the has next method.
            while (i < locations.length) {
                String line = fileScannerProcess.nextLine(); //Turn the line into a string
                Scanner scan = new Scanner(line); //Create a scanner for the string
                scan.useDelimiter("\t"); //Set the delimeter to a tab character
                String name = scan.next().trim(); //Trim white space from place name
                String state = scan.next().trim(); //Trim white space from state
                String latitude = scan.next();
                double latitudeAsDouble = Double.parseDouble(latitude); 
                String longitude = scan.next();
                double longitudeAsDouble = Double.parseDouble(longitude);
                locations[i] = new Location(name, state, latitudeAsDouble, longitudeAsDouble);
                i++;
            }
            introMenu(locations); //Pass the locations array onto introMenu() for processing
        } catch (FileNotFoundException e) { //Syntactically necessary
            System.out.println("Error accessing file.");
            System.exit(1);
        }
    }
/** 
 * introMenu contains the looping structures that validate proper input.
 * It also accepts the locations array as a parameter from main because we will need 
 * to pass it to other methods for processing.
 * @param locations an array of location objects
 */
    public static void introMenu(Location[] locations) {
        Scanner console = new Scanner(System.in); //Scanner for user input
        prompts(); //Call for the prompts
        String anyInput = console.next(); //Get whatever the user inputs
        String correctInput = anyInput.toLowerCase(); //Convert it to lower case
        while (!correctInput.equals("d") && !correctInput.equals("l") && //Ensure correct input
                  !correctInput.equals("s") && !correctInput.equals("f") 
                  && !correctInput.equals("q")) {
            System.out.println("Invalid option.");
            //console.next(); //Discard the invalid input
            prompts(); //Call for the prompts again
            anyInput = console.next(); //Get input again
            correctInput = anyInput.toLowerCase(); //Convert it to lower case again
        }
        if (correctInput.equals("d")) {
            displayStatistics(locations);
        }
        if (correctInput.equals("l")) {
            Scanner input = new Scanner(System.in); //Input scanner to pass to the method
            listLocationsInState(input, locations);
        }
        if (correctInput.equals("s")) {
            Scanner inputTwo = new Scanner(System.in); //Input scanner to pass to the method
            searchForLocation(inputTwo, locations); 
        }
        if (correctInput.equals("f")) {
            Scanner inputThree = new Scanner(System.in); //Input scanner to pass to the method
            findLocations(inputThree, locations);
        }
        if (correctInput.equals("q")) { 
            System.out.println("Goodbye!");
            System.exit(1);  //Exit if user enters "q" 
        }
        while (!correctInput.equals("q")) {
            prompts();
            anyInput = console.next();
            correctInput = anyInput.toLowerCase();
            if (correctInput.equals("l")) {
                Scanner input = new Scanner(System.in); //Input scanner to pass to the method
                listLocationsInState(input, locations);
            }
        }
        System.out.println("Goodbye!");
        System.exit(1);
    }
/**
 * The prompts take up a lot of line space and can distract from the control
 * structures (while loops) that validate user input. The prompts are collected
 * here so as to make the program more readable. 
 */
    public static void prompts() {
        System.out.println();
        System.out.println("Information about Locations in the U.S.");
        System.out.println();
        System.out.print("- Please enter an option below.\n");
        System.out.println("D - Display statistics");
        System.out.println("L - List all locations in given state");
        System.out.println("S - Search for location");
        System.out.println("F - Find locations within given distance of a location");
        System.out.println("Q - Quit the program");
        System.out.println();
        System.out.print("Option: ");
    }
/**
 * displayStatistics() will find the state with the maximum and minimum
 * number of locations. It will also output these values. 
 *@param locations an array of location objects
 */    
    public static void displayStatistics(Location[] locations) {        
        //We'll count the number of places in each state using this array
        //Count how many states are in the location array
        int [] placesCounter = new int [NUM_STATES];
        for (int i = 0; i < placesCounter.length; i++) {
            int difference = ((lastIndexOf(locations, STATES[i]) - indexOf(locations, STATES[i]))
                                    + 1);
            //We add 1 because subtracting here does not count inclusively
            //We've found the difference in the indices, which is the number of locations
            //Now we'll store that number of places in the array
            placesCounter[i] = difference;       
        }
        //We're now basically finding the max/min values of the placesCounter array
        //Find max
        int max = placesCounter[0]; //Initial value for max
        for (int k = 0; k < placesCounter.length; k++) {
            if (placesCounter[k] > max) {
                max = placesCounter[k];
            }
        }
        //Find min
        int min = placesCounter[0]; //Initial value for min
        for (int j = 0; j < placesCounter.length; j++) {
            if (placesCounter[j] < min) {
                min = placesCounter[j];
            }
        }
        //Figure out where in the int array the values are stored 
        System.out.println("Number of locations: " + locations.length);
        int maxLocationIndex = indexOf(placesCounter, max);
        System.out.println(STATES[maxLocationIndex] + " has the most locations with "
                                    + max);
        int minLocationIndex = indexOf(placesCounter,min);
        System.out.println(STATES[minLocationIndex] + " has the fewest locations with "
                                    + min);
        System.out.println();
        introMenu(locations); //Loop back to menu
    }
/**
 * This modified version of the indexOf method will accept
 * an int array to find the index of a particular value.
 * @param array an array integers
 * @param target the value we're looking for 
 * @return i the index of the target value
 */    
    public static int indexOf(int[] array, int target) {
        for (int i = 0; i < array.length; i++) {
            if (target == array[i]) {
                return i;
            }
        }
        return -1; //If it the value is not in the array
    } 
/**
 * This listLocations method will list all the locations in a given state.
 * It does this by searching the locations array for the first occurrence of a state
 * and recording that array element's index. It then searches for the last occurrence of
 * a state's value and records that array element's index. It then uses those values
 * to output the list of places.
 * @param console for user input
 * @param locations an array of location objects
 */
    public static void listLocationsInState(Scanner console, Location[] locations) {
        System.out.print("State: "); //Prompt for state
        String input = console.next(); //Get user input
        String correctInput = input.toUpperCase(); //Accept input regardless of case
        //The if statement searches the STATES array to ensure we have a valid state
        if (lastIndexOf(STATES, correctInput) == -1) {
            System.out.println(); //For readability between the prompt and program output
            System.out.println("Invalid State");
            introMenu(locations); //Return to the menu with locations
        }
        //We'll also make sure the state actually exists in the locations
        //array. Otherwise, we might end up passing a -1 into firstOcurrence below
        //and causing an array index out of bounds exception. (The user could, for example,
        //supply a locations file without all 51 states.)
        for (int i = 0; i < locations.length; i++) {
            //Last index of will return -1 if we don't find a state, which gives us a 
            //convenient means of testing whether or not a state exists in the array
            if (lastIndexOf(locations, correctInput) == -1) {
                System.out.println();
                System.out.println("Invalid State.");
                introMenu(locations); //Return to the menu with locations
            }
        }
        //Assertion: at this point we have a valid state that IS an element of the
        //locations array. 
        int firstOccurence = indexOf(locations, correctInput); //Get first index of a state
        int lastOccurence = lastIndexOf(locations, correctInput); //Get last index of a state
        //Now print the name and state 
        for (int i = firstOccurence; i <= lastOccurence; i++) {
            System.out.println(locations[i].getName() + ", " + locations[i].getState());
        }
        System.out.println(); //A blank space separates output from the menu
        introMenu(locations); //After showing output loop back to the menu
    }
/**
 * This modified version of the indexOf method will accept
 * the locations array to find the first index of a particular state.
 * @param locations an array of location objects
 * @param state a state to search for the index of
 * @return i the index of a particular state
 */
    public static int indexOf(Location[] locations, String state) {
        for (int i = 0; i < locations.length; i++) {
            String toFind  = locations[i].getState(); //Make this easier to test as a String value
            if (toFind.equals(state)) {
                return i;
            }
        }
        return -1; //If the value is not in the array
    } 
/**
 * This modified version of the indexOf method will accept
 * the locations array to find the first index of a particular state. 
 * It searches for a location instead of a state.
 * @param locations an array of location objects
 * @param name a name to search for
 * @return i the index of a particular location
 */
    public static int indexOfLocation(Location[] locations, String name) {
        for (int i = 0; i < locations.length; i++) {
            String toFind  = locations[i].getName(); //Make this easier to test as a String value
            if (toFind.equals(name)) {
                return i;
            }
        }
        return -1; //If it the value is not in the array
    } 
/**
 * This modified version of the indexOf method will accept
 * the locations array to find the index of a particular state and location pair.
 * @param locations an array of location objects
 * @param state a state  to search the index of
 * @param name the name of the corresponding location
 * @return i the index of a particular location 
 */    
    public static int indexOfLocationAndState(Location[] locations, String state, 
                                                                 String name) {
        for (int i = 0; i < locations.length; i++) {
            String toFindName  = locations[i].getName(); //Make this easier to test as a String
            String toFindState = locations[i].getState();
            //If both the name and the state match, we'll return i
            if (toFindName.equals(name) && toFindState.equals(state)) {
                return i;
            }
        }
        return -1; //If it the value is not in the array
    } 
/**
 * This modified version of the lastIndexOf method from ICE 17.1 will
 * accept the locations array to find the last index of a particular state.
 * @param locations an array of location objects
 * @param state a state to search for the last index of
 * @return i the last index of a particular location
 */
    public static int lastIndexOf(Location[] locations, String state) { //From ICE 17.1
        for (int i = (locations.length - 1); i >= 0; i--) {
            String toFind  = locations[i].getState(); //Make this easier to test as a String value
            if (toFind.equals(state)) {
                return i;
            }
        }
        return -1; //If it the value is not in the array
    } 
/**
 * This modified version of the lastIndexOf method from ICE 17.1 will
 * accept a String array to see if the States array contains a certain value.
 * @param array of String values to search
 * @param lastValue a String to search for the last index of
 * @return i the last index of the string value
 */
    public static int lastIndexOf(String[] array, String lastValue) { //From ICE 17.1
        for (int i = (array.length - 1); i >= 0; i--) {
            if (array[i].equals(lastValue)) {
                return i;
            }
        }
        return -1; //If it the value is not in the array
    }

/**
 * The searchForLocation method uses the String contain method in order to look for
 * matching character sequences that a user would input. It searches the name field
 * of the locations array and prints any matches.
 * @param console a scanner for user input
 * @param locations an array of location objects
 */
    public static void searchForLocation(Scanner console, Location[] locations) {
        System.out.print("Location (is/contains): ");
        String input = console.nextLine(); //We need the whole line due to possible spaces
        String correctInput = input.toLowerCase(); //Ignore case of user input
        for (int i = 0; i < locations.length; i++) {
            //If we find a matching sequence of characters, we'll print out the information for it
            String formatGetName = locations[i].getName().toLowerCase();
            if (formatGetName.contains(correctInput)) {
                System.out.print(locations[i].getName() + ", " + locations[i].getState() + "\t"
                                       + "Lat: " + locations[i].getLatitude() + " " + "Lon: "
                                       + locations[i].getLongitude() + "\n");
                System.out.println(); //An empty line for readability
            } 
        }
        introMenu(locations); //Loop back to main menu
    }
/**
 * The findLocations method will output a list of places within a user-specified
 * distance of a location also entered by the user.
 * @param console a scanner for user input
 * @param locations an array of location objects
 */
    public static void findLocations(Scanner console, Location[] locations) {
        System.out.print("Name: ");
        String name = console.nextLine(); //Get the name of a place
        System.out.print("State: ");
        String state = console.nextLine(); //Get the name of a state
        //indexOf will ensure the particular name and state combination exists in the 
        //locations array. 
        if (indexOf(locations, state) == -1 || indexOfLocation(locations, name) == -1) {
            System.out.println("Invalid location");
            System.out.println(); //Blank line for readability
            introMenu(locations); //If the location is not found, we loop back to the menu
        }
        System.out.print("Distance: ");
        try {
            int distance = console.nextInt();  //User-specified distance  
            if (distance < 1) {
                introMenu(locations);
            }
            //The index below is used to get the index of the user's location and state in the 
            //locations array. It will then be used for the getDistance method.
            int index = indexOfLocationAndState(locations, state, name); //
            for (int i = 0; i < locations.length; i++) {
                //We'll cycle through the locations array calculating distances. If the distance 
                //between the user's location and another location is less than the distance
                //specified by the user, we'll output that location and distance.
                //The condition index != i means we will omit the location the user inputs
                if ((locations[index].getDistance(locations[i])) < distance && index != i) { 
                    //The expression for distance is complex, so we'll use a variable
                    double locationDistance = (locations[index].getDistance(locations[i]));
                    System.out.println(locations[i].getName() + ", " + locations[i].getState()
                                                + ": " 
                                                + (Math.round(locationDistance * RND) / RND)
                                                + " miles");
                //We'll also use the round method in a way to keep the distance to
                //two decimal digits by multiplying (and then diving) by 100.0
                }
            }
            introMenu(locations); //Loop back to main menu
        } catch (InputMismatchException e) { //In case the user enters a non-integer
            System.out.println("Invalid distance");
            introMenu(locations); //Display error message and loop back to the menu
        }
    }
}