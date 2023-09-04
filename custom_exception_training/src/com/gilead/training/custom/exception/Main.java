package com.gilead.training.custom.exception;

public class Main {
    public static void main(String[] args) {
        System.out.println("Start Program");
        InformationLoader informationLoader = new InformationLoader();
        VoterCard bevin = informationLoader.createVoter();
        VotingMachine votingMachine = new VotingMachine();
        try {
            System.out.println("Bevin is being checked if he can vote");
            votingMachine.isCitizen(bevin);
            System.out.println("Check for citizen passed!!!"); //not exe
            votingMachine.isUnderAge(bevin.getAge());//not exe
            System.out.println("Check of Age passed!!!");//not exe
            System.out.println("Bevin has casted his vote");//not exe

        } catch (NotaCitizenOfIndiaException | VoterUnderAgeException exception) {
            exception.printStackTrace();
        }
    }
}