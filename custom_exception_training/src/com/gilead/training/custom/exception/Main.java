package com.gilead.training.custom.exception;

public class Main {
    public static void main(String[] args) {
        System.out.println("Start Program");
        InformationLoader informationLoader = new InformationLoader();
        //VoterCard bevin = informationLoader.createVoter();
        VoterCard denisha = new VoterCard();
       /* Address denishaAddrr = new Address();
        denishaAddrr.setCountry("India");
        denisha.setAddress(denishaAddrr);
        denisha.setName("Denisha");*/
        VotingMachine votingMachine = new VotingMachine();
        try {
        votingMachine.canVote(denisha, denisha.getAge());//null pointer exception
        }

        //System.out.println(denisha.getAddress().getCountry()); //get a null pointer
    //} //catch (InformationInSufficientException e) {
                //System.out.println("Insufficient Information for the user");
          //  }
            catch(Exception ex){
                System.out.println("General Exception for the user");
            }
}
}
