package scanner;

import common.ContainerTestBase;
import io.github.jwdeveloper.dependance.Dependance;
import org.junit.Assert;
import org.junit.Test;

public class JarScannerTests {

    public static class ExampleClass1 {

    }

    public static class ExampleClass2 {

    }

    @Test
    public void shouldFindAllClasses() {
        var scanner = Dependance.newJarScanner(options ->
        {
            options.setRootPackage(JarScannerTests.class);
        }).initialize();


        Assert.assertEquals(3, scanner.size());
    }

    @Test
    public void shouldIncludeAnotherPackage() {
        var scanner = Dependance.newJarScanner(options ->
        {
            options.setRootPackage(JarScannerTests.class);
            options.includePackage(ContainerTestBase.class);
        }).initialize();

        var result = scanner.size() > 3;
        Assert.assertTrue(result);
    }

    @Test
    public void shouldExcludeClass() {
        var scanner = Dependance.newJarScanner(options ->
        {
            options.setRootPackage(JarScannerTests.class);
            options.excludeClass(ExampleClass1.class);
        });

        var result = scanner.findAll();

        Assert.assertTrue(result.stream().filter(e -> e.equals(ExampleClass1.class)).findFirst().isEmpty());
    }

    @Test
    public void shouldExcludePackage() {
        var scanner = Dependance.newJarScanner(options ->
        {
            options.setRootPackage(JarScannerTests.class);
            options.excludePackage(ExampleClass1.class.getPackageName());
        });

        var result = scanner.findAll();
        Assert.assertEquals(0, result.size());
        Assert.assertTrue(result.stream().filter(e -> e.equals(ExampleClass1.class)).findFirst().isEmpty());
        Assert.assertTrue(result.stream().filter(e -> e.equals(ExampleClass2.class)).findFirst().isEmpty());
    }
}
