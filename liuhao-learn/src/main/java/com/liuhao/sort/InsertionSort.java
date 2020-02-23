package com.liuhao.sort;

/**
 * 插入排序
 */
public class InsertionSort {

    public void insertionSort(int[] a,int n){
        if(n<1)return ;
        for(int i=1;i<n;i++){
            int value = a[i];
            int j = i - 1;
            for(;j>=0;j--){
                if(value < a[j]){
                    a[j+1]=a[j];
                }else{
                    break;
                }
            }
            a[j+1]=value;
        }
    }

    public static void main(String[] args) {
        InsertionSort i = new InsertionSort();
        int[] a = {4,5,6,7,4,3,9,33,44,555};
        i.insertionSort(a,a.length);
        for(int inta:a){
            System.out.println(inta);
        }
    }
}
