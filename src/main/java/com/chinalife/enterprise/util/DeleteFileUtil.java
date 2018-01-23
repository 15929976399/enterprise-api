package com.chinalife.enterprise.util;

import java.io.File;

/**
 * Created by Admin on 2018/1/18.
 */
public class DeleteFileUtil {
    /**
     * 删除文件，可以是文件或文件夹
     * @param fileName 要删除的文件名
     * @return 删除成功返回true，否则返回false
     */
    public static boolean delete(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("删除文件失败:" + fileName + "不存在！");
            return false;
        } else {
            if (file.isFile()){
                return    file.delete();
            }else{
                return true;//deleteDirectory(fileName);
            }

        }
    }
}
