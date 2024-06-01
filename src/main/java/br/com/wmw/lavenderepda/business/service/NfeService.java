package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.exception.ApplicationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.sync.transport.http.HttpConnectionManager;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Empresa;
import br.com.wmw.lavenderepda.business.domain.Estoque;
import br.com.wmw.lavenderepda.business.domain.LogPda;
import br.com.wmw.lavenderepda.business.domain.Nfe;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.SerieNfe;
import br.com.wmw.lavenderepda.business.domain.TipoPedido;
import br.com.wmw.lavenderepda.business.domain.dto.NfeDTO;
import br.com.wmw.lavenderepda.business.domain.dto.SerieNfeDTO;
import br.com.wmw.lavenderepda.business.validation.EnvioDadosJsonException;
import br.com.wmw.lavenderepda.business.validation.EnvioDadosNfeException;
import br.com.wmw.lavenderepda.business.validation.NfeException;
import br.com.wmw.lavenderepda.integration.dao.pdbx.NfeDao;
import br.com.wmw.lavenderepda.sync.LavendereWeb2Tc;
import br.com.wmw.lavenderepda.sync.SyncManager;
import totalcross.json.JSONObject;
import totalcross.sys.Convert;
import totalcross.sys.InvalidNumberException;
import totalcross.sys.Time;
import totalcross.sys.Vm;
import totalcross.util.Date;
import totalcross.util.Random;
import totalcross.util.Vector;

public class NfeService extends CrudService {

	private static NfeService instance;

	private NfeService() {
        //--
    }

    public static NfeService getInstance() {
        if (instance == null) {
            instance = new NfeService();
        }
        return instance;
    }

	protected CrudDao getCrudDao() {
		return NfeDao.getInstance();
	}
	
	public Nfe getNfe(String cdEmpresa, String cdRepresentante, String nuPedido, String flOrigemPedido) throws SQLException {
    	Nfe nfeFilter = new Nfe();
    	if (nuPedido == null || flOrigemPedido == null) {
    		return nfeFilter;
    	}
		nfeFilter.cdEmpresa = cdEmpresa;
		nfeFilter.cdRepresentante = cdRepresentante;
		nfeFilter.nuPedido = nuPedido;
		nfeFilter.flOrigemPedido = flOrigemPedido;
		Nfe nfe = (Nfe) findByRowKey(nfeFilter.getRowKey());
		if (nfe != null) {
			nfe.itemNfeList = LavenderePdaConfig.usaNfePorReferencia ? ItemNfeReferenciaService.getInstance().getItemNfeReferenciaList(nfeFilter) : ItemNfeService.getInstance().getItemNfeList(nfeFilter);
		}
    	return nfe;
    }
	
	public boolean isExisteNfeParaPedidos() throws SQLException {
		Nfe nfeFilter = new Nfe();
		nfeFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		if (SessionLavenderePda.isUsuarioSupervisor()) {
			Vector repSupList = SupervisorRepService.getInstance().getCdRepresentantesBySupervisor(nfeFilter.cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdRepresentante);
			nfeFilter.cdRepresentanteSupList = (String[])repSupList.toObjectArray();
		} else {
			nfeFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    }
		return countByExample(nfeFilter) > 0;
    }
	
