package org.easy.qbeasy.repository.criteria;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.OneToMany;

import org.easy.qbeasy.QBEFilter;
import org.easy.qbeasy.api.FetchMode;
import org.easy.qbeasy.api.Filter;
import org.easy.qbeasy.api.Identifiable;
import org.easy.qbeasy.api.OperatorProcessorRepository;
import org.easy.qbeasy.api.exception.QbeException;
import org.easy.qbeasy.api.operator.Operators;
import org.easy.qbeasy.repository.criteria.FetchesManualProcessor.PropertyGroup;
import org.easy.qbeasy.util.ReflectionUtil;
import org.hibernate.Session;


/**
 * Realiza o fetch de coleções mapeadas através de "mappedBy". 
 * <br/>  
 * Ex:  
 *  
 *  <br/>
 * 	@OneToMany(mappedBy="uf", fetch=FetchType.LAZY)
 *  <br/>
 *	@Fetch(FetchMode.SUBSELECT)
 *  <br/>
 *	private List<Cidade> cidades;
 */
public class CollectionMappedByFetcher extends CollectionFetcher {

	public CollectionMappedByFetcher(
			OperatorProcessorRepository operatorProcessorRepository, 
			Session session, 
			Map<Identifiable, Collection<Identifiable>> fetchResultMap,
			Filter<?> filter, 
			PropertyGroup groupToFetch,
			List<?> inProjectionToCollectionFetch) {
		
		super(operatorProcessorRepository, session, fetchResultMap, filter, groupToFetch, inProjectionToCollectionFetch);
		
	}

	@Override
	public void fetch() {
		// Nome da propriedade no relacionamento inverso, configurada em mappedBy
		String mappedByProperty = findMapedByProperty(getFilter(), getGroupToFetch().getPrimaryProperty().getProperty());
		
		Class<Identifiable> entityCollectionType = findCollectionEntityType(getGroupToFetch().getPrimaryProperty().getProperty());
		
		// implementa nova consulta para recuperar os relacionamentos para todos o registros encontrados na consulta principal
		QBEFilter<Identifiable> filter = new QBEFilter<Identifiable>(entityCollectionType);
		filter.filterBy(mappedByProperty, Operators.in(), getInProjectionToCollectionFetch()); // cria o filtro para identificar a coleção de dependentes
		filter.addFetch(mappedByProperty); // realiza fetch da associação inversa para evitar carregamento LAZY caso os objetos não estejam em cache
		filter.addFetch(getGroupToFetch().getNestedProperties().toArray(new FetchMode[0])); // fetch das propriedades aninhadas
		
		
		CriteriaQBEProcessor<Identifiable> qbeProcessor = new CriteriaQBEProcessor<Identifiable>(getOperatorProcessorRepository(), getSession(), filter);
		List<Identifiable> fetchResult = qbeProcessor.search();
		
		for (Identifiable fetchedEntity : fetchResult) {
			
			// recupera a associação com a entidade principal
			Identifiable primaryEntity = (Identifiable) ReflectionUtil.getValue(fetchedEntity, mappedByProperty);
			addToFetchResultMap(primaryEntity, fetchedEntity);
		}		
		
	}

	/**
	 * Descobre o nome da propriedade do atributo simples do outro lado do relacionamento, associado à coleção. 
	 * <br/>
	 * Ex: Servidor(dependentes) 1 -- * Dependente(servidor), onde servidor é o atributo mappedBy. 
	 * @param qbeFilter Filtro original que solicitou o fetch.
	 * @param colProperty Nome da propriedade que representa a coleção no relacionamento. 
	 * @return Nome da propriedade do atributo simples no relacionamento.
	 */
	private static String findMapedByProperty(Filter<?> qbeFilter, String colProperty) {
                
		String mappedBy = null;
		/*
		 * Relacionamentos com collections são implementados, geralmente, com OneToMany
		 */
		try {
			Field colField = ReflectionUtil.getField(qbeFilter.getEntityClass(), colProperty);
			
			if (colField.isAnnotationPresent(OneToMany.class)) {
				OneToMany oneToMany = colField.getAnnotation(OneToMany.class);
				mappedBy = oneToMany.mappedBy();
				
			} else {
				throw new QbeException(
                                        "Não foi encontrado a anotação @OneToMany na relação ." + 
                                        colProperty);
			}
			
		} catch (Exception e) {
			throw new QbeException("Não foi possível encontrar informações sobre a coleção " 
					+ qbeFilter.getEntityClass().getSimpleName() + "." + colProperty, e);
		}
		
		return mappedBy;
	}
	
}
