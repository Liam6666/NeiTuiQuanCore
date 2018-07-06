package com.neituiquan.work;

import java.util.ArrayList;
import java.util.List;

public class Test2 {


    public static void main(String[] args){
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        for(Integer i : list){
            System.out.println(i);
        }

        list.set(0,3);
        for(Integer i : list){
            System.err.println(i);
        }
    }
}
