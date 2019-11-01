import net.sf.ntru.*;
import net.sf.ntru.encrypt.EncryptionKeyPair;
import net.sf.ntru.encrypt.NtruEncrypt;

import java.security.KeyPair;

public class NTRUContext {

    public EncryptionKeyPair kp;
    public EncryptionKeyPair peer_kp;


    public NTRUContext(String kpURL, Peer )
    {

    }

    public String encrypt(String input)
    {
        return ntru.encrypt(input.getBytes(), this.peer_kp.getPublic());
    }

    public String decrypt(String enc)
    {
        return ntru.decrypt(enc, this.kp);
    }
}
