package gg.launchblock.template.annotations.base.pricing;

import jakarta.enterprise.util.Nonbinding;
import jakarta.interceptor.InterceptorBinding;

import java.lang.annotation.*;

@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
public @interface PricingIdentifier {

    /**
     * The identifier of the unique pricing key
     *
     * @return the pricing unique identifier for this rest method
     */
    @Nonbinding String value() default "";

}