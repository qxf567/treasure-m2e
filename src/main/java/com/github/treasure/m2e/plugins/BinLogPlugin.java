package com.github.treasure.m2e.plugins;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.elasticsearch.common.component.LifecycleComponent;
import org.elasticsearch.common.inject.AbstractModule;
import org.elasticsearch.common.inject.Module;
import org.elasticsearch.common.inject.multibindings.Multibinder;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.repositories.RepositoriesModule;
import org.elasticsearch.rest.action.cat.AbstractCatAction;

public class BinLogPlugin extends Plugin {
    private final Settings settings;

    public BinLogPlugin(Settings settings) {
	this.settings = settings;
    }

    public String name() {
	return "jvm-example";
    }

    public String description() {
	return "A plugin that extends all extension points";
    }

    public Collection<Class<? extends LifecycleComponent>> nodeServices() {
	Collection<Class<? extends LifecycleComponent>> services = new ArrayList();
	return services;
    }

    public Collection<Module> indexModules(Settings indexSettings) {
	return Collections.emptyList();
    }

    public Collection<Class<? extends Closeable>> indexServices() {
	return Collections.emptyList();
    }

    public Collection<Module> shardModules(Settings indexSettings) {
	return Collections.emptyList();
    }

    public Collection<Class<? extends Closeable>> shardServices() {
	return Collections.emptyList();
    }

    public Settings additionalSettings() {
	return Settings.EMPTY;
    }

    public void onModule(RepositoriesModule repositoriesModule) {
    }

    public static class ConfiguredExampleModule extends AbstractModule {
	protected void configure() {
	    // bind(ExamplePluginConfiguration.class).asEagerSingleton();
	    Multibinder<AbstractCatAction> catActionMultibinder = Multibinder.newSetBinder(binder(), AbstractCatAction.class);
	    catActionMultibinder.addBinding().to(BinLogAction.class).asEagerSingleton();
	}
    }
}
