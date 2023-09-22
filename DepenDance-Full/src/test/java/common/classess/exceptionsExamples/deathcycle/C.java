package common.classess.exceptionsExamples.deathcycle;

public class C
{
    private final CycleDependencyExample cycleDependencyExample;

    public C(CycleDependencyExample cycleDependencyExample) {
        this.cycleDependencyExample = cycleDependencyExample;
    }
}
