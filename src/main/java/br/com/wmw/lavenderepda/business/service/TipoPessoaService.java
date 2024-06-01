package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.TipoPessoa;
import totalcross.util.Vector;

/**
 * Classe responsável pelos serviços relacionados a uma TipoPessoa.
 */
public class TipoPessoaService {

	/**
	 * Instância 'singleton' de um TipoPessoaService.
	 */
	private static TipoPessoaService instance;

	private TipoPessoaService() {
		//--
	}

	 /**
     * Retorna uma instância de TipoPessoaService.
	 * @return TipoPessoaService
     */ 
	public static TipoPessoaService getInstance() {
		if (instance == null) {
			instance = new TipoPessoaService();
		}
		return instance;
	}
	
	//@Override
	public Vector findAll() throws java.sql.SQLException {
		
		Vector list = new Vector();
		//--
		TipoPessoa tipoPessoa = new TipoPessoa();
		if (!LavenderePdaConfig.isUsaCadastroNovoClienteApenasPessoaJuridica()) {
			tipoPessoa.flTipoPessoa = Messages.TIPOPESSOA_FLAG_FISICA;
			tipoPessoa.dsTipoPessoa = Messages.TIPOPESSOA_DESCRICAO_FISICA;
			list.addElement(tipoPessoa);
		}
		//--
		if (!LavenderePdaConfig.isUsaCadastroNovoClienteApenasPessoaFisica()) {
			tipoPessoa = new TipoPessoa();
			tipoPessoa.flTipoPessoa = Messages.TIPOPESSOA_FLAG_JURIDICA;
			tipoPessoa.dsTipoPessoa = Messages.TIPOPESSOA_DESCRICAO_JURIDICA;
			list.addElement(tipoPessoa);
		}
		//--
		return list;
	}



}
