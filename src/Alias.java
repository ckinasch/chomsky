public class Alias {
    private String alias;
    private String publicKeyUrl;
    public NTRUContext ntru_ctx;

    public Alias(String alias, String pub_key_url) {
        this.alias = alias;
        this.publicKeyUrl = pub_key_url;
    }

    public Alias(String alias, NTRUContext ctx) {
        this.alias = alias;
        this.ntru_ctx = ctx;
    }

    public String getAlias() {
        return alias;
    }


    public String getPublicKeyUrl() {
        return this.publicKeyUrl;
    }

    public NTRUContext getNtru_ctx() {
        return ntru_ctx;
    }

    @Override
    public String toString() {
        return alias;
    }

    @Override
    public boolean equals(Object o) {
        if (this.toString().equals(o)) {
            return true;
        } else {
            return false;
        }
    }
}