	public void geraNfe(Pedido pedido,  boolean isPossuiConexao) throws SQLException {
		if (!isGeraNfe(pedido)) return;
		
		Nfe nfe = null;
		try {
			nfe = getNewNfe(pedido);
			nfe.dsTipoEmissao = getTipoEmissao(isPossuiConexao);
			List<SerieNfeDTO> serieNfeList = new ArrayList<>();
			String flContingencia = StringUtil.getStringValue(nfe.isContingencia() && LavenderePdaConfig.isNecessitaQueRepresentantePossuaSerieExclusivaDeContingencia());
			if (nfe.isContingencia()) {
				nfe.dtResposta = new Date();
				nfe.hrResposta = TimeUtil.getCurrentTimeHHMMSS();
			}
			try {
				if (isPossuiConexao) {
					serieNfeList = SyncManager.getSerieAndProximoNumero(SessionLavenderePda.cdEmpresa, SessionLavenderePda.getRepresentante().cdRepresentante, flContingencia);
				}
				setaValoresNfe(pedido, nfe, serieNfeList);
			} catch (NfeException e) {
				throw e;
			} catch (Throwable e) {
				ExceptionUtil.handle(e);
				controlaDadosNfeNaExcessao(pedido, isPossuiConexao, nfe, serieNfeList);
			} 
			ItemNfeService.getInstance().geraItensNfe(pedido, nfe);
			if (LavenderePdaConfig.isSomaVlStNoVlTotalNfe(nfe.dsTipoEmissao)) {
				nfe.vlTotalNfe += nfe.vlTotalSt;
			}
			insert(nfe);
			SerieNfeService.getInstance().updateNuProximoNumero(nfe.cdEmpresa, nfe.cdRepresentante, ValueUtil.getIntegerValue(nfe.dsSerieNfe), nfe.nuNfe);
		} catch (Throwable e) {
			trataExcecaoNfe(pedido, nfe, e);
		}
	}

	private void trataExcecaoNfe(Pedido pedido, Nfe nfe, Throwable e) {
		ExceptionUtil.handle(e);
		LogPdaService.getInstance().error(LogPda.LOG_CATEGORIA_ERRO_GERACAO_NFE, MessageUtil.getMessage(Messages.LOG_ERROR_GERACAO_NFE, new Object[] {pedido.nuPedido, Vm.getStackTrace(e)}));
		deletaNfeNaExcecao(nfe);
		throw new NfeException(e.getMessage());
	}

	private void deletaNfeNaExcecao(Nfe nfe) {
		if (nfe == null) return;
		try {
			delete(nfe);
		} catch (Throwable ex) {
			ExceptionUtil.handle(ex);
		}
		try {
			ItemNfeService.getInstance().excluiuItensNfe(nfe);
		} catch (Throwable ex) {
			ExceptionUtil.handle(ex);
		}
	}

	public boolean isGeraNfe(Pedido pedido) throws SQLException {
		TipoPedido tipoPedido =  pedido.getTipoPedido();
		return LavenderePdaConfig.usaGeracaoNfePedidoAposFechamento() && !LavenderePdaConfig.usaNfePorReferencia && !LavenderePdaConfig.isConfigGradeProduto() && tipoPedido != null && tipoPedido.isGeraNfe();
	}

	private void controlaDadosNfeNaExcessao(Pedido pedido, boolean isPossuiConexao, Nfe nfe, List<SerieNfeDTO> serieNfeList) throws SQLException {
		if (nfe.nuNfe == 0 || ValueUtil.isEmpty(nfe.dsSerieNfe)) {
			nfe.dsTipoEmissao = getTipoEmissaoNfeExcessao();
			setaValoresNfe(pedido, nfe, serieNfeList);
		}
	}

	private String getTipoEmissaoNfeExcessao() {
		return LavenderePdaConfig.isUsaGeracaoNotaNfeContingenciaPedidoSemConexao() ||  LavenderePdaConfig.isUsaSomenteGeracaoNotaNfeContingenciaPedido() ? Nfe.EMISSAO_CONTINGENCIA : Nfe.EMISSAO_NORMAL;
	}

	private void setaValoresNfe(Pedido pedido, Nfe nfe, List<SerieNfeDTO> serieNfeList) throws SQLException {
		setSerieENuNfe(nfe, serieNfeList);
		nfe.vlChaveAcesso = geraChaveAcessoNfe(pedido, nfe);
		if (ValueUtil.isEmpty(nfe.vlChaveAcesso)) throw new NfeException(Messages.NFE_ERRO_GERACAO_CHAVE);
	}
	
