import java.util.Scanner;

public class Terminal {

    String username = "user";
    String host = "pseudobash";
    Scanner input;

    public Terminal() {
        input = new Scanner(System.in);
    }

    public void launchTerminal() {
        while (true) {
            System.out.print("\u001B[1m" + username + "@" + host + ":~$ \u001B[0m");
            String[] command = input.nextLine().split(" ");

            // check length or sum idk

            switch (command[0]) {
                case "":
                    break;

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
                case "rename": // check length
                    renameUser(command[1]); // make constants for this
                    break;
                case "rehost": // check length
                    changeHost(command[1]);
                    break;
                default:
                    System.out.println("Unknown command.. try \"help\" to see commands.");
            }
        }
    }


    // -- IDENTITY METHODS --------------------------------------

    private void renameUser(String newUsername) {
        username = newUsername;
    }

    private void changeHost(String newHost) {
        host = newHost;
    }

}
