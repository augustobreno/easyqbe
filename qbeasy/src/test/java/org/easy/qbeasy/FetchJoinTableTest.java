package org.easy.qbeasy;

import static org.junit.Assert.*;

import java.util.List;

import org.easy.qbeasy.QBEFilter;
import org.easy.qbeasy.api.FetchMode;
import org.easy.qbeasy.api.JoinType;
import org.easy.qbeasy.api.QBERepository;
import org.easy.qbeasy.api.operator.Operators;
import org.easy.qbeasy.domain.Projeto;
import org.easy.qbeasy.domain.ProjetoServidor;
import org.easy.qbeasy.repository.criteria.CollectionJoinTableFetcher;
import org.easy.qbeasy.repository.criteria.CriteriaQbeRepository;
import org.easy.qbeasy.repository.criteria.OperatorProcessorRepositoryFactory;
import org.hibernate.Hibernate;
import org.junit.Test;

/**
 * Casos de teste para mapeamentos de coleção utilizado @JoinTable.
 * Principal classe testada: {@link CollectionJoinTableFetcher}
 * @author augusto
 *
 */
public class FetchJoinTableTest extends QbeTestBase {

	@Test
	public void consultarEntidadeComFetchInnerJoin() {

		/*
		 * O script de carga contém projetos com e sem associação com ferramntas.
		 */
		
		// realiza mesma consulta com QBE
		QBERepository qbe = new CriteriaQbeRepository(getJpa().getEm(), OperatorProcessorRepositoryFactory.create());
		
		QBEFilter<Projeto> filtro = new QBEFilter<Projeto>(Projeto.class);
		filtro.addFetch(new FetchMode("ferramentas", JoinType.INNER)); 
		
		List<Projeto> projetosQbe = qbe.search(filtro);

		assertNotEmpty(projetosQbe);
		
		for (Projeto projeto : projetosQbe) {
			assertTrue("As ferramentas deveriam estar inicializadas: " + projeto, Hibernate.isInitialized(projeto.getFerramentas()));
			assertFalse("O projeto deveria ter ferramentas associadas: " + projeto, projeto.getFerramentas().isEmpty());
		}
		
	}
	
	/**
	 * Testa o fetch de coleções mapeadas com tabela associativa através de @JoinTable
	 */
	@Test
	public void fetchColecao() {
		
		// utiliza os dados pré-fabricados no arquivo script_dml.sql

		// executa a consulta utilizando QBE
		QBERepository qbe = new CriteriaQbeRepository(getJpa().getEm(), OperatorProcessorRepositoryFactory.create());

		QBEFilter<Projeto> filtro = new QBEFilter<Projeto>(Projeto.class);
		filtro.filterBy("ferramentas", Operators.isNotEmpty());
		filtro.addFetch("ferramentas");
		filtro.paginate(0, 3); // para evitar um número muito grande de registros
		
		List<Projeto> projetos = qbe.search(filtro);

		// compara as listas e verifica o resultado
		assertFalse("A lista não deveria estar vazia", projetos.isEmpty());
		
		// varre a lista verificando se todas os Projetos encontrados possuem ferramentas associadas
		for (Projeto projeto : projetos) {
			assertTrue("As ferramentas deveriam estar inicializadas: " + projeto, Hibernate.isInitialized(projeto.getFerramentas()));
			assertFalse("O projeto deveria ter ferramentas associadas: " + projeto, projeto.getFerramentas().isEmpty());
		}
		
	}
	
	/**
	 * Testa o fetch de coleções mapeadas com tabela associativa através de @JoinTable, em um
	 * cenário com aninhamento
	 */
	@Test
	public void fetchColecaoAninhada() {
		
		// utiliza os dados pré-fabricados no arquivo script_dml.sql

		// executa a consulta utilizando QBE
		QBERepository qbe = new CriteriaQbeRepository(getJpa().getEm(), OperatorProcessorRepositoryFactory.create());

		QBEFilter<ProjetoServidor> filtro = new QBEFilter<ProjetoServidor>(ProjetoServidor.class);
		filtro.filterBy("projeto.ferramentas", Operators.isNotEmpty());
		filtro.addFetch("projeto.ferramentas");
		filtro.paginate(0, 3); // para evitar um número muito grande de registros
		
		List<ProjetoServidor> projetos = qbe.search(filtro);

		// compara as listas e verifica o resultado
		assertFalse("A lista não deveria estar vazia", projetos.isEmpty());
		
		// varre a lista verificando se todas os Projetos encontrados possuem ferramentas associadas
		for (ProjetoServidor projetoServidor : projetos) {
			assertTrue("O projeto deveria estar inicializado: " + projetoServidor, Hibernate.isInitialized(projetoServidor.getProjeto()));
			assertTrue("As ferramentas deveriam estar inicializadas: " + projetoServidor, Hibernate.isInitialized(projetoServidor.getProjeto().getFerramentas()));
			assertFalse("O projeto deveria ter ferramentas associadas: " + projetoServidor, projetoServidor.getProjeto().getFerramentas().isEmpty());
		}
		
	}
}
