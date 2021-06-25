package ru.itmo.p3114.s312198.authentication;

import ru.itmo.p3114.s312198.commands.actions.AbstractCommand;
import ru.itmo.p3114.s312198.commands.results.CommandResult;
import ru.itmo.p3114.s312198.commands.results.Status;
import ru.itmo.p3114.s312198.io.ConsoleReader;
import ru.itmo.p3114.s312198.transmission.AuthenticationRequest;
import ru.itmo.p3114.s312198.transmission.User;

import java.io.IOException;
import java.util.ArrayList;

public class Register extends AuthenticationCommand {
    public Register() {
        super("register", "", "Use it to register");
    }

    @Override
    public CommandResult execute() {
        ArrayList<String> output = new ArrayList<>();
        return new CommandResult(Status.OK, output);
    }

    @Override
    public AuthenticationRequest formRequest(ConsoleReader consoleReader, Boolean suppressOutput) {
        String username;
        String credentials1, credentials2;
        try {
            if (!suppressOutput) {
                System.out.println("Enter your username: ");
            }
            username = consoleReader.flexibleConsoleReadLine();
            if (!suppressOutput) {
                System.out.println("Enter your password: ");
            }
            credentials1 = SHA1(username + consoleReader.flexibleConsoleReadPassword());
            if (!suppressOutput) {
                System.out.println("Enter your password one more time: ");
            }
            credentials2 = SHA1(username + consoleReader.flexibleConsoleReadPassword());
            if (credentials1 != null && credentials1.equals(credentials2)) {
                return new AuthenticationRequest("REG", new User(username, credentials1, null));
            } else {
                if (!suppressOutput) {
                    System.out.println("Passwords are different");
                }
                return null;
            }
        } catch (IOException ioException) {
            System.out.println("An unexpected IOException occurred");
            return null;
        }
    }

    @Override
    public AbstractCommand clone() {
        ArrayList<String> cloneArguments = new ArrayList<>(arguments);
        Register commandClone = new Register();
        commandClone.setArguments(cloneArguments);
        commandClone.setOwner(owner);
        return commandClone;
    }
}
