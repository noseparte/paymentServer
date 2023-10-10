package com.lung.util.fileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileContentUtil {
	
	/**
	 * 读取文件内容
	 * 
	 * @param path 文件在项目中的路径
	 * @return
	 * @throws Exception
	 */
    public static String readFile(String path) throws Exception {
        File file=new File(path);
        FileInputStream fis = new FileInputStream(file);
        FileChannel fc = fis.getChannel();
        ByteBuffer bb = ByteBuffer.allocate(new Long(file.length()).intValue());
        //fc向buffer中读入数据
        fc.read(bb);
        bb.flip();
        String str=new String(bb.array(),"UTF8");
        fc.close();
        fis.close();
        return str;
    }
    
}
