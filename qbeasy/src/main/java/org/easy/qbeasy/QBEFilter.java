package org.easy.qbeasy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.easy.qbeasy.api.FetchMode;
import org.easy.qbeasy.api.Filter;
import org.easy.qbeasy.api.Identifiable;
import org.easy.qbeasy.api.Operation;
import org.easy.qbeasy.api.OperationContainer;
import org.easy.qbeasy.api.Operator;
import org.easy.qbeasy.api.Pagination;
import org.easy.qbeasy.api.SortConfig;
import org.easy.qbeasy.api.OperationContainer.ContainerType;
import org.easy.qbeasy.api.operator.Operators;

/**
 * Classe que encapsula as opções de configuração para consultas dinâmicas utilizando
 * a API de Query By Example deste framework.
 * @author augusto
 *
 */
@SuppressWarnings("serial")
public class QBEFilter<TIPO extends Identifiable> implements Serializable, Filter<TIPO>{

	/** Instância da entidade que servirá como exemplo para "descoberta" dos atributos
	 * e valores a serem considerados na consulta */
	private TIPO example;
	
	/** Para manter o tipo da entidade associada a este filtro */
	private Class<? extends TIPO> entityClass;

	/** Lista com as configurações de ordenação na ordem em que devem ser aplicadas */
	private List<SortConfig> orderings = new ArrayList<SortConfig>();
	
	/** Lista de configurações de carregamento das dependências durante a consulta */
	private List<FetchMode> fetches = new ArrayList<FetchMode>();
	
	/** Configura comportamento de paginação da consulta */
	private Pagination pagination;
	
	/** Container Raiz deste filtro */
	private OperationContainer rootContainer = new OperationContainer(ContainerType.AND);
	
	private Operator<?> stringDefaultOperator = Operators.like(false);

	/** Determina até que nível de associação o objeto exemplo deverá ser verificado
	 * na descoberta de propriedades preenchidas. Valor inicial é 2, considerando que o nível inicial é 1, representando uma margem razoável
	 * para considerar as propriedades de consulta. */
	private int nivel = 2;
	
	/**
	 * No mecanismo de QBE, as coleções são carregadas (fetched) de uma forma manual, minimizando limitações
	 * do Hibernate quando realiza join fetch da forma tradicional. Em alguns casos, o próprio mecanismo precisará forçar
	 * o uso do fetch tradicional do hibernate.
	 */
	private boolean forcePreFetchCollection = false;
	
	/**
	 * Neste caso específico, o valor default para o nível de profundidade da consulta é 1.
	 * @see QBEFilter#Consulta(Identifiable, int)
	 */		
	@SuppressWarnings("unchecked")
	public <SUBTIPO extends TIPO> QBEFilter(SUBTIPO entity) {
		this.example = entity;
		this.entityClass = (Class<TIPO>) entity.getClass();
	}

	public <SUBTIPO extends TIPO> QBEFilter(Class<SUBTIPO> clazz) {
		this.entityClass = clazz;
	}
	
	/* (non-Javadoc)
	 * @see br.jus.trt.lib.qbe.Filter#addOrder(br.jus.trt.lib.qbe.OrderConfig)
	 */
	public Filter<TIPO> sortBy(SortConfig ... orderings) {
		return sortBy(false, orderings);
	}
	
	/* (non-Javadoc)
	 * @see br.jus.trt.lib.qbe.Filter#addOrder(boolean, br.jus.trt.lib.qbe.OrderConfig)
	 */
	public Filter<TIPO> sortBy(boolean putFirst, SortConfig ... orderings) {
		if (orderings != null) {
			for (SortConfig ordenacao : orderings) {
				if (ordenacao != null) {
					if (putFirst) {
						this.orderings.add(0, ordenacao);
					} else {
						this.orderings.add(ordenacao);
					}
				}
			}
		}
		return this;
	}
	
