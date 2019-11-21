import net.sf.ntru.encrypt.EncryptionKeyPair;
import net.sf.ntru.encrypt.EncryptionParameters;
import net.sf.ntru.encrypt.EncryptionPublicKey;
import net.sf.ntru.encrypt.NtruEncrypt;

import java.io.*;


public class NTRUContext {

    private EncryptionKeyPair kp;
    private EncryptionPublicKey peer_kp;
    private NtruEncrypt ntru_ctx;

    public NTRUContext(Alias selfIdentity, Alias currentPeer) {
        try {
            InputStream kp_inpt = new FileInputStream(selfIdentity.getPublicKeyUrl());
            InputStream peer_pub_inpt = new FileInputStream(currentPeer.getPublicKeyUrl());
            this.kp = new EncryptionKeyPair(kp_inpt);
            this.peer_kp = new EncryptionPublicKey(peer_pub_inpt);
            this.ntru_ctx = new NtruEncrypt(EncryptionParameters.EES1499EP1);
        } catch (FileNotFoundException e) {
            System.out.println(String.format("Error reading from file: {}", e));
        }
    }

    public NTRUContext(Alias selfIdentity) {
        try {
            InputStream kp_inpt = new FileInputStream(selfIdentity.getPublicKeyUrl());
            this.kp = new EncryptionKeyPair(kp_inpt);
            this.ntru_ctx = new NtruEncrypt(EncryptionParameters.EES1499EP1);
        } catch (FileNotFoundException e) {
            System.out.println(String.format("Error reading from file: {}", e));
        }
    }

    public NTRUContext() {
        this.ntru_ctx = new NtruEncrypt(EncryptionParameters.EES1499EP1);
        this.kp = ntru_ctx.generateKeyPair();
    }

    public NTRUContext(EncryptionKeyPair e) {
        this.kp = e;
    }

    public void writeKeyPair(String file_path) {
        try {
            OutputStream kp_out = new FileOutputStream(file_path);

            kp.writeTo(kp_out);
        } catch (IOException e) {
            System.out.println(String.format("Error writing to file: %s", e));
        }
    }

    public void writePublicKey(String file_path) {
        try {
            OutputStream kp_out = new FileOutputStream(file_path);

            kp.getPublic().writeTo(kp_out);
        } catch (IOException e) {
            System.out.println(String.format("Error writing to file: %s", e));
        }
    }

    public void setPeer_kp(Alias peer) {
        try {
            InputStream peer_pub_inpt = new FileInputStream(peer.getPublicKeyUrl());
            this.peer_kp = new EncryptionPublicKey(peer_pub_inpt);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setPeer_kp(EncryptionPublicKey pub_key) {
        this.peer_kp = pub_key;
    }

    public byte[] encrypt(String input) {
        return ntru_ctx.encrypt(input.getBytes(), this.peer_kp);
    }

    public byte[] decrypt(byte[] enc) {
        return ntru_ctx.decrypt(enc, this.kp);
    }

    public EncryptionPublicKey getKp_pub() {
        return kp.getPublic();
    }

    public EncryptionPublicKey getPeer_kp() {
        return peer_kp;
    }

    public NTRUContext copy() {
        return new NTRUContext(this.kp);
    }
}
