package org.easy.qbeasy;

import org.easy.qbeasy.api.Operation;
import org.easy.qbeasy.api.OperationContainer;




/**
 * Container de operação que implementar um XOR.
 * @author augusto
 *
 */
@SuppressWarnings("serial")
public class XorContainer extends OperationContainer {

	/**
	 * Implementa uma variação do container aplicando uma operação XOR sobre
	 * as operações (não considerando subcontainers, por enquanto).
	 */
	@Override
	public OperationContainer transform() {
		OperationContainer xor = new OperationContainer(ContainerType.OR);
		
		/*
		 * O Operador Lógico XOR, ou EXCLUSIVE OR, determina que apenas um dos operandos deve ser verdadeiro.
		 * Portanto, abaixo todos as operações são comparadas, de forma que em cada tupla, apenas uma delas é verdadeira.
		 * 
		 * Ex:
		 * (A and not (B or C)) OR (B and not (A or C)) OR (C and not (B or A))
		 */
		if (getOperations() != null && !getOperations().isEmpty()) {
			for (Operation operacao : getOperations()) {
				OperationContainer and = xor.addAnd(operacao);
				
				OperationContainer or = and.addOr().negate();
				for (Operation outraOperacao : getOperations()) {
					if (!outraOperacao.equals(operacao)) {
						or.addOperation(outraOperacao);
					}
				}
			}
		}	
		
		return xor;
	}
	
}
