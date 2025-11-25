package parser;


public class Main
{
	public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java -jar Parser.jar <xmlfile>");
            return;
        }

        new XMLParser().parseFile(args[0]);
    }

}
