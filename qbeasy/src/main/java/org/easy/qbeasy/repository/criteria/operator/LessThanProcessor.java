package org.easy.qbeasy.repository.criteria.operator;

import org.easy.qbeasy.api.Operator;
import org.easy.qbeasy.api.operator.Between;
import org.easy.qbeasy.api.operator.LessThan;
import org.easy.qbeasy.repository.criteria.CriteriaOperatorProcessorBase;
import org.hibernate.criterion.Restrictions;

/**
 * Processador do operador {@link Between} 
 * @author augusto
 */
@SuppressWarnings("serial")
public class LessThanProcessor extends CriteriaOperatorProcessorBase<Object> {

	@Override
	protected void executeOperation(String propriedade, Operator<Object> operator, Object... valores) {
		
		LessThan lessThan = (LessThan) operator;
		if (lessThan.isLessThen()) {
			getJunction().add(Restrictions.le(propriedade, valores[0]));
		} else {
			getJunction().add(Restrictions.lt(propriedade, valores[0]));
		}
	}
	
}
