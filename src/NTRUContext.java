import net.sf.ntru.encrypt.EncryptionKeyPair;
import net.sf.ntru.encrypt.EncryptionParameters;
import net.sf.ntru.encrypt.NtruEncrypt;

import java.io.*;


public class NTRUContext {

    private EncryptionKeyPair kp;
    private EncryptionKeyPair peer_kp;
    private NtruEncrypt ntru_ctx;

    public NTRUContext(Alias selfIdentity, Alias currentPeer)
    {
        try
        {
            InputStream kp_inpt = new FileInputStream(selfIdentity.getPublicKeyUrl());
            InputStream peer_pub_inpt = new FileInputStream(currentPeer.getPublicKeyUrl());
            this.kp = new EncryptionKeyPair(kp_inpt);
            this.peer_kp = new EncryptionKeyPair(peer_pub_inpt);
            this.ntru_ctx = new NtruEncrypt(EncryptionParameters.EES1499EP1);
        }
        catch (FileNotFoundException e)
        {
            System.out.println(String.format("Error reading from file: {}", e));
        }
    }

    public NTRUContext()
    {
        this.ntru_ctx = new NtruEncrypt(EncryptionParameters.EES1499EP1);
    }

    public void writeKeyPair(String file_path)
    {
        EncryptionKeyPair kp = this.ntru_ctx.generateKeyPair();

        try
        {
                OutputStream kp_otpt = new FileOutputStream(file_path);

                kp.writeTo(kp_otpt);
        }
        catch (IOException e)
        {
            System.out.println(String.format("Error writing to file: {}", e));
        }
    }

    public byte[] encrypt(String input)
    {
        return ntru_ctx.encrypt(input.getBytes(), this.peer_kp.getPublic());
    }

    public byte[] decrypt(byte[] enc)
    {
        return ntru_ctx.decrypt(enc, this.kp);
    }
}
