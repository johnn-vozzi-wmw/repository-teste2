package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.CondPagtoLinha;
import br.com.wmw.lavenderepda.business.domain.CondicaoPagamento;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ParcelaPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ParcelaPedidoPdbxDao;
import totalcross.util.Date;
import totalcross.util.Hashtable;
import totalcross.util.Vector;

public class ParcelaPedidoService extends CrudService {

	private static ParcelaPedidoService instance;

	private ParcelaPedidoService() {
		//--
	}

	public static ParcelaPedidoService getInstance() {
		if (ParcelaPedidoService.instance == null) {
			ParcelaPedidoService.instance = new ParcelaPedidoService();
		}
		return ParcelaPedidoService.instance;
	}

	//@Override
	protected CrudDao getCrudDao() {
		return ParcelaPedidoPdbxDao.getInstance();
	}

	//@Override
	public void validate(BaseDomain domain) throws java.sql.SQLException {
		ParcelaPedido parcelaPedido = (ParcelaPedido) domain;
		//vlParcela
		if (!LavenderePdaConfig.isGeraParcelasEmPercentual() && parcelaPedido.vlParcela == 0) {
			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PARCELAPEDIDO_LABEL_VLPARCELA);
		}
		//vlPctParcela
		if (LavenderePdaConfig.isGeraParcelasEmPercentual() && parcelaPedido.vlPctParcela == 0) {
			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PARCELAPEDIDO_LABEL_PCT);
		} else if (parcelaPedido.vlPctParcela > ValueUtil.round(100)) {
			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_ULTRAPASSOU_VALOR_MAXIMO, Messages.PARCELAPEDIDO_LABEL_PCT);
		}
		//qtDiasPrazo
		if (LavenderePdaConfig.isGeraParcelasEmPercentual() && parcelaPedido.qtDiasPrazo == 0) {
			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PARCELAPEDIDO_LABEL_PRAZO);
		}
		//dtVencimento
		if (ValueUtil.isEmpty(parcelaPedido.dtVencimento)) {
			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PARCELAPEDIDO_LABEL_DTVENCIMENTO);
		}
		if (parcelaPedido.dtVencimento.isBefore(DateUtil.getCurrentDate())) {
			throw new ValidationException(Messages.PARCELAPEDIDO_DTVENCIMENTO_ANTERIOR_PERMITIDO);
		}
	}

	public void validateValorMinimo(ParcelaPedido parcelaPedido) {
		double valorMinimoParcela = LavenderePdaConfig.valorMinimoParcela();
		if (valorMinimoParcela == 0 || parcelaPedido.vlParcela >= valorMinimoParcela || parcelaPedido.vlPctParcela >= valorMinimoParcela) return;
		
		throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_MSG_PARCELA_VALOR_MINIMO, new String[] {StringUtil.getStringValueToInterface(valorMinimoParcela)}));
	}

	public boolean isTipoOutro(Pedido pedido) throws SQLException {
		if (pedido.getCondicaoPagamento() != null) {
			return CondicaoPagamento.TIPOCONDPAGTO_OUTRO == ValueUtil.getIntegerValue(pedido.getCondicaoPagamento().cdTipoCondPagto);
		}
		return false;
	}

	public boolean isTipoParceladoUsuario(Pedido pedido) throws SQLException {
		if (pedido.getCondicaoPagamento() != null) {
			return CondicaoPagamento.TIPOCONDPAGTO_PARCELADO_USUARIO == ValueUtil.getIntegerValue(pedido.getCondicaoPagamento().cdTipoCondPagto);
		}
		return false;
	}

	public Vector geraParcelasAuto(Pedido pedido) throws SQLException {
		CondicaoPagamento condicaoPagamento = pedido.getCondicaoPagamento();
		if (!LavenderePdaConfig.isGeraParcelasEmPercentual() && condicaoPagamento != null && ValueUtil.isNotEmpty(pedido.cdCondicaoPagamento)) {
			if (ValueUtil.getIntegerValue(condicaoPagamento.cdTipoCondPagto) == (CondicaoPagamento.TIPOCONDPAGTO_LINHA)) {
				return tipoCondPagtoLinha(pedido);
			} else if (ValueUtil.getIntegerValue(condicaoPagamento.cdTipoCondPagto) == (CondicaoPagamento.TIPOCONDPAGTO_MEDIO)) {
				Vector list = new Vector();
				list.addElement(tipoCondPagtoMedio(pedido));
				return list;
			} else if (ValueUtil.getIntegerValue(condicaoPagamento.cdTipoCondPagto) == (CondicaoPagamento.TIPOCONDPAGTO_PARCELADO)) {
				return tipoCondPagtoParcelado(pedido, condicaoPagamento);
			} else if (ValueUtil.getIntegerValue(condicaoPagamento.cdTipoCondPagto) == (CondicaoPagamento.TIPOCONDPAGTO_VENCIMENTO)) {
				return tipoCondPagtoVencimento(pedido, condicaoPagamento);
			} else if (ValueUtil.getIntegerValue(condicaoPagamento.cdTipoCondPagto) == (CondicaoPagamento.TIPOCONDPAGTO_PRAZO_BASE)) {
				Vector list = new Vector();
				list.addElement(tipoCondPagtoPrazoBase(pedido, condicaoPagamento));
				return list;
			} else if (ValueUtil.getIntegerValue(condicaoPagamento.cdTipoCondPagto) == (CondicaoPagamento.TIPOCONDPAGTO_RATEIO)) {
				return tipoCondPagtoRateio(pedido, condicaoPagamento);
			} else if (ValueUtil.getIntegerValue(condicaoPagamento.cdTipoCondPagto) == (CondicaoPagamento.TIPOCONDPAGTO_VENCIMENTO_UNICO)) {
				Vector list = new Vector();
				list.addElement(tipoCondPagtoVencimentoUnico(pedido, condicaoPagamento));
				return list;
			}
		}
		return new Vector(0);
	}

	public void resetParcelasPedidoPorTipoCondPgto(final Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.permiteEditarVecimentoParcela) {
			pedido.parcelaPedidoList = new Vector();
			insertParcelasPedido(pedido);
		}
	}
	
	public void insertParcelasPedido(final Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.isGeraParcelasPorTipoCondPgto()) {
			if (ValueUtil.isNotEmpty(pedido.itemPedidoList)) {
				if (!isTipoOutro(pedido) && !isTipoParceladoUsuario(pedido)) {
					pedido.parcelaPedidoList = geraParcelasAuto(pedido);
				}
				if (pedido.parcelaPedidoList != null) {
					int size = pedido.parcelaPedidoList.size();
					for (int i = 0; i < size; i++) {
						validate((ParcelaPedido) pedido.parcelaPedidoList.items[i]);
					}
				}
				deleteParcelasPedido(pedido);
				if (pedido.parcelaPedidoList != null) {
					for (int i = 0; i < pedido.parcelaPedidoList.size(); i++) {
						insert((ParcelaPedido) pedido.parcelaPedidoList.items[i]);
					}
				}
			} else {
				deleteParcelasPedido(pedido);
				pedido.parcelaPedidoList = new Vector(0);
			}
		}
	}

	public void deleteParcelasPedido(final Pedido pedido) throws SQLException {
		ParcelaPedido parcelaPedido = new ParcelaPedido();
		parcelaPedido.cdEmpresa = pedido.cdEmpresa;
		parcelaPedido.cdRepresentante = pedido.cdRepresentante;
		parcelaPedido.flOrigemPedido = pedido.flOrigemPedido;
		parcelaPedido.nuPedido = pedido.nuPedido;
		deleteAllByExample(parcelaPedido);
	}

	public void reorganizaCodigoParcelas(final Pedido pedido) throws SQLException {
		if (isTipoOutro(pedido) || isTipoParceladoUsuario(pedido)) {
			deleteParcelasPedido(pedido);
			int size = pedido.parcelaPedidoList.size();
			for (int i = 0; i < size; i++) {
				ParcelaPedido parcelaPedido = (ParcelaPedido) pedido.parcelaPedidoList.items[i];
				parcelaPedido.cdParcela = StringUtil.getStringValue(1 + i);
				getCrudDao().insert(parcelaPedido);
			}
		}
	}

	public void fecharParcelaPedido(final Pedido pedido) throws SQLException {
		int size = pedido.parcelaPedidoList.size();
		for (int i = 0; i < size; i++) {
			ParcelaPedido parcelaPedido = (ParcelaPedido) pedido.parcelaPedidoList.items[i];
			parcelaPedido.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_ALTERADO;
			getCrudDao().update(parcelaPedido);
		}
		reorganizaCodigoParcelas(pedido);
	}

	public void reabrirParcelaPedido(Pedido pedido) throws SQLException {
		int size = pedido.parcelaPedidoList.size();
		for (int i = 0; i < size; i++) {
			ParcelaPedido parcelaPedido = (ParcelaPedido) pedido.parcelaPedidoList.items[i];
			parcelaPedido.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_ORIGINAL;
			getCrudDao().update(parcelaPedido);
		}
	}

	public void validateParcelas(Pedido pedido) throws SQLException {
		if (pedido.getCondicaoPagamento() != null && isTipoParceladoUsuario(pedido)) {
			double totalPctParcelas = 0;
			if (ValueUtil.isNotEmpty(pedido.parcelaPedidoList)) {
				int size = pedido.parcelaPedidoList.size();
				for (int i = 0; i < size; i++) {
					totalPctParcelas += ((ParcelaPedido)pedido.parcelaPedidoList.items[i]).vlPctParcela;
				}
			}
			if (ValueUtil.round(totalPctParcelas) != ValueUtil.round(100)) {
				throw new ValidationException(Messages.PARCELAPEDIDO_MSG_VLTOTALPCTPARCELAS);
			}
		} else if (ValueUtil.isNotEmpty(pedido.getCondicaoPagamento().cdTipoCondPagto)) {
			String[] args = {StringUtil.getStringValueToInterface(CondicaoPagamento.TIPOCONDPAGTO_PARCELADO_USUARIO)};
			throw new ValidationException(MessageUtil.getMessage(Messages.PARCELAPEDIDO_MSG_PARCELA_CONDPAGTO_INVALIDA, args));
		}
	}

	protected void validateParcelasPercentualTotal(Pedido pedido) throws SQLException {
		if (pedido.getCondicaoPagamento() != null && isTipoParceladoUsuario(pedido)) {
			double totalPctParcelas = 0;
			if (ValueUtil.isNotEmpty(pedido.parcelaPedidoList)) {
				int size = pedido.parcelaPedidoList.size();
				for (int i = 0; i < size; i++) {
					totalPctParcelas += ((ParcelaPedido)pedido.parcelaPedidoList.items[i]).vlPctParcela;
				}
			}
			if (ValueUtil.round(totalPctParcelas) != ValueUtil.round(100)) {
				throw new ValidationException(Messages.PARCELAPEDIDO_MSG_VLTOTALPCTPARCELAS);
			}
		} else if (ValueUtil.isNotEmpty(pedido.getCondicaoPagamento().cdTipoCondPagto)) {
			String[] args = {StringUtil.getStringValueToInterface(CondicaoPagamento.TIPOCONDPAGTO_PARCELADO_USUARIO)};
			throw new ValidationException(MessageUtil.getMessage(Messages.PARCELAPEDIDO_MSG_PARCELA_CONDPAGTO_INVALIDA, args));
		}
	}

	public void validateParcelaNova(Pedido pedido, ParcelaPedido parcelaPedido) throws SQLException {
		if (ValueUtil.isNotEmpty(pedido.parcelaPedidoList)) {
			int size = pedido.parcelaPedidoList.size();
			if (size == pedido.getCondicaoPagamento().nuParcelas) {
				String[] args = {StringUtil.getStringValueToInterface(size + 1), StringUtil.getStringValueToInterface(pedido.getCondicaoPagamento().nuParcelas)};
				throw new ValidationException(MessageUtil.getMessage(Messages.PARCELAPEDIDO_MSG_NUPARCELA_SUPERIOR_PERMITIDO, args));
			}
			if (LavenderePdaConfig.isGeraParcelasEmPercentual()) {
				double totalPctParcelas = 0;
				for (int i = 0; i < size; i++) {
					ParcelaPedido parcela = (ParcelaPedido)pedido.parcelaPedidoList.items[i];
					if (!parcela.cdParcela.equalsIgnoreCase(parcelaPedido.cdParcela) && parcela.qtDiasPrazo == parcelaPedido.qtDiasPrazo) {
						throw new ValidationException(Messages.PARCELAPEDIDO_MSG_PARCELA_MESMO_DIA);
					}
					if (!parcela.cdParcela.equalsIgnoreCase(parcelaPedido.cdParcela)) {
						totalPctParcelas += parcela.vlPctParcela;
					}
				}
				totalPctParcelas += parcelaPedido.vlPctParcela;
				if (ValueUtil.round(totalPctParcelas) > ValueUtil.round(100)) {
					throw new ValidationException(Messages.PARCELAPEDIDO_MSG_VLTOTALPCTPARCELAS);
				}
			}
		}
	}

	//OK
	protected ParcelaPedido tipoCondPagtoMedio(Pedido pedido) throws SQLException {
		int size = pedido.itemPedidoList.size();
		Hashtable hashLinha = new Hashtable(size);
		Hashtable hashValor = new Hashtable(size);
		CondPagtoLinha condPagtoLinha;
		try {
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
				if (hashLinha.get(itemPedido.cdLinha) == null) {
					condPagtoLinha = new CondPagtoLinha();
					condPagtoLinha.cdEmpresa = SessionLavenderePda.cdEmpresa;
					condPagtoLinha.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
					condPagtoLinha.cdCondicaoPagamento = pedido.cdCondicaoPagamento;
					condPagtoLinha.cdLinha = itemPedido.cdLinha;
					condPagtoLinha.cdRegiao = "0";
					String nuDiasPrazo = CondPagtoLinhaService.getInstance().findColumnByRowKey(condPagtoLinha.getRowKey(), "NUDIASPRAZO");
					hashLinha.put(itemPedido.cdLinha, ValueUtil.isEmpty(nuDiasPrazo) ? "0" : nuDiasPrazo);
				}
				//--
				if (hashValor.get(hashLinha.get(itemPedido.cdLinha)) == null) {
					hashValor.put(hashLinha.get(itemPedido.cdLinha), StringUtil.getStringValueToInterface(itemPedido.vlTotalItemPedido));
				} else {
					double totalVlItemPedido = ValueUtil.getDoubleValue(StringUtil.getStringValue(hashValor.get(hashLinha.get(itemPedido.cdLinha))));
					totalVlItemPedido += itemPedido.vlTotalItemPedido;
					hashValor.remove(hashLinha.get(itemPedido.cdLinha));
					hashValor.put(hashLinha.get(itemPedido.cdLinha), StringUtil.getStringValueToInterface(totalVlItemPedido));
				}
			}
			//--
			double totalMultValor = 0;
			double vlParcela = 0;
			Vector keys = hashValor.getKeys();
			int sizeKeys = keys.size();
			String key;
			for (int i = 0; i < sizeKeys; i++) {
				key = (String) keys.items[i];
				int nuPrazoDias = ValueUtil.getIntegerValue(key);
				double vlTotalItem = ValueUtil.getDoubleValue(StringUtil.getStringValue(hashValor.get(key)));
				totalMultValor += nuPrazoDias * vlTotalItem;
				vlParcela += vlTotalItem;
			}
			vlParcela = ValueUtil.round(vlParcela);
			int prazoMedioParcela = ValueUtil.getIntegerValue(StringUtil.getStringValue(totalMultValor / vlParcela, 0));
			//--
			Date dtVencimento = new Date();
			dtVencimento.advance(prazoMedioParcela);
			//--
			ParcelaPedido parcelaPedido = new ParcelaPedido();
			parcelaPedido.cdEmpresa = pedido.cdEmpresa;
			parcelaPedido.cdRepresentante = pedido.cdRepresentante;
			parcelaPedido.flOrigemPedido = pedido.flOrigemPedido;
			parcelaPedido.nuPedido = pedido.nuPedido;
			parcelaPedido.cdParcela = StringUtil.getStringValue("1");
			parcelaPedido.vlParcela = vlParcela;
			parcelaPedido.dtVencimento = dtVencimento;
			//--
			return parcelaPedido;
		} catch (Exception ex) {
			throw new ValidationException(Messages.PARCELAPEDIDO_MSG_PRODUTOSEMLINHA);
		}
	}

	//OK
	protected Vector tipoCondPagtoLinha(Pedido pedido) throws SQLException {
		Hashtable hashLinha = new Hashtable(20);
		Hashtable hashValor = new Hashtable(20);
		int size = pedido.itemPedidoList.size();
		CondPagtoLinha condPagtoLinha;
		try {
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
				if (hashLinha.get(itemPedido.cdLinha) == null) {
					condPagtoLinha = new CondPagtoLinha();
					condPagtoLinha.cdEmpresa = SessionLavenderePda.cdEmpresa;
					condPagtoLinha.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
					condPagtoLinha.cdCondicaoPagamento = pedido.cdCondicaoPagamento;
					condPagtoLinha.cdLinha = itemPedido.cdLinha;
					condPagtoLinha.cdRegiao = "0";
					hashLinha.put(itemPedido.cdLinha, CondPagtoLinhaService.getInstance().findColumnByRowKey(condPagtoLinha.getRowKey(), "NUDIASPRAZO"));
				}
				//--
				if (hashValor.get(hashLinha.get(itemPedido.cdLinha)) == null) {
					hashValor.put(hashLinha.get(itemPedido.cdLinha), StringUtil.getStringValueToInterface(itemPedido.vlTotalItemPedido));
				} else {
					double totalVlItemPedido = ValueUtil.getDoubleValue(StringUtil.getStringValue(hashValor.get(hashLinha.get(itemPedido.cdLinha))));
					totalVlItemPedido += itemPedido.vlTotalItemPedido;
					hashValor.remove(hashLinha.get(itemPedido.cdLinha));
					hashValor.put(hashLinha.get(itemPedido.cdLinha), StringUtil.getStringValueToInterface(totalVlItemPedido));
				}
			}
			//--
			Vector parcelasPedidos = new Vector();
			Vector keys = hashValor.getKeys();
			int cd = 1;
			double vlTotalParcelas = 0;
			for (int i = 0; i < keys.size(); i++) {
				ParcelaPedido parcelaPedido = new ParcelaPedido();
				parcelaPedido.cdEmpresa = pedido.cdEmpresa;
				parcelaPedido.cdRepresentante = pedido.cdRepresentante;
				parcelaPedido.flOrigemPedido = pedido.flOrigemPedido;
				parcelaPedido.nuPedido = pedido.nuPedido;
				parcelaPedido.cdParcela = StringUtil.getStringValue(cd);
				cd++;
				Date dtVencimento = new Date();
				dtVencimento.advance(ValueUtil.getIntegerValue(StringUtil.getStringValue(keys.items[i])));
				parcelaPedido.dtVencimento = dtVencimento;
				parcelaPedido.vlParcela = ValueUtil.round(ValueUtil.getDoubleValue(StringUtil.getStringValue(hashValor.get(keys.items[i]))));
				vlTotalParcelas += parcelaPedido.vlParcela;
				//--
				parcelasPedidos.addElement(parcelaPedido);
			}
			//Caso sobre alguns valores por causa do arredondamento, deve-se adicioná-lo na primeira parcela
			if (!ValueUtil.isEmpty(parcelasPedidos)) {
				ParcelaPedido parcelaPedido = (ParcelaPedido) parcelasPedidos.items[0];
				parcelaPedido.vlParcela += pedido.vlTotalItens - vlTotalParcelas;
			}
			//--
			return parcelasPedidos;
		} catch (Exception ex) {
			throw new ValidationException(Messages.PARCELAPEDIDO_MSG_PRODUTOSEMLINHA);
		}
	}

	//OK
	protected Vector tipoCondPagtoRateio(Pedido pedido, CondicaoPagamento condicaoPagamento) throws SQLException {
		ParcelaPedido parcelaPedido = tipoCondPagtoMedio(pedido);
		double vlParcela = ValueUtil.round(pedido.vlTotalItens / 3);
		int nuDiasPrazoMedio = DateUtil.getDaysBetween(parcelaPedido.dtVencimento, DateUtil.getCurrentDate());
		int nuIntervaloParcelas = condicaoPagamento.nuIntervaloParcelas;
		//Parcela 1
		Date dtVencimento1 = DateUtil.getCurrentDate();
		dtVencimento1.advance(nuDiasPrazoMedio - nuIntervaloParcelas);
		ParcelaPedido parcelaPedido1 = new ParcelaPedido();
		parcelaPedido1.cdEmpresa = pedido.cdEmpresa;
		parcelaPedido1.cdRepresentante = pedido.cdRepresentante;
		parcelaPedido1.flOrigemPedido = pedido.flOrigemPedido;
		parcelaPedido1.nuPedido = pedido.nuPedido;
		parcelaPedido1.cdParcela = "1";
		parcelaPedido1.vlParcela = vlParcela + (pedido.vlTotalItens - (vlParcela * 3));
		parcelaPedido1.dtVencimento = dtVencimento1;
		//Parcela 2
		Date dtVencimento2 = DateUtil.getCurrentDate();
		dtVencimento2.advance(nuDiasPrazoMedio);
		ParcelaPedido parcelaPedido2 = new ParcelaPedido();
		parcelaPedido2.cdEmpresa = pedido.cdEmpresa;
		parcelaPedido2.cdRepresentante = pedido.cdRepresentante;
		parcelaPedido2.flOrigemPedido = pedido.flOrigemPedido;
		parcelaPedido2.nuPedido = pedido.nuPedido;
		long cd = ValueUtil.getLongValue("1");
		cd++;
		parcelaPedido2.cdParcela = StringUtil.getStringValue(cd);
		parcelaPedido2.vlParcela = vlParcela;
		parcelaPedido2.dtVencimento = dtVencimento2;
		//Parcela 3
		Date dtVencimento3 = DateUtil.getCurrentDate();
		dtVencimento3.advance(nuDiasPrazoMedio + nuIntervaloParcelas);
		ParcelaPedido parcelaPedido3 = new ParcelaPedido();
		parcelaPedido3.cdEmpresa = pedido.cdEmpresa;
		parcelaPedido3.cdRepresentante = pedido.cdRepresentante;
		parcelaPedido3.flOrigemPedido = pedido.flOrigemPedido;
		parcelaPedido3.nuPedido = pedido.nuPedido;
		cd++;
		parcelaPedido3.cdParcela = StringUtil.getStringValue(cd);
		parcelaPedido3.vlParcela = vlParcela;
		parcelaPedido3.dtVencimento = dtVencimento3;
		//--
		Vector parcelasPedidos = new Vector();
		parcelasPedidos.addElement(parcelaPedido1);
		parcelasPedidos.addElement(parcelaPedido2);
		parcelasPedidos.addElement(parcelaPedido3);
		//--
		return parcelasPedidos;
	}

	//OK
	protected ParcelaPedido tipoCondPagtoVencimentoUnico(Pedido pedido, CondicaoPagamento condicaoPagamentoFinal) {

		double vlTotalParcela = pedido.vlTotalItens;
		//--
		ParcelaPedido parcelaPedido = new ParcelaPedido();
		parcelaPedido.cdEmpresa = pedido.cdEmpresa;
		parcelaPedido.cdRepresentante = pedido.cdRepresentante;
		parcelaPedido.flOrigemPedido = pedido.flOrigemPedido;
		parcelaPedido.nuPedido = pedido.nuPedido;
		parcelaPedido.cdParcela = "1";
		parcelaPedido.vlParcela = vlTotalParcela;
		parcelaPedido.dtVencimento = condicaoPagamentoFinal.dtPagamento;
		//--
		return parcelaPedido;
	}
	
	private double getVlAdicionaisDoPedido(Pedido pedido) {
		double vlAdicionais = 0;
		if (pedido.transportadoraReg != null && pedido.transportadoraReg.transportadora != null) {
			if (!pedido.transportadoraReg.transportadora.isFlSomaFrete()) {
				vlAdicionais = pedido.vlFrete;
			}
		}
		return vlAdicionais;
	}

	//OK
	protected Vector tipoCondPagtoParcelado(Pedido pedido, CondicaoPagamento condicaoPagamentoFinal) {
		if (condicaoPagamentoFinal.nuParcelas > 0) {
			double vlTotalPedido = pedido.vlTotalPedido + getVlAdicionaisDoPedido(pedido);
			double vlParcela = ValueUtil.round(vlTotalPedido/ condicaoPagamentoFinal.nuParcelas);
			Vector parcelasPedidos = new Vector();
			int cd = 1;
			Date lastVencimento = null;
			for (int i = 0; i < condicaoPagamentoFinal.nuParcelas; i++) {
				ParcelaPedido parcelaPedido = new ParcelaPedido();
				parcelaPedido.cdEmpresa = pedido.cdEmpresa;
				parcelaPedido.cdRepresentante = pedido.cdRepresentante;
				parcelaPedido.flOrigemPedido = pedido.flOrigemPedido;
				parcelaPedido.nuPedido = pedido.nuPedido;
				parcelaPedido.cdParcela = StringUtil.getStringValue(cd);
				cd++;
				parcelaPedido.vlParcela = vlParcela;
				if (i == 0) {
					parcelaPedido.dtVencimento = proximaDtVencimentoCondPagtoParcelado(null, condicaoPagamentoFinal);
				} else {
					parcelaPedido.dtVencimento = proximaDtVencimentoCondPagtoParcelado(lastVencimento, condicaoPagamentoFinal);
				}
				lastVencimento = parcelaPedido.dtVencimento;
				ajustaParcelaEditVencimento(pedido, parcelaPedido);
				parcelasPedidos.addElement(parcelaPedido);
			}
			//Caso sobre alguns valores por causa do arredondamento, deve-se adicioná-lo na primeira parcela
			if (ValueUtil.isNotEmpty(parcelasPedidos)) {
				ParcelaPedido parcelaPedido = (ParcelaPedido) parcelasPedidos.items[0];
				parcelaPedido.vlParcela += ValueUtil.round(vlTotalPedido - (condicaoPagamentoFinal.nuParcelas * vlParcela));
			}
			//--
			pedido.parcelaPedidoList = parcelasPedidos;
			return parcelasPedidos;
		} else {
			return new Vector();
		}
	}

	public void ajustaParcelaEditVencimento(Pedido pedido, ParcelaPedido parcelaPedido) {
		if (LavenderePdaConfig.permiteEditarVecimentoParcela) {
			ParcelaPedido parcelaPedidoAntiga = getParcelaPedidoInList(pedido.parcelaPedidoList, parcelaPedido);
			if (parcelaPedidoAntiga != null) {
				Date dtVencimento = new Date();
				dtVencimento.advance(parcelaPedidoAntiga.qtDiasPrazo);
				parcelaPedido.dtVencimento = dtVencimento;
				parcelaPedido.qtDiasPrazo = parcelaPedidoAntiga.qtDiasPrazo;
			} else {
				parcelaPedido.qtDiasPrazo = DateUtil.getDaysBetween(parcelaPedido.dtVencimento, new Date());
			}
		}
	}

	private ParcelaPedido getParcelaPedidoInList(Vector parcelaPedidoList, ParcelaPedido parcelaPedido) {
		try {
			int index = parcelaPedidoList.indexOf(parcelaPedido);
			if (index >= 0) {
				return (ParcelaPedido) parcelaPedidoList.elementAt(index);
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	private ParcelaPedido getParcelaPedidoInListByCdParcela(Pedido pedido, String cdParcela) {
		try {
			ParcelaPedido proxParcela = new ParcelaPedido();
			proxParcela.cdEmpresa = pedido.cdEmpresa;
			proxParcela.cdRepresentante = pedido.cdRepresentante;
			proxParcela.flOrigemPedido = pedido.flOrigemPedido;
			proxParcela.nuPedido = pedido.nuPedido;
			proxParcela.cdParcela = cdParcela;
			return getParcelaPedidoInList(pedido.parcelaPedidoList, proxParcela);
		} catch (Exception e) {
			return null;
		}
	}
	
	public Date proximaDtVencimentoCondPagtoParcelado(Date dtVencimentoAtual, CondicaoPagamento condicaoPagamentoFinal) {
		Date dtVencimento;
		if (dtVencimentoAtual == null) {
			dtVencimento = new Date();
			dtVencimento.advance(condicaoPagamentoFinal.nuIntervaloEntrada);
		} else {
			dtVencimento = DateUtil.getDateValue(dtVencimentoAtual.toString());
			dtVencimento.advance(condicaoPagamentoFinal.nuIntervaloParcelas);
		}
		return dtVencimento;
	}
	
	public Vector tipoCondPagtoVencimento(Pedido pedido, CondicaoPagamento condicaoPagamentoFinal) throws SQLException {
		if (ValueUtil.isEmpty(pedido.qtDiasVctoParcelas)) {
			return new Vector();
		}
		String[] parcelas = pedido.qtDiasVctoParcelas.split(",");
		if (condicaoPagamentoFinal.nuParcelas > 0 && parcelas.length == condicaoPagamentoFinal.nuParcelas) {
			if (getMediaDias(parcelas) > condicaoPagamentoFinal.qtDiasMediosPagamento) {
				UiUtil.showWarnMessage(MessageUtil.getMessage(Messages.PARCELAPEDIDO_PRAZO_MEDIO_INVALIDO, condicaoPagamentoFinal.qtDiasMediosPagamento));
				return new Vector();
			}
			double vlParcela = ValueUtil.round(pedido.vlTotalPedido / condicaoPagamentoFinal.nuParcelas);
			Vector parcelasPedidos = new Vector();
			for (int i = 0; i < condicaoPagamentoFinal.nuParcelas; i++) {
				ParcelaPedido parcelaPedido = new ParcelaPedido();
				parcelaPedido.cdEmpresa = pedido.cdEmpresa;
				parcelaPedido.cdRepresentante = pedido.cdRepresentante;
				parcelaPedido.flOrigemPedido = pedido.flOrigemPedido;
				parcelaPedido.nuPedido = pedido.nuPedido;
				parcelaPedido.cdParcela = StringUtil.getStringValue(i + 1);
				parcelaPedido.vlParcela = vlParcela;
				parcelaPedido.dtVencimento = getDtVencimentoParcela(ValueUtil.getIntegerValue(parcelas[i]));
				parcelasPedidos.addElement(parcelaPedido);
			}
			//Caso sobre alguns valores por causa do arredondamento, deve-se adicioná-lo na primeira parcela
			if (!ValueUtil.isEmpty(parcelasPedidos)) {
				ParcelaPedido parcelaPedido = (ParcelaPedido) parcelasPedidos.items[0];
				parcelaPedido.vlParcela += ValueUtil.round(pedido.vlTotalPedido - (condicaoPagamentoFinal.nuParcelas * vlParcela));
			}
			//--
			pedido.parcelaPedidoList = parcelasPedidos;
			return parcelasPedidos;
		} else {
			UiUtil.showConfirmMessage("Quantidade de parcelas informada diferente da quantidade especificada na condição de pagamento: " + condicaoPagamentoFinal.nuParcelas);
			return new Vector();
		}
	}

	private Date getDtVencimentoParcela(int daysToAdd) throws SQLException {
		Date date = new Date();
		date.advance(daysToAdd);
		while (!isDtVencimentoValido(date)) {
			if (FeriadoService.getInstance().isDtFeriado(date) || DateUtil.DATA_SEMANA_DOMINGO == date.getDayOfWeek()) date.advance(1);
			else if (DateUtil.DATA_SEMANA_SABADO == date.getDayOfWeek()) date.advance(2);
		}
		return date;
	}

	private boolean isDtVencimentoValido(Date date) throws SQLException {
		if (FeriadoService.getInstance().isDtFeriado(date)) return false;
		if (DateUtil.DATA_SEMANA_SABADO == date.getDayOfWeek() || DateUtil.DATA_SEMANA_DOMINGO == date.getDayOfWeek()) return false;
		return true;
	}

	private int getMediaDias(String[] parcelas) {
		int totalDias = 0;
		for (int i = 0; i < parcelas.length; i++) {
			totalDias += ValueUtil.getIntegerValue(parcelas[i]);
		}

		return totalDias / parcelas.length;
	}

	//OK
	protected ParcelaPedido tipoCondPagtoPrazoBase(Pedido pedido, CondicaoPagamento condicaoPagamentoFinal) {

		Date dtVencimento = new Date();
		if (ValueUtil.getIntegerValue(condicaoPagamentoFinal.flPeriodicidadePrazoBase) == (CondicaoPagamento.PERIODO_PRAZO_BASE_MENSAL)) {
			dtVencimento.advanceMonth();
			dtVencimento.advance(condicaoPagamentoFinal.nuPrazoBase - 1);
		}
		if (ValueUtil.getIntegerValue(condicaoPagamentoFinal.flPeriodicidadePrazoBase) == (CondicaoPagamento.PERIODO_PRAZO_BASE_SEMANAL)) {
			int restoSemana = CondicaoPagamento.PRAZOBASE_SEMANAL_SABADO - DateUtil.getCurrentDate().getDayOfWeek();
			dtVencimento.advance(restoSemana);
			dtVencimento.advance(condicaoPagamentoFinal.nuPrazoBase);
		}
		if (ValueUtil.getIntegerValue(condicaoPagamentoFinal.flPeriodicidadePrazoBase) == (CondicaoPagamento.PERIODO_PRAZO_BASE_NENHUM)) {
			return null;
		}
		//--
		ParcelaPedido parcelaPedido = new ParcelaPedido();
		parcelaPedido.cdEmpresa = pedido.cdEmpresa;
		parcelaPedido.cdRepresentante = pedido.cdRepresentante;
		parcelaPedido.flOrigemPedido = pedido.flOrigemPedido;
		parcelaPedido.nuPedido = pedido.nuPedido;
		parcelaPedido.cdParcela = "1";
		parcelaPedido.vlParcela = pedido.vlTotalItens;
		parcelaPedido.dtVencimento = dtVencimento;

		return parcelaPedido;
	}

	public boolean validateProduto(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		if (ValueUtil.isNotEmpty(pedido.cdCondicaoPagamento)) {
			CondPagtoLinha condPagtoLinhaFilter = new CondPagtoLinha();
			condPagtoLinhaFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			condPagtoLinhaFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
			condPagtoLinhaFilter.cdCondicaoPagamento = pedido.cdCondicaoPagamento;
			condPagtoLinhaFilter.cdLinha = itemPedido.cdLinha;
			condPagtoLinhaFilter.cdRegiao = "0";
			String rowKeyCondLinha = CondPagtoLinhaService.getInstance().findColumnByRowKey(condPagtoLinhaFilter.getRowKey(), "ROWKEY");
			//--
			CondicaoPagamento condicaoPagamento = pedido.getCondicaoPagamento();
			//--
			if (rowKeyCondLinha == null
					&& (ValueUtil.getIntegerValue(condicaoPagamento.cdTipoCondPagto) == (CondicaoPagamento.TIPOCONDPAGTO_MEDIO)
					|| ValueUtil.getIntegerValue(condicaoPagamento.cdTipoCondPagto) == (CondicaoPagamento.TIPOCONDPAGTO_LINHA))) {
				throw new ValidationException(Messages.PARCELAPEDIDO_MSG_LINHAITEMTABPRECO_SEMRELACAO_CONDPAGTO);
			}
		}
		return true;
	}

	protected void setDadosAlteracao(BaseDomain domain) {
		domain.cdUsuario = Session.getCdUsuario();
	}

	public double getRestante(Pedido pedido, double vlTotalParcelas) {
		double valorRestante = 0;
		if (LavenderePdaConfig.isGeraParcelasEmPercentual()) {
			valorRestante = 100 - vlTotalParcelas;
		} else {
			vlTotalParcelas = ValueUtil.round(vlTotalParcelas, 2);
			double vlTotalPedido = ValueUtil.round(pedido.vlTotalPedido + getVlAdicionaisDoPedido(pedido));
			valorRestante = vlTotalPedido - vlTotalParcelas;
		}
		return valorRestante;
	}
	
	/**
	 * Retorna um array com o resultado da divisao entre <b>vlTotal</b> com <b>nuParcelas</b>.
	 * O valor restante da divisão é somado ao valor da última parcela
	 * @param vlTotal dividendo
	 * @param nuParcelas divisor
	 * @return array em double de tamanho igual a nuParcelas
	 */
	public double[] getVlParcelas(double vlTotal, int nuParcelas) {
		if (nuParcelas <= 0) {
			nuParcelas = 1;
		}
		double[] parcelas = new double[nuParcelas];
		if (nuParcelas == 1) {
			parcelas[0] = vlTotal;
		} else {
			double vlParcela = ValueUtil.round(ValueUtil.getDoubleValueTruncated(vlTotal / nuParcelas, 2));
			double vlRestante = ValueUtil.round(vlTotal - ValueUtil.round(vlParcela * nuParcelas, 2), 2);
			for (int i = 0; i < nuParcelas; i++) {
				parcelas[i] = vlParcela;
			}
			parcelas[nuParcelas - 1] = ValueUtil.round(vlParcela + vlRestante, 2);
		}
		return parcelas;
	}

	public void validaTrocaDataVencimento(Date newDtVencimento, Pedido pedido, ParcelaPedido parcelaPedido) throws SQLException {
		if (newDtVencimento.isBefore(DateUtil.getCurrentDate())) {
			throw new ValidationException(Messages.PARCELAPEDIDO_DTVENCIMENTO_ANTERIOR_PERMITIDO);
		}
		CondicaoPagamento condicaoPagamento = pedido.getCondicaoPagamento();
		int cdParcela = ValueUtil.getIntegerValue(parcelaPedido.cdParcela);
		Date afterDtVencimento = null;
		Date beforeDtVencimento = null;
		boolean firstParcela = 1 == cdParcela;
		if (firstParcela) {
			afterDtVencimento = getDtVencimento(pedido, StringUtil.getStringValue(cdParcela + 1));
		} else if (cdParcela == condicaoPagamento.nuParcelas) {
			beforeDtVencimento = getDtVencimento(pedido, StringUtil.getStringValue(cdParcela - 1));
		} else {
			afterDtVencimento = getDtVencimento(pedido, StringUtil.getStringValue(cdParcela + 1));
			beforeDtVencimento = getDtVencimento(pedido, StringUtil.getStringValue(cdParcela - 1));
		}
		if (ValueUtil.isNotEmpty(afterDtVencimento) && (newDtVencimento.equals(afterDtVencimento) || newDtVencimento.isAfter(afterDtVencimento))) {
			afterDtVencimento.advance(-1);
			throw new ValidationException(MessageUtil.getMessage(Messages.PARCELAPEDIDO_ERRO_EDITAR_PARCELA_VENCIMENTO_INTERVALO, new Object[]{new Date(), afterDtVencimento}));

		}
		if (ValueUtil.isNotEmpty(beforeDtVencimento) && (newDtVencimento.equals(beforeDtVencimento) || newDtVencimento.isBefore(beforeDtVencimento))) {
			beforeDtVencimento.advance(+1);
			if (afterDtVencimento != null) {
				afterDtVencimento.advance(-1);
			} else {
				throw new ValidationException(MessageUtil.getMessage(Messages.PARCELAPEDIDO_ERRO_EDITAR_PARCELA_VENCIMENTO_MAIORQUE, beforeDtVencimento));
			}
			throw new ValidationException(MessageUtil.getMessage(Messages.PARCELAPEDIDO_ERRO_EDITAR_PARCELA_VENCIMENTO_INTERVALO, new Object[]{beforeDtVencimento, afterDtVencimento}));

		}
		int periodoValido;
		int dias;
		if (firstParcela) {
			dias = DateUtil.getDaysBetween(newDtVencimento, new Date());
			periodoValido = condicaoPagamento.nuInterMaxEditEntrada;
		} else {
			dias = DateUtil.getDaysBetween(newDtVencimento, beforeDtVencimento);
			periodoValido = condicaoPagamento.nuInterMaxEditParcelas;
		}
		if (dias < 0) {
			dias *= -1;
		}
		if (dias > periodoValido) {
			Date ini = new Date();
			Date fim = DateUtil.getDateValue(ini);
			if (!firstParcela) {
				ini =  DateUtil.getDateValue(beforeDtVencimento);
				fim =  DateUtil.getDateValue(ini);
			}
			if (fim != null) {
				fim.advance(periodoValido);
			}
			throw new ValidationException(MessageUtil.getMessage(Messages.PARCELAPEDIDO_ERRO_EDITAR_PARCELA_VENCIMENTO_MAX, new Object[]{periodoValido, ini, fim}));
		}
	}
	
	private Date getDtVencimento(Pedido pedido, String cdParcela) {
		ParcelaPedido parcelaPedido = getParcelaPedidoInListByCdParcela(pedido, cdParcela);
		if (parcelaPedido != null) {
			return parcelaPedido.dtVencimento;
		}
		return null;
	}
}
