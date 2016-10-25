package com.moviedatabase.binding.annotations;

/**
 * Created by lucas on 09/10/16.
 */


import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Qualifier @Retention(RUNTIME)
public @interface ForApplication {
}