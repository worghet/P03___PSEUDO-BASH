import java.io.File;
import java.util.Map;
import java.util.Scanner;

public class Terminal {

    // -- CONSTANT / UTILITY VARIABLES ------

    static final String FILE_SEPARATOR = File.separator; // test this on another os
    static final Map<String, String> COMMAND_DICTIONARY = Map.of(

            // ASSISTANCE METHODS
            "help", "[ help (none) | Prints all available commands.]",
            "explain", "[ explain (function_name, all) | Explains the given command. ]",
            "secrets", "[ secrets (none) | Prints a list of secret commands. ]",

            // NAVIGATION METHODS
            "whereami", "[ whereami (none) | Prints the current working directory. ]",
            "go", "[ go (directory_name, up) | Will switch the working directory. ]",
            "lookhere", "[ lookhere (none, directory_name) | Will print the contents of the given directory. ]",
            "exit", "[ exit (none) | Closes the program. ]",

            // FILE MANAGEMENT METHODS

            "mkfile" , "[ mkfile (file_name) | Makes a text file with the given name. ]",
            "mkdir", "[ mkdir (directory_name) | Makes a directory with the given name. ]",
            "delete", "[ delete (file_name, directory_name) | Deletes the given item. ]"
            // MISC

    );

    // -- OBJECT FIELDS ---------------------

    String username = "user", host = "pseudobash";
    String previousCommand = "";
    Scanner input;

    // -- CONSTRUCTOR ------------------------

    public Terminal() {
        input = new Scanner(System.in);
    }

    // -- TERMINAL LAUNCHER ------------------

    public void launchTerminal() {

        // put this in a try/catch?
        username = System.getProperty("user.name");


        // START LOOP

        while (true) {

            // get command

            System.out.print("\u001B[1m" + username + "@" + host + ":~" + getPrintableFilePath() + "$ \u001B[0m");
            String command = input.nextLine().toLowerCase();
            String[] tokenizedCommand = command.split(" ");

            try {

                // check input or sum idk
                // try {switch.. throws errors here} catch(ERROR MESSAGE HERE) {print error}
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

                            if ((commandIndex + 1) % 3 == 0) {
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
                        }
                        else {

                            if (COMMAND_DICTIONARY.get(tokenizedCommand[1]) == null) { // or use containsKey()
                                System.out.println("Not a command. Use 'help' to get a list of commands.");
                            }
                            else {
                                System.out.println(COMMAND_DICTIONARY.get(tokenizedCommand[1]));
                            }
                        }

                        break;


                    case "secrets":
                        System.out.println("secret 1, secret 2 ...");
                        break;

                    // UTILITY CASES

                    case "clear":

                        // check if gui or tui

                        // tui workaround.
                        for (int i = 0; i < 50; i++) {
                            System.out.println();
                        }
                        break;

                    case "exit":
                        return;

                    case "whoami":
                        System.out.println(username);
                        break;
                    case "whoishost":
                        System.out.println(host); // HOST OR DOMAIN??
                        break;
                    case "whereami":
                        System.out.println(System.getProperty("user.dir"));
                        break;
                    case "print":
                        System.out.println(tokenizedCommand[1]);
                        break;
                    default:
                        System.out.println("Unknown command.. try \"help\" to see commands.");
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Invalid arguments. Use 'explain (command)' to see the valid arguments. ");
            } catch (Exception e) {
                System.out.println(e);
            }

//            previousCommand = command;

        }
    }


    // -- USER METHODS --------------------------------------

    // This will not change the actual device username; just the instance.
    private void renameUser(String newUsername) {
        username = newUsername;
    }

    private void changeHost(String newHost) {
        host = newHost;
    }

    private String getPrintableFilePath() {
        return "";
    }

}
