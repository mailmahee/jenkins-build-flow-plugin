/*
 * Copyright (C) 2011 CloudBees Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * along with this program; if not, see <http://www.gnu.org/licenses/>.
 */

package com.cloudbees.plugins.flow

import static hudson.model.Result.SUCCESS
import static hudson.model.Result.FAILURE
import hudson.model.Job

class BuildTest extends DSLTestCase {

    public void testUnknownJob() {
        def flow = run("""
            build("unknown")
        """)
        assert FAILURE == flow.result
    }

    public void testSingleBuild() {
        Job job1 = createJob("job1")
        def flow = run("""
            build("job1")
        """)
        assertSuccess(job1)
        assert SUCCESS == flow.result
    }

    public void testBuildWithParams() {
        Job job1 = createJob("job1")
        def flow = run("""
            build("job1",
                  param1: "one",
                  param2: "two")
        """)
        def build = assertSuccess(job1)
        assertHasParameter(build, "param1", "one")
        assertHasParameter(build, "param2", "two")
        assert SUCCESS == flow.result
    }

    public void testJobFailure() {
        Job willFail = createFailJob("willFail");
        def flow = run("""
            build("willFail")
        """)
        assertFailure(willFail)
        assert FAILURE == flow.result
    }

    public void testSequentialBuilds() {
        def jobs = createJobs(["job1", "job2", "job3"])
        def flow = run("""
            build("job1")
            build("job2")
            build("job3")
        """)
        assertAllSuccess(jobs)
        assert SUCCESS == flow.result
        println flow.builds.edgeSet()
    }

    public void testSequentialBuildsWithFailure() {
        def jobs = createJobs(["job1", "job2", "job3"])
        def willFail = createFailJob("willFail")
        def notRan = createJob("notRan")
        def flow = run("""
            build("job1")
            build("job2")
            build("job3")
            build("willFail")
            build("notRan")
        """)
        assertAllSuccess(jobs)
        assertFailure(willFail)
        assertDidNotRun(notRan)
        assert FAILURE == flow.result
        println flow.builds.edgeSet()
    }

    public void testParametersFromBuild() {
        Job job1 = createJob("job1")
        Job job2 = createJob("job2")
        def flow = run("""
            b = build("job1")
            build("job2",
                  param1: b.result.name,
                  param2: b.name)
        """)
        assertSuccess(job1)
        def build = assertSuccess(job2)
        assertHasParameter(build, "param1", "SUCCESS")
        assertHasParameter(build, "param2", "job1")
        assert SUCCESS == flow.result
    }

    public void testBuildOn() {
        createSlave("node0", "foo bar", null)
        createSlave("node1", "bar baz", null)
        Job job1 = createJob("job1")
        def flow = run("""
            buildOn("node1", "job1")
        """)
        def build = assertSuccess(job1)
        assert build.getBuiltOnStr() == "node1"
        assert SUCCESS == flow.result
    }

    public void testBuildOnWithParams() {
        createSlave("node0", "foo bar", null)
        createSlave("node1", "bar baz", null)
        Job job1 = createJob("job1")
        def flow = run("""
            buildOn("node1", "job1", param1: "foo")
        """)
        def build = assertSuccess(job1)
        assert build.getBuiltOnStr() == "node1"
        assertHasParameter(build, "param1", "foo")
        assert SUCCESS == flow.result
    }
}
