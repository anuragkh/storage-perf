package edu.berkeley.cs.storage.perf;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.Path;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class RandomAccessBench {

    private static final int MAX_QUERIES = 10000;
    private Path path;
    private Configuration conf;

    public RandomAccessBench(Path path) {
        this.path = path;
        this.conf = BenchmarkUtils.getConf();
    }

    public void benchRandomAccess(String resPath) throws IOException {
        FSDataInputStream is = BenchmarkUtils.getStream(path, conf);
        long[] offsets = BenchmarkUtils.generateRandoms(MAX_QUERIES, BenchmarkUtils.getFileSize(path, conf) - 8L);

        double totalTime = 0.0;
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(resPath));

        for(long offset: offsets) {
            long start = System.nanoTime();
            randomAccess(is, offset);
            long end = System.nanoTime();
            bufferedWriter.write(offset + "\t" + (end - start) + "\n");
            totalTime += (end - start);
        }

        is.close();

        double avgTime = totalTime / MAX_QUERIES;
        System.out.println("Average time per 8 byte random-access: " + avgTime);
        bufferedWriter.close();
    }

    private long randomAccess(FSDataInputStream is, long pos) throws IOException {
        is.seek(pos);
        return is.readLong();
    }

}
