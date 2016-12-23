package com.github.treasure.m2e.plugin;

import org.elasticsearch.common.inject.AbstractModule;
import org.elasticsearch.river.River;

public class RiverModule extends AbstractModule {

    @Override
    protected void configure() {
	//将InjectableService接口类型绑定到InjectableServiceImpl实现类  
        //在需要注入InjectableService的地方，就会使用InjectableServiceImpl实例  
//        //使HelloService为单例状态  
//        bind(HelloService.class).in(Scopes.SINGLETON);  
	bind(River.class).to(BinRiver.class).asEagerSingleton();

    }

}
