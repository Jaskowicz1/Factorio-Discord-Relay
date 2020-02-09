package me.jaskowicz.factoriodiscordrelay.Exceptions;

import java.io.IOException;

public class MalformedPacketException extends IOException {

    public MalformedPacketException(String message) {
        super(message);
    }

}