	private Nfe getNewNfe(Pedido pedido) throws SQLException {
		Date dtAtual = new Date();
		Nfe nfe = new Nfe();
		nfe.cdEmpresa = SessionLavenderePda.cdEmpresa;
		nfe.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		nfe.nuPedido = pedido.nuPedido;
		nfe.flOrigemPedido = OrigemPedido.FLORIGEMPEDIDO_PDA;
		nfe.cdCliente = pedido.cdCliente;
		nfe.cdTipoPedido = pedido.cdTipoPedido;
		nfe.dsNaturezaOperacao = pedido.getTipoPedido().dsNaturezaOperacao;
		nfe.dtSolicitacao = dtAtual;
		nfe.nuLote = 0;
		nfe.cdTipoOperacaoNfe = "1";
		nfe.dtEmissao = dtAtual;
		nfe.dtSaida = dtAtual;
		nfe.hrSaida = TimeUtil.getCurrentTimeHHMMSS();
		nfe.vlTotalDescontoFin = pedido.isTipoPedidoBonificacao() ? pedido.vlTotalPedido : pedido.vlTotalNotaCredito + pedido.vlDescontoIndiceFinanCliente;
		nfe.vlTotalNfe = pedido.isTipoPedidoBonificacao() ? pedido.vlTotalPedido : (pedido.vlTotalPedido + nfe.vlTotalDescontoFin);
		nfe.vlTotalDesconto = !pedido.isTipoPedidoBonificacao() ? pedido.getVlTotalDesconto() : 0;
		nfe.cdTransportadora = pedido.cdTransportadora;
		nfe.flAmbiente = String.valueOf(LavenderePdaConfig.usaGeracaoNfePedidoAposFechamento);
		nfe.hrEmissao = TimeUtil.getCurrentTimeHHMMSS();
		nfe.dsMensagemNotaCredito = NotaCreditoPedidoService.getInstance().getMensagemNotaCredito(pedido);
		return nfe;
	}

	private String getTipoEmissao(boolean possuiConexao) {
		return isGeraNotaContingencia(possuiConexao) ? Nfe.EMISSAO_CONTINGENCIA : Nfe.EMISSAO_NORMAL;
	}
	
	private boolean isGeraNotaContingencia(boolean possuiConexao) {
		if (LavenderePdaConfig.isUsaGeracaoNotaNfeContingenciaPedidoSemConexao()) {
			return !possuiConexao;
		}
		return LavenderePdaConfig.isUsaSomenteGeracaoNotaNfeContingenciaPedido();
	}

	private String geraChaveAcessoNfe(Pedido pedido, Nfe nfe) throws SQLException {
		Empresa empresa = pedido.getEmpresa();
		int[] chave = new int[43];
		addUfIbgeChave(chave);
		addDtEmissaoChave(chave, pedido.dtEmissao);
		if (ValueUtil.isEmpty(empresa.nuCnpj)) throw new NfeException(MessageUtil.getMessage(Messages.NFE_CNPJ_NAO_INFORMADO, new String[] {empresa.toString()}));
		addCnpjChave(chave, empresa.nuCnpj);
		//Tipo de nota 55 (NFe)
		chave[SerieNfe.POS_TIPO_NOTA] = chave[SerieNfe.POS_TIPO_NOTA + 1] = 5;
		addValueToChave(chave, ValueUtil.getIntegerValue(nfe.dsSerieNfe), SerieNfe.SIZE_SERIENFE, 22);
		addValueToChave(chave, nfe.nuNfe, SerieNfe.SIZE_NUNFE, 25);
		chave[SerieNfe.POS_TIPO_EMISSAO] = (LavenderePdaConfig.atualizaChaveNfeContingencia) ? ValueUtil.getIntegerValue(nfe.dsTipoEmissao) : Nfe.TIPO_EMISSAO;
		addRandomNumberChave(chave);
		int dv = geraDigitoVerificador(chave);
		StringBuffer sb = new StringBuffer(44);
		for (int chaveNum : chave) {
			sb.append(chaveNum);
		}
		sb.append(dv);
		return sb.toString();
	}
	
