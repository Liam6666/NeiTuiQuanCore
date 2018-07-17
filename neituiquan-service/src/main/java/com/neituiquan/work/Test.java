package com.neituiquan.work;

public class Test {


    public static void main(String[] args){
        String path = "/storage/emulated/0/PictureSelector/CameraImage/PictureSelector_20180717_085208.JPEG";
        String title = "PictureSelector_20180717_085208";

        int index = path.indexOf(title);
        System.out.println(index);
        String smilpePath = path.substring(0,index);
        System.out.println(smilpePath);
        String parentPath = smilpePath.substring(0,smilpePath.lastIndexOf("/"));
        System.out.println(parentPath.substring(parentPath.lastIndexOf("/") + 1,parentPath.length()));
    }
}


