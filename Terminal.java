// == IMPORTS =======================
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

// == CLASS =======================
// TERMINAL | Deals with the command input, processing, and execution.
public class Terminal {

    // -- CONSTANT / UTILITY VARIABLES ------

    static final String FILE_SEPARATOR = File.separator; // test this on another os
    static final int FILE = 0, DIRECTORY = 1;
    static final Map<String, String> COMMAND_DICTIONARY = initializeCommandDictionary();

    // -- OBJECT FIELDS ---------------------

    private String username = "user", hostname = "pseudobash";
    private File workingDirectory = new File(System.getProperty("user.dir"));
    private ArrayList<String> log = new ArrayList<>();
    private Scanner input = new Scanner(System.in); ;

    // -- "CONSTRUCTOR" (DEFAULT) ------------

    public static void launchTerminal() { new Terminal().startProcesses(); }

    // -- TERMINAL LAUNCHER ------------------

    public void startProcesses() {

        // Set username to the system username.
        username = System.getProperty("user.name");

        // Start main processing loop.
        while (true) {

            // =====================================================
            // -- PRE-PROCESSING BEGINS ----------------------------
            // =====================================================

            // Print the input field.
            System.out.print("\u001B[1m" + username + "@" + hostname + ":~" + workingDirectory.toString() + "$ \u001B[0m");

            // Gather and tokenize the entered command.
            String command = input.nextLine(); String[] tokenizedCommand = command.split(" "); // error is thrown here if input is just spaces.

            // Set the command itself to lowercase such that it can be recognized if there's an uppercase mistake.
            tokenizedCommand[0] = tokenizedCommand[0].toLowerCase();

            // =====================================================
            // -- PRE-PROCESSING ENDS | PROCESSING BEGINS ----------
            // =====================================================

            // Use try to catch various types of errors.
            try {

                // Identify the command itself.
                switch (tokenizedCommand[0]) {

                    // Empty case; do nothing.
                    case "":
                        break;


                    // ====================================
                    // -- ASSISTANCE COMMANDS -------------
                    // ====================================


                    // [ help (none) | Prints all available commands. ]
                    case "help":

                        // Print a helpful message.
                        System.out.println("* use 'explain (command_name)' to get a description of the command *");

                        // Get a String[] of every command name.
                        String[] commandNames = COMMAND_DICTIONARY.keySet().toArray(new String[0]);

                        // Print a formatted list of all the command names.
                        for (int commandIndex = 0; commandIndex < COMMAND_DICTIONARY.size(); commandIndex++) {

                            // Prints the name of a command.
                            System.out.print(commandNames[commandIndex] + " ".repeat(23 - commandNames[commandIndex].length()));

                            // Makes a new line every 3 names.
                            if (commandIndex != (commandNames.length - 1) && (commandIndex + 1) % 3 == 0) {
                                System.out.println();
                            }
                        }
                        System.out.println();
                        break;

                    // [ explain (function_name, all) | Explains the given command. ]
                    case "explain":

                        // This allows all the command explanations to be printed at once.
                        if (tokenizedCommand[1].equals("all")) {
                            for (String commandExplanation : COMMAND_DICTIONARY.values()) {
                                System.out.println(commandExplanation);
                            }
                        }
                        else {

                            // Check that the command requested actually exists.
                            if (COMMAND_DICTIONARY.containsKey(tokenizedCommand[1])) { // or use containsKey()
                                System.out.println(COMMAND_DICTIONARY.get(tokenizedCommand[1]));
                            }

                            // Print a message if the command requested doesn't exist.
                            else {
                                System.out.println("The command you want explained does not exist.");
                            }
                        }

                        break;

                    // [ secrets (none) | Prints a list of secret commands. ]
                    case "secrets":
                        // TODO ADD SECRETS
                        System.out.println("secret 1, secret 2 ...");
                        break;


                    // ====================================
                    // -- NAVIGATION COMMANDS -------------
                    // ====================================


                    // [ whereami (none) | Prints the current working directory. ]
                    case "whereami":
                        System.out.println(workingDirectory);
                        break;

                    // [ go (directory_name, up) | Will switch the working directory. ]
                    case "go":
                        switchDirectory(tokenizedCommand[1]); // FINISH
                        break;

                    // [ lookhere (none, directory_name) | Will print the contents of the given directory. ]
                    case "lookhere":

                        // Check if the command is 'stand-alone' --> Show the contents of the working directory.
                        if (tokenizedCommand.length == 1) {
                            printDirectoryContents(getDirectoryContents(workingDirectory.toString()));
                        }

                        // If there is a specific path that is given (* from the working directory *), there look in there.
                        else {

                            // Check that the requested directory exists.
                            if (resourceExists(workingDirectory.toString() + FILE_SEPARATOR + tokenizedCommand[1], DIRECTORY)) {
                                printDirectoryContents(getDirectoryContents(workingDirectory.toString() + FILE_SEPARATOR + tokenizedCommand[1]));
                            }

                            // Print message if doesn't exist.
                            else {
                                System.out.println("No such directory exists.");
                            }
                        }
                        break;

                    // [ exit (none) | Closes the program. ]
                    case "exit":
                        return; // Just quits the while loop.


                    // ====================================
                    // -- FILE MANAGEMENT COMMANDS --------
                    // ====================================


                    case "read":
                        readFile(tokenizedCommand[1]);
                        break;


                    // ====================================
                    // -- USER DIAGNOSTICS COMMANDS -------
                    // ====================================


                    // [ whoami (none) | Prints username. ]
                    case "whoami":
                        System.out.println(username);
                        break;

                    // [ whoishost (none) Prints hostname. ]
                    case "whoishost":
                        System.out.println(hostname);
                        break;


                    // ====================================
                    // -- MISCILLANIOUS COMMANDS ----------
                    // ====================================


                    // [ clear (none) | Prints many newlines on text-UI; clears the text on graphics-UI. ]
                    case "clear":

                        // TODO check if tui or gui

                        // Just prints many newlines to simulate cleared screen.
                        for (int i = 0; i < 50; i++) {
                            System.out.println();
                        }

                        break;

                    // [ log (none) | Prints an order of commands used in the session. ]
                    case "log":

                        // Prints a formatted list of the commands used.
                        for (int commandIndex = 0; commandIndex < log.size(); commandIndex++) {
                            System.out.println((commandIndex + 1) + " ".repeat((Integer.toString((log.size()))).length() - (Integer.toString(commandIndex + 1)).length()) + " | " + log.get(commandIndex));
                        }

                        break;

                    // [ print (text) | Prints the given text on the terminal. ]
                    case "print":

                        // Goes through each word/phrase inputted after the 'print' command.
                        for (int possibleTerm = 1; possibleTerm < tokenizedCommand.length; possibleTerm++) {
                            System.out.print(tokenizedCommand[possibleTerm] + " ");
                        }

                        System.out.println();
                        break;


                    // ====================================
                    // -- UNRECOGNIZED COMMANDS -----------
                    // ====================================


                    // In case the inputted command was was not recognized, prints message.
                    default:
                        System.out.println("Unknown command.. try \"help\" to see commands.");
                }
            }

            // Handles improper argument placements.
            catch (IndexOutOfBoundsException e) {
                System.out.println("Invalid argument(s). Use 'explain (command)' to see the valid argument(s). ");
            }

            // Handles file management.
            catch (FileNotFoundException e) {
                System.out.println("The file that you're trying to read cannot be found.");
            }

            // Handles all other exceptions.
            catch (Exception e) {
                System.out.println("Error occured: " + e);
            }

            // Saves the command into the log.
            logCommand(command);

        }
    }

