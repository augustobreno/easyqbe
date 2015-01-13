package org.easy.qbeasy.api.operator;



/**
 * Operador "like": [valor]%.
 */
@SuppressWarnings("serial")
public class LikePrefix extends Like {

	public LikePrefix() {
		super();
	}

	/**
	 * @see Like#Like(boolean)
	 */
	public LikePrefix(boolean caseSensitive) {
		super(caseSensitive);
	}

}
