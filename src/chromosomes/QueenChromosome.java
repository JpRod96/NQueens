package chromosomes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Jp on 07/10/2018.
 */
public class QueenChromosome implements Chromosome, Comparable{
    private int[] structure;
    private int size;
    private final double MERGE_RATE=0.25;

    public QueenChromosome(int[] structure){
        this.structure=structure;
        size=structure.length;
    }

    public int getFitness(){
        int nonThreatingCounter=0;
        for (int index=0; index<size; index++){
            if(!doesThreatSomeone(index)){
                nonThreatingCounter++;
            }
        }
        return nonThreatingCounter;
    }

    public boolean converged(){
        return getFitness()==size;
    }

    /*public List<Chromosome> cross(Chromosome anotherChromosome){
        ArrayList<Chromosome> newGeneration=new ArrayList<>();
        QueenChromosome anotherQC=(QueenChromosome)anotherChromosome;
        newGeneration.add(cross(this, anotherQC));
        newGeneration.add(cross(anotherQC, this));
        return newGeneration;
    }*/

    public List<Chromosome> cross(Chromosome anotherChromosome){
        ArrayList<Chromosome> newGeneration=new ArrayList<>();
        QueenChromosome anotherQC=(QueenChromosome)anotherChromosome;
        Random random=new Random();
        int firstIndex=(int)(random.nextDouble()*size);
        int secondIndex=(int)(random.nextDouble()*size);
        while (firstIndex==secondIndex){
            secondIndex=(int)(random.nextDouble()*size);
        }
        if(firstIndex>secondIndex){
            if(firstIndex==size-1){
                firstIndex--;
            }
            newGeneration.add(cross(this, anotherQC, secondIndex, firstIndex));
            newGeneration.add(cross(anotherQC, this, secondIndex, firstIndex));
        }
        else {
            if(secondIndex==size-1){
                secondIndex--;
            }
            newGeneration.add(cross(this, anotherQC, firstIndex, secondIndex));
            newGeneration.add(cross(anotherQC, this, firstIndex, secondIndex));
        }
        return newGeneration;
    }

    public QueenChromosome cross(QueenChromosome base, QueenChromosome modifier, int innerBound, int outerBound){
        boolean[] visited=generatedLog();
        int[] structure=new int[size];
        for (int index=innerBound; index<=outerBound; index++){
            int value=modifier.structure[index];
            structure[index]=value;
            visited[value]=true;
        }
        for(int index=0; index<innerBound; index++){
            int value=base.structure[index];
            if(!visited[value]){
                structure[index]=value;
                visited[value]=true;
            }else {
                value=getFirstFreeIndex(visited);
                structure[index]=value;
                visited[value]=true;
            }
        }
        for(int index=outerBound+1; index<size; index++){
            int value=base.structure[index];
            if(!visited[value]){
                structure[index]=value;
                visited[value]=true;
            }else {
                value=getFirstFreeIndex(visited);
                structure[index]=value;
                visited[value]=true;
            }
        }
        return new QueenChromosome(structure);
    }

    private int getFirstFreeIndex(boolean[] visited){
        for (int index=0; index<size; index++){
            if(!visited[index]){
                return index;
            }
        }
        return -1;
    }

    private boolean[] generatedLog(){
        boolean[] visited=new boolean[size];
        for (int index=0; index<size; index++){
            visited[index]=false;
        }
        return visited;
    }

    public void mutate(){
        Random random=new Random();
        int firstIndex=(int)(random.nextDouble()*size);
        int secondIndex=(int)(random.nextDouble()*size);
        while (firstIndex==secondIndex){
            secondIndex=(int)(random.nextDouble()*size);
        }
        modifyQueenChromosome(this, secondIndex, structure[firstIndex]);
    }

    public QueenChromosome cross(QueenChromosome base, QueenChromosome modifier){
        QueenChromosome newChromosome=base;
        Random random=new Random();
        int repetitions=(int)Math.round(size*MERGE_RATE);
        for(int index=1; index<=repetitions; index++){
            int mergeIndex=(int)(random.nextDouble()*size);
            int modifierValue=modifier.structure[mergeIndex];
            modifyQueenChromosome(base, mergeIndex, modifierValue);
        }
        return newChromosome;
    }

    private void modifyQueenChromosome(QueenChromosome toModify, int index, int newValue){
        if(toModify.structure[index]!=newValue){
            int backUp=toModify.structure[index];

            for (int innerIndex=0; innerIndex<size; innerIndex++){
                if(toModify.structure[innerIndex]==newValue){
                    toModify.structure[innerIndex]=backUp;
                }
            }
            toModify.structure[index]=newValue;
        }
    }

    @Override
    public String toString(){
        String representation="";
        for (int part: structure){
            representation+=part+" ";
        }
        representation+="\n";
        return representation;
    }

    private boolean doesThreatSomeone(int column){
        int row=structure[column];
        return doesThreatOnRow(row, column) || doesThreatOnMainDiagonal(row, column) || doesThreatOnSecondaryDiagonal(row, column);
    }

    private boolean doesThreatOnRow(int row, int column){
        for(int index=0; index<size; index++){
            if(structure[index]==row && column!=index){
                return true;
            }
        }
        return false;
    }

    private boolean doesThreatOnMainDiagonal(int row, int column){
        int tempRow=row;
        int tempColumn=column;
        while (row<size-1 && column<size-1){
            row++;
            column++;
            if(structure[column]==row){
                return true;
            }
        }
        return threatMainUpwards(tempRow, tempColumn);
    }

    private boolean threatMainUpwards(int row, int column){
        while (row>0 && column>0){
            row--;
            column--;
            if(structure[column]==row){
                return true;
            }
        }
        return false;
    }

    private boolean doesThreatOnSecondaryDiagonal(int row, int column){
        int tempRow=row;
        int tempColumn=column;
        while (row<size-1 && column>0){
            row++;
            column--;
            if(structure[column]==row){
                return true;
            }
        }
        return threatSecondaryUpwards(tempRow, tempColumn);
    }

    private boolean threatSecondaryUpwards(int row, int column){
        while (row>0 && column<size-1){
            row--;
            column++;
            if(structure[column]==row){
                return true;
            }
        }
        return false;
    }

    @Override
    public int compareTo(Object o){
        QueenChromosome anotherQueen=(QueenChromosome)o;
        if(anotherQueen.getFitness()==getFitness()){
            return 0;
        }
        else if(anotherQueen.getFitness()>getFitness()){
            return 1;
        }else{
            return -1;
        }
    }
}
