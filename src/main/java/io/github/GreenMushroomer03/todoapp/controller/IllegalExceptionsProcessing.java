package io.github.GreenMushroomer03.todoapp.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//dotyczy tylko klas interfejsów enumów itd.
@Target(ElementType.TYPE)
//adnotacja jest zachowana w trakcie działania aplikacji.
@Retention(RetentionPolicy.RUNTIME)
@interface IllegalExceptionsProcessing {
}
