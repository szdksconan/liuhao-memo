package com.liuhao.base;

import java.util.HashMap;

public class BinarySearchTree {

    int value;
    BinarySearchTree left;
    BinarySearchTree right;

    public BinarySearchTree(int value) {
        this.value = value;
    }

    //构建二叉树
    public void insert(BinarySearchTree root,int value){
        if(root.value>value) {
            if (root.left == null) {
                root.left = new BinarySearchTree(value);
            } else {
                insert(root.left,value);
            }
        }else{
            if (root.right == null) {
                root.right = new BinarySearchTree(value);
            } else {
                insert(root.right,value);
            }
        }
    }


    public void prePrint(BinarySearchTree root){
        if(root!=null){
            System.out.print(root.value);
            prePrint(root.left);
            prePrint(root.right);
        }
    }

    public void inPrint(BinarySearchTree root){
        if(root!=null){
            inPrint(root.left);
            System.out.print(root.value);
            inPrint(root.right);
        }
    }

    public void endPrint(BinarySearchTree root){
        if(root!=null){
            endPrint(root.left);
            endPrint(root.right);
            System.out.print(root.value);

        }
    }

    public static void main(String[] args) {
        BinarySearchTree root = new BinarySearchTree(5);
        for (int i = 0; i < 10; i++) {
            if(i==5){
                continue;
            }
            root.insert(root,i);
        }

        root.prePrint(root);
        System.out.println("\n");
        root.inPrint(root);
        System.out.println("\n");
        root.endPrint(root);
        System.out.println("\n");
    }


}
