package com.gilead.training.custom.exception;

public class InformationLoader {
    public VoterCard createVoter(){
        VoterCard bevinVoteCard = new VoterCard();
        bevinVoteCard.setVoterID("12345");
        Address bevinAddress = new Address();
        bevinAddress.setCountry("India");
        bevinAddress.setState("Tamilnadu");
        bevinAddress.setDistrict("Thoothukudi");
        bevinAddress.setStreetName("Car Street");
        bevinVoteCard.setAddress(bevinAddress);
        bevinVoteCard.setAge(22);
        bevinVoteCard.setName("Bevin");
        bevinVoteCard.setSex("Male");
        return bevinVoteCard;
    }
}
