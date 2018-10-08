package chromosomes;

import java.util.List;

/**
 * Created by Jp on 06/10/2018.
 */
public interface Chromosome {
    int getFitness();

    List<Chromosome> cross(Chromosome toCross);

    boolean converged();

    void mutate();

    @Override
    String toString();
}
