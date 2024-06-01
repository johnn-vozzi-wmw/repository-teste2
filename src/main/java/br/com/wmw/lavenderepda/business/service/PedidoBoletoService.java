package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.BoletoConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.CondicaoPagamento;
import br.com.wmw.lavenderepda.business.domain.PagamentoPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PedidoBoleto;
import br.com.wmw.lavenderepda.business.domain.TipoPagamento;
import br.com.wmw.lavenderepda.business.domain.VenctoPagamentoPedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PedidoBoletoDao;
import br.com.wmw.lavenderepda.sync.LavendereWeb2Tc;
import br.com.wmw.lavenderepda.sync.SyncManager;
import totalcross.json.JSONObject;
import totalcross.sys.Convert;
import totalcross.util.BigDecimal;
import totalcross.util.Date;
import totalcross.util.Hashtable;
import totalcross.util.InvalidDateException;
import totalcross.util.Vector;

public class PedidoBoletoService extends CrudService {

    private static PedidoBoletoService instance;
     
    public static PedidoBoletoService getInstance() {
        if (instance == null) {
            instance = new PedidoBoletoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return PedidoBoletoDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException { }
    
    public Vector getPedidoBoletoList(final Pedido pedido) throws SQLException {
    	if (pedido != null) {
    		PedidoBoleto pedidoBoletoFilter = new PedidoBoleto();
    		pedidoBoletoFilter.cdEmpresa = pedido.cdEmpresa;
    		pedidoBoletoFilter.cdRepresentante = pedido.cdRepresentante;
    		pedidoBoletoFilter.flOrigemPedido = pedido.flOrigemPedido;
    		pedidoBoletoFilter.nuPedido = pedido.nuPedido;
    		pedidoBoletoFilter.sortAtributte = PedidoBoleto.NMCOLUNA_NUSEQUENCIABOLETOPEDIDO;
    		pedidoBoletoFilter.sortAsc = ValueUtil.VALOR_SIM;
    		return findAllByExample(pedidoBoletoFilter);
    	}
    	return null;
    }
    
    public boolean isExisteBoletoParaPedidos() throws SQLException {
    	PedidoBoleto pedidoBoletoFilter = new PedidoBoleto();
    	pedidoBoletoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	if (SessionLavenderePda.isUsuarioSupervisor()) {
    		Vector repSupList = SupervisorRepService.getInstance().getCdRepresentantesBySupervisor(pedidoBoletoFilter.cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdRepresentante);
    		pedidoBoletoFilter.cdRepresentanteSupList = (String[])repSupList.toObjectArray();
    	} else {
    	pedidoBoletoFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		}
    	return countByExample(pedidoBoletoFilter) > 0;
    }

    public void geraBoletoPedido(Pedido pedido) throws SQLException, InvalidDateException {
 		boolean usaVctoAdicional = LavenderePdaConfig.usaVencimentosAdicionaisBoleto;
    	pedido.pedidoBoletoList = findAllBoletoPedido(pedido);
		if (ValueUtil.isNotEmpty(pedido.pedidoBoletoList)) {
			return;
		}
		Vector pagamentoPedidoList = null;
		if (!LavenderePdaConfig.usaMultiplosPagamentosParaPedido && (pedido.getTipoPagamento() == null || !pedido.getTipoPagamento().isBoleto())) {
			throw new ValidationException(Messages.BOLETO_OFFLINE_VALIDACAO_TIPO_PAGAMENTO);
		} 
		Date ultimaDtVencimento = new Date();
		Hashtable nuLastDocumentoHash =  null;
		if (LavenderePdaConfig.usaMultiplosPagamentosParaPedido) {
			pagamentoPedidoList = getPagamentoPedidoBoletoList(pedido);
			int size = pagamentoPedidoList.size();
			
			if (size == 0) return; 
			
			Vector parcelaPagamentoPedidoList = new Vector();
			nuLastDocumentoHash = new Hashtable(size);
			for (int i = 0; i < size; i++) {
				PagamentoPedido pagamentoPedido = (PagamentoPedido) pagamentoPedidoList.items[i];
				CondicaoPagamento condicaoPagamento = CondicaoPagamentoService.getInstance().getCondicaoPagamento(pagamentoPedido.cdCondicaoPagamento);
				String cdBoletoConfig = getCdBoletoConfigPagamentoPedido(pagamentoPedido);
				if (ValueUtil.isNotEmpty(cdBoletoConfig)) {
					Date dtVencimento = new Date();
					if (condicaoPagamento != null && condicaoPagamento.nuParcelas > 1) {
						double vlTotalParcelas = 0;
						double vlParcela = ValueUtil.round(calculaValorDePagamento(pagamentoPedido) / condicaoPagamento.nuParcelas);
						for (int j = 0; j < condicaoPagamento.nuParcelas; j++) {
							vlTotalParcelas += vlParcela;
							PagamentoPedido pagamentoPedidoParcela = new PagamentoPedido();
							pagamentoPedidoParcela = (PagamentoPedido) pagamentoPedido.clone();
							pagamentoPedidoParcela.vlPagamentoPedido = vlParcela;
							pagamentoPedidoParcela.nuDocumento = getNuDocumento(pedido, cdBoletoConfig, nuLastDocumentoHash);
							pagamentoPedidoParcela.dtVencimentoBoleto = dtVencimento = gerarDtVencimento(ultimaDtVencimento, condicaoPagamento, dtVencimento, pedido.getCliente(),j == 0);
							parcelaPagamentoPedidoList.addElement(pagamentoPedidoParcela);
							ultimaDtVencimento.set(dtVencimento.getDay(), dtVencimento.getMonth(), dtVencimento.getYear());
						}
						//Caso sobre alguns valores por causa do arredondamento, deve-se adicioná-lo na primeira parcela
						if (!ValueUtil.isEmpty(parcelaPagamentoPedidoList)) {
							PagamentoPedido primeiroPagamentoPedido = (PagamentoPedido) parcelaPagamentoPedidoList.items[0];
							primeiroPagamentoPedido.vlPagamentoPedido += ValueUtil.round(calculaValorDePagamento(pagamentoPedido) - vlTotalParcelas);
						}
					} else {
						pagamentoPedido.dtVencimentoBoleto = getDtVencimento(pagamentoPedido.dtVencimento, condicaoPagamento);
						if (usaVctoAdicional) {
							VenctoPagamentoPedidoService.getInstance().findVctosPagamentoPedido(pagamentoPedido);
							if (pagamentoPedido.venctoPagamentoPedidos == null || pagamentoPedido.venctoPagamentoPedidos.isEmpty()) {
								pagamentoPedido.nuDocumento = getNuDocumento(pedido, cdBoletoConfig, nuLastDocumentoHash);
							}
						} else {
							pagamentoPedido.nuDocumento = getNuDocumento(pedido, cdBoletoConfig, nuLastDocumentoHash);
						}
						pagamentoPedido.vlPagamentoPedido = calculaValorDePagamento(pagamentoPedido);
						parcelaPagamentoPedidoList.addElement(pagamentoPedido);
					}
				}
			}
			int parcelaSize = parcelaPagamentoPedidoList.size();
			int k = 1;
			for (int i = 0; i < parcelaSize; i++) {
				PagamentoPedido pagamentoPedido = (PagamentoPedido) parcelaPagamentoPedidoList.items[i];
				String cdBoletoConfig = getCdBoletoConfigPagamentoPedido(pagamentoPedido);
				if (usaVctoAdicional && pagamentoPedido.venctoPagamentoPedidos != null && !pagamentoPedido.venctoPagamentoPedidos.isEmpty()) {
					for (VenctoPagamentoPedido vencto : pagamentoPedido.venctoPagamentoPedidos) {
						vencto.nuDocumento = getNuDocumento(pedido, cdBoletoConfig, nuLastDocumentoHash);
						criaPedidoBoletoCont(pedido, vencto.nuDocumento, pagamentoPedido.cdPagamentoPedido, cdBoletoConfig, vencto.vlBoleto, k++, vencto.dtVencimento);
					}
				} else {
					criaPedidoBoletoCont(pedido, pagamentoPedido.nuDocumento, pagamentoPedido.cdPagamentoPedido, getCdBoletoConfigPagamentoPedido(pagamentoPedido), pagamentoPedido.vlPagamentoPedido, i + 1, pagamentoPedido.dtVencimentoBoleto); 
				}
			}
		} else {
			CondicaoPagamento condicaoPagamento = CondicaoPagamentoService.getInstance().getCondicaoPagamento(pedido.cdCondicaoPagamento);
			Date dtVencimento = new Date();
			if (condicaoPagamento != null && condicaoPagamento.nuParcelas > 1) {
				Vector parcelaList = new Vector();
				double vlParcela = pedido.vlTotalPedido / condicaoPagamento.nuParcelas;
				nuLastDocumentoHash = new Hashtable(condicaoPagamento.nuParcelas);
				for (int i = 0; i < condicaoPagamento.nuParcelas; i++) {
					Pedido pedidoParcela = (Pedido) pedido.clone();
					pedidoParcela.vlTotalPedido = vlParcela;
					pedidoParcela.nuDocumento = getNuDocumento(pedidoParcela, pedidoParcela.getTipoPagamento().cdBoletoConfig, nuLastDocumentoHash);
					pedidoParcela.dtVencimento = dtVencimento = gerarDtVencimento(ultimaDtVencimento, condicaoPagamento, dtVencimento, pedido.getCliente(), i == 0);
					parcelaList.addElement(pedidoParcela);
					ultimaDtVencimento.set(dtVencimento.getDay(), dtVencimento.getMonth(), dtVencimento.getYear());
				}
				int size = parcelaList.size();
				for (int i = 0; i < size; i++) {
					Pedido pedidoParcela = (Pedido) parcelaList.items[i];
					criaPedidoBoletoCont(pedidoParcela, pedidoParcela.nuDocumento, PedidoBoleto.CDPAGAMENTOPEDIDO_ZERO, pedidoParcela.getTipoPagamento().cdBoletoConfig, pedidoParcela.vlTotalPedido, i + 1, pedidoParcela.dtVencimento);
				}
			} else {
				nuLastDocumentoHash = new Hashtable(1);
				BigDecimal nuDocumento = getNuDocumento(pedido, pedido.getTipoPagamento().cdBoletoConfig, nuLastDocumentoHash);
				if (condicaoPagamento != null) {
					dtVencimento.advance(condicaoPagamento.nuIntervaloEntrada);
				}
				criaPedidoBoletoCont(pedido, nuDocumento, PedidoBoleto.CDPAGAMENTOPEDIDO_ZERO, pedido.getTipoPagamento().cdBoletoConfig, pedido.vlTotalPedido, 1, dtVencimento);
			}
		}
	}
    
    private double calculaValorDePagamento(PagamentoPedido pagamentoPedido) {
    	return pagamentoPedido.vlPagamentoPedido -  pagamentoPedido.vlDesconto;
    }

	private Vector getPagamentoPedidoBoletoList(Pedido pedido) throws SQLException {
		Vector pagamentoPedidoList;
		PagamentoPedido pagamentoPedidoFilter = PagamentoPedidoService.getInstance().getPagamentoPedidoFilter(pedido);
		pagamentoPedidoList = PagamentoPedidoService.getInstance().findAllByExample(pagamentoPedidoFilter);
		for (int i = 0; i < pagamentoPedidoList.size(); i++) {
			PagamentoPedido pagamentoPedido = (PagamentoPedido) pagamentoPedidoList.items[i];
			if (!TipoPagamentoService.getInstance().isBoleto(pagamentoPedido)) {
				pagamentoPedidoList.removeElement(pagamentoPedido);
				i--;
			}
		}
		return pagamentoPedidoList;
	}
	
	private Date getDtVencimento(Date dtVencimentoPagamentoPedido, CondicaoPagamento condicaoPagamento) {
		Date dtVencimento = new Date();
		if (LavenderePdaConfig.permiteAlterarVencimentoConformeCondPagto || LavenderePdaConfig.permiteAlterarVencimentoConformeCliente) {
			dtVencimento = dtVencimentoPagamentoPedido;
		} else if (condicaoPagamento != null) {
			dtVencimento.advance(condicaoPagamento.nuIntervaloEntrada);
		}
		return DateUtil.getDateValue(dtVencimento);
	}

	private Date gerarDtVencimento(Date ultimaDtVencimento, CondicaoPagamento condicaoPagamento, Date dtVencimento,Cliente cliente ,boolean isPrimeiraDtVencimento) throws InvalidDateException {
		if (LavenderePdaConfig.permiteAlterarVencimentoConformeCliente) {
			geraDataConformeIntervalo(isPrimeiraDtVencimento, dtVencimento, ultimaDtVencimento, cliente != null ? cliente.qtDiasMaximoPagamento : 0);
		} else {
			geraDataConformeIntervalo(isPrimeiraDtVencimento, dtVencimento, ultimaDtVencimento, isPrimeiraDtVencimento ? condicaoPagamento.nuIntervaloEntrada : condicaoPagamento.nuIntervaloParcelas);
		}
		return dtVencimento;
	}
	
	private Date geraDataConformeIntervalo(boolean isPrimeiraDtVencimento, Date dtVencimento, Date ultimaDtVencimento,int intervalo) throws InvalidDateException{
		if (isPrimeiraDtVencimento) {
			dtVencimento.advance(intervalo);
		} else {
			dtVencimento = new Date();
			dtVencimento.set(ultimaDtVencimento.getDay(), ultimaDtVencimento.getMonth(), ultimaDtVencimento.getYear());
			dtVencimento.advance(intervalo);
		}
		return dtVencimento;
	}

	protected Vector findAllBoletoPedido(Pedido pedido) throws SQLException {
		PedidoBoleto pedidoBoletoFilter = getFilterByPedido(pedido);
		return findAllByExample(pedidoBoletoFilter);
	}

	private String getCdBoletoConfigPagamentoPedido(PagamentoPedido pagamentoPedido) throws SQLException {
		TipoPagamento tipoPagamento = TipoPagamentoService.getInstance().getTipoPagamento(pagamentoPedido.cdTipoPagamento);
		if (tipoPagamento != null) {
			BoletoConfig boletoConfig = BoletoConfigService.getInstance().getBoletoConfig(tipoPagamento.cdBoletoConfig);
			if (boletoConfig == null || ValueUtil.isEmpty(boletoConfig.cdBoletoConfig)) {
				throw new ValidationException(MessageUtil.getMessage(Messages.GERACAO_BOLETO_CONTIGENCIA_ERRO_BANCO_TIPOPAGAMENTO, tipoPagamento.toString()));
			}
			return boletoConfig.cdBoletoConfig;
		} else {
			throw new ValidationException(Messages.GERACAO_BOLETO_CONTIGENCIA_ERRO_TIPOPAGAMENTO);
		}
	}

	private BigDecimal getNuDocumento(Pedido pedido, String cdBoletoConfig, Hashtable nuLastDocumentoHash) throws SQLException {
		BigDecimal nuDocumento = null;
		BigDecimal nuDocumentoPda = new BigDecimal(0);
		BigDecimal nuDocumentoWeb = new BigDecimal(0);
		BigDecimal nuDocumentoPedidoBoleto = null;
		BigDecimal nuDocumentoFaixaBoleto = null;
		
		nuDocumentoPedidoBoleto = getLastNuDocumento(pedido, cdBoletoConfig);
		if (nuDocumentoPedidoBoleto != null) {
			nuDocumentoPedidoBoleto = FaixaBoletoService.getInstance().getNextNuDocumento(pedido.cdEmpresa, pedido.cdRepresentante, cdBoletoConfig, nuDocumentoPedidoBoleto);
		}
		
		nuDocumentoFaixaBoleto = FaixaBoletoService.getInstance().getNextNuDocumentoFaixa(pedido, cdBoletoConfig);
		
		nuDocumentoPedidoBoleto = (nuDocumentoPedidoBoleto) != null ? nuDocumentoPedidoBoleto : new BigDecimal(0);
		nuDocumentoFaixaBoleto = (nuDocumentoFaixaBoleto) != null ? nuDocumentoFaixaBoleto : new BigDecimal(0);
		
		nuDocumentoPda = nuDocumentoPedidoBoleto.max(nuDocumentoFaixaBoleto);
		nuDocumentoWeb = getNuDocumentoWeb(pedido.cdEmpresa, pedido.cdRepresentante, cdBoletoConfig);
		
		nuDocumento = nuDocumentoPda.max(nuDocumentoWeb);
		return nuDocumento;
	}

	private BigDecimal getNuDocumentoWeb(String cdEmpresa, String cdRepresentante, String cdBoletoConfig) {
		try {
			if (!SyncManager.isConexaoPdaDisponivel()) return new BigDecimal(0);
			LavendereWeb2Tc webToPda = new LavendereWeb2Tc();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("cdPda", Session.getCdUsuario());
			jsonObject.put("cdEmpresa", cdEmpresa);
			jsonObject.put("cdRepresentante", cdRepresentante);
			jsonObject.put("cdBoletoConfig", cdBoletoConfig);
			String value = webToPda.getNextNuBoleto(jsonObject.toString());
			return ValueUtil.isNotEmpty(value) ? new BigDecimal(value) : new BigDecimal(0);
		} catch (Throwable ex) {
			ExceptionUtil.handle(ex);
			return new BigDecimal(0);
		}
	}

	private void criaPedidoBoletoCont(Pedido pedido, BigDecimal nuDocumento, String cdPagamentoPedido, String cdBoletoConfig, double vlBoleto, int nuSequencia, Date dtVencimento) throws SQLException {
		if (nuDocumento == null) {
			throw new ValidationException(Messages.BOLETO_OFFLINE_VALIDACAO_NUDOCUMENTO_NULL);
		}
		PedidoBoleto pedidoBoleto = new PedidoBoleto(); 
		BoletoConfig boletoConfig = BoletoConfigService.getInstance().getBoletoConfig(cdBoletoConfig);
		pedidoBoleto.cdEmpresa = pedido.cdEmpresa;
		pedidoBoleto.cdRepresentante = pedido.cdRepresentante;
		pedidoBoleto.nuPedido = pedido.nuPedido;
		pedidoBoleto.flOrigemPedido = pedido.flOrigemPedido;
		pedidoBoleto.cdPagamentoPedido = cdPagamentoPedido; 
		pedidoBoleto.cdBoletoConfig = cdBoletoConfig;
		pedidoBoleto.dtDocumento = pedidoBoleto.dtProcessamento = DateUtil.getCurrentDate(); 
		pedidoBoleto.vlBoleto = vlBoleto; 
		pedidoBoleto.nuDocumento = nuDocumento;
		pedidoBoleto.dtVencimento = DateUtil.getCurrentDate();
		pedidoBoleto.dtVencimento = dtVencimento;
		pedidoBoleto.nuSequenciaBoletoPedido = nuSequencia;
		pedidoBoleto.nuNossoNumero = createNuNossoNumero(nuDocumento, pedidoBoleto, boletoConfig); 
		pedidoBoleto.cdBarras = createCdBarras(pedidoBoleto, boletoConfig);
		pedidoBoleto.dsLinhasDigitavel = createDsLinhasDigitavel(pedidoBoleto);
		pedidoBoleto.dsObsCedente = !ValueUtil.VALOR_NAO.equals(LavenderePdaConfig.dsLinhasTextoResponsabilidadeCedenteBoletoContingencia) ? createDsObsCedente(pedidoBoleto, pedido, boletoConfig) : "";
		pedidoBoleto.nuAgenciaCodigoCedente = boletoConfig.nuAgenciaCodigoCedente;
		pedidoBoleto.dsEspecie = ValueUtil.isNotEmpty(boletoConfig.dsEspecieBanco) ? boletoConfig.dsEspecieBanco : PedidoBoleto.DSESPECIE;
		pedidoBoleto.dsEspecieDocumento = PedidoBoleto.DSESPECIEDOCUMENTO;
		pedidoBoleto.flAceite = pedido.getCliente().getFlBoletoAceite();
		pedidoBoleto.nuCarteira = boletoConfig.nuCarteira;
		if (ValueUtil.isNotEmpty(boletoConfig.dsLocalPagamento)) {
			pedidoBoleto.dsLocalPagamento = boletoConfig.dsLocalPagamento;
		} else {
			pedidoBoleto.dsLocalPagamento = Messages.IMPRESSAOBOLETO_LOCAL_PAGAMENTO_MSG_PADRAO;
		}
		insert(pedidoBoleto);
		pedido.pedidoBoletoList.addElement(pedidoBoleto);
	}
	
	private String createDsObsCedente(PedidoBoleto pedidoBoleto, Pedido pedido, BoletoConfig boletoConfig) throws SQLException {
		StringBuffer textoCedente = new StringBuffer();
		TipoPagamento tipoPagamento = null;
		PagamentoPedido pagamentoPedido = PagamentoPedidoService.getInstance().getPagamentoPedido(pedidoBoleto);
		if (pagamentoPedido != null) {
			tipoPagamento = TipoPagamentoService.getInstance().getTipoPagamento(pagamentoPedido.cdTipoPagamento);
		}
		if (tipoPagamento == null) {
			tipoPagamento = pedido.getTipoPagamento();
		}
		List<String> listMsgCedente = Arrays.asList(LavenderePdaConfig.dsLinhasTextoResponsabilidadeCedenteBoletoContingencia.split(";"));
		int size = listMsgCedente.size();
		int nuLinhas = 0;
		for (String cdMsg : listMsgCedente) {
			switch (cdMsg) {
			case PedidoBoleto.TEXTO_RESP_CDEDENTE_1:
				double multa = pedidoBoleto.vlBoleto * (tipoPagamento.vlPctMultaDiario / 100);
				Date dtProtesto = DateUtil.getDateValue(pedidoBoleto.dtVencimento);
				dtProtesto.advance(tipoPagamento.nuDiasProtesto);
				textoCedente.append(MessageUtil.getMessage(Messages.IMPRESSAOBOLETO_TEXTO_RESP_CEDENTE_1, new String[] {StringUtil.getStringValueToInterface(multa), DateUtil.formatDateDDMMYYYY(dtProtesto)}));
				textoCedente.append(size > 1 ? "\n" : "");
				break;
			case PedidoBoleto.TEXTO_RESP_CDEDENTE_2:
				textoCedente.append(MessageUtil.getMessage(Messages.IMPRESSAOBOLETO_TEXTO_RESP_CEDENTE_2, boletoConfig.nmBanco));
				textoCedente.append(size > 1 ? "\n" : "");
				break;
			case PedidoBoleto.TEXTO_RESP_CDEDENTE_3:
				textoCedente.append(MessageUtil.getMessage(Messages.IMPRESSAOBOLETO_TEXTO_RESP_CEDENTE_3, pedido.getEmpresa().nmEmpresa));
				textoCedente.append(size > 1 ? "\n" : "");
				break;
			case PedidoBoleto.TEXTO_RESP_CDEDENTE_4:
				double vlMulta = pedidoBoleto.vlBoleto * (tipoPagamento.vlPctMulta / 100);
				Date dtMulta = DateUtil.getDateValue(pedidoBoleto.dtVencimento);
				dtMulta.advance(tipoPagamento.nuDiasMulta);
				textoCedente.append(MessageUtil.getMessage(Messages.IMPRESSAOBOLETO_TEXTO_RESP_CEDENTE_4, new String[] {StringUtil.getStringValueToInterface(vlMulta), DateUtil.formatDateDDMMYYYY(dtMulta)}));
				textoCedente.append(size > 1 ? "\n" : "");
				break;
			case PedidoBoleto.TEXTO_RESP_CDEDENTE_5:
				textoCedente.append(MessageUtil.getMessage(Messages.IMPRESSAOBOLETO_TEXTO_RESP_CEDENTE_5, tipoPagamento.nuDiasMaxPagamento));
				textoCedente.append(size > 1 ? "\n" : "");
				break;
			}
			nuLinhas++;
			if (nuLinhas >= 4) {
				break;
			}
		}
		return textoCedente.toString();
	}

	private String pegaTresPrimeirosCaracteres(String valor) {
		if (valor.length() <= 3) return valor;

		return valor.substring(0, 3);
	}

	private String createDsLinhasDigitavel(PedidoBoleto pedidoBoleto) {
		StringBuffer dsLinhasDigitavel = new StringBuffer();
		String id3 = pedidoBoleto.cdBarras.substring(24, 34);
		String id5 = pedidoBoleto.cdBarras.substring(34, 44);
		dsLinhasDigitavel.append(pedidoBoleto.cdBarras.substring(0, 4));
		dsLinhasDigitavel.append(pedidoBoleto.cdBarras.substring(19, 24));
		dsLinhasDigitavel.append(geraDigitoVerificadorDsLinhasDigitavel(dsLinhasDigitavel.toString()));
		dsLinhasDigitavel.append(id3);
		dsLinhasDigitavel.append(geraDigitoVerificadorDsLinhasDigitavel(id3));
		dsLinhasDigitavel.append(id5);
		dsLinhasDigitavel.append(geraDigitoVerificadorDsLinhasDigitavel(id5));
		dsLinhasDigitavel.append(pedidoBoleto.cdBarras.substring(4, 19));
		return dsLinhasDigitavel.toString();
	}

	private int geraDigitoVerificadorDsLinhasDigitavel(String caracters) {
		String[] digitos = caracters.split("(?!^)");
		int indiceMult = 2;
		int vlTotal = 0;
		int size  = digitos.length;
		for (int i = size - 1; i >= 0; i--) {
			String digito = digitos[i];
			try {
				int digitoAsInt = ValueUtil.getIntegerValue(digito);
				int resultMult = indiceMult * digitoAsInt;
				if (resultMult > 9) {
					String[] values = StringUtil.getStringValue(resultMult).split("(?!^)");
					vlTotal += ValueUtil.getIntegerValue(values[0]);
					vlTotal += ValueUtil.getIntegerValue(values[1]);
				} else {
					vlTotal += resultMult;
				}
			} catch (Throwable e) {
			} finally {
				if (indiceMult == 1) {
					indiceMult = 2;
				} else { 
					indiceMult = 1;
				}
			}
		}
		if (vlTotal < 10) {
			return 10 - vlTotal;
		}
		int vlResto = vlTotal % 10;
		return vlResto != 0 ? 10 - vlResto : 0;
	}

	private String createNuNossoNumero(BigDecimal nuDocumento, PedidoBoleto pedidoBoleto, BoletoConfig boletoConfig) {
		StringBuffer nuNossoNumero = new StringBuffer();
		validaColunaPreenchida(boletoConfig.flModoBoleto, "FLMODOBOLETO");
		if (BoletoConfig.MODO_CEF.equals(boletoConfig.flModoBoleto)) {
			if (ValueUtil.isEmpty(boletoConfig.nuCarteira)) throw new ValidationException(Messages.NFE_NUCARTEIRA_NAO_INFORMADO);
			nuNossoNumero.append(boletoConfig.nuCarteira.substring(0, 2))
			.append(Convert.zeroPad(String.valueOf(nuDocumento), 15)).append("-")
			.append(geraDigitoVerificadorNuNossoNumero(nuNossoNumero.toString(), BoletoConfig.MODO_CEF));
		} else if (BoletoConfig.MODO_BRADESCO.equals(boletoConfig.flModoBoleto)) {
			if (ValueUtil.isEmpty(boletoConfig.nuCarteira)) throw new ValidationException(Messages.NFE_NUCARTEIRA_NAO_INFORMADO);
			nuNossoNumero.append(Convert.zeroPad(String.valueOf(nuDocumento), 11).substring(0, 11))
			.append(geraDigitoVerificadorNuNossoNumero(boletoConfig.nuCarteira + nuNossoNumero.toString(), BoletoConfig.MODO_BRADESCO));
		} else if (BoletoConfig.MODO_SICREDI.equals(boletoConfig.flModoBoleto)) {
			String date =  DateUtil.formatDateDDMMYYYY(DateUtil.getCurrentDate());
			validaColunaPreenchida(boletoConfig.nuAgenciaCodigoCedente, "NUAGENCIACODIGOCEDENTE");
			nuNossoNumero.append(date.substring(8)).append("/")
			.append(boletoConfig.nuByteBoleto)
			.append(Convert.zeroPad(String.valueOf(nuDocumento), 5).substring(0, 5)).append("-")
			.append(geraDigitoVerificadorNuNossoNumero(boletoConfig.nuAgenciaCodigoCedente.replace(".", "") + nuNossoNumero.toString(), BoletoConfig.MODO_SICREDI));
		} else if (BoletoConfig.MODO_SANTANDER.equals(boletoConfig.flModoBoleto)) {
			nuNossoNumero.append(Convert.zeroPad(String.valueOf(nuDocumento), 12).substring(0, 12))
			.append(geraDigitoVerificadorNuNossoNumero(nuNossoNumero.toString(), BoletoConfig.MODO_SANTANDER));
		} else if (BoletoConfig.MODO_UNICRED.equals(boletoConfig.flModoBoleto)) {
			String nuNossuNumeroDocumento = (Convert.zeroPad(String.valueOf(nuDocumento), 10).substring(0, 10));
			nuNossoNumero.append(nuNossuNumeroDocumento).append("-")
			.append(geraDigitoVerificadorNuNossoNumero(nuNossuNumeroDocumento, BoletoConfig.MODO_UNICRED));
		} else if (BoletoConfig.MODO_ITAU.equals(boletoConfig.flModoBoleto)) {
			montaNuNossoNumeroItau(nuDocumento, boletoConfig, nuNossoNumero);
		}
		return nuNossoNumero.toString();
	}

	public void montaNuNossoNumeroItau(BigDecimal nuDocumento, BoletoConfig boletoConfig, StringBuffer nuNossoNumero) {
		if (ValueUtil.isEmpty(boletoConfig.nuCarteira)) throw new ValidationException(Messages.NFE_NUCARTEIRA_NAO_INFORMADO);
		nuNossoNumero.append(boletoConfig.nuCarteira.substring(0, 3)).append("/")
		.append(Convert.zeroPad(String.valueOf(nuDocumento), 8).substring(0,8));
		String digito = Convert.zeroPad(boletoConfig.nuAgencia,4) + Convert.zeroPad(boletoConfig.nuConta,5).substring(0,5) + boletoConfig.nuCarteira + nuDocumento; 
		digito = geraDigitoVerificadorNuNossoNumero(digito, BoletoConfig.MODO_ITAU);
		nuNossoNumero.append("-").append(digito);
	}

	private void validaColunaPreenchida(String valor, String nmColuna) {
		if (ValueUtil.isEmpty(valor)) throw new ValidationException(MessageUtil.getMessage(Messages.BOLETOCONFIG_DADOS_FALTANTES, new String[] {nmColuna}));
	}

	public String geraDigitoVerificadorNuNossoNumero(String nuNossoNumero, String flModo) {
		int vlTotal = getVlTotalCalculoDigitoVerificador(nuNossoNumero, flModo);
		int vlDigitoVerificador = 0;
		int mod = ValueUtil.valueEquals(BoletoConfig.MODO_ITAU, flModo) ? 10 : 11;
		vlDigitoVerificador = mod - (vlTotal % mod);
		if (BoletoConfig.MODO_CEF.equals(flModo) || BoletoConfig.MODO_SICREDI.equals(flModo) || BoletoConfig.MODO_SANTANDER.equals(flModo) || BoletoConfig.MODO_UNICRED.equals(flModo)) {
			return vlDigitoVerificador > 9 ? "0" : String.valueOf(vlDigitoVerificador);
		} else if (BoletoConfig.MODO_CDBARRAS.equals(flModo)) {
			vlDigitoVerificador = vlDigitoVerificador > 9 ? 0 : vlDigitoVerificador;
			return vlDigitoVerificador != 0 ? String.valueOf(vlDigitoVerificador) : "1";
		} else if (BoletoConfig.MODO_BRADESCO.equals(flModo)) {
			if (vlDigitoVerificador == 10) {
				return PedidoBoleto.DIGITOVEFICADOR_P;
			} else if (vlDigitoVerificador == mod) {
				return ValueUtil.VALOR_ZERO;
			} else {
				return String.valueOf(vlDigitoVerificador);
			}
		} else if (ValueUtil.valueEquals(BoletoConfig.MODO_ITAU, flModo)) {
			return vlDigitoVerificador == 10 ? ValueUtil.VALOR_ZERO : String.valueOf(vlDigitoVerificador);
		}
		return null;
	}

	public String createCdBarras(PedidoBoleto pedidoBoleto, BoletoConfig boletoConfig) {
		Date dtBaseFatorVencimento = DateUtil.isAfterOrEquals(pedidoBoleto.dtVencimento, DateUtil.getDateValue(22, 02, 2025)) ? DateUtil.getDateValue(22, 02, 2025) : DateUtil.getDateValue(07, 10, 1997);
		String vlBoletoAsString = StringUtil.getStringValue(ValueUtil.round(pedidoBoleto.vlBoleto, 2));
		vlBoletoAsString = vlBoletoAsString.replaceAll("\\.", "");
		String cdBeneficiario = "";
		if (ValueUtil.isNotEmpty(boletoConfig.cdBeneficiario)) {
			cdBeneficiario = boletoConfig.cdBeneficiario;
		}
		cdBeneficiario = Convert.zeroPad(cdBeneficiario, 6).substring(0, 6);
		int dvCdBeneficiario = geraDigitoVerificadorCampoLivre(cdBeneficiario);
		StringBuffer cdBarras = new StringBuffer();
		cdBarras.append(pegaTresPrimeirosCaracteres(boletoConfig.nuBanco))
		.append(PedidoBoleto.CD_MOEDA_REAL)
		.append(DateUtil.getDaysBetween(pedidoBoleto.dtVencimento, dtBaseFatorVencimento))
		.append(Convert.zeroPad(vlBoletoAsString, 10));
		if (BoletoConfig.MODO_CEF.equals(boletoConfig.flModoBoleto)) {
			montaCdBarrasModoCef(pedidoBoleto, cdBeneficiario, dvCdBeneficiario, cdBarras);
		} else if (BoletoConfig.MODO_BRADESCO.equals(boletoConfig.flModoBoleto)) {
			montaCdBarrasModoBradesco(pedidoBoleto, cdBarras, boletoConfig);
		} else if (BoletoConfig.MODO_SICREDI.equals(boletoConfig.flModoBoleto)) {
			montaCdBarrasModoSicredi(pedidoBoleto, cdBarras, boletoConfig);
		} else if (BoletoConfig.MODO_SANTANDER.equals(boletoConfig.flModoBoleto)) {
			montaCdBarrasModoSantander(pedidoBoleto, cdBarras, boletoConfig);
		} else if (BoletoConfig.MODO_UNICRED.equals(boletoConfig.flModoBoleto)) {
			montaCdBarrasModoUnicred(pedidoBoleto, cdBarras, boletoConfig);
		} else if (BoletoConfig.MODO_ITAU.equals(boletoConfig.flModoBoleto)) {
			montaCdBarrasModoItau(pedidoBoleto, cdBarras, boletoConfig);
		}
		String dvCdBarras = StringUtil.getStringValue(geraDigitoVerficadorCdBarras(cdBarras.toString(), boletoConfig.flModoBoleto));
		String cdBarrasAsString = cdBarras.toString();
		cdBarrasAsString = StringUtil.insertStringPos(cdBarrasAsString, dvCdBarras, 4);
		pedidoBoleto.cdBeneficiario = cdBeneficiario;
		return cdBarrasAsString;
	}

	private void montaCdBarrasModoCef(PedidoBoleto pedidoBoleto, String cdBeneficiario, int dvCdBeneficiario, StringBuffer cdBarras) {
		cdBarras.append(cdBeneficiario)
		.append(dvCdBeneficiario)
		.append(pedidoBoleto.nuNossoNumero.substring(2, 5))
		.append(pedidoBoleto.nuNossoNumero.substring(0, 1))
		.append(pedidoBoleto.nuNossoNumero.substring(5, 8))
		.append(pedidoBoleto.nuNossoNumero.substring(1, 2))
		.append(pedidoBoleto.nuNossoNumero.substring(8, 17))
		.append(geraDigitoVerificadorCampoLivre(cdBarras.toString().substring(18, 42)));
	}
	
	private void montaCdBarrasModoBradesco(PedidoBoleto pedidoBoleto, StringBuffer cdBarras, BoletoConfig boletoConfig) {
		String nuCarteira = Convert.zeroPad(StringUtil.getStringValue(boletoConfig.nuCarteira), 2);
		cdBarras.append(Convert.zeroPad(boletoConfig.nuAgencia, 4).substring(0, 4))
		.append(nuCarteira.substring(0, 2))
		.append(pedidoBoleto.nuNossoNumero.substring(0, 11))
		.append(Convert.zeroPad(boletoConfig.nuConta, 7).substring(0, 7))
		.append(ValueUtil.VALOR_ZERO);
	}
	
	private void montaCdBarrasModoSicredi(PedidoBoleto pedidoBoleto, StringBuffer cdBarras, BoletoConfig boletoConfig) {
		cdBarras.append("11")
		.append(pedidoBoleto.nuNossoNumero.replaceAll("[^0-9]", ValueUtil.VALOR_NI))
		.append(boletoConfig.nuAgenciaCodigoCedente.replace(".", ValueUtil.VALOR_NI))
		.append("1").append(ValueUtil.VALOR_ZERO)
		.append(geraDigitoVerificadorCampoLivre(cdBarras.substring(18, 42)));
	}
	
	private void montaCdBarrasModoSantander(PedidoBoleto pedidoBoleto, StringBuffer cdBarras, BoletoConfig boletoConfig) {
		cdBarras.append("9")
		.append(Convert.zeroPad(boletoConfig.cdBeneficiario, 7).substring(0, 7))
		.append(pedidoBoleto.nuNossoNumero)
		.append(ValueUtil.VALOR_ZERO);
		String nuCarteira = Convert.zeroPad(boletoConfig.nuCarteira, 3);
		nuCarteira = nuCarteira.substring(0, 3);
		cdBarras.append(nuCarteira);
	}
	
	private void montaCdBarrasModoUnicred(PedidoBoleto pedidoBoleto, StringBuffer cdBarras, BoletoConfig boletoConfig) {
		cdBarras.append(Convert.zeroPad(boletoConfig.nuAgencia, 4).substring(0, 4))
		.append(Convert.zeroPad(boletoConfig.cdBeneficiario, 10).substring(0, 10))
		.append(pedidoBoleto.nuNossoNumero.replaceAll("[^0-9]", ValueUtil.VALOR_NI));
    }
	
	public void montaCdBarrasModoItau(PedidoBoleto pedidoBoleto, StringBuffer cdBarras, BoletoConfig boletoConfig) {
		cdBarras.append(pedidoBoleto.nuNossoNumero.replaceAll("[^0-9]", ValueUtil.VALOR_NI))
		.append(Convert.zeroPad(boletoConfig.nuAgencia, 4).substring(0, 4))
		.append(Convert.zeroPad(boletoConfig.nuConta, 6).replaceAll("[^0-9]", ValueUtil.VALOR_NI))
		.append(Convert.zeroPad("", 3));
    }
	
	private String geraDigitoVerficadorCdBarrasItau(String string) {
		int vlTotal = getVlTotalCalculoDigitoVerificador(string,  BoletoConfig.MODO_CDBARRAS);
		int vlDigitoVerificador = 0;
		vlDigitoVerificador = 11 - (vlTotal % 11);
		return vlDigitoVerificador == 0 || vlDigitoVerificador == 1 || vlDigitoVerificador == 10 || vlDigitoVerificador== 11 ? "1" : String.valueOf(vlDigitoVerificador);
	}

	public String geraDigitoVerficadorCdBarras(String cdBarras) {
		return geraDigitoVerificadorNuNossoNumero(cdBarras, BoletoConfig.MODO_CDBARRAS);
	}
	
	public String geraDigitoVerficadorCdBarras(String cdBarras, String flModo) {
		if (BoletoConfig.MODO_ITAU.equals(flModo)) return geraDigitoVerficadorCdBarrasItau(cdBarras);
		return geraDigitoVerificadorNuNossoNumero(cdBarras, BoletoConfig.MODO_CDBARRAS);
	}

	public int geraDigitoVerificadorCampoLivre(String caracters) {
		int vlTotal = getVlTotalCalculoDigitoVerificador(caracters, BoletoConfig.MODO_CDBARRAS);
		int vlDigitoVerificador = 0;
		if (vlTotal >= 11) {
			vlDigitoVerificador = vlTotal % 11;
		}
		vlDigitoVerificador = 11 - vlDigitoVerificador;
		return vlDigitoVerificador > 9 ? 0 : vlDigitoVerificador;
	}
	
	public int getVlTotalCalculoDigitoVerificador(String caracters, String flModo) {
		if (BoletoConfig.MODO_ITAU.equals(flModo)) return getVlTotalCalculoDigitoVerificadorItau(caracters);
		return getVlTotalCalculoDigitoVerificadorOutrosBancos(caracters, flModo);
	}

	private int getVlTotalCalculoDigitoVerificadorOutrosBancos(String caracters, String flModo) {
		String[] digitos = caracters.split("(?!^)");
		int indiceMult = 2;
		int vlTotal = 0;
		int size  = digitos.length;
		for (int i = size - 1; i >= 0; i--) {
			String digito = digitos[i];
			try {
				int digitoAsInt = ValueUtil.getIntegerValue(digito);
				vlTotal += indiceMult * digitoAsInt;
			} catch (Throwable e) {
			} finally {
				if (indiceMult == 9 && (BoletoConfig.MODO_CEF.equals(flModo) || BoletoConfig.MODO_UNICRED.equals(flModo) || BoletoConfig.MODO_CDBARRAS.equals(flModo))) {
					indiceMult = 2;
				} else if (indiceMult == 7 && BoletoConfig.MODO_BRADESCO.equals(flModo)) {
					 indiceMult = 2;
				} else {
					indiceMult++;
				}
			}
		}
		return vlTotal;
	}
	
	public int getVlTotalCalculoDigitoVerificadorItau(String caracters){
		String[] digitos = caracters.split("(?!^)");
		int indiceMult = 2;
		int vlTotal = 0;
		int size  = digitos.length;
		for (int i = size - 1; i >= 0; i--) {
			String digito = digitos[i];
			int digitoAsInt = ValueUtil.getIntegerValue(digito);
			vlTotal += trataProdutoItau(indiceMult * digitoAsInt); 
			indiceMult = indiceMult == 2 ? 1 : 2;
		}
		return vlTotal;
	}
	
	private int trataProdutoItau(int valor) {
		String[] individual = StringUtil.getStringValue(valor).split("(?!^)");
		if (individual.length == 1) return valor;
		int total = 0;
		for (int i = 0; i < individual.length; i++) {
			total += ValueUtil.getIntegerValue(individual[i]);
		}
		return total;
	}

	private BigDecimal getLastNuDocumento(Pedido pedido, String cdBoletoConfig) throws SQLException {
		PedidoBoleto pedidoBoletoFilter = new PedidoBoleto();
		pedidoBoletoFilter.cdEmpresa = pedido.cdEmpresa;
		pedidoBoletoFilter.cdRepresentante = pedido.cdRepresentante;
		pedidoBoletoFilter.flOrigemPedido = pedido.flOrigemPedido;
		pedidoBoletoFilter.cdBoletoConfig = cdBoletoConfig;
		PedidoBoleto pedidoBoleto = (PedidoBoleto) PedidoBoletoDao.getInstance().findLastBoleto(pedidoBoletoFilter);
		if (pedidoBoleto != null) {
			return pedidoBoleto.nuDocumento;
		}
		return null;
	}

	public String formataDsLinhasDigitaveis(String dsdigitavel) {
		if (dsdigitavel != null && dsdigitavel.length() == 47) {
			dsdigitavel = StringUtil.insertStringPos(dsdigitavel, " ", 0);
			dsdigitavel = StringUtil.insertStringPos(dsdigitavel, ".", 6);
			dsdigitavel = StringUtil.insertStringPos(dsdigitavel, " ", 12);
			dsdigitavel = StringUtil.insertStringPos(dsdigitavel, ".", 18);
			dsdigitavel = StringUtil.insertStringPos(dsdigitavel, " ", 25);
			dsdigitavel = StringUtil.insertStringPos(dsdigitavel, ".", 31);
			dsdigitavel = StringUtil.insertStringPos(dsdigitavel, " ", 38);
			dsdigitavel = StringUtil.insertStringPos(dsdigitavel, " ", 40);
		}
		return dsdigitavel;
	}

	public void deleteBoletosPedido(Pedido pedido) throws SQLException {
		PedidoBoleto pedidoBoletoFilter = getFilterByPedido(pedido);
		deleteAllByExample(pedidoBoletoFilter);
	}
	
	private PedidoBoleto getFilterByPedido(Pedido pedido) {
		PedidoBoleto pedidoBoletoContFilter = new PedidoBoleto();
		pedidoBoletoContFilter.cdEmpresa = pedido.cdEmpresa;
		pedidoBoletoContFilter.cdRepresentante = pedido.cdRepresentante;
		pedidoBoletoContFilter.flOrigemPedido = pedido.flOrigemPedido;
		pedidoBoletoContFilter.nuPedido = pedido.nuPedido;
		return pedidoBoletoContFilter;
	}
	
	public boolean isPedidoContemBoletos(Pedido pedido) throws SQLException {
		PedidoBoleto filter = getFilterByPedido(pedido);
		return countByExample(filter) > 0;
	}
	
	public String getNossoNumeroBradesco(String nuNossoNumero, String nuCarteira) {
		if (ValueUtil.isNotEmpty(nuNossoNumero) && nuNossoNumero.length() == 12) {
			nuNossoNumero = StringUtil.insertStringPos(nuNossoNumero, nuCarteira + "/", 0);
			nuNossoNumero = StringUtil.insertStringPos(nuNossoNumero, "-", 14);
		}
		return nuNossoNumero;
	}
	
	public List<PedidoBoleto> buscaBoletosPor(String cdEmpresa, String cdRepresentante, Date dtDocumento) throws java.sql.SQLException {
		return PedidoBoletoDao.getInstance().buscaBoletosPor(cdEmpresa, cdRepresentante, dtDocumento);
	}
	
	public PedidoBoleto buscaBoletosPor(PedidoBoleto pedidoBoleto) throws java.sql.SQLException {
		return PedidoBoletoDao.getInstance().buscaBoletoPorTitulo(pedidoBoleto);
	}
	
}