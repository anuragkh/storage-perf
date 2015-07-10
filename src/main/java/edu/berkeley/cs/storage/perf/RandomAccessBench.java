package edu.berkeley.cs.storage.perf;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.Path;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;

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
            long val = randomAccess(is, offset);
            long end = System.nanoTime();
            bufferedWriter.write(offset + "\t" + val + "\t" + (end - start) + "\n");
            totalTime += (end - start);
        }

        is.close();

        double avgTime = totalTime / MAX_QUERIES;
        System.out.println("Average time per 8 byte random-access: " + avgTime);
        bufferedWriter.close();
    }

    public void benchRandomAccessInMemory(String resPath) throws IOException {
        FSDataInputStream is = BenchmarkUtils.getStream(path, conf);
        long fileSize = BenchmarkUtils.getFileSize(path, conf);

        // We only support in-memory benchmark for files < 2GB
        if(fileSize > Integer.MAX_VALUE - 8) {
            throw new IOException("File too large.");
        }

        byte[] data = new byte[(int)fileSize];
        is.readFully(0, data);
        ByteBuffer buf = ByteBuffer.wrap(data);
        is.close();

        long[] offsets = BenchmarkUtils.generateRandoms(MAX_QUERIES, fileSize - 8L);

        double totalTime = 0.0;
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(resPath));

        for(long offset: offsets) {
            long start = System.nanoTime();
            long val = buf.get((int) offset);
            long end = System.nanoTime();
            bufferedWriter.write(offset + "\t" + val + "\t" + (end - start) + "\n");
            totalTime += (end - start);
        }

        double avgTime = totalTime / MAX_QUERIES;
        System.out.println("Average time per 8 byte random-access: " + avgTime);
        bufferedWriter.close();
    }

    private long randomAccess(FSDataInputStream is, long pos) throws IOException {
        is.seek(pos);
        return is.readLong();
    }

    public void benchAll(String resPath) throws IOException {
        benchRandomAccess(resPath + "_storage");
        benchRandomAccessInMemory(resPath + "_mem");
    }
}
