package com.example.fatcalapplication.Entity;

public class Credential
{
    private Integer credentialid;
    private String username;
    private String password;
    private String hash;
    private String signupdate;
    private Appuser userid;

    public Credential() {
    }

    public Credential(Integer credentialid) {
        this.credentialid = credentialid;
    }

    public Credential(Integer credentialid, String username, String password, String hash, String signupdate) {
        this.credentialid = credentialid;
        this.username = username;
        this.password = password;
        this.hash = hash;
        this.signupdate = signupdate;
    }

    public Integer getCredentialid() {
        return credentialid;
    }

    public void setCredentialid(Integer credentialid) {
        this.credentialid = credentialid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getSignupdate() {
        return signupdate;
    }

    public void setSignupdate(String signupdate) {
        this.signupdate = signupdate;
    }

    public Appuser getUserid() {
        return userid;
    }

    public void setUserid(Appuser userid) {
        this.userid = userid;
    }
}
