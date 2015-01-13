package org.easy.qbeasy.repository.criteria.operator;

import org.easy.qbeasy.api.Operator;
import org.easy.qbeasy.api.operator.Between;
import org.easy.qbeasy.api.operator.GreaterThan;
import org.easy.qbeasy.repository.criteria.CriteriaOperatorProcessorBase;
import org.hibernate.criterion.Restrictions;

/**
 * Processador do operador {@link Between} 
 * @author augusto
 */
@SuppressWarnings("serial")
public class GreaterThanProcessor extends CriteriaOperatorProcessorBase<Object> {

	@Override
	protected void executeOperation(String propriedade, Operator<Object> operator, Object... valores) {
		
		GreaterThan greaterThan = (GreaterThan) operator;
		if (greaterThan.isGreaterEqual()) {
			getJunction().add(Restrictions.ge(propriedade, valores[0]));			
		} else {
			getJunction().add(Restrictions.gt(propriedade, valores[0]));
		}
		
	}
	
}
