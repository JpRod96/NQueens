/**
 * Created by Jp on 06/10/2018.
 */
public class Main {

    public static void main(String[] args){
        int populationSize=100;
        int chromosomeSize=20;
        double mutationProbability=0.999;
        double convergeMinimumRate=1.0;
        double crossProbability=0.7;
        GA ga=new GA(populationSize, chromosomeSize, mutationProbability, convergeMinimumRate, crossProbability);

        System.out.println(ga.run());
    }
}
