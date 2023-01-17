
import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class test {
    public static void main(String args[]) throws IOException{
        compile();
    }
    private static void compile(){
        try {
            if(new File("select.class").exists()){
                new File("select.class").delete();
            }
            Thread.sleep(2000);
            com.sun.org.apache.xalan.internal.xslt.Process._main(new String[]{"-XSLTC", "-IN", "1.xml", "-XSL", "select.xslt", "-SECURE", "-XX", "-XT"});
        }catch (Exception e){}
    }

}
