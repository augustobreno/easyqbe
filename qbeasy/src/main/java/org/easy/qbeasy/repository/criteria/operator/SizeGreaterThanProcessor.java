package org.easy.qbeasy.repository.criteria.operator;

import org.easy.qbeasy.api.Operator;
import org.easy.qbeasy.api.operator.Between;
import org.easy.qbeasy.api.operator.SizeGreaterThan;
import org.easy.qbeasy.repository.criteria.CriteriaOperatorProcessorBase;
import org.hibernate.criterion.Restrictions;

/**
 * Processador do operador {@link Between} 
 * @author augusto
 */
@SuppressWarnings("serial")
public class SizeGreaterThanProcessor extends CriteriaOperatorProcessorBase<Integer> {

	@Override
	protected void executeOperation(String propriedade, Operator<Integer> operator, Integer...valores) {
		
		SizeGreaterThan greaterThan = (SizeGreaterThan) operator;
		if (greaterThan.isGreaterEqual()) {
			getJunction().add(Restrictions.sizeGe(propriedade, valores[0]));
		} else {
			getJunction().add(Restrictions.sizeGt(propriedade, valores[0]));
		}
	}	
}
