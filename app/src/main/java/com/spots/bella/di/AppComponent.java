package com.spots.bella.di;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(DaggerApplication application);

    void inject(BaseActivity baseActivity);
    void inject(BaseFragment baseFragment);
}
