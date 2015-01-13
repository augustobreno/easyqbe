package org.easy.qbeasy.api.operator;


/**
 * Operador "like": %[valor]%.
 */
@SuppressWarnings("serial")
public class Like extends CaseSensitiveOperator<String> {

	/**
	 * Construtor default. Configura caseSensitive=false;
	 */
	public Like() {
		super(false);
	}
	
	/**
	 * @param caseSensitive true caso o like deva considerar caseSensitive na operação. false caso contrário.
	 */
	public Like(boolean caseSensitive) {
		super(caseSensitive);
	}

}
