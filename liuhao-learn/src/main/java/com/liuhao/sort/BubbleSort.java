package com.liuhao.sort;

public class BubbleSort {


    public void bubbleSort(int[] a,int n){
        if(n<1) return ;

        for(int i=0;i<n;i++){
            boolean flag = false;
            for(int j=0 ;j<n-i-1;j++){
                if(a[j]>a[j+1]){
                    int tmp = a[j];
                    a[j] = a[j+1];
                    a[j+1] = tmp;
                    flag = true;
                }
            }
            if(!flag) break;
        }



    }

    public static void main(String[] args) {
        int[] a = {4,5,6,7,8,9,0,6};
        BubbleSort b = new BubbleSort();
        b.bubbleSort(a,a.length);
        for(int i:a){
            System.out.print(i+" ");
        }
    }
}
