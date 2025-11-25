package parser;

/**
 * FILE: Main.java
 * 
 * Main file
 *
 * GROUP 9: Jasmine Cheema, Monica Leung, Precious Robert-Ezenta, Mitali Vaid
 * DATE: 2025/11/24
 */
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
