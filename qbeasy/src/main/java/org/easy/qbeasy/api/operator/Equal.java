package org.easy.qbeasy.api.operator;


/**
 * Operador "=";
 */
@SuppressWarnings("serial")
public class Equal extends CaseSensitiveOperator<Object> {

	public Equal() {
		this(true);
	}

	public Equal(boolean caseSensitive) {
		super(caseSensitive);
	}

}
