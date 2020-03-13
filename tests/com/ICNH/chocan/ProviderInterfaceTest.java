package com.ICNH.chocan;

import com.ICNH.chocan.records.MemberRecord;
import com.ICNH.chocan.records.ServiceRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ProviderInterfaceTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    DatabaseInterface database;
    ProviderInterface Provface;


    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        database = new DatabaseInterface();
        Provface = new ProviderInterface(database, 12345);
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    void menu() {
    }

    // CheckID Tests
    @Test // Tests that checkID returns with "x" input
    void testCheckIDReturn(){
        String input = "x";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        assertEquals(0, Provface.checkID());
    }
    @Test // Tests that checkID rejects 0 input
    void testCheckIDZero(){
        String input = "0\nx";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        assertEquals(0, Provface.checkID());
    }
    @Test // Tests that checkID rejects negative input
    void testCheckIDNegative(){
        String input = "-23\nx";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        assertEquals(0, Provface.checkID());
    }
    @Test // Tests that checkID rejects alphabetic input
    void testCheckIDAlphabetic(){
        String input = "HelloWorld\nx";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        assertEquals(0, Provface.checkID());
    }
    @Test // Tests that checkID returns correct code when ID isn't in database
    void testCheckIDNotFound(){
        int i = 1;
        int retVal;
        boolean found = false;
        while(!found){
            try{
                retVal = database.validateMember(i);
                if(retVal == -1){ // if we've found an unused ID
                    found = true;
                } else {
                    ++i;
                }
            }
            catch(SQLException e){
                ++i;
            }
        }
        String input = Integer.toString(i);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        assertEquals(-1, Provface.checkID());
    }
    @Test // Tests that checkID returns correct code when given a valid member's ID
    void testCheckIDValid(){
        MemberRecord memTest = new MemberRecord();
        memTest.name = "Test";
        memTest.address = "Test";
        memTest.city = "Test";
        memTest.state = "TE";
        memTest.zip = "Test";
        memTest.valid = true;
        try {
            int id = database.insertMember(memTest);
            String input = Integer.toString(id) + "\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            assertEquals(id, Provface.checkID());
            database.deleteMember(id);
        } catch (SQLException e) {
            fail();
        }
    }
    @Test // Tests that checkID returns correct code when given a suspended member's ID
    void testCheckIDSuspended(){
        MemberRecord memTest = new MemberRecord();
        memTest.name = "Test";
        memTest.address = "Test";
        memTest.city = "Test";
        memTest.state = "TE";
        memTest.zip = "Test";
        memTest.valid = false;

        try {
            int id = database.insertMember(memTest);
            String input = Integer.toString(id) + "\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            assertEquals(-2, Provface.checkID());
            database.deleteMember(id);
        } catch (SQLException e) {
            fail();
        }
    }

    // logService Tests
    @Test // Tests that logService quits if user quits checkID
    void testLogServiceReturn(){
        String input = "x";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        assertFalse(Provface.logService());
    }
    @Test // Tests that logService quits if checkID receives inappropriate input
    void testLogServiceInvalidID(){
        String input = "HelloWorld\nx";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        assertFalse(Provface.logService());
    }
    @Test // Tests that logService quits if checkID receives inappropriate input
    void testLogServiceSuspended(){
        MemberRecord memTest = new MemberRecord();
        memTest.name = "Test";
        memTest.address = "Test";
        memTest.city = "Test";
        memTest.state = "TE";
        memTest.zip = "Test";
        memTest.valid = false;

        try {
            int id = database.insertMember(memTest);
            String input = id + "\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            assertFalse(Provface.logService());
            database.deleteMember(id);
        } catch (SQLException e) {
            fail();
        }
    }
    @Test // Tests that logService quits if user enters x for the date
    void testLogServiceValid_XDate(){
        MemberRecord memTest = new MemberRecord();
        memTest.name = "Test";
        memTest.address = "Test";
        memTest.city = "Test";
        memTest.state = "TE";
        memTest.zip = "Test";
        memTest.valid = true;

        try {
            int id = database.insertMember(memTest);
            String input = id + "\nx\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            assertFalse(Provface.logService());
            database.deleteMember(id);
        } catch (SQLException e) {
            fail();
        }
    }
    @Test // Tests that logService handles invalid dates appropriately
    void testLogServiceValid_FutureDate(){
        MemberRecord memTest = new MemberRecord();
        memTest.name = "Test";
        memTest.address = "Test";
        memTest.city = "Test";
        memTest.state = "TE";
        memTest.zip = "Test";
        memTest.valid = true;

        try {
            int id = database.insertMember(memTest);
            String input = id + "\n01-01-2050\nx";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            assertFalse(Provface.logService());
            database.deleteMember(id);
        } catch (SQLException e) {
            fail();
        }
    }
    @Test // Tests that logService handles invalid dates appropriately
    void testLogServiceValid_InvalidDate(){
        MemberRecord memTest = new MemberRecord();
        memTest.name = "Test";
        memTest.address = "Test";
        memTest.city = "Test";
        memTest.state = "TE";
        memTest.zip = "Test";
        memTest.valid = true;

        try {
            int id = database.insertMember(memTest);
            String input = id + "\n20-20-2020\nx";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            assertFalse(Provface.logService());
            database.deleteMember(id);
        } catch (SQLException e) {
            fail();
        }
    }
    // TODO: Add member at start with ID = 256 and valid = true, remove member at end
    @Test // Tests that logService quits if user enters x for service code
    void testLogServiceValid_XServiceCode(){
        String input = "256\n01-01-2020\nY\nx\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        assertFalse(Provface.logService());
    }
    // TODO: Add member at start with ID = 256 and valid = true, remove member at end
    @Test // Tests that logService handles invalid service names appropriately
    void testLogServiceValid_ServiceNotFound(){
        String input = "256\n01-01-2020\nY\ns\nNotAValidService\nx\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        assertFalse(Provface.logService());
    }
    // TODO: Add member at start with ID = 256 and valid = true, remove member at end
    @Test // Tests that logService rejects service ID 0
    void testLogServiceValid_ServiceIDZero(){
        String input = "256\n01-01-2020\nY\n0\nx\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        assertFalse(Provface.logService());
    }
    // TODO: Add member at start with ID = 256 and valid = true, remove member at end
    @Test // Tests that logService rejects negative service IDs
    void testLogServiceValid_ServiceIDNegative(){
        String input = "256\n01-01-2020\nY\n-24\nx\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        assertFalse(Provface.logService());
    }
    // TODO: Add member at start with ID = 256 and valid = true, remove member at end
    @Test // Tests that logService creates a log successfully when all input is correct
    void testLogServiceValid_HappyPath(){
        String input = "256\n01-01-2020\nY\n1\nY\ntestLogServiceValid_HappyPath";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        assertTrue(Provface.logService());
    }

    // checkProviderDirectory Tests
    @Test // Tests that checkProviderDirectory appropriately handles searching for a non-existant servic3
    void testCheckProviderDirectoryNotFound(){
        String input = "This Doesn't Exist\n\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        assertFalse(Provface.checkProviderDirectory());
    }
    @Test // Tests that checkProviderDirectory appropriately handles searching for a valid service
    void testCheckProviderDirectoryFound(){
        String input = "TestServiceInfo\n\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        assertTrue(Provface.checkProviderDirectory());
    }

    // checkProviderDirectory(int) Tests
    @Test // Tests that checkProviderDirectory(int) returns false when given an invalid service ID
    void testCheckProviderDirectoryInvalid(){
        assertFalse(Provface.checkProviderDirectory(0));
    }
    @Test // Tests that checkProviderDirectory(int) returns true when given an valid service ID
    void testCheckProviderDirectoryValid(){
        assertTrue(Provface.checkProviderDirectory(1));
    }

}