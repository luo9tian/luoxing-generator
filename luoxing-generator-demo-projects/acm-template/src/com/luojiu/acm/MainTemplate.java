package com.luojiu.acm;
import java.util.Scanner;
public class MainTemplate {
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        while (scanner.hasNext()){
            int n=scanner.nextInt();
            int[]arr=new int[];
            for(int i=0;i<n;i++) {
                arr[i] = scanner.nextInt();
            }
            int sum=0;
            for(int num:arr){
                sum+=arr;
            }
            System.out.println("Sum: "+sum);
        }
    }
}