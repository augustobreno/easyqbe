package org.easy.qbeasy.repository.criteria.operator;

import org.easy.qbeasy.api.operator.Like;
import org.easy.qbeasy.api.operator.LikeSufix;
import org.hibernate.criterion.MatchMode;

/**
 * Processador do operador {@link LikeSufix} 
 * @author augusto
 */
@SuppressWarnings("serial")
public class LikeSufixProcessor extends LikeProcessor {

	/**
	 * Define o tipo de like que será executado
	 * @param like Operador sendo processado.
	 * @return 
	 */
	protected MatchMode getMatchMode(Like like) {
		return MatchMode.END;
	}
	
}
