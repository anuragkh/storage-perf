package edu.berkeley.cs.storage.perf;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.util.Random;

public class BenchmarkUtils {
    /**
     * Opens a new FSDataInputStream on the provided file.
     *
     * @param path Path of the file.
     * @param conf The configuration for the filesystem.
     * @return A FSDataInputStream.
     * @throws IOException
     */
    public static FSDataInputStream getStream(Path path, Configuration conf) throws IOException {
        FileSystem fs = FileSystem.get(path.toUri(), conf);
        return fs.open(path);
    }

    public static long getFileSize(Path filePath, Configuration conf) throws IOException {
        return filePath.getFileSystem(conf).getContentSummary(filePath).getLength();
    }

    public static Configuration getConf() {
        Configuration conf = new Configuration();
        String confDir = System.getenv("HADOOP_CONF_DIR");
        if(confDir != null) {
            conf.addResource(new Path(confDir + "/core-site.xml"));
            conf.addResource(new Path(confDir + "/hdfs-site.xml"));
        }
        return conf;
    }

    public static long[] generateRandoms(int numQueries, long limit) {
        long[] randoms = new long[numQueries];
        Random rand = new Random();
        for(int i = 0; i < numQueries; i++) {
            randoms[i] = nextLong(rand, limit);
        }
        return randoms;
    }

    private static long nextLong(Random rng, long n) {
        // error checking and 2^x checking removed for simplicity.
        long bits, val;
        do {
            bits = (rng.nextLong() << 1) >>> 1;
            val = bits % n;
        } while (bits-val+(n-1) < 0L);
        return val;
    }
}
