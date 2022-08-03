package net.defade.bismuth.core.exceptions;

public class PasswordInvalidException extends Exception {
    public PasswordInvalidException() {
        super("Wrong password.");
    }
}
