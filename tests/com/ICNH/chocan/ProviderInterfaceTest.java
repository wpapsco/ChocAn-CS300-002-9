package com.ICNH.chocan;

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
    // TODO: testCheckIDValid, testCheckIDSuspended. I need the database interface remove function for these, so I can add & remove a test member

    // logService Tests
    @Test // Tests that logService quits if user quits checkID
    void testLogServiceReturn(){
        String input = "x";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        assertEquals(false, Provface.logService());
    }
    @Test // Tests that logService quits if checkID receives inappropriate input
    void testLogServiceInvalidID(){
        String input = "HelloWorld\nx";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        assertEquals(false, Provface.logService());
    }
    // TODO: testLogServiceSuspend, testLogServiceValid_XDate, testLogServiceValid_FutureDate, testLogServiceValid_XServiceCode,
    //  testLogServiceValid_ServiceNotFound, testLogServiceValid_ServiceIDZero, testLogServiceValid_ServiceIDNegative,
    //  testLogServiceValid_HappyPath
    //  I need the database interface remove functions for these, so I can add & remove a test member

    //
}