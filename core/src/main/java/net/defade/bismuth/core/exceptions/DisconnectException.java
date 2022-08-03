package net.defade.bismuth.core.exceptions;

import java.net.SocketException;

public class DisconnectException extends SocketException {
    public DisconnectException() {
        super("Disconnected from the server.");
    }
}
