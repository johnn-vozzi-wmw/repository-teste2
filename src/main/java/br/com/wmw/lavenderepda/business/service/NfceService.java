package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.domain.Empresa;
import br.com.wmw.lavenderepda.business.domain.LogPda;
import br.com.wmw.lavenderepda.business.domain.Nfce;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.SerieNfe;
import br.com.wmw.lavenderepda.business.domain.TipoPedido;
import br.com.wmw.lavenderepda.business.domain.dto.SerieNfeDTO;
import br.com.wmw.lavenderepda.business.validation.NfceException;
import br.com.wmw.lavenderepda.integration.dao.pdbx.NfceDbxDao;
import br.com.wmw.lavenderepda.sync.SyncManager;
import totalcross.crypto.NoSuchAlgorithmException;
import totalcross.crypto.digest.SHA1Digest;
import totalcross.sys.Convert;
import totalcross.sys.Vm;
import totalcross.ui.image.Image;
import totalcross.util.Date;
import totalcross.util.Random;
import totalcross.util.Vector;

public class NfceService extends CrudService {

    private static NfceService instance = null;
    
    private NfceService() {
        //--
    }
    
    public static NfceService getInstance() {
        if (instance == null) {
            instance = new NfceService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return NfceDbxDao.getInstance();
    }

    public Nfce getNfce(final String cdEmpresa, final String cdRepresentante, final String nuPedido, final String flOrigemPedido) throws SQLException {
    	Nfce nfceFilter = new Nfce();
    	nfceFilter.cdEmpresa = cdEmpresa;
    	nfceFilter.cdRepresentante = cdRepresentante;
    	nfceFilter.nuPedido = nuPedido;
    	nfceFilter.flOrigemPedido = flOrigemPedido;
    	Nfce nfce = (Nfce) findByPrimaryKey(nfceFilter);
    	if (nfce != null) {
    		nfce.listItensNfce = ItemNfceService.getInstance().getItemNfceList(nfceFilter); 
    	}
//    	
		return nfce;
    }
 
    //@Override
    public void validate(BaseDomain domain) {
    }

    public Image geraQrCode(Nfce nfce, String nuCnpj) throws Exception {
    	Image imageQrCode = null;
    	if (ValueUtil.isEmpty(nfce.dsUrlQrCode)) {
    		String url = nfce.dsUrlQrCode;
    		if (url.length() > Nfce.MAX_CARACTERES_QRCODE) {
    			throw new ValidationException(Messages.IMPRESSAONFCE_MSG_ERRO_TAMANHO_QRCODE);
    		}
    		imageQrCode = SyncManager.geraQrCode(url.toString());
    		if (imageQrCode != null) {
    			updateDsUrlQrCode(nfce, url.toString());
    		}
    	} else {
    		imageQrCode = SyncManager.geraQrCode(nfce.dsUrlQrCode);
    	}
    	
    	return imageQrCode;
    }

	private void updateDsUrlQrCode(final Nfce nfce, String url) throws SQLException {
		updateColumn(nfce.rowKey, "DSURLQRCODE", url, totalcross.sql.Types.VARCHAR);
	}

	private String getDoubleValueUrl(double value) {
		String valueAsString = StringUtil.getStringValue(value);
		return valueAsString.replace(",", ".");
	}

	private String getDtHrEmissaoUrl(Nfce nfce, String cdEstado) throws SQLException {
		String dtHrEmissaoAsString = DateUtil.formatDateDb(nfce.dtEmissao) + "T" + nfce.hrEmissao + fusoHorario(cdEstado);
		String hexa = "";
		for (char ch : dtHrEmissaoAsString.toCharArray()) {
			hexa += Integer.toHexString(ch);
		}
		return hexa;
	}
	
	public String fusoHorario(String cdEstado) throws SQLException {
		String dsEstado = UFService.getInstance().getDsUf(cdEstado);
		String fusoHorario = ConfigInternoService.getInstance().getFusoHorario(DateUtil.getCurrentDate() + ";" + dsEstado);
		if (ValueUtil.isNotEmpty(fusoHorario)) return fusoHorario;
		try {
			fusoHorario = buscaFusoHorarioWeb(cdEstado, dsEstado);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		if (ValueUtil.isNotEmpty(fusoHorario)) return fusoHorario; 
		fusoHorario = fusoHorarioDiaAnterior(cdEstado);	
		if (ValueUtil.isNotEmpty(fusoHorario)) return fusoHorario; 
		throw new ValidationException (Messages.NFCE_ERRO_FUSO_HORARIO);
	}
	
	public String buscaFusoHorarioWeb(String cdEstado, String dsEstado) throws Exception {
		String fusoHorario = SyncManager.fusoHorario(cdEstado);
		if (fusoHorario.equalsIgnoreCase("INVALIDO")) fusoHorario = null; 
		if (ValueUtil.isNotEmpty(fusoHorario)) {
			ConfigInterno configInternoFilter = new ConfigInterno();
			configInternoFilter.cdConfigInterno = ConfigInterno.FUSOHORARIO;
			ConfigInternoService.getInstance().deleteAllByExample(configInternoFilter);
			ConfigInternoService.getInstance().addValue(ConfigInterno.FUSOHORARIO, DateUtil.getCurrentDate() + ";" + dsEstado, fusoHorario);
		}
		return fusoHorario;
	}
	
	public String fusoHorarioDiaAnterior(String cdEstado) throws SQLException {
		ConfigInterno configInternoFilter = new ConfigInterno();
		configInternoFilter.cdConfigInterno = ConfigInterno.FUSOHORARIO;
		Vector fusoHorarioAnteriorConfigInterno = ConfigInternoService.getInstance().findAllByExample(configInternoFilter);		
		if (fusoHorarioAnteriorConfigInterno.size() > 0) {
			ConfigInterno configInterno = (ConfigInterno) fusoHorarioAnteriorConfigInterno.items[0];
			return configInterno.vlConfigInterno;
		}
		return null;
	}
	
	public void geraNfce(Pedido pedido,  boolean isPossuiConexao) throws SQLException {
		if (!isGeraNfce(pedido)) return;
		
		Nfce nfce = null;
		try {
			nfce = getNewNfce(pedido);
			List<SerieNfeDTO> serieNfeList = new ArrayList<>();
			String flContingencia = StringUtil.getStringValue(nfce.isNfceContingencia() && LavenderePdaConfig.isNecessitaQueRepresentantePossuaSerieExclusivaDeContingencia());
			try {
				if (isPossuiConexao) {
					serieNfeList = SyncManager.getSerieAndProximoNumero(SessionLavenderePda.cdEmpresa, SessionLavenderePda.getRepresentante().cdRepresentante, flContingencia, "2");
				}
				setaValoresNfce(pedido, nfce, serieNfeList);
			} catch (Throwable e) {
				ExceptionUtil.handle(e);
				controlaDadosNfceNaExcessao(pedido, nfce, serieNfeList);
				if (ValueUtil.isEmpty(fusoHorario(pedido.getEmpresa().cdEstado))) {
					throw new ValidationException (Messages.NFCE_ERRO_FUSO_HORARIO);
				}
			} 
			ItemNfceService.getInstance().geraItensNfce(pedido, nfce);
			insert(nfce);
			SerieNfeService.getInstance().updateNuProximoNumero(nfce.cdEmpresa, nfce.cdRepresentante, Integer.valueOf(nfce.nuSerie), Integer.valueOf(nfce.nuNfce), "2");
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
			LogPdaService.getInstance().error(LogPda.LOG_CATEGORIA_ERRO_GERACAO_NFE, MessageUtil.getMessage(Messages.LOG_ERROR_GERACAO_NFE, new Object[] {pedido.nuPedido, Vm.getStackTrace(e)}));
			if (nfce != null) {
				try {
					delete(nfce);
				} catch (Throwable ex) {
					ExceptionUtil.handle(ex);
				}
				try {
					ItemNfceService.getInstance().excluirItensNfce(nfce);
				} catch (Throwable ex) {
					ExceptionUtil.handle(ex);
				}
			}
			throw new NfceException();
		}
	}
	
	private void setaValoresNfce(Pedido pedido, Nfce nfce, List<SerieNfeDTO> serieNfeList) throws SQLException, NoSuchAlgorithmException {
		setSerieENuNfce(nfce, serieNfeList);
		nfce.nuChaveAcesso = geraChaveAcessoNfce(pedido, nfce);
		nfce.dsUrlQrCode = geraUrlQrCode(nfce, pedido.getCliente().nuCnpj);
	}
	
	private void setSerieENuNfce(Nfce nfce, List<SerieNfeDTO> serieNfeList) throws SQLException {
		if (serieNfeList == null || serieNfeList.isEmpty()) {
			SerieNfe filter = new SerieNfe();
			filter.cdEmpresa = nfce.cdEmpresa;
			filter.cdRepresentante = nfce.cdRepresentante;
			if (LavenderePdaConfig.isNecessitaQueRepresentantePossuaSerieExclusivaDeContingencia()) {
				filter.flExclusivaCont = nfce.isNfceContingencia() ? ValueUtil.VALOR_SIM : ValueUtil.VALOR_NAO;
			}
			filter.sortAtributte = SerieNfe.COLUNA_NUSERIENFE;
			filter.sortAsc = ValueUtil.VALOR_SIM;
			Vector serieNfeItems = SerieNfeService.getInstance().findAllByExample(filter);
			int size = serieNfeItems.size();
			if (setMaxNuNfceBySeries(serieNfeItems, nfce)) {
				return;
			}
			for (int i = 0; i < size; i++) {
				SerieNfe serieNfe = (SerieNfe)serieNfeItems.items[i];
				if (serieNfe.nuProximoNumero <= SerieNfe.MAX_PROX_NUMERO) {
					nfce.nuNfce = String.valueOf(serieNfe.nuProximoNumero);
					nfce.nuSerie = String.valueOf(serieNfe.nuSerieNfe);
					return;
				}
			}
		} else {
			for (SerieNfeDTO serieNfe : serieNfeList) {
				nfce.nuSerie = String.valueOf(serieNfe.nuSerieNfe);
				int nuNfce = ValueUtil.getIntegerValue(maxByExample(nfce, "nuNfce"));
				if (nuNfce < serieNfe.nuProximoNumero) {
					nuNfce = serieNfe.nuProximoNumero;
				} else {
					nuNfce++;
				}
				if (nuNfce <= SerieNfe.MAX_PROX_NUMERO) {
					nfce.nuNfce = String.valueOf(nuNfce);
					nfce.nuSerie = String.valueOf(serieNfe.nuSerieNfe);
					return;
				}
			}
		}
		throw new ValidationException(Messages.NFE_ERRO_GERACAO);
	}
	
	private boolean setMaxNuNfceBySeries(Vector serieNfeList, Nfce nfce) throws SQLException {
		Nfce filter = new Nfce();
		int nuNfce = 0;
		int size = serieNfeList.size();
		for (int i = 0; i < size; i++) {
			SerieNfe serieNfe = (SerieNfe)serieNfeList.items[i];
			filter.cdEmpresa = serieNfe.cdEmpresa;
			filter.cdRepresentante = serieNfe.cdRepresentante;
			filter.nuSerie = String.valueOf(serieNfe.nuSerieNfe);
			nuNfce = ValueUtil.getIntegerValue(maxByExample(filter, "nuNfce"));
			if (nuNfce > 0 && nuNfce < SerieNfe.MAX_PROX_NUMERO) {
				nfce.nuNfce = String.valueOf(Integer.valueOf(nuNfce) + 1);
				nfce.nuSerie = String.valueOf(serieNfe.nuSerieNfe);
				return true;
			}
		}
		return false;
	}
	
	public boolean isGeraNfce(Pedido pedido) throws SQLException {
		TipoPedido tipoPedido =  pedido.getTipoPedido();
		return LavenderePdaConfig.usaGeracaoNfePedidoAposFechamento() && !LavenderePdaConfig.usaNfePorReferencia && !LavenderePdaConfig.isConfigGradeProduto() && tipoPedido != null && "S".equals(pedido.getTipoPedido().flGeraNfce);
	}

	private void controlaDadosNfceNaExcessao(Pedido pedido, Nfce nfce, List<SerieNfeDTO> serieNfeList) throws SQLException, NoSuchAlgorithmException {
		if ("0".equals(String.valueOf(nfce.nuNfce)) || "0".equals(String.valueOf(nfce.nuSerie))) {
			nfce.cdTipoEmissao = getTipoEmissaoNfeExcessao();
			setaValoresNfce(pedido, nfce, serieNfeList);
		}
	}
	
	private String getTipoEmissaoNfeExcessao() {
		return LavenderePdaConfig.isUsaGeracaoNotaNfeContingenciaPedidoSemConexao() ||  LavenderePdaConfig.isUsaSomenteGeracaoNotaNfeContingenciaPedido() ? "9" : "1";
	}
	
	private Nfce getNewNfce(Pedido pedido) throws SQLException, NoSuchAlgorithmException {
		Nfce nfce = new Nfce();
		nfce.cdEmpresa = SessionLavenderePda.cdEmpresa;
		nfce.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		nfce.nuPedido = pedido.nuPedido;
		nfce.flOrigemPedido = OrigemPedido.FLORIGEMPEDIDO_PDA;
		nfce.cdTipoEmissao = nfce.isNfceContingencia() ? "9" : "1";
		nfce.qtTotalItem = pedido.itemPedidoList.size();
		nfce.vlTotalNfce = pedido.vlTotalPedido;
		nfce.vlTotalDesconto = !pedido.isTipoPedidoBonificacao() ? pedido.getVlTotalDesconto() : 0;
		nfce.vlTotalLiquidoNfce = pedido.vlTotalPedido - pedido.getVlTotalDesconto();
		nfce.dsFormaPagamento = pedido.getTipoPagamento().dsTipoPagamento;
		nfce.vlTotalPago = pedido.vlTotalPedido;
		nfce.vlTroco = 0;
		nfce.dtEmissao = new Date();
		nfce.hrEmissao = TimeUtil.getCurrentTimeHHMMSS();
		nfce.nuVersaoQrCode = "2";
		nfce.nuAmbienteNfce = String.valueOf(LavenderePdaConfig.usaGeracaoNfePedidoAposFechamento);
		return nfce;
	}

	private String geraUrlQrCode(Nfce nfce, String nuCnpj) throws SQLException, NoSuchAlgorithmException {
		Empresa empresa = EmpresaService.getInstance().getEmpresa(SessionLavenderePda.cdEmpresa);
		StringBuilder url = new StringBuilder();
		url.append(empresa.dsUrlConsulta);
		url.append("chNFe=").append(nfce.nuChaveAcesso);
		url.append("&nVersao=").append(nfce.nuVersaoQrCode);
		url.append("&tpAmb=").append(nfce.nuAmbienteNfce);
		url.append("&cDest=").append(nuCnpj);
		url.append("&dhEmi=").append(getDtHrEmissaoUrl(nfce, empresa.cdEstado));
		url.append("&vNF=").append(getDoubleValueUrl(nfce.vlTotalLiquidoNfce));
		url.append("&vICMS=").append(getDoubleValueUrl(nfce.vlTotalTributos));
		url.append("&digVal=").append(nfce.dsDigestValueNfce);
		url.append("&cIdToken=").append(nfce.cdIdentificacaoCsc).append(empresa.nuCsc);
		SHA1Digest sha1Digest = new SHA1Digest();
		sha1Digest.update(url.toString().getBytes());
		String digest = new String(sha1Digest.getDigest());
		String hexa = "";
		for (char c : digest.toCharArray()) {
			hexa += Integer.toHexString(c);
		}
		url.append("&cHashQRCode=").append(hexa);
		return url.toString();
	}

	private String geraChaveAcessoNfce(Pedido pedido, Nfce nfce) throws SQLException {
		Empresa empresa = pedido.getEmpresa();
		int[] chave = new int[43];
		addUfIbgeChave(chave);
		addDtEmissaoChave(chave, pedido.dtEmissao);
		addCnpjChave(chave, empresa.nuCnpj);
		chave[SerieNfe.POS_TIPO_NOTA] = 6;
		chave[SerieNfe.POS_TIPO_NOTA + 1] = 5;
		addValueToChave(chave, Integer.valueOf(nfce.nuSerie), SerieNfe.SIZE_SERIENFE, 22);
		addValueToChave(chave, Integer.valueOf(nfce.nuNfce), SerieNfe.SIZE_NUNFE, 25);
		chave[SerieNfe.POS_TIPO_EMISSAO] = Integer.valueOf(nfce.cdTipoEmissao);
		addRandomNumberChave(chave);
		int dv = geraDigitoVerificador(chave);
		StringBuffer sb = new StringBuffer(44);
		for (int chaveNum : chave) {
			sb.append(chaveNum);
		}
		sb.append(dv);
		return sb.toString();
	}
	
	
	private void addUfIbgeChave(int[] chave) throws SQLException {
		int[] cdUf = UFService.getInstance().getCdUfIbge();
		if (cdUf != null && cdUf.length == 2) {
			chave[0] = cdUf[0];
			chave[1] = cdUf[1];
		} else {
			throw new ValidationException(Messages.NFE_ERRO_UFIBGE);
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
	
	public void insertOrUpdateRetornoNfce(Nfce nfce) throws SQLException {
		if (countByExample(nfce) > 0) {
			getCrudDao().update(nfce);
		} else {
			getCrudDao().insert(nfce);
		}
	}

    public boolean isExisteNfceParaPedidos() throws SQLException {
    	Nfce nfceFilter = new Nfce();
    	nfceFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	if (SessionLavenderePda.isUsuarioSupervisor()) {
    		Vector repSupList = SupervisorRepService.getInstance().getCdRepresentantesBySupervisor(nfceFilter.cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdRepresentante);
    		nfceFilter.cdRepresentanteSupList = (String[])repSupList.toObjectArray();
    	} else {
			nfceFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		}
    	return countByExample(nfceFilter) > 0;
    }

}	
