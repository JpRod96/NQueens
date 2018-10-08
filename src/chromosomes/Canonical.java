package chromosomes;

import javax.management.relation.RelationNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Jp on 06/10/2018.
 */
public class Canonical implements Chromosome {

    private short[] structure;
    private int size;

    public Canonical(short[] structure){
        this.structure=structure;
        size=structure.length;
    }

    public Canonical(int size){
        this.size=size;
        structure=new short[size];
    }

    public int getFitness(){
        int counter=0;
        for (short chromosome: structure){
            if(chromosome==1){
                counter++;
            }
        }
        return counter;
    }

    public boolean converged(){
        return getFitness()==size;
    }

    public List<Chromosome> cross(Chromosome anotherChromosome){
        Canonical anotherCanonical=(Canonical)anotherChromosome;
        Random random=new Random();
        int firstBound=(int)(random.nextDouble()*(size -1));
        int secondBound=(int)(random.nextDouble()*(size -1));
        while(secondBound==firstBound){
            secondBound=(int)(random.nextDouble()*(size -1));
        }
        if(firstBound<secondBound){
            return cross(anotherCanonical.structure,firstBound,secondBound);
        }
        else{
            return cross(anotherCanonical.structure,secondBound,firstBound);
        }
    }

    private List<Chromosome> cross (short[] chromosome2, int lowerBound, int upperBound){
        ArrayList<Chromosome> newGeneration=new ArrayList<>();
        short[] firstDescendent=new short[size];
        short[] secondDescendent=new short[size];
        for(int index = 0; index< size; index++){
            if(index>=lowerBound&&index<=upperBound){
                firstDescendent[index]=chromosome2[index];
                secondDescendent[index]=structure[index];
            }
            else{
                firstDescendent[index]=structure[index];
                secondDescendent[index]=chromosome2[index];
            }
        }
        newGeneration.add(new Canonical(firstDescendent));
        newGeneration.add(new Canonical(secondDescendent));
        return newGeneration;
    }

    public void mutate(){
        Random random=new Random();
        int randomIndex=(int)random.nextDouble()*(size-1);
        if(structure[randomIndex]==1){
            structure[randomIndex]=0;
        }
        else {
            structure[randomIndex]=1;
        }
    }

    @Override
    public String toString(){
        String representation="";
        for (short part: structure){
            representation+=part+" ";
        }
        representation+="\n";
        return representation;
    }
}
