package br.com.wmw.lavenderepda.business.service;

import java.lang.reflect.Field;
import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.Campo;
import br.com.wmw.framework.business.validator.CpfCnpjValidator;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.sync.LogSync;
import br.com.wmw.framework.sync.SyncComponent;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.Consignacao;
import br.com.wmw.lavenderepda.business.domain.NovoCliEndereco;
import br.com.wmw.lavenderepda.business.domain.NovoCliente;
import br.com.wmw.lavenderepda.business.domain.Pagamento;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PesquisaApp;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercado;
import br.com.wmw.lavenderepda.business.validation.DuplicatedCnpjException;
import br.com.wmw.lavenderepda.business.validation.FotoNovoClienteException;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ClientePdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ConsignacaoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.NovoClientePdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PagamentoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PedidoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PesquisaMercadoPdbxDao;
import br.com.wmw.lavenderepda.sync.SyncManager;
import totalcross.sql.Types;
import totalcross.util.Date;
import totalcross.util.InvalidDateException;
import totalcross.util.Vector;

public class NovoClienteService extends CrudPersonLavendereService {

    private static NovoClienteService instance;

    private NovoClienteService() {
        //--
    }

    public static NovoClienteService getInstance() {
        if (instance == null) {
            instance = new NovoClienteService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return NovoClientePdbxDao.getInstance();
    }

    @Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
        NovoCliente novoCliente = (NovoCliente) domain;
        validateCampoDateNovoCliente(novoCliente);
        //nuCnpj
        validateCnpj(LavenderePdaConfig.gravaCpfCnpjNovoClienteSemSeparadores ? novoCliente.nuCnpj : ValueUtil.getValidNumbers(novoCliente.nuCnpj));
        //cdEmpresa
        if (ValueUtil.isEmpty(novoCliente.cdEmpresa)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.EMPRESA_NOME_ENTIDADE);
        }
        //cdRepresentante
        if (ValueUtil.isEmpty(novoCliente.cdRepresentante)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.REPRESENTANTE_NOME_ENTIDADE);
        }
        //flOrigemnovocliente
        if (ValueUtil.isEmpty(novoCliente.flOrigemNovoCliente)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PEDIDO_LABEL_FLORIGEM);
        }
        //cdNovocliente
        if (ValueUtil.isEmpty(novoCliente.cdNovoCliente)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.NOVOCLIENTE_LABEL_CODIGO);
        }
        //dtCadastro
        if (ValueUtil.isEmpty(novoCliente.dtCadastro)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.DATA_LABEL_DATA);
        }
        if (LavenderePdaConfig.isQtMinEnderecosNovoClienteHabilitado()) {
        	int qtEnderecoCadastrada = novoCliente.novoCliEnderecoList.size();
        	if (LavenderePdaConfig.getQtMinEnderecosCadastroNovoCliente() > qtEnderecoCadastrada) {
        		String[] args = {StringUtil.getStringValueToInterface(LavenderePdaConfig.getQtMinEnderecosCadastroNovoCliente()), StringUtil.getStringValueToInterface(qtEnderecoCadastrada)};
        		throw new ValidationException(MessageUtil.getMessage(Messages.NOVOCLIENTE_MSG_QTMINENDERECOS_NAO_ATINGIDA, args));
        	}
        }
        Campo campoIE = getCampoIE();
        String flObrigatorio = campoIE != null ? campoIE.flObrigatorio : null;
        if (!ValueUtil.VALOR_NAO.equals(LavenderePdaConfig.informaUfNaoPermiteInscEstadualVazio) && campoIE != null) {
        	String vlUf = novoCliente.getHashValuesDinamicos().getString(campoIE.dsCampoRelacionado);
        	flObrigatorio = campoIE.flObrigatorio;
        	if (ValueUtil.isNotEmpty(vlUf) && LavenderePdaConfig.informaUfNaoPermiteInscEstadualVazio.contains(vlUf)) {
        		campoIE.flObrigatorio = ValueUtil.VALOR_SIM;
        	} else {
        		campoIE.flObrigatorio = ValueUtil.VALOR_NAO;
        		novoCliente.salvaFlConsumidorFinal = LavenderePdaConfig.usaConfirmacaoCPFCNPJNovoClienteSemEmpresa(); 
        	}
        }
        if (campoIE != null) {
        	campoIE.setValidaIE(novoCliente.isPessoaJuridica());
			
		}
        setUfNaoPermiteInscEstadualVazio(NovoCliente.TABLE_NAME);
        super.validate(domain);
        if (campoIE != null) {
        	campoIE.flObrigatorio = flObrigatorio;
        }
        //Foto Novo Cliente
        int qtdFotoNovoCliente = novoCliente.getFotoNovoClienteList().size();
        if (("2".equals(LavenderePdaConfig.getUsaCadastroFotoNovoCliente()) && qtdFotoNovoCliente == 0) || LavenderePdaConfig.isUsaCadastroFotoNovoCliente() && LavenderePdaConfig.qtMinimaFotosCadastroNovoCliente > 0 && qtdFotoNovoCliente < LavenderePdaConfig.qtMinimaFotosCadastroNovoCliente) {
    		if (qtdFotoNovoCliente < LavenderePdaConfig.qtMinimaFotosCadastroNovoCliente) {
    			throw new FotoNovoClienteException(MessageUtil.getMessage(Messages.NOVOCLIENTE_MSG_OBRIGATORIEDADE_FOTO_QTD, new String[]{StringUtil.getStringValue(LavenderePdaConfig.qtMinimaFotosCadastroNovoCliente), StringUtil.getStringValue(qtdFotoNovoCliente)}));
    		} else {
    			throw new FotoNovoClienteException(Messages.NOVOCLIENTE_MSG_OBRIGATORIEDADE_FOTO);
    		}
    	}
        if (LavenderePdaConfig.isUsaControleFlagsEnderecoNovoCliente()) {
        	validateFlagsEndereco(novoCliente.novoCliEnderecoList);
        }
        if (novoCliente.validaQtContato) {
        	validateQtMinContato(novoCliente.qtContato);
        }
    }
    
    private void validateFlagsEndereco(Vector enderecoList) {
    	int[] validation = new int[2];
    	int indexValidation;
    	int size = enderecoList.size();
    	for (int i = 0; i < size; i++) {
    		indexValidation = 0;
    		NovoCliEndereco novoCliEndereco = (NovoCliEndereco)enderecoList.items[i];
    		if (ValueUtil.valueEquals(novoCliEndereco.getHashValuesDinamicos().get(NovoCliEndereco.NMCOLUNA_FLCOMERCIAL), ValueUtil.VALOR_SIM)) {
    			if (validation[indexValidation] == 1) {
    				throw new ValidationException(Messages.NOVO_CLIENTE_ENDERECO_COMERCIAL_ERRO);
    			}
    			validation[indexValidation]++;
    		}
    		indexValidation++;
    		if (ValueUtil.valueEquals(novoCliEndereco.getHashValuesDinamicos().get(NovoCliEndereco.NMCOLUNA_FLENTREGAPADRAO), ValueUtil.VALOR_SIM)) {
    			if (validation[indexValidation] == 1) {
    				throw new ValidationException(Messages.NOVO_CLIENTE_ENDERECO_ENTREGA_PADRAO_ERRO);
    			}
    			validation[indexValidation]++;
    		}
    	}
    	if (validation[0] == 0) {
    		throw new ValidationException(Messages.NOVO_CLIENTE_ENDERECO_COMERCIAL_NAO_INFORMADO);
    	} else if (validation[1] == 0) {
    		throw new ValidationException(Messages.NOVO_CLIENTE_ENDERECO_ENTREGA_PADRAO_NAO_INFORMADO);
    	}
    }

    public Vector deleteNovoClientePdaByClienteErp() throws SQLException {
		Vector clienteNovos = new Vector();
		if (LavenderePdaConfig.isUsaCadastroNovoCliente()) {
			Vector novoClienteList = findAll();
	    	//--
    		int size = novoClienteList.size();
    		NovoCliente novoCliente;
    		Vector clienteList;
    		for (int i = 0; i < size; i++) {
    			novoCliente = (NovoCliente) novoClienteList.items[i];
	    		//--
	    		Cliente cliente = new Cliente();
	    		cliente.cdEmpresa = novoCliente.cdEmpresa;
	    		cliente.cdRepresentante = novoCliente.cdRepresentante;
	    		cliente.nuCnpj = novoCliente.nuCnpj;
	    		cliente.cdClienteDif = novoCliente.nuCnpj;
	    		//--
	    		clienteList = ClienteService.getInstance().findAllByExample(cliente);
    			if (clienteList.size() == 1) {
    				cliente = (Cliente)clienteList.items[0];
    				if (LavenderePdaConfig.permitePesquisaMercadoNovoCliente) {
    					atualizaPesquisaMercadoNovoCliente(novoCliente, cliente);
    				}
    				if (LavenderePdaConfig.isPermitePedidoNovoCliente()) {
    					atualizaPedidoNovoCliente(novoCliente, cliente);
    				}
    				if (LavenderePdaConfig.isUsaPesquisaNovoCliente()) {
    					atualizaPesquisaNovoCliente(novoCliente, cliente);
    				}
    				if (LavenderePdaConfig.usaModuloConsignacao) {
    					atualizarConsignacaoNovoCliente(novoCliente, cliente);
    				}
    				if (LavenderePdaConfig.isUsaModuloPagamento()) {
    					atualizaPagamentoNovoCliente(novoCliente, cliente);
    				}
    				if (!LavenderePdaConfig.marcaNovoClienteParaAnalise || !ValueUtil.VALOR_SIM.equals(novoCliente.flEmAnalise)) {
    					if (LavenderePdaConfig.isUsaCadastroFotoNovoCliente()) {
    						FotoNovoClienteService.getInstance().excluiFotosNovoCliente(novoCliente);
    					}
    					if (LavenderePdaConfig.isUsaMultiplosEnderecosCadastroNovoCliente()) {
    						NovoCliEnderecoService.getInstance().deleteRegistrosNovoCliEndereco(novoCliente);
    					}
    					try {
    						if (cliente != null) {
    							delete(novoCliente);
    							ClienteService.getInstance().deleteNovoCliente(novoCliente);
    							clienteNovos.addElement(cliente);
    						}
    					} catch (Throwable e) {
    					}
    				} else {
					    marcaFlOcultoNovoCliente(novoCliente);
					    cliente.cdCliente = novoCliente.nuCnpj;
					    ClienteService.getInstance().marcaFlOcultoClienteNovoCliente(cliente);
				    }
    			}
	    	}
		}
		return clienteNovos;
    }

	public void marcaFlOcultoNovoCliente(NovoCliente novoCliente) {
		try {
			NovoClienteService.getInstance().updateColumn(novoCliente.getRowKey(), NovoCliente.NMCOLUNA_FLOCULTO, ValueUtil.VALOR_SIM, Types.VARCHAR);
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}


	private void atualizaPesquisaNovoCliente(NovoCliente novoCliente, Cliente cliente) throws SQLException {
		PesquisaApp pesquisaAppFilter = new PesquisaApp();
		pesquisaAppFilter.cdEmpresa = novoCliente.cdEmpresa;
		pesquisaAppFilter.cdRepresentante = novoCliente.cdRepresentante;
		pesquisaAppFilter.cdCliente = novoCliente.nuCnpj;
		Vector pesquisasByNovoCliente = PesquisaAppService.getInstance().findAllByExample(pesquisaAppFilter);
		int size = pesquisasByNovoCliente.size();
		for (int i = 0; i < size; i++) {
			try {
				PesquisaApp pesquisa = (PesquisaApp)pesquisasByNovoCliente.items[i];
				pesquisa.cdCliente = cliente.cdCliente;
				pesquisa.flPesquisaNovoCliente = ValueUtil.VALOR_NAO;
				PesquisaAppService.getInstance().update(pesquisa);
			} catch (Throwable ex) {
			}
		}
	}

	private void atualizaPagamentoNovoCliente(NovoCliente novoCliente, Cliente cliente) throws SQLException {
		Vector pagamentos;
		Pagamento pagamentoFilter = new Pagamento();
		pagamentoFilter.cdEmpresa = novoCliente.cdEmpresa;
		pagamentoFilter.cdRepresentante = novoCliente.cdRepresentante;
		pagamentoFilter.cdCliente = novoCliente.nuCnpj;
		pagamentos = PagamentoService.getInstance().findAllByExample(pagamentoFilter);
		int pagamentosSize = pagamentos.size();
		for (int j = 0; j < pagamentosSize; j++) {
			try {
				Pagamento pagamento = (Pagamento)pagamentos.items[j];
				PagamentoPdbxDao.getInstance().delete(pagamento);
				pagamento.cdCliente = cliente.cdCliente;
				PagamentoPdbxDao.getInstance().insert(pagamento);
			} catch (Throwable ex) {
			}
		}
	}

	private void atualizarConsignacaoNovoCliente(NovoCliente novoCliente, Cliente cliente) throws SQLException {
		Vector consignacoes;
		Consignacao consignacaoFilter = new Consignacao();
		consignacaoFilter.cdEmpresa = novoCliente.cdEmpresa;
		consignacaoFilter.cdRepresentante = novoCliente.cdRepresentante;
		consignacaoFilter.cdCliente = novoCliente.nuCnpj;
		consignacoes = ConsignacaoService.getInstance().findAllByExample(consignacaoFilter);
		int consignacoesSize = consignacoes.size();
		for (int j = 0; j < consignacoesSize; j++) {
			try {
				Consignacao consignacao = (Consignacao)consignacoes.items[j];
				ConsignacaoPdbxDao.getInstance().delete(consignacao);
				consignacao.cdCliente = cliente.cdCliente;
				ConsignacaoPdbxDao.getInstance().insert(consignacao);
			} catch (Throwable ex) {
			}
		}
	}

	private void atualizaPesquisaMercadoNovoCliente(NovoCliente novoCliente, Cliente cliente) throws SQLException {
		Vector pesquisaMercadoList;
		PesquisaMercado pesquisaMercadoFilter = new PesquisaMercado();
		pesquisaMercadoFilter.cdEmpresa = novoCliente.cdEmpresa;            
		pesquisaMercadoFilter.cdRepresentante = novoCliente.cdRepresentante;
		pesquisaMercadoFilter.cdCliente = novoCliente.nuCnpj;
		pesquisaMercadoList = PesquisaMercadoService.getInstance().findAllByExample(pesquisaMercadoFilter);
		int size = pesquisaMercadoList.size();
		for (int i = 0; i < size; i++) {
			try {
				PesquisaMercado pesquisaMercado = (PesquisaMercado) pesquisaMercadoList.items[i];
				pesquisaMercado.cdCliente = cliente.cdCliente;
				PesquisaMercadoPdbxDao.getInstance().update(pesquisaMercado);
			} catch (Throwable e) {
			}
		}
	}

	private void atualizaPedidoNovoCliente(NovoCliente novoCliente, Cliente cliente) throws SQLException {
		Vector pedidos;
		Pedido pedidoFilter = new Pedido();
		pedidoFilter.cdEmpresa = novoCliente.cdEmpresa;
		pedidoFilter.cdRepresentante = novoCliente.cdRepresentante;
		pedidoFilter.cdCliente = novoCliente.nuCnpj;
		pedidos = PedidoService.getInstance().findAllByExample(pedidoFilter);
		int pedidosSize = pedidos.size();
		for (int j = 0; j < pedidosSize; j++) {
			try {
				Pedido pedido = (Pedido)pedidos.items[j];
				PedidoPdbxDao.getInstance().delete(pedido);
				pedido.cdCliente = cliente.cdCliente;
				pedido.flPedidoNovoCliente = ValueUtil.VALOR_NAO;
				PedidoPdbxDao.getInstance().insert(pedido);
			} catch (Throwable ex) {
			}
		}
	}

    public Cliente getClienteOficialByNovoCliente(NovoCliente novoCliente) throws SQLException {
    	Cliente cliente = new Cliente();
		cliente.cdEmpresa = novoCliente.cdEmpresa;
		cliente.cdRepresentante = novoCliente.cdRepresentante;
		cliente.cdClienteExato = novoCliente.nuCnpj;
		Vector clienteList = ClienteService.getInstance().findAllByExample(cliente);
		if (clienteList.size() > 0) {
			return (Cliente)clienteList.items[0];
		} else {
			return null;
		}
    }

    public NovoCliente getNovoClienteByCliente(Cliente cliente) throws SQLException {
    	NovoCliente novoCliente = new NovoCliente();
    	novoCliente.cdEmpresa = cliente.cdEmpresa;
    	novoCliente.cdRepresentante = cliente.cdRepresentante;
    	novoCliente.nuCnpj = cliente.cdCliente;
    	Vector novoClienteList = findAllByExample(novoCliente);
    	if (novoClienteList.size() > 0) {
    		return (NovoCliente)novoClienteList.items[0];
    	} else {
    		return null;
    	}
    }

	public void carregaEnderecoClienteOficialByNovoCliente(Cliente cliente) throws SQLException {
		String cdNovoCliente = findCdNovoClienteByCnpj(cliente.nuCnpj);
		NovoCliEndereco novoCliEndereco = NovoCliEnderecoService.getInstance().getEnderecoNovoCli(cliente.cdEmpresa, cliente.cdRepresentante, cdNovoCliente);
		if (novoCliEndereco != null) {
			cliente.dsLogradouroComercial = novoCliEndereco.dsLogradouro;
			cliente.dsBairroComercial = novoCliEndereco.dsBairro;
			cliente.dsCidadeComercial = novoCliEndereco.dsCidade;
			cliente.cdEstadoComercial = novoCliEndereco.cdUf;
			cliente.dsEstadoComercial = novoCliEndereco.cdUf;
			cliente.dsCepComercial = novoCliEndereco.dsCep;
		}
	}

	@Override
	public void insert(BaseDomain domain) throws SQLException {
    	NovoCliente novoCliente = (NovoCliente) domain;
    	novoCliente.cdStatusNovoCliente = LavenderePdaConfig.cdStatusNovoClienteFechado;
		super.insert(novoCliente);
		FotoNovoClienteService.getInstance().insereFotoNovoClienteNoBanco(novoCliente);
		if (LavenderePdaConfig.isPermitePedidoNovoCliente() || LavenderePdaConfig.permitePesquisaMercadoNovoCliente || LavenderePdaConfig.isUsaCadastroCoordenadasGeograficasNovoCliente() || LavenderePdaConfig.isUsaPesquisaNovoCliente() || LavenderePdaConfig.isUsaContatosNovoCliente()) {
			boolean isModoDeProspeccao = novoCliente.isModoDeProspeccao();
			try {
				if (isModoDeProspeccao) {
					ClientePdbxDao.getInstance().updateByNovoCliente(domain.getRowKey(), novoCliente.nuCnpj, novoCliente.cdRepresentante, isModoDeProspeccao, false);
				} else {
					Cliente cliente = ClienteService.getInstance().getCliente(novoCliente.cdEmpresa, novoCliente.cdRepresentante, novoCliente.nuCnpj);
					if (ValueUtil.isNotEmpty(cliente.cdCliente) && cliente.isNovoCliente()) {
						ClientePdbxDao.getInstance().updateByNovoCliente(domain.getRowKey(), novoCliente.nuCnpj, novoCliente.cdRepresentante, false, true);
					} else {
						ClienteService.getInstance().insertClienteByNovoCliente(novoCliente);
					}
				}
			} catch (Throwable e) {
				try {
					if (isModoDeProspeccao) {
						ClientePdbxDao.getInstance().updateByNovoCliente(domain.getRowKey(), novoCliente.oldNuCnpj, novoCliente.oldCdRepresentante, isModoDeProspeccao, false);
						return;
					} else {
						delete(domain);
						VisitaService.getInstance().deleteVisitaByNovoCliente(novoCliente);
					}
				} catch (Throwable ex) {
					delete(domain);
					VisitaService.getInstance().deleteVisitaByNovoCliente(novoCliente);
					ExceptionUtil.handle(ex);
					return;
				}
				ExceptionUtil.handle(e);
			}
		}
		if (novoCliente.salvaFlConsumidorFinal) {
			try {
				ClientePdbxDao.getInstance().updateFlConsumidorFinal(novoCliente.nuCnpj);
			} catch (Throwable e) {
				ExceptionUtil.handle(e);
			}
		}
	}

	@Override
	public void update(BaseDomain domain) throws SQLException {
		NovoCliente novoCliente = (NovoCliente)domain;
		String flPrimeiraEtapa = novoCliente.flPrimeiraEtapa;
		if (LavenderePdaConfig.isValidaCadastroDuasEtapas()) {
			novoCliente.flPrimeiraEtapa = ValueUtil.VALOR_NAO;
		}
		try {
			super.update(domain);
		} catch (Throwable e) {
			novoCliente.flPrimeiraEtapa = flPrimeiraEtapa;
			throw e;
		}
		if (LavenderePdaConfig.isPermitePedidoNovoCliente()
				|| LavenderePdaConfig.permitePesquisaMercadoNovoCliente
				|| LavenderePdaConfig.isUsaCadastroCoordenadasGeograficasNovoCliente()
				|| LavenderePdaConfig.isUsaPesquisaNovoCliente()
				|| LavenderePdaConfig.isUsaContatosNovoCliente()) {
			ClientePdbxDao.getInstance().updateByNovoCliente(novoCliente.getRowKey(), novoCliente.nuCnpj, novoCliente.cdRepresentante, novoCliente.isModoDeProspeccao(), true);
		}
		FotoNovoClienteService.getInstance().insereFotoNovoClienteNoBanco(novoCliente);
	}

	public NovoCliente getClienteSemFoto() throws SQLException {
		NovoCliente novoClienteFilter = new NovoCliente();
		novoClienteFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		novoClienteFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		novoClienteFilter.flTipoAlteracao = NovoCliente.FLTIPOALTERACAO_ALTERADO;
		Vector novoClienteList = findAllByExample(novoClienteFilter);
		int size = novoClienteList.size();
		for (int i = 0; i < size; i++) {
			NovoCliente novoCliente = (NovoCliente) novoClienteList.items[i];
			if (!FotoNovoClienteService.getInstance().hasFoto(novoCliente)) {
				return novoCliente;
			}
		}
		return null;
	}
	
	public void geraNovoClienteAPartirDeUmClienteProspects(NovoCliente novoCliente, Cliente clienteProspects) {
    	if (clienteProspects != null) {
    		Field[] camposCliente = Cliente.class.getDeclaredFields();
    		for (int i = 0; i < camposCliente.length; i++) {
    			Field field = camposCliente[i];
    			if (novoCliente.getHashValuesDinamicos().exists(field.getName().toUpperCase())) {
    				try {
    					if (CampoLavendereService.getCampoLavendereInstance().isNecessarioExtrairNumerosDeCamposMascarados(NovoCliente.TABLE_NAME, field.getName().toUpperCase())) {
    						field.set(clienteProspects, ValueUtil.getValidNumbers((String) field.get(clienteProspects)));
    	    			}
						novoCliente.getHashValuesDinamicos().put(field.getName().toUpperCase(), field.get(clienteProspects));
					} catch (Throwable e) {
						// Não faz nada
					} 
    			}
			}
    		verificaCamposDinamicosDeClienteProspect(novoCliente, clienteProspects);
    		novoCliente.flTipoCadastro = clienteProspects.flTipoCadastro;
    		novoCliente.flTipoPessoa = clienteProspects.isPessoaFisica() ? Messages.TIPOPESSOA_FLAG_FISICA : Messages.TIPOPESSOA_FLAG_JURIDICA;
    		novoCliente.nuCnpj = ValueUtil.getValidNumbers(clienteProspects.nuCnpj);
    	}
    }
	
	public void verificaCamposDinamicosDeClienteProspect(NovoCliente novoCliente, Cliente clienteProspects) {
		Vector camposDinamicosClienteProspectList = clienteProspects.getHashValuesDinamicos().getKeys();
		for (int i = 0; i < camposDinamicosClienteProspectList.size(); i++) {
			String coluna = ((String) camposDinamicosClienteProspectList.items[i]).toUpperCase();
			String value = (String) clienteProspects.getHashValuesDinamicos().get(coluna);
			if (ValueUtil.isNotEmpty(value) && novoCliente.getHashValuesDinamicos().exists(coluna)) {
				if (CampoLavendereService.getCampoLavendereInstance().isNecessarioExtrairNumerosDeCamposMascarados(NovoCliente.TABLE_NAME, coluna)) {
					value = ValueUtil.getValidNumbers(value);
				}
				novoCliente.getHashValuesDinamicos().put(coluna, value);
			}			
		}
	}

	public boolean validaCpfCnpjDuplicado(String nuCnpj, String oldNuCnpjCpf, boolean isFromSync, String flTipoCadastro) throws Exception {
		if (ValueUtil.isNotEmpty(nuCnpj)) {
			Cliente clienteFilter = new Cliente();
			clienteFilter.nuCnpj = nuCnpj;
			if ("2".equals(LavenderePdaConfig.getUsaConfirmacaoCPFCNPJIgualNovoCliente())) {
				clienteFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			}
			if (!isFromSync && !ValueUtil.valueEquals(nuCnpj, oldNuCnpjCpf) && ClienteService.getInstance().countByExample(clienteFilter) != 0) {
				throw new DuplicatedCnpjException(Messages.NOVOCLIENTE_CPFCNPJ_IGUAL_CLIENTE_ERRO);
			}
			
			NovoCliente novoClienteFilter = new NovoCliente();
			novoClienteFilter.nuCnpj = nuCnpj;
			String flValidaProspect = ValueUtil.valueEquals("P", flTipoCadastro) ? ValueUtil.VALOR_SIM : ValueUtil.VALOR_NAO;
			if ("2".equals(LavenderePdaConfig.getUsaConfirmacaoCPFCNPJIgualNovoCliente())) {
				novoClienteFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			}
			if (!isFromSync && countByExample(novoClienteFilter) != 0 && !ValueUtil.getBooleanValue(flValidaProspect)) {
				throw new DuplicatedCnpjException(Messages.NOVOCLIENTE_CPFCNPJ_IGUAL_CLIENTE_ERRO);
			}
			if (LavenderePdaConfig.usaValidacaoCPFCNPJBaseWeb && ValueUtil.VALOR_NAO.equals(LavenderePdaConfig.getUsaConfirmacaoCPFCNPJIgualNovoCliente())) {
				flValidaProspect = ValueUtil.VALOR_NAO;
			}
			
			if (LavenderePdaConfig.usaValidacaoCPFCNPJBaseWeb && !SyncManager.isNovoClienteValidoWeb(SessionLavenderePda.cdEmpresa, nuCnpj, flValidaProspect)) {
				throw new DuplicatedCnpjException(Messages.NOVOCLIENTE_CPFCNPJ_IGUAL_CLIENTE_ERRO);
			}
		}
		return false;
	}

	public void validaNovoClienteConfirmadoErp(Cliente cliente, boolean onPesquisaMercSemCli) throws SQLException {
		if (cliente.cdCliente.equals(cliente.nuCnpj)) {
			NovoCliente novoCliente = getNovoClienteByCliente(cliente);
			if (!novoCliente.isAlteradoPalm()) {
				if (onPesquisaMercSemCli) {
					throw new ValidationException(Messages.NOVOCLIENTE_MSG_PESQUISA_MERCADO_BLOQUEADO);
				}
				throw new ValidationException(Messages.NOVOCLIENTE_MSG_CADCOORDENADA_BLOQUEADO);
			}
		}
	}

	public boolean validaNovoClienteCoordenadaCadastradaEnvioServidor(String tableName, String values, String columns) {
		Vector columnsList = new Vector(StringUtil.split(columns, SyncComponent.SEPARADOR_ATUALIZACOES, true));
		Vector valuesList = new Vector(StringUtil.split(values, SyncComponent.SEPARADOR_ATUALIZACOES, true));
		int indexLatitude = columnsList.indexOf(NovoCliente.NMCOLUNA_CDLATITUDE);
		int indexLongitude = columnsList.indexOf(NovoCliente.NMCOLUNA_CDLONGITUDE);
		int indexLiberadoSenha = columnsList.indexOf(NovoCliente.NMCOLUNA_FLCADCOORDENADALIBERADO);
		String cdLatitude = "";
		String cdLongitude = "";
		String flLiberadoSenha = "";
		if (indexLatitude == -1) {
			indexLatitude = columnsList.indexOf(NovoCliente.NMCOLUNA_CDLATITUDE.toUpperCase());
		}
		if (indexLongitude == -1) {
			indexLongitude = columnsList.indexOf(NovoCliente.NMCOLUNA_CDLONGITUDE.toUpperCase());
		}
		if (indexLiberadoSenha == -1) {
			indexLiberadoSenha = columnsList.indexOf(NovoCliente.NMCOLUNA_FLCADCOORDENADALIBERADO.toUpperCase());
		}
		if (indexLatitude != -1) {
			cdLatitude = StringUtil.getStringValue(valuesList.items[indexLatitude]);
		}
		if (indexLongitude != -1) {
			cdLongitude = StringUtil.getStringValue(valuesList.items[indexLongitude]);
		}
		if (indexLiberadoSenha != -1 && indexLiberadoSenha < valuesList.size()) {
			flLiberadoSenha = StringUtil.getStringValue(valuesList.items[indexLiberadoSenha]);
		}
		if ((ValueUtil.getDoubleValue(cdLatitude) == 0d || ValueUtil.getDoubleValue(cdLongitude) == 0d) && !ValueUtil.VALOR_SIM.equals(flLiberadoSenha)) {
			LogSync.error(NovoCliente.NOME_ENTIDADE + " " + Messages.NOVOCLIENTE_ERRO_ENVIO_SERVIDOR_CADCOORD_OBRIGATORIO);
			LogSync.info(ValueUtil.VALOR_NI);
			return false;
		}
		return true;
	}
	
	@Override
	public void delete(BaseDomain domain) throws java.sql.SQLException {
		super.delete(domain);
		NovoCliente novoCliente = (NovoCliente) domain;
		if (ValueUtil.isEmpty(novoCliente.cdClienteOriginal)) {
			ClienteService.getInstance().deleteNovoCliente(novoCliente);
		} else {
			novoCliente.flTipoCadastro = NovoCliente.FLTIPOCADASTRO_PROSPECT;
			ClienteService.getInstance().updateFlTipoCadastro(novoCliente);
		}
		if (LavenderePdaConfig.isUsaContatosNovoCliente()) {
			ContatoPdaService.getInstance().deleteContatosNovoCliente(novoCliente);
		}
	}
	
	
	public void validateCnpj(String cnpj) {
		if (ValueUtil.isEmpty(cnpj)) {
			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.NOVOCLIENTE_LABEL_NUCNPJ);
		}
		if (LavenderePdaConfig.isUsaSistemaIdiomaIngles()) {
			return;
		} 
		if (LavenderePdaConfig.isUsaSistemaIdiomaEspanhol()) {
			if (CpfCnpjValidator.isRuc(cnpj) && !CpfCnpjValidator.isRucValido(cnpj)) {
				throw new ValidationException(Messages.NOVOCLIENTE_MSG_NUCNPJ_INVALIDO);
			}
		} else if (!CpfCnpjValidator.validateCnpjCpf(cnpj)) {
			throw new ValidationException(Messages.NOVOCLIENTE_MSG_NUCNPJCPF_INVALIDO);
		}
	}
	
	public Vector findNovoClienteListParaValidacao() throws SQLException {
		NovoCliente novoCliente = new NovoCliente();
		novoCliente.filtraNaoEnviados = true;
		novoCliente.filtraStatusCadastro = true;
		return findAllByExample(novoCliente);
	}
	
	public void updateFlStatusCadastro(NovoCliente novoCliente) throws SQLException {
		NovoClientePdbxDao.getInstance().updateFlStatusCadastro(novoCliente);
	}
	
	public Campo getCampoIE() {
		Vector campoList = NovoCliente.getConfigPersonCadList(NovoCliente.TABLE_NAME);
		int size = campoList.size();
		for (int i = 0; i < size; i++) {
			Campo campo = (Campo)campoList.items[i];
			if (Campo.FORMATO_INSCRICAOESTADUAL.equals(campo.dsFormato)) {
				return campo;
			}
		}
		return null;
	}
	
	public Campo getCamposDate() {
		Vector campoList = NovoCliente.getConfigPersonCadList(NovoCliente.TABLE_NAME);
		String vlChaveParametro = LavenderePdaConfig.getNmCampoValidaDataNovoCliente();
		int size = campoList.size();
		for (int i = 0; i < size; i++) {
			Campo campo = (Campo)campoList.items[i];
			if (campo.nmCampo.equals(vlChaveParametro)) {
				return campo;
			}
		}
		return null;
	}
	
	public void validateCampoDateNovoCliente(NovoCliente novoCliente) {
		Campo campoDate = getCamposDate();
		if (campoDate == null) return;
		String dateString = novoCliente.getHashValuesDinamicos().getString(campoDate.nmCampo);
		try {
			Date data = new Date(dateString);
			Date dataAtual = DateUtil.getCurrentDate();
			double intervalo = DateUtil.getYearsBetween(dataAtual, data);
			if (intervalo < LavenderePdaConfig.getNuDataAnoMinCadastroNovoCliente()) {
				throw new ValidationException(MessageUtil.getMessage(Messages.NOVO_CLIENTE_VALIDADE_DATA, StringUtil.getStringValue(LavenderePdaConfig.getNuDataAnoMinCadastroNovoCliente())));
			}
		} catch (InvalidDateException invDate) {
			ExceptionUtil.handle(invDate);
		}
	}

	public int findQtContatoNovoCliente(NovoCliente novoCli) throws SQLException {
		Vector vector = ContatoService.getInstance().findAllContatoByCliente(novoCli.nuCnpj);
		return vector.size();
	}

	public void validateQtMinContato(int qtContatos) {
		if (!LavenderePdaConfig.isUsaContatosNovoCliente()) return;
		if (qtContatos < LavenderePdaConfig.qtMinContatosNovoCliente()) {
			String[] args = {StringUtil.getStringValueToInterface(LavenderePdaConfig.qtMinContatosNovoCliente()), StringUtil.getStringValueToInterface(qtContatos)};
			throw new ValidationException(MessageUtil.getMessage(Messages.NOVOCLIENTE_MSG_QTMINCONTATO_NAO_ATINGIDA, args));
		}
	}

	public String findCdNovoClienteByCnpj(String nuCnpj) throws SQLException {
		return NovoClientePdbxDao.getInstance().findCdNovoClienteByCnpj(nuCnpj);
	}

}