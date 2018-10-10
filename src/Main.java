import chromosomes.Canonical;
import chromosomes.Chromosome;
import chromosomes.QueenChromosome;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Jp on 06/10/2018.
 */
public class Main {

    public static void main(String[] args){
        double populationRate=0.02;
        int chromosomeSize=10;
        int populationSize=5000;
        double mutationProbability=0.001;
        double convergeMinimumRate=1.0;
        double crossProbability=0.7;
        for(int index=1; index<=20; index++){
            System.out.println(index+" corrida");
            List<Chromosome> initialPopulation=generateQueenPopulation(populationSize,chromosomeSize);
            long before=System.currentTimeMillis();
            System.out.println(GA.run(initialPopulation, crossProbability, mutationProbability, convergeMinimumRate));
            long after=System.currentTimeMillis();
            System.out.println(after-before+" ms");
        }
        /*int[] struc={1,3,8,6,4,9,0,5,7,2};
        QueenChromosome q=new QueenChromosome(struc);
        System.out.println(q.getFitness());*/
        //canonicalTest();
    }

    private static void canonicalTest(){
        int populationSize=100;
        int chromosomeSize=20;
        double mutationProbability=1.0;
        double convergeMinimumRate=1.0;
        double crossProbability=0.02;

        List<Chromosome> initialPopulation=generateCanonicalPopulation(populationSize,chromosomeSize);
        System.out.println(GA.run(initialPopulation, crossProbability, mutationProbability, convergeMinimumRate));
    }

    private static List<Chromosome> generateQueenPopulation(int populationSize, int chromosomeSize){
        List<Chromosome> population=new ArrayList<>();
        for(int index = 1; index<= populationSize; index++){
            population.add(new QueenChromosome(generateUnit(chromosomeSize, chromosomeSize)));
        }
        for (int index=1; index<100; index++){
            int[] structure={0,2,5,7,9,4,8,1,3,6};
            population.add(0, new QueenChromosome(structure));
        }
        for (int index=1; index<100; index++){
            int[] structure={9, 7, 4, 2, 0, 5, 1, 8, 6, 3};
            population.add(0, new QueenChromosome(structure));
        }
        return population;
    }

    private static long factorial(int number){
        int output=1;
        for (int counter=2; counter<=number; counter++){
            output*=counter;
        }
        return output;
    }

    private static List<Chromosome> generateCanonicalPopulation(int populationSize, int chromosomeSize){
        List<Chromosome> population=new ArrayList<>();
        for(int index = 1; index<= populationSize; index++){
            population.add(generateUnitShorts(chromosomeSize));
        }
        return population;
    }

    public static int[] generateUnit(int chromosomeSize, int exclusiveBound){
        int[] structure=new int[chromosomeSize];
        boolean[] checker=generateChecker(chromosomeSize);
        for (int index = 0; index< chromosomeSize; index++){
            Random random=new Random();
            int randomValue=(int)(random.nextDouble()*exclusiveBound);
            while (checker[randomValue]){
                randomValue=(int)(random.nextDouble()*exclusiveBound);
            }
            checker[randomValue]=true;
            structure[index]=randomValue;
        }
        return structure;
    }

    private static boolean[] generateChecker(int size){
        boolean[] checker=new boolean[size];
        for(int index=0; index<size; index++){
            checker[index]=false;
        }
        return checker;
    }

    public static Canonical generateUnitShorts(int chromosomeSize){
        short[] unit=new short[chromosomeSize];
        for (int index = 0; index< chromosomeSize; index++){
            Random random=new Random();
            int aux=(random.nextInt()*1)+10;
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
