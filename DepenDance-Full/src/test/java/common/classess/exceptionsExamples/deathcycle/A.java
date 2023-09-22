package common.classess.exceptionsExamples.deathcycle;

public class A
{
    private final B b;

    public A(B b) {
        this.b = b;
    }
}
