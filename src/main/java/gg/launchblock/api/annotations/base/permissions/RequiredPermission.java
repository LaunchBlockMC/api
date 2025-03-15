package gg.launchblock.api.annotations.base.permissions;

import gg.launchblock.api.constants.Permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiredPermission {

    Permission[] value();

    boolean containerOnly() default false;

}