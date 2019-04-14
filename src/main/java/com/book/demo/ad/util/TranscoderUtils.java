package com.book.demo.ad.util;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class TranscoderUtils {

    public static byte[] encodeObject(Object o) {
        return compress(serialize(o));
    }

    public static Object decodeObject(byte[] b) {
        return deserialize(decompress(b));
    }

    private static byte[] compress(byte[] in) throws RuntimeException {
        if (in == null) {
            throw new NullPointerException("Can't compress null");
        }
        ByteArrayOutputStream bos = null;
        GZIPOutputStream gz = null;
        try {
            bos = new ByteArrayOutputStream();
            gz = new GZIPOutputStream(bos);
            gz.write(in);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("IO exception compressing data", e);
        } finally {
            CloseUtil.close(gz);
            CloseUtil.close(bos);
        }
        return bos.toByteArray();
    }

    private static byte[] decompress(byte[] in) throws RuntimeException {
        if (in != null) {
            ByteArrayOutputStream byteArrayOutputStream = null;
            ByteArrayInputStream byteArrayInputStream = null;
            GZIPInputStream gzipInputStream = null;
            try {
                byteArrayInputStream = new ByteArrayInputStream(in);
                byteArrayOutputStream = new ByteArrayOutputStream();
                gzipInputStream = new GZIPInputStream(byteArrayInputStream);
                byte[] buf = new byte[8192];
                int r = -1;
                while (((r = gzipInputStream.read(buf))) > 0) {
                    byteArrayOutputStream.write(buf, 0, r);
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("IO exception decompress data", e);
            } finally {
                CloseUtil.close(gzipInputStream);
                CloseUtil.close(byteArrayInputStream);
                CloseUtil.close(byteArrayOutputStream);
            }
            return byteArrayOutputStream.toByteArray();
        } else {
            return null;
        }
    }

    private static byte[] serialize(Object o) {
        if (o == null) {
            throw new NullPointerException("Can't serialize null");
        }

        ByteArrayOutputStream byteArrayOutputStream = null;
        ObjectOutputStream objectOutputStream = null;

        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(o);
        } catch (IOException e) {
            e.printStackTrace();
            throw  new RuntimeException("Non-serialize object", e);
        } finally {
            CloseUtil.close(byteArrayOutputStream);
            CloseUtil.close(objectOutputStream);
        }

        return byteArrayOutputStream.toByteArray();
    }

    private static Object deserialize(byte[] in) {
        if (in != null) {
            ByteArrayInputStream byteArrayInputStream = null;
            ObjectInputStream objectInputStream = null;
            Object obj = null;

            try {
                byteArrayInputStream = new ByteArrayInputStream(in);
                objectInputStream = new ObjectInputStream(byteArrayInputStream);
                obj = objectInputStream.readObject();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally {
                CloseUtil.close(byteArrayInputStream);
                CloseUtil.close(objectInputStream);
            }
            return obj;
        }
        return null;
    }
}
