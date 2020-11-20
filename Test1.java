import java.math.BigInteger;
import java.util.*;
import java.io.*;

public class Test1 {
    static List<Integer> intToBin(BigInteger n){
        ArrayList<Integer> bin = new ArrayList<>();
        // array to store binary number 
        int[] binNum = new int[1000]; 
   
        // counter for binary array 
        int i = 0; 
        while (n.compareTo(n))  
        { 
            // storing remainder in binary array 
            binNum[i] = n % 2; 
            n = n / 2; 
            i++; 
        }
        for (int j=i-1; j >= 0; j--){
            bin.add(j);
        }
        return bin;
    }
    public static BigInteger ltrmodoExpo(BigInteger a, BigInteger expo, BigInteger modn){
        List<Integer> bin = intToBin(modn);
        int x = bin.size();
        BigInteger y = 1;
        for(int i=x-1;i>0;i--){
            y = y.multiply(y).mod(modn);
            if(bin.get(i)==1){
                y = y.multiply(a).mod(modn);
            }
        }
        return y;
    }
    
    public static void main(String[] args){
        if (args.length > 0){
            a = args [0];
            expo = args[1];
            modn = args[2];
        }
        result = ltrmodoExpo(a, expo, modn);
        System.out.print(result);
    }
}
