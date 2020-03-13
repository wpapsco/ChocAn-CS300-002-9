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

    // getValidProvider Tests
    @Test // Test that getValidProvider handles x input correctly
    void testGetValidProviderReturn() {
        String input = "x";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        assertEquals(-1, Manface.getValidProvider());
    }
    @Test // Test that getValidProvider handles "0" input correctly
    void testGetValidProviderZero() {
        String input = "0\nx";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        assertEquals(-1, Manface.getValidProvider());
    }
    @Test // Test that getValidProvider handles negative input correctly
    void testGetValidProviderNegative() {
        String input = "-24\nx";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        assertEquals(-1, Manface.getValidProvider());
    }
    @Test // Test that getValidProvider handles valid input correctly
    void testGetValidProviderValid() {
        String input = "1\nx";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        assertEquals(1, Manface.getValidProvider());
    }

    // getValidMember Tests
    @Test // Test that getValidMember handles x input correctly
    void testGetValidMemberReturn() {
        String input = "x";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        assertEquals(-1, Manface.getValidMember());
    }
    @Test // Test that getValidMember handles "0" input correctly
    void testGetValidMemberZero() {
        String input = "0\nx";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        assertEquals(-1, Manface.getValidMember());
    }
    @Test // Test that getValidMember handles negative input correctly
    void testGetValidMemberNegative() {
        String input = "-24\nx";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        assertEquals(-1, Manface.getValidMember());
    }
    @Test // Test that getValidMember handles valid input correctly
    void testGetValidMemberValid() {
        String input = "1\nx";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        assertEquals(1, Manface.getValidMember());
    }
}