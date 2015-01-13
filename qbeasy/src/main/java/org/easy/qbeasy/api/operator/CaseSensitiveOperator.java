package org.easy.qbeasy.api.operator;

@SuppressWarnings("serial")
public abstract class CaseSensitiveOperator<TYPE> extends OperatorBase<TYPE> {

	private boolean caseSensitive;

	/**
	 * Construtor default. Configura caseSensitive=false;
	 */
	public CaseSensitiveOperator() {
		this(false);
	}
	
	/**
	 * @param caseSensitive true caso o operador deva considerar caseSensitive na operação. false caso contrário.
	 */
	public CaseSensitiveOperator(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

}