	private void setSerieENuNfe(Nfe nfe, List<SerieNfeDTO> serieNfeList) throws SQLException {
		if (serieNfeList == null || serieNfeList.isEmpty()) {
			SerieNfe filter = new SerieNfe();
			filter.cdEmpresa = nfe.cdEmpresa;
			filter.cdRepresentante = nfe.cdRepresentante;
			if (LavenderePdaConfig.isNecessitaQueRepresentantePossuaSerieExclusivaDeContingencia()) {
				filter.flExclusivaCont = nfe.isContingencia() ? ValueUtil.VALOR_SIM : ValueUtil.VALOR_NAO;
			}
			filter.sortAtributte = SerieNfe.COLUNA_NUSERIENFE;
			filter.sortAsc = ValueUtil.VALOR_SIM;
			Vector serieNfeItems = SerieNfeService.getInstance().findAllByExample(filter);
			if (ValueUtil.isEmpty(serieNfeItems)) throw new NfeException(Messages.NFE_SERIE_NAO_ENCONTRADA);
			
			int size = serieNfeItems.size();
			Nfe nfeFilter = new Nfe();
			int nuNfe = 0;
			for (int i = 0; i < size; i++) {
				SerieNfe serieNfe = (SerieNfe)serieNfeItems.items[i];
				nfeFilter.cdEmpresa = serieNfe.cdEmpresa;
				nfeFilter.cdRepresentante = serieNfe.cdRepresentante;
				nfeFilter.dsSerieNfe = StringUtil.getStringValue(serieNfe.nuSerieNfe);
				nuNfe = ValueUtil.getIntegerValue(maxByExample(nfeFilter, Nfe.NMCOLUNA_VLNUNFE));
				if (nuNfe < serieNfe.nuProximoNumero) {
					nuNfe = serieNfe.nuProximoNumero;
				} else {
					nuNfe++;
				}
				if (nuNfe <= SerieNfe.MAX_PROX_NUMERO) {
					nfe.nuNfe = nuNfe;
					nfe.dsSerieNfe = StringUtil.getStringValue(serieNfe.nuSerieNfe);
					return;
				} 
			}			
			
		} else {
			for (SerieNfeDTO serieNfe : serieNfeList) {
				nfe.dsSerieNfe = StringUtil.getStringValue(serieNfe.nuSerieNfe);
				int nuNfe = NfeDao.getInstance().getMaxNuNfe(nfe);
				if (nuNfe < serieNfe.nuProximoNumero) {
					nuNfe = serieNfe.nuProximoNumero;
				} else {
					nuNfe++;
				}
				if (nuNfe <= SerieNfe.MAX_PROX_NUMERO) {
					nfe.nuNfe = nuNfe;
					nfe.dsSerieNfe = StringUtil.getStringValue(serieNfe.nuSerieNfe);
					return;
				}
			}
		}
		throw new NfeException(Messages.NFE_SERIE_IMPOSSIVEL_GERAR);
	}
	
	private void addUfIbgeChave(int[] chave) throws SQLException {
		int[] cdUf = UFService.getInstance().getCdUfIbge();
		if (cdUf != null && cdUf.length == 2) {
			chave[0] = cdUf[0];
			chave[1] = cdUf[1];
		} else {
			throw new NfeException(Messages.NFE_ERRO_UFIBGE);
		}
	}
	
	private void addDtEmissaoChave(int[] chave, Date dtEmissao) {
		int i = SerieNfe.POS_DTEMISSAO;
		String date = DateUtil.formatDateYYMM(dtEmissao);
		int[] array = ValueUtil.charsToIntArray(date);
		for (int j : array) {
			chave[i++] = j;
		}
	}
	
	private void addCnpjChave(int[] chave, String nuCnpj) {
		int k = SerieNfe.POS_CNPJ;
		int[] array = ValueUtil.charsToIntArray(ValueUtil.getValidNumbers(nuCnpj));
		for (int j : array) {
			chave[k++] = j;
		}
	}
	
	private void addValueToChave(int[] chave, int value, int size, int pos) {
		String vlSerieStr = Convert.zeroPad(String.valueOf(value), size);
		int[] array = ValueUtil.charsToIntArray(vlSerieStr);
		for (int j : array) {
			chave[pos++] = j;
		}
	}
	
