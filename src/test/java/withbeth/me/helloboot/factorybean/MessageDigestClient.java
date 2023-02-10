package withbeth.me.helloboot.factorybean;


import java.security.MessageDigest;
import java.util.Arrays;

public class MessageDigestClient {
    private final MessageDigest messageDigest;

    public MessageDigestClient(MessageDigest messageDigest) {
        this.messageDigest = messageDigest;
    }
    public void computeHash(String message) {
        System.out.println("사용 하는 해시 알고리즘 : " + messageDigest.getAlgorithm());
        byte[] input = message.getBytes();
        byte[] output = messageDigest.digest(input);
        System.out.println("input : " + Arrays.toString(input));
        System.out.println("output: " + Arrays.toString(output));
    }
}
