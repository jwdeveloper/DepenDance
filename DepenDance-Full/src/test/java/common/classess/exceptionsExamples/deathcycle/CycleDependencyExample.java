package common.classess.exceptionsExamples.deathcycle;

public class CycleDependencyExample
{
    private final A a;

    public CycleDependencyExample(A a)
    {
        this.a = a;
    }
}
