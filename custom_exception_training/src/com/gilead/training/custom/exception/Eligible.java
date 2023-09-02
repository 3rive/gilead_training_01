package com.gilead.training.custom.exception;

public interface Eligible {

    public boolean isEligibleToVote(VoterCard card);

    public boolean isCitizen(VoterCard card);

    public boolean isUnderAge(int age);

    public boolean canVote(VoterCard card, int age);
}
