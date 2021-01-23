package com.webservice.Dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.webservice.models.Produto;
import com.webservices.exception.DaoException;
import com.webservices.exception.ErrorCode;

public class ProdutoDAO {

	@PersistenceContext
	private EntityManager manager;

	// CREATE
	public void inserir(Produto produto) {

		if (!produtoIsValid(produto)) {
			throw new DaoException("O Produto Informado não e valido", ErrorCode.BAD_REQUEST.getCode());
		}
		manager.persist(produto);
	}

	// READY
	public List<Produto> listarProdutos() {

		String JPQLProdutos = "";

		try {
			JPQLProdutos = "select p from Produto p";
		} catch (RuntimeException ex) {
			throw new DaoException("Erro ao Recuperar Lista de Produtos" + ex.getMessage(),
					ErrorCode.SERVER_ERROR.getCode());
		}
		return manager.createQuery(JPQLProdutos, Produto.class).getResultList();

	}

	// UPDATE
	public void atualizar(Produto produto) {

		if (produto.getId() <= 0) {
			throw new DaoException("O ID do Produto Precia ser maior do que 0", ErrorCode.BAD_REQUEST.getCode());
		}
		if (!produtoIsValid(produto)) {
			throw new DaoException("O Produto Informado não e valido", ErrorCode.BAD_REQUEST.getCode());
		}
		manager.merge(produto);
	}

	// READY_BY_ID
	public Produto buscarById(Produto produto) {

		if (produto.getId() <= 0) {
			throw new DaoException("O ID do Produto Precia ser maior do que 0", ErrorCode.BAD_REQUEST.getCode());
		}

		try {
			manager.find(Produto.class, produto.getId());
		} catch (Exception ex) {
			throw new DaoException("Erro ao Buscar Produto" + ex.getMessage(), ErrorCode.SERVER_ERROR.getCode());
		}

		return produto;
	}

	// DELETE
	public void remover(Produto produto) {

		try {
			manager.remove(produto);
		} catch (Exception ex) {
			throw new DaoException("Erro ao remover o Produto " + produto.getId() + "tente novamente" + ex.getMessage(),
					ErrorCode.NOT_FOUND.getCode());
		}

	}

	private boolean produtoIsValid(Produto produto) {		
		if(produto.getNome().isEmpty() || produto.getQuantidade() <=0 ) {
			return false;
		}else {
			return true;
		} 
	}
	
	//PAGINAÇÂO DE PRODUTOS POR QUANTIDADE DE REGISTROS
	public List<Produto> getByPagination(int minResultado, int maxResultado){
		List<Produto> produtos;
		
		try {
			produtos = manager.createQuery("select p from Produto p", Produto.class)
			.setFirstResult(minResultado -1)
			.setMaxResults(maxResultado)
			.getResultList();
		} catch (RuntimeException ex) {
			
			throw new DaoException("Erro Reveja os parametros passados"+ 
			ex.getMessage(), ErrorCode.SERVER_ERROR.getCode());
		}
		if(produtos.isEmpty()) {
			throw new DaoException("Lista Vazia ", ErrorCode.SERVER_ERROR.getCode());
		}
		
		return produtos;
	}
	
	//PAGINAÇÂO DE PRODUTOS POR NOME
	public List<Produto> getByNameProduto(String nome){
		List<Produto> produtos;
		
		
		try {
			produtos = manager.createQuery("SELECT p FROM Produto p WHERE p.nome like :nome", Produto.class)
					.setParameter("nome", "%"+ nome +"%")
					.getResultList();
			
		} catch (RuntimeException ex) {
			throw new DaoException("Erro ao Buscar Produto por nome no BD"
		+ex.getMessage(), ErrorCode.BAD_REQUEST.getCode());
		}
		if(produtos.isEmpty()) {
			throw new DaoException("A Consulta não retornou Elementos ", ErrorCode.SERVER_ERROR.getCode());
		}
		
		return produtos;
		
	}

}
