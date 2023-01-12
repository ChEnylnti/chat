package com.hbnu.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class BannerUtil {
    public static void getBanner(String path) throws IOException {
        FileInputStream fis = new FileInputStream(path);
        // 防止路径乱码   如果utf-8 乱码  改GBK     eclipse里创建的txt  用UTF-8，在电脑上自己创建的txt  用GBK
        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
        BufferedReader br = new BufferedReader(isr);
        String line = "";
        while ((line = br.readLine()) != null) {
            // 如果 t x t文件里的路径 不包含---字符串       这里是对里面的内容进行一个筛选
            //if (line.lastIndexOf("---") < 0) {
                System.out.println(line);
            //}
        }
        br.close();
        isr.close();
        fis.close();
    }
}
