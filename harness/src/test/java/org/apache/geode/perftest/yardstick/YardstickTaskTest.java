/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.geode.perftest.yardstick;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junitpioneer.jupiter.TempDirectory;

import org.apache.geode.perftest.Task;
import org.apache.geode.perftest.TestContext;
import org.apache.geode.perftest.WorkloadConfig;
import org.apache.geode.perftest.benchmarks.EmptyBenchmark;
import org.apache.geode.perftest.runner.DefaultTestContext;
import org.apache.geode.perftest.yardstick.hdrhistogram.HdrHistogramWriter;

@ExtendWith(TempDirectory.class)
public class YardstickTaskTest {

  public Path folder;

  @BeforeEach
  void createTempFolder(@TempDirectory.TempDir Path tempDir) {
    this.folder = tempDir;
  }

  @Test
  public void testExecuteBenchmark() throws Exception {
    EmptyBenchmark benchmark = new EmptyBenchmark();
    WorkloadConfig workloadConfig = new WorkloadConfig();
    workloadConfig.threads(1);
    Task task = new YardstickTask(benchmark, workloadConfig);
    File outputDir = folder.toFile();
    TestContext context = new DefaultTestContext(null, outputDir, 1);
    task.run(context);

    assertTrue(1 <= benchmark.getInvocations());

    assertTrue(Files.walk(outputDir.toPath()).findFirst().isPresent());

    assertTrue(Files.walk(outputDir.toPath()).anyMatch(path -> path.toString().contains(
        HdrHistogramWriter.FILE_NAME)));

    // TODO -verify probes are shutdown
    // TODO -verify benchmark is shutdown
  }

}
