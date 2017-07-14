package blue.aodev.animeultimetv.data.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/** Flag for a request for all animes. */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface All {}