    // -- TERMINAL METHODS -------------------

    // Constructs the command dictionary constant (the constructor went up to 10 keys/values).
    private static Map<String, String> initializeCommandDictionary() {

        // Create the dictionary.
        Map<String, String> completeDictionary = new java.util.HashMap<>(Map.of());

        // Add the assistance commands.
        completeDictionary.put("help", "[ help (none) | Prints all available commands. ]");
        completeDictionary.put("explain", "[ explain (function_name, all) | Explains the given command. ]");
        completeDictionary.put("secrets", "[ secrets (none) | Prints a list of secret commands. ]");

        // Add the navigation commands.
        completeDictionary.put("whereami", "[ whereami (none) | Prints the current working directory. ]");
        completeDictionary.put("go", "[ go (directory_name, up) | Will switch the working directory. ]");
        completeDictionary.put("lookhere", "[ lookhere (none, directory_name) | Will print the contents of the given directory. ]");
        completeDictionary.put("exit", "[ exit (none) | Closes the program. ]");

        // Add the user-diagnostic commands.
        completeDictionary.put("whoami", "[ whoami (none) | Prints username. ]");
        completeDictionary.put("whoishost", "[ whoishost (none) Prints hostname. ]");

        // Add the miscillanious commands.
        completeDictionary.put("clear", "[ clear (none) | Prints many newlines on text-UI; clears the text on graphics-UI. ]");
        completeDictionary.put("log", "[ log (none) | Prints an order of commands used in the session. ]");
        completeDictionary.put("print", "[ print (text) | Prints the given text on the terminal. ]");

        return completeDictionary;
    }

    // Checks whether a resource exists (* specifically as the desired data type *).
    private boolean resourceExists(String absoluteFilePath, int desiredResourceType) {

        // Cast the filepath into a file object.
        File resource = new File(absoluteFilePath);

        // Check that: | 1. An item of the filepath EXISTS. | 2. The resource is of the desired type.
        if ((resource.exists() && (desiredResourceType == DIRECTORY && resource.isDirectory())) || (desiredResourceType == FILE && resource.isFile())) {
            return true;
        }

        // Return false if the conditions are not met.
        return false;

    }

