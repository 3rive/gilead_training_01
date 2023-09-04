package com.gilead.training.custom.exception;

public interface Eligible {

    public void isCitizen(VoterCard card) throws NotaCitizenOfIndiaException;

    public void isUnderAge(int age) throws VoterUnderAgeException;
}
