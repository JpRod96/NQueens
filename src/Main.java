import chromosomes.Canonical;
import chromosomes.Chromosome;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

        List<Chromosome> initialPopulation=generateCanonicalPopulation(populationSize,chromosomeSize);
        System.out.println(GA.run(initialPopulation, crossProbability, mutationProbability, convergeMinimumRate));
    }

    private static List<Chromosome> generateCanonicalPopulation(int populationSize, int chromosomeSize){
        List<Chromosome> population=new ArrayList<>();
        for(int index = 1; index<= populationSize; index++){
            population.add(generateUnit(chromosomeSize));
        }
        return population;
    }

    public static Canonical generateUnit(int chromosomeSize){
        short[] unit=new short[chromosomeSize];
        for (int index = 0; index< chromosomeSize; index++){
            Random r=new Random();
            int aux=(r.nextInt()*1)+10;
            if(aux%2==0){
                unit[index]=1;
            }
            else {
                unit[index]=0;
            }
        }
        return new Canonical(unit);
    }
}
