package data;

public class Cipher {

    public static String cipher(String toCipher){
        char[] tempCipher=toCipher.toCharArray();
        for(int i = 0; i < tempCipher.length; i++){
            tempCipher[i]=(char)(tempCipher[i]+1);
        }
        return String.valueOf(tempCipher);
    }

    public static String decipher(String toDecipher){
        char[] tempDecipher=toDecipher.toCharArray();
        for(int i = 0; i < tempDecipher.length; i++){
            tempDecipher[i]=(char)(tempDecipher[i]-1);
        }
        return String.valueOf(tempDecipher);
    }
}
