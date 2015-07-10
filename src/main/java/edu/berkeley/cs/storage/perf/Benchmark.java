package edu.berkeley.cs.storage.perf;

import org.apache.commons.cli.*;
import org.apache.hadoop.fs.Path;

import java.io.File;
import java.io.IOException;

public class Benchmark {

    // Main class
    public static void main(String[] args) {
        CommandLineParser parser = new DefaultParser();

        Options options = new Options();
        options.addOption("b", true, "The benchmark to run." +
                " To run all benchmarks for all classes, specify \"all\"");
        options.addOption("r", true, "Path where the results will be stored.");
        options.addOption("d", true, "Path to data. (REQUIRED)");

        HelpFormatter formatter = new HelpFormatter();

        try {
            // Parse the command line options
            CommandLine line = parser.parse(options, args);

            String resPath = line.getOptionValue("r");
            String benchType = line.getOptionValue("b");
            String dataPath = line.getOptionValue("d");

            if(dataPath == null) {
                System.out.println("Data path must be specified.");
                formatter.printHelp("storage-perf", options);
                System.exit(0);
            }

            if(resPath == null) {
                System.out.println("Result path not specified; results will be stored in results/");
                resPath = "results/";
                File resDir = new File(resPath);
                if(!resDir.exists())
                    resDir.mkdir();
                resPath += "res";
            }

            if(benchType == null) {
                benchType = "random-access";
            }

            if(benchType.equalsIgnoreCase("all")) {
                new RandomAccessBench(new Path(dataPath)).benchRandomAccess(resPath);
            } else if(benchType.equalsIgnoreCase("random-access")) {
                new RandomAccessBench(new Path(dataPath)).benchRandomAccess(resPath);
            } else {
                System.out.println("Benchmark type not supported.");
                System.exit(0);
            }

        } catch (ParseException exception) {
            System.out.println("Could not parse command line options: " + exception.getMessage());
            formatter.printHelp("storage-perf", options);
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
