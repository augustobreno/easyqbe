package org.easy.qbeasy.repository.criteria.operator;

import org.easy.qbeasy.api.Operator;
import org.easy.qbeasy.api.operator.Between;
import org.easy.qbeasy.repository.criteria.CriteriaOperatorProcessorBase;
import org.hibernate.criterion.Restrictions;

/**
 * Processador do operador {@link Between} 
 * @author augusto
 */
@SuppressWarnings("serial")
public class SizeNotEqualProcessor extends CriteriaOperatorProcessorBase<Integer> {

	@Override
	protected void executeOperation(String propriedade, Operator<Integer> operator, Integer...valores) {
		getJunction().add(Restrictions.sizeNe(propriedade, valores[0]));
	}
	
}
