package com.ICNH.chocan;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class ManagerInterfaceTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    DatabaseInterface database;
    ManagerInterface Manface;


    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        database = new DatabaseInterface();
        Manface = new ManagerInterface(database);
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    void testGetValidProviderReturn() {

        String input = "x";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

       // int output = Manface.getValidProvider();

        //String content = outContent.toString();
        //String differentName = "Invalid Number. Provider ID's are positive numerals.";

        assertEquals(-1, Manface.getValidProvider());
    }
}