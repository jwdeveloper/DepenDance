package tutorial;

import io.github.jwdeveloper.dependance.Dependance;
import io.github.jwdeveloper.dependance.exampleClasses.ExampleClass;


public class _6_AutoScan
{
    public static void main(String[] args) {

        var container = Dependance.newContainer()
                .autoRegistration(_6_AutoScan.class)
                .configure(config ->
                {
                    config.onAutoScan(autoScanEvent ->
                    {
                       if(autoScanEvent.getTarget().equals(ExampleClass.class))
                       {
                           return false;
                       }
                       return true;
                    });
                })
                .build();


        container.find(ExampleClass.class);
    }
}