	private void addRandomNumberChave(int[] chave) {
		Random r = new Random();
		addValueToChave(chave, r.between(10000000, 99999999), SerieNfe.SIZE_RANDOM_NUMBER, SerieNfe.POS_RANDOM_NUMBER);
	}
	
	
	/**
	 * Gera o dígito verificador da NF-e pela soma da multiplicação cada número da chave por um
	 * multiplicador que varia de 2 a 9. Por fim a soma é divida por 11 e o dígito verificador
	 * é 11 menos o resto da divisão.
	 * @param chave
	 * @return digitoVerificador
	 */
	private int geraDigitoVerificador(int[] chave) {
		int multiplicador = 4;
		int size = chave.length;
		int soma = 0;
		for (int i = 0; i < size; i++) {
			soma += chave[i] * multiplicador;
			multiplicador--;
			if (multiplicador == 1) {
				multiplicador = 9;
			}
		}
		int resto = soma % SerieNfe.DIVISOR_DIGITO_VERIFICADOR;
		if (resto < 2) { 
			return 0;
		}
		return SerieNfe.DIVISOR_DIGITO_VERIFICADOR - resto;
	}
	
	private int qtdNotaContingenciaNaoEnviadaServidor(Pedido pedido) throws SQLException {
		Nfe nfeFilter = new Nfe();
		nfeFilter.cdEmpresa = pedido.cdEmpresa;
		nfeFilter.cdRepresentante = pedido.cdRepresentante;
		nfeFilter.flOrigemPedido = pedido.flOrigemPedido;
		nfeFilter.dsTipoEmissaoFiter = Nfe.EMISSAO_CONTINGENCIA;
		nfeFilter.filtraPorNaoEnviadoServidor = true;
		nfeFilter.filtraPorNaoCancelado = true;
		return countByExample(nfeFilter);
	}

	public void processaRetornoNfe(NfeDTO nfeDTO) throws SQLException {
		Nfe nfe = new Nfe(nfeDTO);
		NfeDao.getInstance().updateRetornoNfeByVlChaveAcesso(nfe);
		if (Nfe.CDMENSAGEMRETORNO_ERRO.equals(nfe.cdMensagemRetorno)) {
			throw new EnvioDadosNfeException(MessageUtil.getMessage(Messages.NFE_ERRO_MSG_RETORNO_NFE_ERRO, new Object[] {nfe.cdMensagemRetorno, nfe.dsMensagemRetorno}));
		}
		if (!Nfe.CDMENSAGEMRETORNO_SUCESSO.equals(nfe.cdMensagemRetorno)) {
			throw new EnvioDadosJsonException(MessageUtil.getMessage(Messages.NFE_ERRO_MSG_RETORNO_NFE, new Object[] {nfe.cdMensagemRetorno, nfe.dsMensagemRetorno}));
		}
	}

	public void validate(BaseDomain arg0) { 
		
	}
	
	protected boolean isPossuiNotaContingenciaNaoEnviadaServidor(Pedido pedido) throws SQLException {
		return qtdNotaContingenciaNaoEnviadaServidor(pedido) > 0;
	}
	
	protected void executaAcoesCancelamentoNfe(Pedido pedido) throws SQLException {
		Nfe nfe = pedido.getInfoNfe();
		if (nfe != null) {
			cancelaNfe(nfe);
			RemessaEstoqueService.getInstance().estornaEstoqueDaRemessa(nfe.itemNfeList,  pedido.itemPedidoList, pedido.getTipoPedido().isRemessaEstoque(), Estoque.FLORIGEMESTOQUE_ERP);
		}
	}

	private void cancelaNfe(Nfe nfe) throws SQLException {
		if (LavenderePdaConfig.isUsaCancelamentoNfePedido()) {
			
			if (ValueUtil.isEmpty(nfe.nuProtocolo)) {
				nfe.nuProtocolo = getNuProtocoloWeb(nfe);
				if (ValueUtil.isEmpty(nfe.nuProtocolo)) {
					throw new NfeException(Messages.NFE_IMPOSSIVEL_CANCELAR_NUPROTOCOLO);	
				}
			}
				
			nfe.cdMensagemRetorno = Nfe.CDMENSAGEMRETORNO_CANCELAMENTO;
			nfe.dsMensagemRetorno = Nfe.DSMENSAGEMRETORNO_CANCELAMENTO;
			nfe.dtResposta = new Date();
			nfe.hrResposta = TimeUtil.getCurrentTimeHHMMSS();
			NfeDao.getInstance().updateCancelamentoNfe(nfe);
		}
	}

