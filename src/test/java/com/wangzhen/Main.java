package com.wangzhen;
import org.springframework.aop.scope.ScopedProxyUtils;

import java.util.Scanner;
/**
 * @Author wangzhen
 * @Descannerription
 * @CreateDate 2020/2/3 16:43
 */
public class Main {//
    private static Scanner scanner = new Scanner(System.in);
    public static void main(String[] a) {
//        int N = scanner.nextInt(), Q = scanner.nextInt();
//        BNode tree[] = new BNode[N+2];
//        for (int i = 1; i <= N; i++) tree[i] = new BNode(i);//设置当前版本 }
//        for(int i = 1; i < N; i++){//输入N-1行
//            int u = scanner.nextInt();
//            int v = scanner.nextInt();
//            tree[u].setChildNode(tree[v]);//u的孩子是v
//        }
//        while(Q-- > 0){
//            int x = scanner.nextInt();
//            int y = scanner.nextInt();
//            System.out.println(search(x,y,tree));
//        }scanner.close();
    }
    public static String search(int x, int y,BNode tree[]){
        if(x == y) return "YES";
        if(x > y ) return "NO";//祖先的编号一定小于当前编号
        return has(tree[x],y);//剩下 x < y 的情况 从x节点开始进行广度优先遍历
    }
    public static String has(BNode xNode, int y){
        if(xNode == null) return "NO";
        if(xNode.leftChildNode == null && xNode.rightChildNode == null) return "NO";
        if(xNode.leftChildNode != null && xNode.leftChildNode.currentNum!= -1 && xNode.leftChildNode.currentNum == y) return "YES";
        if(xNode.rightChildNode != null && xNode.rightChildNode.currentNum!= -1 && xNode.rightChildNode.currentNum == y) return "YES";
        if(has(xNode.leftChildNode,y).equals("NO"))
            return has(xNode.rightChildNode,y);
        return "YES";
    }
}
class BNode{
    int currentNum = -1;//当前版本号
    BNode leftChildNode = null;//孩子版本
    BNode rightChildNode = null;
    public BNode() {}
    public BNode(int currentNum) {
        this.currentNum = currentNum;
    }
    public void setChildNode(BNode child){
        if(leftChildNode == null) leftChildNode = child;
        else rightChildNode = child;
    }
}