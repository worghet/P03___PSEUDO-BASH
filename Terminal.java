import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class Terminal {

    // -- CONSTANT / UTILITY VARIABLES ------

    static final String FILE_SEPARATOR = File.separator; // test this on another os
//    static final int FILE = 0, DIRECTORY = 1;
    static final Map<String, String> COMMAND_DICTIONARY = initializeCommandDictionary();

    // -- OBJECT FIELDS ---------------------

    private String username = "user", host = "pseudobash";
    private File currentDirectory = new File(System.getProperty("user.dir"));
    private ArrayList<String> log = new ArrayList<>();
    private Scanner input;

    // -- CONSTRUCTOR ------------------------

    public static void launchTerminal() { new Terminal().startProcesses(); }
    public Terminal() {
        input = new Scanner(System.in);
    }

    // -- TERMINAL LAUNCHER ------------------

    public void startProcesses() {

        // put this in a try/catch?
        username = System.getProperty("user.name");


        // START LOOP

        while (true) {

            // get command

            System.out.print("\u001B[1m" + username + "@" + host + ":~" + currentDirectory.toString() + "$ \u001B[0m");
            String command = input.nextLine().toLowerCase();
            String[] tokenizedCommand = command.split(" ");

            try {
                switch (tokenizedCommand[0]) {

                    // EMPTY CASE

                    case "":
                        break;

                    // ASSISTANCE CASES

                    case "help":
                        System.out.println("* use 'explain (command_name)' to get a description of the command *");
                        String[] keys = COMMAND_DICTIONARY.keySet().toArray(new String[0]);
                        for (int commandIndex = 0; commandIndex < COMMAND_DICTIONARY.size(); commandIndex++) {

                            System.out.print(keys[commandIndex] + " ".repeat(23 - keys[commandIndex].length()));

                            if (commandIndex != (keys.length - 1) && (commandIndex + 1) % 3 == 0) {
                                System.out.println();
                            }
                        }
                        System.out.println();
                        break;

                    case "explain": // what if there are more than requested parameters?

                        if (tokenizedCommand[1].equals("all")) {
                            for (String commandExplanation : COMMAND_DICTIONARY.values()) {
                                System.out.println(commandExplanation);
                            }
                        } else {

                            if (COMMAND_DICTIONARY.get(tokenizedCommand[1]) == null) { // or use containsKey()
                                System.out.println("Not a command. Use 'help' to get a list of commands.");
                            } else {
                                System.out.println(COMMAND_DICTIONARY.get(tokenizedCommand[1]));
                            }
                        }

                        break;

                    case "secrets":
                        System.out.println("secret 1, secret 2 ...");
                        break;

                    // NAVIGATION CASES

                    case "whereami":
                        System.out.println(currentDirectory);
                        break;

                    case "go":
                        switchDirectory(tokenizedCommand[1]); // FINISH
                        break;

                    case "lookhere":
                        if (tokenizedCommand.length == 1) {
                            printDirectoryContents(getDirectoryContents(currentDirectory.toString()));
                        } else {
                            printDirectoryContents(getDirectoryContents(tokenizedCommand[1]));
                        }
                        break;

                    case "exit":
                        return;

                    // UTILITY / MISC

                    case "clear":

                        // check if gui or tui

                        // tui workaround.
                        for (int i = 0; i < 50; i++) {
                            System.out.println();
                        }
                        break;

                    case "log":
                        for (int commandIndex = 0; commandIndex < log.size(); commandIndex++) {
                            System.out.println((commandIndex + 1) + " ".repeat((Integer.toString((log.size()))).length() - (Integer.toString(commandIndex + 1)).length()) + " | " + log.get(commandIndex));
                        }
                        break;


                    case "whoami":
                        System.out.println(username);
                        break;
                    case "whoishost":
                        System.out.println(host);
                        break;
                    case "print":
                        System.out.println(tokenizedCommand[1]);
                        break;
                    default:
                        System.out.println("Unknown command.. try \"help\" to see commands.");
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Invalid argument(s). Use 'explain (command)' to see the valid argument(s). ");
            } catch (Exception e) {
                System.out.println(e);
            }

            log.add(command);

        }
    }


    // -- USER METHODS --------------------------------------

    private static Map<String, String> initializeCommandDictionary() {
        Map<String, String> completeDictionary = Map.of();
//        completeDictionary.put();
        return completeDictionary;
    }
//    private boolean resourceExists(String absoluteFilePath, int desiredResourceType) {
//
//        File resource = new File(absoluteFilePath);
//
//        if ((resource.exists()) && (desiredResourceType == DIRECTORY && resource.isDirectory() || desiredResourceType == FILE && resource.isFile())) {
//            return true;
//        }
//
//        return false;
//
//    }



    private void switchDirectory(String desiredDirectory) {
        if (desiredDirectory.equals("up")) {
            File parentDirectory = currentDirectory.getParentFile();

            if (parentDirectory == null) {
                System.out.println("You are already at the root directory.");
            } else {
                currentDirectory = parentDirectory;
            }
        } else {
            String[] directoryContents = getDirectoryContents(currentDirectory.toString());
            String switchTo = null;
            for (String directoryItem : directoryContents) {
                if (directoryItem.equalsIgnoreCase(desiredDirectory) && (new File(currentDirectory, directoryItem)).isDirectory()) {
                    switchTo = directoryItem;
                }
            }
            if (switchTo != null) {
                currentDirectory = new File(currentDirectory.toString() + FILE_SEPARATOR + switchTo);
            } else {
                System.out.println("No such directory exists.");
            }
        }
    }

    private String[] getDirectoryContents(String desiredDirectory) {
        return new File(desiredDirectory).list();
    }

    private void printDirectoryContents(String[] directoryContents) {
        for (int directoryIndex = 0; directoryIndex < directoryContents.length; directoryIndex++) {

            System.out.print(directoryContents[directoryIndex] + " ".repeat(23 - directoryContents[directoryIndex].length()));

            if (directoryIndex != (directoryContents.length - 1) && (directoryIndex + 1) % 3 == 0) {
                System.out.println();
            }
        }
        System.out.println();
    }

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
