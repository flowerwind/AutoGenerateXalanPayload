
import org.apache.xalan.xsltc.compiler.XSLTC;
import org.xml.sax.InputSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class test {
    public static void main(String args[]) throws IOException{
        InputStream inputStream=new FileInputStream("select.xslt");
        InputSource inputSource=new InputSource(inputStream);
        new XSLTC().compile(null,inputSource,0);
    }


    public static boolean compare(byte[] a , byte[] b){
        // 首先比较数组长度，如果长度不相同，数组内容肯定不相同，返回false
        if(a.length != b.length){
            return false;
        }

        // 其次遍历，比较两个数组中的每一个元素，只要有元素不相同，返回false

        for(int i = 0; i < a.length ; i++){   // a数组循环变遍历
            if(a[i] != b[i]){                // 判断a数组  是否相同 与 b数组
                return false;                // 不相同返回值false
            }
        }

        // //最后循环遍历结束后，返回true
        return true;    // 数组相同  返回值trun

    }

    private static void remoteArr(String[] testArr){
        String[] temp=new String[testArr.length-1];
        for(int i=0;i<testArr.length-1;i++){
            temp[i]=testArr[i];
        }
        testArr=temp;
        //System.out.println("function:"+testArr);
    }
}
