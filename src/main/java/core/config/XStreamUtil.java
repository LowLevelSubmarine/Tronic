package core.config;

import com.thoughtworks.xstream.XStream;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class XStreamUtil {

    public static <T extends Config> T loadConfigFrom(File file, T alpha, XStream xStream) {
        try {
            FileReader reader = new FileReader(file);
            return (T) xStream.fromXML(reader);
        } catch (IOException | ClassCastException e1) {
            try {
                FileWriter writer = new FileWriter(file);
                xStream.toXML(alpha, writer);
                return alpha;
            } catch (IOException e2) {
                e2.printStackTrace();
                return null;
            }
        }
    }

}
