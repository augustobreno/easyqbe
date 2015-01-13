package org.easy.qbeasy.repository.criteria.operator;

import org.easy.qbeasy.api.Operator;
import org.easy.qbeasy.api.operator.Between;
import org.easy.qbeasy.api.operator.Equal;
import org.easy.qbeasy.repository.criteria.CriteriaOperatorProcessorBase;
import org.hibernate.criterion.Restrictions;

/**
 * Processador do operador {@link Between} 
 * @author augusto
 */
@SuppressWarnings("serial")
public class EqualProcessor extends CriteriaOperatorProcessorBase<Object> {

	@Override
	protected void executeOperation(String property, Operator<Object> operator, Object...values) {
		
		Equal equal = (Equal) operator;
		Object value = values[0];
		
		boolean isString = String.class.isAssignableFrom(value.getClass());
		if (!isString || equal.isCaseSensitive()) {
			// se o objeto não for uma String, deverá entrar sempre nesta condição.
			getJunction().add(Restrictions.eq(property, value));			
		} else {
			String stringValue = (String) value;
			getJunction().add(Restrictions.eq(property, stringValue.toLowerCase()).ignoreCase());
		}		
	}
	
}
