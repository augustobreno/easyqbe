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
public class NotInProcessor extends CriteriaOperatorProcessorBase<Object> {

	@Override
	protected void executeOperation(String propriedade, Operator<Object> operator, Object... valores) {
		
		List<Object> listNotIn = Util.extrairValores(valores);
		
		if (listNotIn != null && !listNotIn.isEmpty()) {
			getJunction().add(Restrictions.not(Restrictions.in(propriedade, listNotIn)));	
		}
	}
	
}
