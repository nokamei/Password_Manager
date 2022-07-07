package data;

public class Cipher {

    public static String cipher(String toCipher, int step) {

        char[] tempCipher = toCipher.toCharArray();
        for(int i=0; i<tempCipher.length; i++){
            tempCipher[i] = (char)(tempCipher[i] + step);
        }

        return String.valueOf(tempCipher);
    }
}
