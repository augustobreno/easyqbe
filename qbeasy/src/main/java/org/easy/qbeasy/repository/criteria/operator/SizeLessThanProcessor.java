package org.easy.qbeasy.repository.criteria.operator;

import org.easy.qbeasy.api.Operator;
import org.easy.qbeasy.api.operator.Between;
import org.easy.qbeasy.api.operator.SizeLessThan;
import org.easy.qbeasy.repository.criteria.CriteriaOperatorProcessorBase;
import org.hibernate.criterion.Restrictions;

/**
 * Processador do operador {@link Between} 
 * @author augusto
 */
@SuppressWarnings("serial")
public class SizeLessThanProcessor extends CriteriaOperatorProcessorBase<Integer> {

	@Override
	protected void executeOperation(String propriedade, Operator<Integer> operator, Integer...valores) {
		
		SizeLessThan lessThan = (SizeLessThan) operator;
		if (lessThan.isLessEqual()) {
			getJunction().add(Restrictions.sizeLe(propriedade, valores[0]));
		} else {
			getJunction().add(Restrictions.sizeLt(propriedade, valores[0]));
		}
	}
	
}
