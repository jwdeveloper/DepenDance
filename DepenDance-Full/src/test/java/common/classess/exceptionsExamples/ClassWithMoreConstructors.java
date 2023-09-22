package common.classess.exceptionsExamples;

import common.classess.ExampleClass;
import common.classess.ExampleClassV2;

public class ClassWithMoreConstructors
{
    private ExampleClass exampleClass;
    private ExampleClassV2 exampleClassV2;

    public ClassWithMoreConstructors(ExampleClass exampleClass)
    {
        this.exampleClass = exampleClass;
    }

    public ClassWithMoreConstructors(ExampleClassV2 exampleClassV2)
    {
        this.exampleClassV2 =exampleClassV2;
    }
}