	/* (non-Javadoc)
	 * @see br.jus.trt.lib.qbe.Filter#addAscOrder(java.lang.String)
	 */
	public Filter<TIPO> sortAscBy(String...properties) {
		if (properties != null) {
			for (String prop : properties) {
				sortBy(SortConfig.ASC(prop));
			}
		}
		return this;
	}
	
	/* (non-Javadoc)
	 * @see br.jus.trt.lib.qbe.Filter#addDescOrder(java.lang.String)
	 */
	public Filter<TIPO> sortDescBy(String...properties) {
		if (properties != null) {
			for (String prop : properties) {
				sortBy(SortConfig.DESC(prop));
			}
		}
		return this;
	}
	
	/* (non-Javadoc)
	 * @see br.jus.trt.lib.qbe.Filter#removeOrder(java.lang.String)
	 */
	public Filter<TIPO> clearSortings(String... properties) {
		this.orderings.clear(); 
		return this;		
	}
	
	/* (non-Javadoc)
	 * @see br.jus.trt.lib.qbe.Filter#paginate(java.lang.Integer, java.lang.Integer)
	 */
	public Filter<TIPO> paginate(Integer posPrimeiroRegistro, Integer numeroMaxRegistros) {
		this.pagination = new Pagination(posPrimeiroRegistro, numeroMaxRegistros);
		return this;
	}
	
	/* (non-Javadoc)
	 * @see br.jus.trt.lib.qbe.Filter#addOperation(br.jus.trt.lib.qbe.Operation)
	 */
	public Filter<TIPO> filterBy(Operation...operations) {
		rootContainer.addOperation(operations);
		return this;
	}
	
	/**
	 * @see Filter#filterBy(String, Operator)
	 */
	public Filter<TIPO> filterBy(String property, Operator<?> operador) {
		rootContainer.addOperation(new Operation(property, operador));
		return this;
	}
	
	/* (non-Javadoc)
	 * @see br.jus.trt.lib.qbe.Filter#addOperation(java.lang.String, br.jus.trt.lib.qbe.Operator, java.lang.Object)
	 */
	public Filter<TIPO> filterBy(String property, Operator<?> operator, Object ... values) {
		rootContainer.addOperation(new Operation(property, operator, values));
		return this;
	}
	
	/* (non-Javadoc)
	 * @see br.jus.trt.lib.qbe.Filter#addContainerOperation(br.jus.trt.lib.qbe.OperationContainer)
	 */
	public Filter<TIPO> addContainerOperation(OperationContainer container) {
		this.rootContainer.addSubContainer(container);
		return this;
	}
	
	/* (non-Javadoc)
	 * @see br.jus.trt.lib.qbe.Filter#addOr(br.jus.trt.lib.qbe.Operation)
	 */
	public OperationContainer addOr(Operation...operations) {
		OperationContainer or = OperationContainer.or();
		addContainer(or, operations);
		return or;
	}
	
	/* (non-Javadoc)
	 * @see br.jus.trt.lib.qbe.Filter#addAnd(br.jus.trt.lib.qbe.Operation)
	 */
	public OperationContainer addAnd(Operation...operations) {
		OperationContainer or = OperationContainer.and();
		addContainer(or, operations);
		return or;
	}

	private Filter<TIPO> addContainer(OperationContainer container, Operation... operations) {
		container.addOperation(operations);
		addContainerOperation(container);
		return this;
	}
	
	/* (non-Javadoc)
	 * @see br.jus.trt.lib.qbe.Filter#addFetch(br.jus.trt.lib.qbe.FetchMode)
	 */
	public Filter<TIPO> addFetch(FetchMode...fetches) {
		if (fetches != null) {
			for (FetchMode fetch : fetches) {
				this.fetches.add(fetch);
			}
		}
		return this;
	}
	
	/* (non-Javadoc)
	 * @see br.jus.trt.lib.qbe.Filter#addFetch(java.lang.String)
	 */
	public Filter<TIPO> addFetch(String...properties) {
		if (properties != null) {
			for (String prop : properties) {
				addFetch(new FetchMode(prop));
			}
		}
		return this;
	}
	
