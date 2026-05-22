import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
public class CreaTuContrasenia {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode("miAdmin123.");
        System.out.println(hash);
    }
}

