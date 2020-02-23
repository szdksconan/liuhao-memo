package com.liuhao.sort;

public class MergeSort {

    public void mergeSort(int[] a,int n){
        mergeSortT(a,0,n-1);
    }


    /**
     * 递归    分割-》排序->合并排序
     */
    private void mergeSortT(int[] a,int s,int e){
        if(s>=e) return;
        int mid = (s+e)/2;
        mergeSortT(a,s,mid);
        mergeSortT(a,mid+1,e);

        int[] temp = new int[e-s+1];
        int  t = 0;
        int i=s,j=mid+1;
        for(;i<=mid&&j<=e;){
            if(a[i]<=a[j]){
                temp[t++] = a[i++];
            }else{
                temp[t++] = a[j++];
            }
        }

        while(t<e-s+1){
            //继续写入没有读完的数组
            if(i<=mid){
                temp[t++] = a[i++];
            }else{
                temp[t++] = a[j++];
            }

        }




        for(int k=0;k<temp.length;k++){
            a[s+k] = temp[k];
        }

    }


    public static void main(String[] args) {
        MergeSort m = new MergeSort();
        int[] a = {5,6,7,8,8,9,3,44,43,3232,555,2,3,4,123,56,87,62,75,85,87,55,732,32};
        m.mergeSort(a,a.length);
        for(int i:a){
            System.out.println(i);
        }

    }


}
