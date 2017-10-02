/*
 * Hibernate Validator, declare and validate application constraints
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.validator.scripting.impl;

import static org.hibernate.validator.internal.util.logging.Messages.MESSAGES;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.hibernate.validator.internal.util.scriptengine.ScriptEvaluatorImpl;
import org.hibernate.validator.scripting.ScriptEvaluationException;
import org.hibernate.validator.scripting.ScriptEvaluator;
import org.hibernate.validator.scripting.ScriptEvaluatorFactory;

/**
 * {@link ScriptEvaluatorFactory} which allows you to pass multiple {@link ClassLoader}s that will be used
 * to search for {@link ScriptEngine}s. Is useful in environments similar to OSGi, where script engines can be
 * found only by {@link ClassLoader} different from default one.
 *
 * @author Marko Bekhta
 */
public class MultiClassloaderScriptEvaluatorFactory extends AbstractCacheableScriptEvaluatorFactory {

	private final ClassLoader[] classLoaders;

	public MultiClassloaderScriptEvaluatorFactory(ClassLoader... classLoaders) {
		if ( classLoaders.length == 0 ) {
			throw new IllegalArgumentException( "No class loaders were passed" );
		}
		this.classLoaders = classLoaders;
	}

	@Override
	protected ScriptEvaluator createNewScriptEvaluator(String languageName) throws ScriptEvaluationException {
		for ( ClassLoader classLoader : classLoaders ) {
			ScriptEngine engine = new ScriptEngineManager( classLoader ).getEngineByName( languageName );
			if ( engine != null ) {
				return new ScriptEvaluatorImpl( engine );
			}
		}
		throw new ScriptEvaluationException( MESSAGES.unableToFindScriptEngine( languageName ) );
	}

}