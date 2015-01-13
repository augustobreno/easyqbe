package org.easy.qbeasy.repository.criteria.operator;

import org.easy.qbeasy.api.Operator;
import org.easy.qbeasy.api.operator.Between;
import org.easy.qbeasy.api.operator.Like;
import org.easy.qbeasy.repository.criteria.CriteriaOperatorProcessorBase;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

/**
 * Processador do operador {@link Between} 
 * @author augusto
 */
@SuppressWarnings("serial")
public class LikeProcessor extends CriteriaOperatorProcessorBase<String> {

	@Override
	protected void executeOperation(String property, Operator<String> operator, String... values) {
		
		Like like = (Like) operator;
		String value = values[0];
		if (like.isCaseSensitive()) {
			getJunction().add(Restrictions.like(property, value, getMatchMode(like) ));
		} else {
			getJunction().add(Restrictions.ilike(property, value.toLowerCase(), getMatchMode(like) ));					
		}
	}

	/**
	 * Define o tipo de like que será executado
	 * @param like Operador sendo processado.
	 * @return 
	 */
	protected MatchMode getMatchMode(Like like) {
		return MatchMode.ANYWHERE;
	}
	
}
