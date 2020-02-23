package com.liuhao.sort;

public class ChoseSort {

    public void choseSort(int[] a,int n){
        if(n<1)return ;

        for(int i=0;i<n;i++){
            int min = a[i];
            int j = i+1;
            int sign = i;
            for(;j<n;j++){
                if(min>a[j]){
                    min = a[j];
                    sign = j;
                }
            }
            a[sign] = a[i];
            a[i] = min;

        }
    }

    public static void main(String[] args) {
        ChoseSort c=  new ChoseSort();
        int[] a = {55,666,1,4,3,32,12,5,6,898};
        c.choseSort(a,a.length);
        for(int i:a){
            System.out.println(i);
        }
    }

}
