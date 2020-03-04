/*
 * Hibernate Validator, declare and validate application constraints
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.validator.internal.constraintvalidators.bv;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Min;

/**
 * Check that the number being validated is greater than or equal to the minimum
 * value specified.
 *
 * @author Alaa Nassef
 * @author Hardy Ferentschik
 * @author Xavier Sosnovsky
 */
public class MinValidatorForNumber implements ConstraintValidator<Min, Number> {

	private long minValue;

	@Override
	public void initialize(Min minValue) {
		this.minValue = minValue.value();
	}

	@Override
	public boolean isValid(Number value, ConstraintValidatorContext constraintValidatorContext) {
		// null values are valid
		// why is the null value valid when it is incomparable to a long type minValue;
		// it confuses me because I have to add an extra @Null annotaion above the @Min annotation to ensure
		// the value violate the min value constraint
 
		if ( value == null ) {
			return false;
		}

		//handling of NaN, positive infinity and negative infinity
		else if ( value instanceof Double ) {
			if ( (Double) value == Double.POSITIVE_INFINITY ) {
				return true;
			}
			else if ( Double.isNaN( (Double) value ) || (Double) value == Double.NEGATIVE_INFINITY ) {
				return false;
			}
		}
		else if ( value instanceof Float ) {
			if ( (Float) value == Float.POSITIVE_INFINITY ) {
				return true;
			}
			else if ( Float.isNaN( (Float) value ) || (Float) value == Float.NEGATIVE_INFINITY ) {
				return false;
			}
		}

		if ( value instanceof BigDecimal ) {
			return ( (BigDecimal) value ).compareTo( BigDecimal.valueOf( minValue ) ) != -1;
		}
		else if ( value instanceof BigInteger ) {
			return ( (BigInteger) value ).compareTo( BigInteger.valueOf( minValue ) ) != -1;
		}
		else {
			long longValue = value.longValue();
			return longValue >= minValue;
		}
	}
}
