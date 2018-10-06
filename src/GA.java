
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Jp on 06/10/2018.
 */
public class GA {
    public ArrayList<short[]> population;
    private int populationSize;
    private int chromosomeSize;
    private double mutationProbability;
    private double convergeMinimumRate;
    private double crossProbability;

    public GA(int populationSize, int chromosomeSize, double mutationProbability, double convergeMinimumRate, double crossProbability){
        this.populationSize = populationSize;
        population =new ArrayList<>();
        this.chromosomeSize = chromosomeSize;
        this.mutationProbability=mutationProbability;
        this.convergeMinimumRate=convergeMinimumRate;
        this.crossProbability=crossProbability;
    }

    public int run(){
        generate();
        int generationCounter=0;
        while (!doesConverge()){
            ArrayList<short[]> temporalPopulation=new ArrayList<>();
            int crossNumber=(int)(crossProbability*100);
            for(int index=0; index<crossNumber/2; index++){
                ArrayList<short[]> chosenOnes=getTwoCromosome();
                ArrayList<short[]> descendents=cross(chosenOnes.get(0), chosenOnes.get(1));
                mutate(descendents.get(0));
                mutate(descendents.get(1));
                temporalPopulation.addAll(descendents);
            }
            int criticalFitness=fitnessSummatory()/population.size()+1;
            population.addAll(temporalPopulation);
            if(!doesConverge()){
                System.out.println("       antes "+population.size());
                reduxPopulation(criticalFitness);
                System.out.println("       despues: "+population.size());
                System.out.println("       fitness "+criticalFitness);
            }
            generationCounter++;
        }
        return generationCounter;
    }

    public void reduxPopulation(int averageFitness){
        for(int index=0; index<population.size(); index++){
            short[] chromosome=population.get(index);
            if(fitness(chromosome)<averageFitness){
                population.remove(chromosome);
                index--;
            }
        }
    }

    public ArrayList<short[]> generate(){
        population =new ArrayList<>();
        for(int index = 1; index<= populationSize; index++){
            population.add(generateUnit());
        }
        return population;
    }

    public short[] generateUnit(){
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
        return unit;
    }

    public void mutate(short[] chromosome){
        Random random=new Random();
        double aux=random.nextDouble();
        if(aux>=mutationProbability){
            int randomIndex=(int)random.nextDouble()*(chromosomeSize-1);
            if(chromosome[randomIndex]==1){
                chromosome[randomIndex]=0;
            }
            else {
                chromosome[randomIndex]=1;
            }
            System.out.println("       Muto");
        }
    }

    public boolean doesConverge(){
        int convergeCounter=0;
        for(short[] chromosome: population){
            if(fitness(chromosome)== chromosomeSize){
                convergeCounter++;
            }
        }
        return (convergeCounter/population.size())>=convergeMinimumRate;
    }

    public int fitness(short[] unit){
        int counter=0;
        for (short chromosome: unit){
            if(chromosome==1){
                counter++;
            }
        }
        return counter;
    }

    public ArrayList<short[]> cross(short[] chromosome1, short[] chromosome2){
        Random random=new Random();
        int firstBound=(int)(random.nextDouble()*(chromosomeSize -1));
        int secondBound=(int)(random.nextDouble()*(chromosomeSize -1));
        while(secondBound==firstBound){
            secondBound=(int)(random.nextDouble()*(chromosomeSize -1));
        }
        if(firstBound<secondBound){
            return cross(chromosome1,chromosome2,firstBound,secondBound);
        }
        else{
            return cross(chromosome1,chromosome2,secondBound,firstBound);
        }
    }

    private ArrayList<short[]> cross (short[] chromosome1, short[] chromosome2, int lowerBound, int upperBound){
        ArrayList<short[]> newGeneration=new ArrayList<>();
        short[] firstDescendent=new short[chromosomeSize];
        short[] secondDescendent=new short[chromosomeSize];
        for(int index = 0; index< chromosomeSize; index++){
            if(index>=lowerBound&&index<=upperBound){
                firstDescendent[index]=chromosome2[index];
                secondDescendent[index]=chromosome1[index];
            }
            else{
                firstDescendent[index]=chromosome1[index];
                secondDescendent[index]=chromosome2[index];
            }
        }
        newGeneration.add(firstDescendent);
        newGeneration.add(secondDescendent);
        return newGeneration;
    }

    public ArrayList<short[]> getTwoCromosome(){
        int[] roulette=generateRoulette();
        ArrayList<short[]> chosenOnes=new ArrayList<>();
        int fitnessSummatory=fitnessSummatory();
        Random random=new Random();
        for(int index=0; index<2; index++) {
            int randomIndex = (int) (random.nextDouble() * fitnessSummatory);
            int chromosomeIndex = roulette[randomIndex];
            chosenOnes.add(population.get(chromosomeIndex));
        }
        return chosenOnes;
    }

    private int[] generateRoulette(){
        int fitnessSummatory=fitnessSummatory();
        int[] roulette=new int[fitnessSummatory];
        int rouletteIndex=0;
        int chromosomeIndex=0;
        for(short[] chromosome: population){
            int fitness=fitness(chromosome);
            for(int index=rouletteIndex;index<(rouletteIndex+fitness);index++){
                roulette[index]=chromosomeIndex;
            }
            rouletteIndex+=fitness;
            chromosomeIndex++;
        }
        return roulette;
    }

    private int fitnessSummatory(){
        int fitnessCounter=0;
        for(short[] chromosome : population){
            fitnessCounter+=fitness(chromosome);
        }
        return fitnessCounter;
    }

    @Override
    public String toString(){
        String representation="";
        for (short[] unit: population){
            for (short chromosome: unit){
                representation+=chromosome+" ";
            }
            representation+="\n";
        }
        return representation;
    }
}

