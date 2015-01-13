package org.easy.qbeasy.repository.criteria.operator;

import org.easy.qbeasy.api.operator.Like;
import org.easy.qbeasy.api.operator.LikePrefix;
import org.hibernate.criterion.MatchMode;

/**
 * Processador do operador {@link LikePrefix} 
 * @author augusto
 */
@SuppressWarnings("serial")
public class LikePrefixProcessor extends LikeProcessor {

	/**
	 * Define o tipo de like que será executado
	 * @param like Operador sendo processado.
	 * @return 
	 */
	protected MatchMode getMatchMode(Like like) {
		return MatchMode.START;
	}
	
}
