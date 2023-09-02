package com.gilead.training.custom.exception;

public class VotingMachine implements Eligible{

    @Override
    public boolean isEligibleToVote(VoterCard card) {
        return false;
    }
/*
 Checks if the voter is a citizen of India
 */
    @Override
    public boolean isCitizen(VoterCard card) {
            System.out.println(card.getAddress().getCountry());
            if(card.getAddress().getCountry().equalsIgnoreCase("India")){
                return true;
            }

        return false;
    }
    /*
     Checks if the voter is not underage
     */
    @Override
    public boolean isUnderAge(int  age) {
        if(age< 18){
            return true;
        }
        return false;
    }

    @Override
    public boolean canVote(VoterCard card, int age)  {
        return !isUnderAge(age) && isCitizen(card);
    }

}