    // Changes the current working directory to the desired directory.
    private void switchDirectory(String desiredDirectory) {

        // Case "up": Goes to the parent directory.
        if (desiredDirectory.equalsIgnoreCase("up")) {

            // Initializes the parent directory.
            File parentDirectory = workingDirectory.getParentFile();

            // Check that we are already at root directory.
            if (parentDirectory == null) {
                System.out.println("You are already at the root directory.");
            }
            // Otherwise, go to parent directory.
            else {
                workingDirectory = parentDirectory;
            }
        }

        // Case "go to a specific directory": Goes to the given directory.
        else {
            // Checks that the desired directory exists as a directory.
            if (resourceExists(workingDirectory.toString() + FILE_SEPARATOR + desiredDirectory, DIRECTORY)) {

                // Sets the working directory to the requested one.
                workingDirectory = new File(workingDirectory.toString() + FILE_SEPARATOR + desiredDirectory);

            }
            // Gives a message if doesnt exist.
            else {
                System.out.println("No such directory exists.");
            }
        }
    }

    // Returns an array containing the contents of a requested directory.
    private String[] getDirectoryContents(String directoryFilepath) {
        return new File(directoryFilepath).list();
    }

    // Prints a formatted list of directory contents.
    private void printDirectoryContents(String[] directoryContents) {

        // Iterate through each element and print it.
        for (int directoryIndex = 0; directoryIndex < directoryContents.length; directoryIndex++) {

            // Print element.
            System.out.print(directoryContents[directoryIndex] + " ".repeat(23 - directoryContents[directoryIndex].length()));

            // New line every 3 elements.
            if (directoryIndex != (directoryContents.length - 1) && (directoryIndex + 1) % 3 == 0) {
                System.out.println();
            }
        }
        System.out.println();
    }

    // Adds the command to the log.
    private void logCommand(String commandMade) {

        // Check that the command is not just some blank spaces.
        if (!commandMade.isEmpty()) {
            log.add(commandMade);
        }
    }

    // Reads the given text file.
    private void readFile(String filePath) throws FileNotFoundException {

        // Add file extenstion if not there.
        if (!filePath.endsWith(".txt")) {
            filePath += ".txt";
        }

        // Technically should check if file exists here.. but exception already gives the message.

        // Initialize the reading resources.
        File fileToRead = new File(workingDirectory.toString() + FILE_SEPARATOR + filePath);
        Scanner fileReader = new Scanner(fileToRead);

        // Read the file.
        while (fileReader.hasNextLine()) {
            System.out.println(fileReader.nextLine());
        }

        // Close reader.
        fileReader.close();

    }

    private void makeTextFile() {

    }

    private void makeDirectory() {

    }

    private void deleteResource() {

    }

    private void moveResource() {

    }

    //write / open ide

}


// Start with the current working directory
//        File currentDir = new File(System.getProperty("user.dir"));
//        Scanner scanner = new Scanner(System.in);
//
//        System.out.println("Starting in directory: " + currentDir.getAbsolutePath());
//        System.out.println("Type 'up' to go to the parent directory.");
//        System.out.println("Type the name of a directory to navigate into.");
//        System.out.println("Type 'mkdir' to create a new directory.");
//        System.out.println("Type 'mkfile' to create a new file.");
//        System.out.println("Type 'exit' to quit.");
//
//        while (true) {
//            // List files and directories in the current directory
//            listFiles(currentDir);
//
//            // Read the user's input
//            String selection = scanner.nextLine().trim();
//
//            // If the user wants to go up to the parent directory
//            if (selection.equals("up")) {
//                // Navigate up (to the parent directory) if possible
//                File parentDir = currentDir.getParentFile();
//                if (parentDir != null) {
//                    currentDir = parentDir;
//                } else {
//                    System.out.println("You are already at the root directory.");
//                }
//            } else if (selection.equals("exit")) {
//                break; // Exit the loop if user types 'exit'
//            } else if (selection.startsWith("mkdir ")) {
//                // Create a new directory
//                String dirName = selection.substring(6).trim();
//                File newDir = new File(currentDir, dirName);
//                if (newDir.mkdir()) {
//                    System.out.println("Directory '" + dirName + "' created successfully.");
//                } else {
//                    System.out.println("Failed to create directory '" + dirName + "'.");
//                }
//            } else if (selection.startsWith("mkfile ")) {
//                // Create a new file
//                String fileName = selection.substring(7).trim();
//                File newFile = new File(currentDir, fileName);
//                try {
//                    if (newFile.createNewFile()) {
//                        System.out.println("File '" + fileName + "' created successfully.");
//                    } else {
//                        System.out.println("File '" + fileName + "' already exists.");
//                    }
//                } catch (IOException e) {
//                    System.out.println("An error occurred while creating the file.");
//                    e.printStackTrace();
//                }
//            } else {
//                // Try to navigate into a subdirectory
//                File selectedDir = new File(currentDir, selection);
//                if (selectedDir.isDirectory()) {
//                    currentDir = selectedDir;
//                } else {
//                    System.out.println("Invalid selection, not a directory.");
//                }
//            }

// Method to list all files and directories in the current directory
//    private static void listFiles(File dir) {
//        System.out.println("\nContents of: " + dir.getAbsolutePath());
//        String[] files = dir.list();
//        if (files != null) {
//            for (String file : files) {
//                System.out.println(file);
//            }
//        } else {
//            System.out.println("Unable to list files.");
//        }
//    }
//}
