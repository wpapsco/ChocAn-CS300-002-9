import java.util.Scanner;
// Assumptions: - 0 is an invalid member ID
//              - member IDs are always positive integers

public class ProviderInterface {
    private static DatabaseInterface database;
    private static int ID;
    ProviderInterface(DatabaseInterface database, int ID){this.database = database; this.ID = ID;}

    //clears the standard output device by printing a bunch of newlines
    private static void clearConsole(){
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    }

    //Menu driver
    public static void main(String[] args){
        Scanner userIn = new Scanner(System.in);
        int selection = 0;

        clearConsole();
        while(true){
            //validate user selection
            boolean valid = false;
            do{
                printMenu();
                while(!userIn.hasNextInt()){
                    clearConsole();
                    System.out.println("Invalid selection.\n");
                    userIn.next();
                    printMenu();
                }
                selection = userIn.nextInt();
                if(selection >= 1 && selection <= 5)
                    valid = true;
                else{
                    clearConsole();
                    System.out.println("Invalid selection.\n");
                }
            } while(!valid);

            //process user selection
            switch (selection) {
                //still need to catch and process return values
                case 1: int validMember = checkID();
                    if(validMember == -1)
                        System.out.println("Member ID not recognized.");
                    else if (validMember == -2)
                        System.out.println("Suspended.");
                    else
                        System.out.println("Validated.");
                    break;
                case 2: if(logService()) {
                    clearConsole();
                    System.out.println("\nService recorded.");
                }
                else {
                    System.out.println("\nAborted service record.");
                }
                    break;
                case 3: checkProviderDirectory();
                    break;
                case 4: serviceReport();
                    break;
                case 5: return;
                //should never reach default, if we do, something went wrong
                default: assert false;
            }
        }
    }

    //print provider options menu to System.out
    private static void printMenu(){
        System.out.println("1. Check member status.\n" +
                "2. Log a service.\n" +
                "3. Lookup service by name.\n" +
                "4. Generate service report(last 7 days).\n" +
                "5. Logout.\n" +
                "Make a selection: ");
    }

    // Check member ID number for status (valid: member ID is returned, suspended: -2, or invalid: -1)
    private static int checkID(){
        Scanner sc = new Scanner(System.in);
        int memberID = 0;

        // Loop until user enters reasonable member ID
        clearConsole();
        do {
            System.out.print("Enter member ID to validate: ");
            String input = sc.nextLine();

            // member ID must be a positive int
            try {
                memberID = Integer.parseInt(input);
            } catch (NumberFormatException ex) {
                clearConsole();
                System.out.println("Invalid Number. Member ID's are positive numerals.");
                memberID = 0;
            }
            if(memberID < 0) {
                clearConsole();
                System.out.println("Invalid Number. Member ID's are positive numerals.");
                memberID = 0;
            }
        } while(memberID == 0);

        /*
        // Lookup member in database, return status or no match
        int databaseRetValue = callDatabaseFunction(memberID);
        switch(databaseRetValue) {
            case 1:
                return 1;
                break;
            case 2:
                System.out.println("Member suspended.");
                return 2;
                break;
            case 3: return 3;
                break;
        }*/
        //return ID if valid, this return value is a placeholder until the database is integrated.
        return memberID;
    }

    //log a service to the database
    private static boolean logService(){
        int member = checkID();
        //member ID not recognized
        if(member == -1){
            clearConsole();
            System.out.println("Member ID not recognized.");
            return false;
        }

        //Suspended member ID
        else if(member == -2){
            clearConsole();
            System.out.println("Member is suspended.");
            return false;
        }

        //valid member ID
        else {
            ServiceRecord log = new ServiceRecord();
            Scanner in = new Scanner(System.in);
            log.memberID = member;
            int serviceID = 0;

            clearConsole();
            do {
                System.out.println("Enter the service code: ");
                String input = in.nextLine();

                // service code ID must be a positive int
                try {
                    serviceID = Integer.parseInt(input);
                } catch (NumberFormatException ex) {
                    clearConsole();
                    System.out.println("Invalid Number. Service codes are positive numerals.");
                    serviceID = 0;
                }
                if(serviceID < 0) {
                    clearConsole();
                    System.out.println("Invalid Number. Service codes are positive numerals.");
                    serviceID = 0;
                }
            } while(serviceID == 0);

            //check with database interface if service code is valid, if so, the database should return true.
            if(true/*placeholder. database search for service ID func call here*/){
                log.serviceID = serviceID;
                log.providerID = ID;
            }
            return true;
        }
    }

    // Lookup a service and print the service name, code, and fee if valid
    private static int checkProviderDirectory(){
        return 0;
    }

    //generates service report for the past 7 days
    //returns true on success
    private static boolean serviceReport(){
        return true;
    }
}
