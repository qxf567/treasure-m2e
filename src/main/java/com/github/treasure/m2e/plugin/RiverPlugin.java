package com.github.treasure.m2e.plugin;

import java.util.Collection;

import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.common.inject.Module;
import org.elasticsearch.plugins.AbstractPlugin;

public class RiverPlugin extends AbstractPlugin {

    @Override
    public String name() {
	return "bin-river";
    }

    @Override
    public String description() {
	return "mysql bin lon to elastisearch";
    }

    @Override
    public Collection<Class<? extends Module>> modules() {
        Collection<Class<? extends Module>> modules = Lists.newArrayList();
        modules.add(RiverModule.class);
        return modules;
    }
}
