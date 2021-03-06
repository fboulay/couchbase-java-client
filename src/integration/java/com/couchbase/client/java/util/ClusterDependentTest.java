/**
 * Copyright (C) 2014 Couchbase, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALING
 * IN THE SOFTWARE.
 */
package com.couchbase.client.java.util;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.bucket.BucketType;
import com.couchbase.client.java.cluster.ClusterManager;
import com.couchbase.client.java.cluster.DefaultBucketSettings;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * Base test class for tests that need a working cluster reference.
 *
 * @author Michael Nitschinger
 */
public class ClusterDependentTest {

    private static final String seedNode = TestProperties.seedNode();
    private static final String adminName = TestProperties.adminName();
    private static final String adminPassword = TestProperties.adminPassword();

    private static Cluster cluster;
    private static Bucket bucket;
    private static ClusterManager clusterManager;

    @BeforeClass
    public static void connect() throws Exception {
        cluster = CouchbaseCluster.create(seedNode);
        clusterManager = cluster.clusterManager(adminName, adminPassword);
        boolean exists = clusterManager.hasBucket(bucketName());

        if (!exists) {
            clusterManager.insertBucket(DefaultBucketSettings
                .builder()
                .name(bucketName())
                .quota(256)
                .password("")
                .enableFlush(true)
                .type(BucketType.COUCHBASE)
                .build());
        }

        bucket = cluster.openBucket(bucketName(), password());
        bucket.bucketManager().flush();
    }

    @AfterClass
    public static void disconnect() throws InterruptedException {
        cluster.disconnect();
    }

    public static String password() {
        return  TestProperties.password();
    }

    public static Cluster cluster() {
        return cluster;
    }

    public static Bucket bucket() {
        return bucket;
    }

    public static String bucketName() {
        return TestProperties.bucket();
    }

    public static ClusterManager clusterManager() {
        return clusterManager;
    }
}