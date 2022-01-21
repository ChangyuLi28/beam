/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import CommonJobProperties as commonJobProperties
import PostcommitJobBuilder


// This job runs the Java postcommit tests, including the suite of integration
// tests.
PostcommitJobBuilder.postCommitJob('beam_PostCommit_Java_Jpms_Direct_Java1117', 'Run Jpms Direct Java 17 PostCommit',
        'JPMS Java 17 direct runner  Post Commit Tests', this) {

    description('Runs JPMS tests on the direct runner using the Java 17 SDK.')

    // Set common parameters.
    commonJobProperties.setTopLevelMainJobProperties(delegate, 'master', 240)

    // Publish all test results to Jenkins
    publishers {
        archiveJunit('**/build/test-results/**/*.xml')
    }

    // Gradle goals for this job.
    steps {
        gradle {
            rootBuildScriptDir(commonJobProperties.checkoutDir)
            tasks(':sdks:java:testing:jpms-tests:directRunnerIntegrationTest')
            commonJobProperties.setGradleSwitches(delegate)
            switches("-PcompileAndRunTestsWithJava17")
            switches("-Pjava17Home=${commonJobProperties.JAVA_17_HOME}")
            // Specify maven home on Jenkins, needed by Maven archetype integration tests.
            switches('-Pmaven_home=/home/jenkins/tools/maven/apache-maven-3.5.4')
        }
    }
}
