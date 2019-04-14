package com.book.demo.ad.util;

import java.io.Closeable;
import java.io.IOException;

public class CloseUtil {

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
