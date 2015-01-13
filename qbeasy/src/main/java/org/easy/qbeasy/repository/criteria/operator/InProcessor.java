package org.easy.qbeasy.repository.criteria.operator;

import java.util.List;

import org.easy.qbeasy.api.Operator;
import org.easy.qbeasy.api.operator.Between;
import org.easy.qbeasy.api.operator.Util;
import org.easy.qbeasy.repository.criteria.CriteriaOperatorProcessorBase;
import org.hibernate.criterion.Restrictions;

/**
 * Processador do operador {@link Between} 
 * @author augusto
 */
@SuppressWarnings("serial")
public class InProcessor extends CriteriaOperatorProcessorBase<Object> {

	@Override
	protected void executeOperation(String propriedade, Operator<Object> operator, Object... valores) {
		
		List<Object> listIn = Util.extrairValores(valores);
		
		if (listIn != null && !listIn.isEmpty()) {
			getJunction().add(Restrictions.in(propriedade, listIn));	
		}
	}
	
}
