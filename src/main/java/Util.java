import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Util {
    private static int num=133860;
    private static String[] beforeTwoConstant=new String[]{"atB","atC","atD","atE","atF","atG","atH","atI","atJ","atK","atL","atM","atN","atO","atP","atQ","atR","atS","atT","atU","atV","atW","atX","atY","atZ","aua","aub","auc","aud","aue","auf","aug","auh","aui","auj","auk","aul","aum","aun","auo","aup","auq","aur","aus","aut","auu"};
    private static String[] beforeOneConstant=new String[]{"<xsl:value-of select='ceiling(133830)'/>","<xsl:value-of select='ceiling(133831)'/>"};
    //t914-t1049
    private static String[] afterTwoConstant=new String[]{};
    public static String[] afterOneConstant=new String[]{"<xsl:value-of select='ceiling(133840)'/>","<xsl:value-of select='ceiling(133841)'/>","<xsl:value-of select='ceiling(133842)'/>"};

    static {
        ArrayList arrayList=new ArrayList();
        for(int i=914;i<=1049;i++){
            arrayList.add("t"+String.valueOf(i));
        }
        afterTwoConstant=(String[])arrayList.toArray(new String[arrayList.size()]);
    }
    public static byte[] readFromSourceFile(String file) throws IOException {
        byte[] bytes=new byte[1024];
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        FileInputStream fileInputStream=new FileInputStream(file);
        while (true){
            int len=fileInputStream.read(bytes,0,1024);
            if(len==-1){
                break;
            }else{
                byteArrayOutputStream.write(bytes,0,len);
            }
        }
        fileInputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    public static String byteToHexString(byte a){
        String hexString=Integer.toHexString((int)a&0xFF);
        if(hexString.length()<2){
            int chazhi=2-hexString.length();
            while (chazhi>0){
                hexString="0"+hexString;
                chazhi-=1;
            }
        }
        return hexString;
    }

    /**
     * 截取byte数组   不改变原数组
     * @param b 原数组
     * @param off 偏差值（索引）
     * @param length 长度
     * @return 截取后的数组
     */
    public static byte[] subByte(byte[] b,int off,int length){
        byte[] b1 = new byte[length];
        System.arraycopy(b, off, b1, 0, length);
        return b1;
    }

    public static String toDoubleConstant(String hex){
        String template="<xsl:value-of select='ceiling(%s)'/>\n";
        String payload="import struct\n" +
                "import decimal\n" +
                "\n" +
                "ctx = decimal.Context()\n" +
                "ctx.prec = 20\n" +
                "def to_double(b):\n" +
                "    f = struct.unpack('>d', struct.pack('>Q', b))[0]\n" +
                "    d1 = ctx.create_decimal(repr(f))\n" +
                "    return format(d1, 'f')\n" +
                "\n" +
                "result=to_double(%s)";
//        PythonInterpreter interpreter = new PythonInterpreter();
//        interpreter.exec(String.format(payload,content));
//        return String.format(template,interpreter.get("result").toString());
        hex=hex.substring(2);
        long longBits=Long.valueOf(hex,16).longValue();
        String doubleBits=String.valueOf(Double.longBitsToDouble(longBits));
        int num1=Integer.valueOf(doubleBits.substring(doubleBits.indexOf("-")+1));
        int num2=doubleBits.substring(0,doubleBits.indexOf("-")).length()-(doubleBits.substring(0,doubleBits.indexOf(".")).length()+2);
        return String.format(template,String.format("%"+String.format(".%sf",num1+num2),Double.longBitsToDouble(longBits)));
    }
    public static String bytesToHexString(byte[] a){
        String content="";
        for(byte b:a){
            content+=byteToHexString(b);
        }
        return content;
    }

    public static String generateChangLiang(int quantity){
        String content="";
        String templeate="<xsl:value-of select='ceiling(%s)'/>";
        for(int i=0;i<quantity;i++){
            String temp=String.format(templeate,String.valueOf(num))+"\n\r";
            num+=1;
            content+=temp;
        }
        return content;
    }

    public static String deleteConstant(String content,String BOA,int num){
        if(num/4>0){
            int fourNum=num/4;
            content=deleteFourContent(content,BOA,fourNum);
            int leftNum=num%4;
            if(leftNum/2>0){
                int twoNum=leftNum/2;
                content=deleteTwoContent(content,BOA,twoNum);
                leftNum=leftNum%2;
                //leftNum求余2的结果，不等于0的话那么就是1了
                if(leftNum!=0){
                    content=deleteOneContant(content,BOA);
                }
            }else{
                if(leftNum!=0){
                    content=deleteOneContant(content,BOA);
                }
            }
        }else if(num/2>0){
            int twoNum=num/2;
            content=deleteTwoContent(content,BOA,twoNum);
            int leftNum=num%2;
            if(leftNum!=0){
                content=deleteOneContant(content,BOA);
            }
        }else{
            if(num!=0){
                content=deleteOneContant(content,BOA);
            }
        }
        return content;
    }

    public static String deleteOneContant(String content,String BOA){
        if(BOA.equals("before")){
            String[] newOneConstant=new String[beforeOneConstant.length-1];
            for(int i=0;i< beforeOneConstant.length-1;i++){
                newOneConstant[i]=beforeOneConstant[i];
            }
            content=content.replace(beforeOneConstant[beforeOneConstant.length-1],"");
            beforeOneConstant=newOneConstant;
        }else{
            String[] newOneConstant=new String[afterOneConstant.length-1];
            for(int i=0;i< afterOneConstant.length-1;i++){
                newOneConstant[i]=afterOneConstant[i];
            }
            content=content.replace(afterOneConstant[afterOneConstant.length-1],"");
            afterOneConstant=newOneConstant;
        }
        return content;
    }

    /**
     *
     * @param content
     * @param BOA 删除before的还是after的
     * @param num 需要删除num个两个常量池的内容
     * @return
     */
    private static String deleteTwoContent(String content,String BOA,int num){
        if(BOA.equals("before")){
            ArrayList newTwoConstant=new ArrayList();
            for(int i=0;i< beforeTwoConstant.length;i++){
                if(num>0&&i%2!=0){
                    num--;
                    content=content.replace(beforeTwoConstant[i], "");
                    continue;
                }
                newTwoConstant.add(beforeTwoConstant[i]);
            }
            beforeTwoConstant = (String[])newTwoConstant.toArray(new String[newTwoConstant.size()]);
        }else{
            ArrayList newTwoConstant=new ArrayList();
            for(int i=0;i< afterTwoConstant.length;i++){
                if(num>0&&i%2!=0){
                    num--;
                    content=content.replace(afterTwoConstant[i], "");
                    continue;
                }
                newTwoConstant.add(afterTwoConstant[i]);
            }
            afterTwoConstant = (String[])newTwoConstant.toArray(new String[newTwoConstant.size()]);
        }
        return content;
    }

    private static String deleteFourContent(String content,String BOA,int num){
        if(BOA.equals("before")){
            ArrayList<String> arrayList = new ArrayList(Arrays.asList(beforeTwoConstant));
            for(int i=0;i<num;i++){
                String one=arrayList.get(0);
                String two=arrayList.get(1);
                String result= String.format("%s='%s'", one,two);
                content=content.replace(result,"");
                arrayList.remove(0);
                arrayList.remove(0);
            }
        }else{
            ArrayList<String> arrayList = new ArrayList(Arrays.asList(afterTwoConstant));
            for(int i=0;i<num;i++){
                String one=arrayList.get(0);
                String two=arrayList.get(1);
                String result= String.format("%s=\"%s\"", one,two);
                content=content.replace(result,"");
                arrayList.remove(0);
                arrayList.remove(0);
            }
        }
        return content;
    }

    public static int byteIndexChar(byte[] bytes,char[] chars){
        for(int i=0;i<bytes.length;i++){
            int num=0;
            if(bytes[i]==(byte) chars[0]){
                for(int j=0;j<chars.length;j++){
                    if(bytes[i+j]!=(byte) chars[j]){
                        break;
                    }
                    num++;
                }
                if(num==chars.length){
                    return i;
                }
            }
        }
        return -1;
    }

    public static String getNextLineContent(String content,String posision){
        int idx=content.indexOf(posision);
        int one=content.indexOf("\r\n",idx);
        int two=content.indexOf("\r\n",one+2);
        String part=content.substring(one+2,two);
        return part;
    }

    public static String convertToStringAndPadding(int num,int len){
        String hexString=Integer.toHexString(num);
        String paddingHexString="";
        if(len>=hexString.length()){
            int i=len-hexString.length();
            for(int k=0;k<i;k++){
                paddingHexString+="0";
            }
        }else{
            System.out.println("padding Error!");
        }
        paddingHexString=paddingHexString+hexString;
        return paddingHexString;
    }

    public static String replaceXsltToDoubleConstant(String content,String posision,String modifyPosision){
        String nextLineContent=Util.getNextLineContent(content,posision);
        content=content.replace(nextLineContent,Util.toDoubleConstant(modifyPosision));
        return content;
    }

    public static boolean compareBytesEqual(byte[] a , byte[] b){
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

//    public String hexStringToDouble(String hex){
//        hex=hex.substring(2);
//        long longBits=Long.valueOf(hex,16).longValue();
//        String doubleBits=String.valueOf(Double.longBitsToDouble(longBits));
//        int num1=Integer.valueOf(doubleBits.substring(doubleBits.indexOf("-")+1));
//        int num2=doubleBits.substring(0,doubleBits.indexOf("-")).length()-(doubleBits.substring(0,doubleBits.indexOf(".")).length()+2);
//        return String.format("%"+String.format(".%sf",num1+num2),Double.longBitsToDouble(longBits));
//    }
}