	public void excluiuNfeContingencia(Nfe nfe) throws SQLException {
		if (nfe.isContingencia()) {
			try {
				delete(nfe);
				ItemNfeService.getInstance().excluiuItensNfe(nfe);
			} catch (ApplicationException e) {
				ExceptionUtil.handle(e);
			}
		}
	}
	
	protected boolean isPrazoCancelamentoNfeValido(Nfe nfe) throws SQLException {
		int nuIntervaloCancNfe = EmpresaService.getInstance().findNuIntervaloCancNfe(nfe.cdEmpresa);
		Time dataBaseParaCancelamentoNfe = getDataBaseParaCancelamentoNfe(nfe, nuIntervaloCancNfe);
		if (dataBaseParaCancelamentoNfe != null) {
			return TimeUtil.getSecondsRealBetween(dataBaseParaCancelamentoNfe, new Time()) > 0; 
		}
		return true;
	}
	
	private Time getDataBaseParaCancelamentoNfe(Nfe nfe, int nuIntervaloCancNfe) {
		Date dtResposta = nfe.dtResposta;
		String hrResposta = nfe.hrResposta;
		if (ValueUtil.isNotEmpty(dtResposta) && ValueUtil.isNotEmpty(hrResposta)) {
			Time time = null;
			try {
				time = TimeUtil.toTime(dtResposta + " " + hrResposta);
			} catch (InvalidNumberException e) {
				return null;
			}
			time.inc(0, nuIntervaloCancNfe, 0);
			return time;		
		}
		return null;
	}

	public void atualizaNfeAposEnvioServidor() throws SQLException {
		Nfe nfeFilter = new Nfe();
		nfeFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		nfeFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		nfeFilter.flOrigemPedido = OrigemPedido.FLORIGEMPEDIDO_PDA;
		nfeFilter.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_ORIGINAL;
		nfeFilter.filtraPorNaoEnviadoServidor = true;
		NfeDao.getInstance().updateNfeTransmitidaComSucesso(nfeFilter);
	}
	
	private String getNuProtocoloWeb(Nfe nfe) {
		try {
			if (SyncManager.isConexaoPdaDisponivel()) {
				LavendereWeb2Tc webToPda = new LavendereWeb2Tc(HttpConnectionManager.getDefaultParamsSync());
			JSONObject jsonObject = new JSONObject();
				jsonObject.put("cdPda", Session.getCdUsuario());
			jsonObject.put("cdEmpresa", nfe.cdEmpresa);
			jsonObject.put("cdRepresentante", nfe.cdRepresentante);
			jsonObject.put("nuPedido", nfe.nuPedido);
			jsonObject.put("vlChaveAcesso", nfe.vlChaveAcesso);
			return webToPda.getNuProtocoloNfe(jsonObject.toString());
			}
		} catch (Throwable ex) {
			ExceptionUtil.handle(ex);
		}
			return null;
		}

	public Nfe findNfeByNuPedidoRelacionado(String cdEmpresa, String cdRepresentante, String nuPedidoRelacionado) throws SQLException {
		Nfe filter = new Nfe();
		filter.cdEmpresa = cdEmpresa;
		filter.cdRepresentante = cdRepresentante;
		filter.nuPedido = nuPedidoRelacionado;
		Vector nfeList = findAllByExample(filter);
		return nfeList != null ? (Nfe)nfeList.items[0] : null;
	}
	
	public boolean isNfeRelacionadaAoTituloFinanceiro(int nuNfe) throws SQLException {
		Nfe filter = new Nfe();
		filter.nuNfe = nuNfe;
		Vector nfeList = findAllByExample(filter);
		return !nfeList.isEmpty();
}
	
}
