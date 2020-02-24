package com.liuhao.sort;

public class QuickSort {


    public void quickSort(int[] a,int n){
        quickSortT(a,0,a.length);
    }

    public void quickSortT (int[] a,int s,int e){
        if(s>=e) return;
        int valueSign = s+((e-s)/2);
        int value = a[valueSign];//随便取个中间位置的值
        int i=s;
        int j=s;
        for(;i<e;i++){
            if(a[i]<value){
                if(valueSign == j){//重新设置中间点的位置
                    valueSign = i;
                }
                int b = a[j];
                a[j++] = a[i];
                a[i] = b;
            }
        }
        if(j<=valueSign) {
            int c = a[j];
            a[j] = value;
            a[valueSign] = c;
        }/*else{
            int c = a[j];
            a[j] = a[i-1];
            a[i-1] =  c;
        }*/

        quickSortT(a,s,j);
        quickSortT(a,j+1,e);

    }

    public static void main(String[] args) {
        QuickSort q = new QuickSort();
        int[] a = {5,6,7,8,8,9,3,44,43,3232,555,2,3,4,123,56,87,62,75,85,87,55,732,32};
        q.quickSort(a,a.length);
        for(int i:a){
            System.out.println(i);
        }

    }

}