	/* (non-Javadoc)
	 * @see br.jus.trt.lib.qbe.Filter#incrementNivel()
	 */
	public Filter<TIPO> incrementProspectionLevel() {
		this.nivel++;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see br.jus.trt.lib.qbe.Filter#setRootContainerType(br.jus.trt.lib.qbe.OperationContainer.ContainerType)
	 */
	public Filter<TIPO> setRootContainerType(ContainerType tipo) {
		this.getRootContainer().setType(tipo);
		return this;
	}
	
	/* get e set */
	/* (non-Javadoc)
	 * @see br.jus.trt.lib.qbe.Filter#getEntityClass()
	 */
	public Class<? extends TIPO> getEntityClass() {
		return entityClass;
	}
	
	/* (non-Javadoc)
	 * @see br.jus.trt.lib.qbe.Filter#getExample()
	 */
	public TIPO getExample() {
		return example;
	}

	/* (non-Javadoc)
	 * @see br.jus.trt.lib.qbe.Filter#setExample(TIPO)
	 */
	public void setExample(TIPO exemplo) {
		this.example = exemplo;
	}

	/* (non-Javadoc)
	 * @see br.jus.trt.lib.qbe.Filter#getPagination()
	 */
	public Pagination getPagination() {
		return pagination;
	}

	/* (non-Javadoc)
	 * @see br.jus.trt.lib.qbe.Filter#setPagination(br.jus.trt.lib.qbe.Pagination)
	 */
	public void setPagination(Pagination paginacao) {
		this.pagination = paginacao;
	}

	/* (non-Javadoc)
	 * @see br.jus.trt.lib.qbe.Filter#getOrderings()
	 */
	public List<SortConfig> getSortings() {
		return orderings;
	}

	/* (non-Javadoc)
	 * @see br.jus.trt.lib.qbe.Filter#getNivel()
	 */
	public int getProspectionLevel() {
		return nivel;
	}

	/* (non-Javadoc)
	 * @see br.jus.trt.lib.qbe.Filter#setNivel(int)
	 */
	public void setProspectionLevel(int nivel) {
		this.nivel = nivel;
	}

	/* (non-Javadoc)
	 * @see br.jus.trt.lib.qbe.Filter#getRootContainer()
	 */
	public OperationContainer getRootContainer() {
		return rootContainer;
	}

	/* (non-Javadoc)
	 * @see br.jus.trt.lib.qbe.Filter#getStringDefaultOperator()
	 */
	public Operator<?> getStringDefaultOperator() {
		return stringDefaultOperator;
	}

	/* (non-Javadoc)
	 * @see br.jus.trt.lib.qbe.Filter#setStringDefaultOperator(br.jus.trt.lib.qbe.Operator)
	 */
	public void setStringDefaultOperator(Operator<?> operadorPadraoString) {
		this.stringDefaultOperator = operadorPadraoString;
	}

	/* (non-Javadoc)
	 * @see br.jus.trt.lib.qbe.Filter#getFetches()
	 */
	public List<FetchMode> getFetches() {
		return fetches;
	}
	
	/* (non-Javadoc)
	 * @see br.jus.trt.lib.qbe.Filter#isPaged()
	 */
	public boolean isPaged() {
		return pagination != null && pagination.isActive();
	}
	
	// FIXME Este conceito deve ser movido para dentro do pacote de Criteria
	public boolean isForcePreFetchCollection() {
		return forcePreFetchCollection;
	}

	// FIXME mover para o pacote de criteria
	public void setForcePreFetchCollection(boolean forcePreFetchCollection) {
		this.forcePreFetchCollection = forcePreFetchCollection;
	}

	/**
	 * @see br.jus.trt.lib.qbe.Filter#setEntityClass(java.lang.Class)
	 */
	public void setEntityClass(Class<? extends TIPO> entityClass) {
		this.entityClass = entityClass;
	}
	
}
