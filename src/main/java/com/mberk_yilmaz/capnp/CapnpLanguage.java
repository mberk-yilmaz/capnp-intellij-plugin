package com.mberk_yilmaz.capnp;

import com.intellij.lang.Language;

public class CapnpLanguage extends Language {
    public static final CapnpLanguage INSTANCE = new CapnpLanguage();

    private CapnpLanguage() {
        super("CapnProto");
    }
}

