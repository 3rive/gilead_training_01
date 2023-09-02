package com.gilead.training.custom.exception;

public class VoterCard extends Person{ //is-a

    private String voterID;

    public String getVoterID() {
        return voterID;
    }

    public void setVoterID(String voterID) {
        this.voterID = voterID;
    }
}
