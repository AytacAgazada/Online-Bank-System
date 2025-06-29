import java.security.SecureRandom;
import java.util.Base64;

public class JwtSecretKeyGenerator {
    public static void main(String[] args) {
        // 512-bit (64 byte) güclü açar yaratmaq
        SecureRandom secureRandom = new SecureRandom();
        byte[] keyBytes = new byte[64]; // 64 bayt = 512 bit
        secureRandom.nextBytes(keyBytes);

        // Base64 formatına kodlamaq
        String base64Key = Base64.getEncoder().encodeToString(keyBytes);

        System.out.println("Sizin təhlükəsiz JWT açarınız (Base64):");
        System.out.println(base64Key);
    }
}