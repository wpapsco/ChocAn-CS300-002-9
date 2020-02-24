import java.util.Scanner;
// Assumptions: - 0 is an invalid member ID
//              - member IDs are always positive integers

public class ProviderInterface {

    // Menu interface
    public static void main(String[] args){
        System.out.println("Hello World!");
    }

    // Check member ID number for status (valid, suspended, or invalid)
    private void checkID(){
        Scanner sc = new Scanner(System.in);
        int memberID;

        // Loop until user enters reasonable member ID
        do {
            System.out.print("Enter member ID: ");
            String input = sc.nextLine();

            // member ID must be a positive int
            try {
                memberID = Integer.parseInt(input);
            } catch (NumberFormatException ex) {
                System.out.println("Invalid Number.");
                memberID = 0;
            }
            if(memberID < 0) {
                System.out.println("Invalid Number.");
                memberID = 0;
            }
        } while(memberID == 0);

        /*
        // Lookup member in database, return status or no match
        int databaseRetValue = callDatabaseFunction(memberID);

        switch(databaseRetValue) {
            case 1:
                System.out.println("Invalid Number");
                break;
            case 2:
                System.out.println("Validated");
                break;
            case 3:
                System.out.println("Member suspended");
                break;
        }
        */
    }

    // Charge a member for a service
    private int chargeMember(){
        return 0;
    }

    // Lookup a service and print the service code if valid
    private int checkProviderID(){
        return 0;
    }
}
