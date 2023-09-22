package readme;

import io.github.jwdeveloper.descrabble.api.DescriptionDecorator;
import io.github.jwdeveloper.descrabble.api.elements.Element;
import io.github.jwdeveloper.descrabble.api.elements.ElementFactory;
import io.github.jwdeveloper.descrabble.plugin.spigot.elements.TextElement;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.regex.Pattern;

public class TutorialDecorator implements DescriptionDecorator {
    private String rootPath;

    public TutorialDecorator(String rootPath) {
        this.rootPath = rootPath;
    }

    @Override
    public void decorate(Element root, ElementFactory factory)
    {
        try
        {
            var path = Paths.get(rootPath, "CheckOutExamples", "src", "main", "java", "tutorial");
            var files = loadFileNamesAndContent(path.toString());


         //   root.addElement(addContent(files.keySet(), factory));
            root.addElement(factory.breakElement());
            for(var entry : files.entrySet())
            {

                var text =factory.textElement("### "+entry.getKey());
                var codeElement = factory.codeElement(entry.getValue(),"java");
                root.addElement(text, codeElement);
            }

        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public Element addContent(Set<String> names, ElementFactory factory)
    {
        var container= factory.containerElement("left");
        container.addElement(factory.breakElement());
        container.addElement(factory.textElement("### Content"));
        var counter = 1;

        for(var name : names)
        {

            var path = "https://github.com/jwdeveloper/DepenDance#"+name.replace(" ","-");
            var title = "."+counter+" "+name;
            var link = factory.linkElement(title,path);

            container.addElement(factory.breakElement());
            container.addElement(link);
            counter++;
        }
        return container;
    }



    private Map<String, String> loadFileNamesAndContent(String directoryPath) throws IOException {
        Map<String, String> fileContentMap = new LinkedHashMap<>();
        Path dirPath = Paths.get(directoryPath);

        Files.walkFileTree(dirPath, EnumSet.noneOf(FileVisitOption.class), Integer.MAX_VALUE, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (!attrs.isDirectory()) {
                    String fileName = file.getFileName().toString();

                    if(!fileName.startsWith("_"))
                    {
                        return FileVisitResult.CONTINUE;
                    }
                    fileName = formatSectionName(fileName);

                    var fileContent=  new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
                    String content = transformContent(fileContent);
                    fileContentMap.put(fileName, content);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        });

        return fileContentMap;
    }

    private String transformContent(String input)
    {

        var index = input.indexOf("public static void main");
        if(index == -1)
        {
            return "Main method not found.";
        }

        var lastIndex = input.lastIndexOf("}");
        var parsed =  input.substring(index,lastIndex);
        return parsed;
    }

    private String formatSectionName(String name)
    {
        var formatted = name.replace("_"," ").replace(".java","");
        formatted =  formatted.substring(2);
        formatted = formatted.replaceFirst(" ","");
        return formatted;
    }
}
