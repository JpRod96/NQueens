
import chromosomes.Chromosome;
import com.sun.javafx.scene.CameraHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Jp on 06/10/2018.
 */
public class GA {

    public static List<Chromosome> run(List<Chromosome> population, double crossProbability, double mutationProbability, double convergeMinimumRate){
        int generationCounter=0;
        while (!doesConverge(population, convergeMinimumRate)){
            ArrayList<Chromosome> temporalPopulation=new ArrayList<>();
            int crossNumber=(int)(crossProbability*100);
            for(int index=0; index<crossNumber/2; index++){
                ArrayList<Chromosome> chosenOnes=getTwoCromosome(population);
                List<Chromosome> descendents=chosenOnes.get(0).cross(chosenOnes.get(1));
                mutate(descendents.get(0), mutationProbability);
                mutate(descendents.get(1), mutationProbability);
                temporalPopulation.addAll(descendents);
            }
            int criticalFitness=fitnessSummatory(population)/population.size()+1;
            population.addAll(temporalPopulation);
            if(!doesConverge(population, convergeMinimumRate)){
                System.out.println("       antes "+population.size());
                reduxPopulation(criticalFitness, population);
                System.out.println("       despues: "+population.size());
                System.out.println("       fitness "+criticalFitness);
            }
            generationCounter++;
        }
        System.out.println(generationCounter+" generaciones");
        return population;
    }

    public static void reduxPopulation(int averageFitness, List<Chromosome> population){
        for(int index=0; index<population.size(); index++){
            Chromosome chromosome=population.get(index);
            if(chromosome.getFitness()<averageFitness){
                population.remove(chromosome);
                index--;
            }
        }
    }

    public static void mutate(Chromosome chromosome, double mutationProbability){
        Random random=new Random();
        double aux=random.nextDouble();
        if(aux>=mutationProbability){
            chromosome.mutate();
            System.out.println("       Muto");
        }
    }

    public static boolean doesConverge(List<Chromosome> population, double convergeMinimumRate){
        int convergeCounter=0;
        for(Chromosome chromosome: population){
            if(chromosome.converged()){
                convergeCounter++;
            }
        }
        return (convergeCounter/population.size())>=convergeMinimumRate;
    }

    public static ArrayList<Chromosome> getTwoCromosome(List<Chromosome> population){
        int[] roulette=generateRoulette(population);
        ArrayList<Chromosome> chosenOnes=new ArrayList<>();
        int fitnessSummatory=fitnessSummatory(population);
        Random random=new Random();
        for(int index=0; index<2; index++) {
            int randomIndex = (int) (random.nextDouble() * fitnessSummatory);
            int chromosomeIndex = roulette[randomIndex];
            chosenOnes.add(population.get(chromosomeIndex));
        }
        return chosenOnes;
    }

    private static int[] generateRoulette(List<Chromosome> population){
        int fitnessSummatory=fitnessSummatory(population);
        int[] roulette=new int[fitnessSummatory];
        int rouletteIndex=0;
        int chromosomeIndex=0;
        for(Chromosome chromosome: population){
            int fitness=chromosome.getFitness();
            for(int index=rouletteIndex;index<(rouletteIndex+fitness);index++){
                roulette[index]=chromosomeIndex;
            }
            rouletteIndex+=fitness;
            chromosomeIndex++;
        }
        return roulette;
    }

    private static int fitnessSummatory(List<Chromosome> population){
        int fitnessCounter=0;
        for(Chromosome chromosome : population){
            fitnessCounter+=chromosome.getFitness();
        }
        return fitnessCounter;
    }
}

