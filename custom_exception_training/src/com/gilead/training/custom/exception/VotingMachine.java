package com.gilead.training.custom.exception;

public class VotingMachine implements Eligible{
/*
 Checks if the voter is a citizen of India
 */
    @Override
    public void isCitizen(VoterCard card) throws NotaCitizenOfIndiaException{
        if(!card.getAddress().getCountry().equalsIgnoreCase("India")){
            throw new NotaCitizenOfIndiaException("Voter is not the citizen of India");
        }
    }
    /*
     Checks if the voter is not underage
     */
    @Override
    public void isUnderAge(int  age) throws VoterUnderAgeException{
        if( age < 18) {
            throw new VoterUnderAgeException("Voter is less than 18 Years");
        }
    }


}
