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

package com.cloudbees.plugins.flow;

import hudson.Extension;
import hudson.model.*;
import hudson.model.Descriptor.FormException;
import hudson.model.Queue.FlyweightTask;
import hudson.tasks.BuildStep;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Fingerprinter;
import hudson.tasks.Publisher;
import hudson.Util;
import hudson.util.AlternativeUiTextProvider;
import hudson.util.DescribableList;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import javax.servlet.ServletException;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

/**
 * Defines the orchestration logic for a build flow as a succession o jobs to be executed and chained together
 *
 * @author <a href="mailto:nicolas.deloof@cloudbees.com">Nicolas De loof</a>
 */
public class Immunity extends BuildFlow{

    public Immunity(ItemGroup parent, String name) {
        super(parent, name);
    }

    @Extension
    public static final ImmunityDescriptor DESCRIPTOR = new ImmunityDescriptor();

    @Override
    public BuildFlowDescriptor getDescriptor() {
        return DESCRIPTOR;
    }

    @Override
    public String getDsl() {
//        Chance this to have the groovy script I wrote with the appropriate changes
        return super.getDsl();
    }

    @Override
    public void setDsl(String dsl) {
        super.setDsl(dsl);
    }

    public static class ImmunityDescriptor extends BuildFlowDescriptor {
        @Override
        public String getDisplayName() {
            return Messages.Immunity_Messages();
        }

        @Override
        public TopLevelItem newInstance(ItemGroup parent, String name) {
            return new Immunity(parent, name);
        }
    }
}
