package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.presentation.ui.keyboard.BaseKeyboard;
import br.com.wmw.framework.presentation.ui.keyboard.DefaultKeyboard;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.domain.SenhaDinamica;
import br.com.wmw.lavenderepda.business.domain.SugestaoVenda;
import br.com.wmw.lavenderepda.business.domain.UsuarioPda;
import br.com.wmw.lavenderepda.business.domain.ValorParametro;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ConfigInternoPdbxDao;
import totalcross.sys.Time;
import totalcross.util.Date;
import totalcross.util.Hashtable;
import totalcross.util.Vector;

public class ConfigInternoService extends CrudService {

    private static ConfigInternoService instance;

    private ConfigInternoService() {
        //--
    }

    public static ConfigInternoService getInstance() {
        if (instance == null) {
            instance = new ConfigInternoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ConfigInternoPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

	public void insertOrUpdateConfigInterno(ConfigInterno configInterno) throws SQLException {
		if (findByRowKey(configInterno.getRowKey()) == null) {
			insert(configInterno);
		} else {
			update(configInterno);
		}
	}
	
	public void addValue(int cdConfigInterno, String vlConfigInterno) throws SQLException {
    	addValue(cdConfigInterno, ConfigInterno.VLCHAVEDEFAULT, vlConfigInterno);
    }

    public void addValue(int cdConfigInterno, String vlChave, String vlConfigInterno) throws SQLException {
    	ConfigInterno configInternoFilter = new ConfigInterno();
    	configInternoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	configInternoFilter.cdConfigInterno = cdConfigInterno;
    	configInternoFilter.vlChave = vlChave;
    	configInternoFilter.vlConfigInterno = vlConfigInterno;
    	insertOrUpdateConfigInterno(configInternoFilter);
    }

    public String getVlConfigInterno(int cdConfigInterno) throws SQLException {
    	return getVlConfigInterno(cdConfigInterno, ConfigInterno.VLCHAVEDEFAULT);
    }

    public String getVlConfigInterno(int cdConfigInterno, String vlChave) throws SQLException {
    	ConfigInterno configInternoFilter = new ConfigInterno();
    	configInternoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	configInternoFilter.cdConfigInterno = cdConfigInterno;
    	configInternoFilter.vlChave = vlChave;
    	return findColumnByRowKey(configInternoFilter.getRowKey(), "VLCONFIGINTERNO");
    }

    public void addValueGeral(int cdConfigInterno, String vlConfigInterno) throws SQLException {
    	addValueGeral(cdConfigInterno, vlConfigInterno, ConfigInterno.VLCHAVEDEFAULT);
    }
    
    public void addValueGeral(int cdConfigInterno, String vlConfigInterno, String vlChave) throws SQLException {
    	ConfigInterno configInternoFilter = new ConfigInterno();
    	configInternoFilter.cdEmpresa = ConfigInterno.CDEMPRESADEFAULT;
    	configInternoFilter.cdConfigInterno = cdConfigInterno;
    	configInternoFilter.vlChave = vlChave;
    	configInternoFilter.vlConfigInterno = vlConfigInterno;
    	insertOrUpdateConfigInterno(configInternoFilter);
    }

    public void addValueGeralIgnoreError(int cdConfigInterno, String vlConfigInterno) {
    	try {
    		ConfigInterno configInternoFilter = new ConfigInterno();
    		configInternoFilter.cdEmpresa = ConfigInterno.CDEMPRESADEFAULT;
    		configInternoFilter.cdConfigInterno = cdConfigInterno;
        	configInternoFilter.vlChave = ConfigInterno.VLCHAVEDEFAULT;
    		configInternoFilter.vlConfigInterno = vlConfigInterno;
    		insertOrUpdateConfigInterno(configInternoFilter);
    	} catch (Throwable ex) {
    		//Apenas para não disparar erro ao usuário
    	}
    }

    public String getVlConfigInternoGeral(int cdConfigInterno) throws SQLException {
    	return getVlConfigInternoGeral(cdConfigInterno, ConfigInterno.VLCHAVEDEFAULT);
    }
    
    public String getVlConfigInternoGeral(int cdConfigInterno, String vlChave) throws SQLException {
    	ConfigInterno configInternoFilter = new ConfigInterno();
    	configInternoFilter.cdEmpresa = ConfigInterno.CDEMPRESADEFAULT;
    	configInternoFilter.cdConfigInterno = cdConfigInterno;
    	configInternoFilter.vlChave = vlChave;
    	return findColumnByRowKey(configInternoFilter.getRowKey(), "VLCONFIGINTERNO");
    }

    public String getVlConfigInternoGeralIgnoreError(int cdConfigInterno) {
    	return getVlConfigInternoGeralIgnoreError(cdConfigInterno, ConfigInterno.VLCHAVEDEFAULT);
    }
    
    public String getVlConfigInternoGeralIgnoreError(int cdConfigInterno, String vlChave) {
    	try {
    		ConfigInterno configInternoFilter = new ConfigInterno();
    		configInternoFilter.cdEmpresa = ConfigInterno.CDEMPRESADEFAULT;
    		configInternoFilter.cdConfigInterno = cdConfigInterno;
    		configInternoFilter.vlChave = vlChave;
    		return findColumnByRowKey(configInternoFilter.getRowKey(), "VLCONFIGINTERNO");
    	} catch (Throwable ex) {
    		return null;
    	}
    }

    public Date getDtServidor() {
    	try {
			String dataServidor = ConfigInternoService.getInstance().getVlConfigInternoGeral(ConfigInterno.dataHoraServidor);
			if (dataServidor != null) {
				return new Date(new Time(ValueUtil.getLongValue(dataServidor)));
			}
    	} catch (Throwable e) {
    		//Apenas para não disparar erro ao usuário
    	}
    	return null;
    }
    
	public Time getTimeServidor(boolean dayLight) throws SQLException {
		String dataHoraServLong = "";
		dataHoraServLong = ConfigInternoService.getInstance().getVlConfigInternoGeral(ConfigInterno.dataHoraServidor);
		if (ValueUtil.isEmpty(dataHoraServLong)) {
			return null;
		} else {
			Time timeServidor;
			try {
				timeServidor = new Time(ValueUtil.getLongValue(dataHoraServLong));
				if (isUsaControleTimeZone(LavenderePdaConfig.getValorParametroPorEmpresa(ValorParametro.USACONTROLETIMEZONE))) {
					Vector usuarioPdaList = UsuarioPdaService.getInstance().findAll();
					if (ValueUtil.isNotEmpty(usuarioPdaList)) {
						UsuarioPda usuariopda = (UsuarioPda) usuarioPdaList.items[0];
						timeServidor.hour += usuariopda.nuHoraDiferencaServidor;
						if (dayLight) {
							timeServidor.hour += usuariopda.nuHoraDiferencaHrVerao;
						}
					}
				}
			} catch (Throwable e) {
				timeServidor = new Time();
			}
			return timeServidor;
		}
	}
	
	private boolean isUsaControleTimeZone(String usaControleTimeZone) {
		return ValueUtil.VALOR_SIM.equals(usaControleTimeZone) || "1".equals(usaControleTimeZone) || "2".equals(usaControleTimeZone);
	}
	
	public int getHorasFusoEHorarioDeVerao(String usaControleTimeZone) throws SQLException {
		int horasFusoEHorarioDeVerao = 0;
		if ("2".equals(usaControleTimeZone)) {
			Vector usuarioPdaList = UsuarioPdaService.getInstance().findAll();
			if (ValueUtil.isNotEmpty(usuarioPdaList)) {
				UsuarioPda usuariopda = (UsuarioPda) usuarioPdaList.items[0];
				horasFusoEHorarioDeVerao += usuariopda.nuHoraDiferencaServidor;
				horasFusoEHorarioDeVerao += DateUtil.isDayLight(DateUtil.getCurrentDate()) ? usuariopda.nuHoraDiferencaHrVerao : 0;
			}
		}
		return horasFusoEHorarioDeVerao;
	}
			
	private String getUltimoCdEmpresaCdRepLogado(String value) throws SQLException {
		String ultimoCdEmpresaCdRepLogado = ConfigInterno.CDEMPRESADEFAULT;
		String configInterno = getVlConfigInternoGeral(ConfigInterno.ultimoRepEmpSelecionado);
		if (ValueUtil.isNotEmpty(value)) {
			Hashtable hash = StringUtil.splitPairValue(configInterno);
			if (hash != null) {
				ultimoCdEmpresaCdRepLogado = (String) hash.get(value);
			} 
		}
		return ultimoCdEmpresaCdRepLogado;
	}

	public String getUltimoCdRepLogado() throws SQLException {
		return getUltimoCdEmpresaCdRepLogado("cdRepresentante");
	}

	public String getUltimoCdEmpresaLogado() throws SQLException {
		return getUltimoCdEmpresaCdRepLogado("cdEmpresa");
	}

	public void saveInfosTeclado() throws SQLException {
		Vector configsDefaulTeclado = BaseKeyboard.hashConfigsDefault.getKeys();
		String key;
		for (int i = 0; i < configsDefaulTeclado.size(); i++) {
			key = (String)configsDefaulTeclado.items[i];
			String[] value = (String[])DefaultKeyboard.hashConfigsDefault.get(key);
			ConfigInternoService.getInstance().addValue(ConfigInterno.tecladoConfiguracoes, key, value[0] + ConfigInterno.defaultSeparatorInfoValue + value[1] + ConfigInterno.defaultSeparatorInfoValue);
		}
	}

	public void salvaModoFeiraSelecionado(String cdEmpresa, String cdRepresentante, boolean modoFeira) throws SQLException {
		ConfigInterno configInternoFilter = new ConfigInterno();
		configInternoFilter.cdEmpresa = cdEmpresa;
		configInternoFilter.cdConfigInterno = ConfigInterno.ULTIMACONFIGMODOFEIRA;
		configInternoFilter.vlChave = cdRepresentante;
		configInternoFilter.vlConfigInterno = StringUtil.getStringValue(modoFeira);
		
		insertOrUpdateConfigInterno(configInternoFilter);
	}
	
	public boolean isModoFeiraUltimoSelecionado(String cdEmpresa, String cdRepresentante) throws SQLException {
		ConfigInterno configInternoFilter = new ConfigInterno();
		configInternoFilter.cdEmpresa = cdEmpresa;
		configInternoFilter.cdConfigInterno = ConfigInterno.ULTIMACONFIGMODOFEIRA;
		configInternoFilter.vlChave = cdRepresentante;
		String flModoFeira = findColumnByRowKey(configInternoFilter.getRowKey(), "VLCONFIGINTERNO");
		return ValueUtil.getBooleanValue(flModoFeira);
	}
	
	public void salvaEmpRepSelecionado(String cdEmpresa, String cdRepresentante) throws SQLException {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("cdEmpresa").append(UiUtil.defaultSeparatorPairValue).append(cdEmpresa).append(UiUtil.defaultSeparatorValue);
		stringBuffer.append("cdRepresentante").append(UiUtil.defaultSeparatorPairValue).append(cdRepresentante);
		//--
    	ConfigInterno configInternoFilter = new ConfigInterno();
    	configInternoFilter.cdEmpresa = ConfigInterno.CDEMPRESADEFAULT;
    	configInternoFilter.cdConfigInterno = ConfigInterno.ultimoRepEmpSelecionado;
    	configInternoFilter.vlChave = ConfigInterno.VLCHAVEDEFAULT;
    	configInternoFilter.vlConfigInterno = stringBuffer.toString();
    	//--
    	insertOrUpdateConfigInterno(configInternoFilter);
	}
	
	//-- Padrão meta venda

	public void salvaInfosPadraoMetaVenda(String cdMetaVenda, String cdFamilia, String cdRepresentante, int activeTab) throws SQLException {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(cdMetaVenda).append(UiUtil.defaultSeparatorValue);
		stringBuffer.append(cdFamilia).append(UiUtil.defaultSeparatorValue);
		stringBuffer.append(cdRepresentante).append(UiUtil.defaultSeparatorValue);
		stringBuffer.append(activeTab);
		//--
    	ConfigInterno configInternoFilter = new ConfigInterno();
    	configInternoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	configInternoFilter.cdConfigInterno = ConfigInterno.configsPadraoMetaVenda;
    	configInternoFilter.vlChave = ConfigInterno.VLCHAVEDEFAULT;
    	configInternoFilter.vlConfigInterno = stringBuffer.toString();
    	//--
    	insertOrUpdateConfigInterno(configInternoFilter);
	}
	
	//-- Padrão meta venda cliente

	public void salvaInfosPadraoMetaVendaCli(String cdRepresentante, int activeTab, String sortAsc, String sortAtributte) throws SQLException {
    	insertOrUpdateConfigInterno(createConfigInterno(setVlConfigInterno(cdRepresentante, activeTab, sortAsc, sortAtributte), ConfigInterno.configsPadraoMetaVendaCli));
	}
	
	//-- Padrão meta venda cliente todos
	
	public void salvaInfosPadraoMetaVendaCliTodos(String cdRepresentante, int activeTab, String sortAsc, String sortAtributte) throws SQLException {
		insertOrUpdateConfigInterno(createConfigInterno(setVlConfigInterno(cdRepresentante, activeTab, sortAsc, sortAtributte), ConfigInterno.configsPadraoMetaVendaCliTodos));
	}

	private StringBuffer setVlConfigInterno(String cdRepresentante, int activeTab, String sortAsc, String sortAtributte) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(cdRepresentante).append(UiUtil.defaultSeparatorValue);
		stringBuffer.append(activeTab).append(UiUtil.defaultSeparatorValue);
		stringBuffer.append(sortAsc).append(UiUtil.defaultSeparatorValue);
		stringBuffer.append(sortAtributte);
		return stringBuffer;
	}
	

	private ConfigInterno createConfigInterno(StringBuffer stringBufferVlConfig, int cdConfigInterno) {
		ConfigInterno configInternoFilter = new ConfigInterno();
    	configInternoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	configInternoFilter.cdConfigInterno = cdConfigInterno;
    	configInternoFilter.vlChave = ConfigInterno.VLCHAVEDEFAULT;
    	configInternoFilter.vlConfigInterno = stringBufferVlConfig.toString();
		return configInternoFilter;
	}

	//-- ConfigInterno de Sugestao Venda
	
	public void saveConfigInternoSugestaoVendaObrigatoria(Vector sugestaoVendaList, Cliente cliente, String flTipoSugestaoVenda) throws SQLException {
		if (ValueUtil.isNotEmpty(sugestaoVendaList)) {
			ConfigInterno configInterno = new ConfigInterno();
			if (isHasSugestoesByFrequencia(sugestaoVendaList, SugestaoVenda.FLTIPOFREQUENCIA_DIARIO)) {
				configInterno = getNovoConfigInternoBySugestaoVenda(cliente, flTipoSugestaoVenda, SugestaoVenda.FLTIPOFREQUENCIA_DIARIO);
				ConfigInternoService.getInstance().insertOrUpdateConfigInterno(configInterno);
			}
			if (isHasSugestoesByFrequencia(sugestaoVendaList, SugestaoVenda.FLTIPOFREQUENCIA_SEMANAL)) {
				configInterno = getNovoConfigInternoBySugestaoVenda(cliente, flTipoSugestaoVenda, SugestaoVenda.FLTIPOFREQUENCIA_SEMANAL);
				ConfigInternoService.getInstance().insertOrUpdateConfigInterno(configInterno);
			}
			if (isHasSugestoesByFrequencia(sugestaoVendaList, SugestaoVenda.FLTIPOFREQUENCIA_MENSAL)) {
				configInterno = getNovoConfigInternoBySugestaoVenda(cliente, flTipoSugestaoVenda, SugestaoVenda.FLTIPOFREQUENCIA_MENSAL);
				ConfigInternoService.getInstance().insertOrUpdateConfigInterno(configInterno);
			}
		}
	}

	private boolean isHasSugestoesByFrequencia(Vector sugestaoVendaList, String flTipoFrequencia) {
		if (ValueUtil.isNotEmpty(sugestaoVendaList)) {
			for (int i = 0; i < sugestaoVendaList.size(); i++) {
				if (flTipoFrequencia.equals(((SugestaoVenda) sugestaoVendaList.items[i]).flTipoFrequencia)) {
					return true;
				}
			}
		}
		return false;
	}

	protected ConfigInterno getNovoConfigInternoBySugestaoVenda(Cliente cliente, String flTipoSugestaoVenda, String flTipoFrequencia) {
		ConfigInterno configInterno = new ConfigInterno();
		if (!(LavenderePdaConfig.validaSugestaoVendaMultiplasEmpresas > 0)) {
			configInterno.cdEmpresa = cliente.cdEmpresa;
		}
		configInterno.cdConfigInterno = SugestaoVenda.FLTIPOSUGESTAOVENDA_SEMQUANTIDADE.equals(flTipoSugestaoVenda) ? ConfigInterno.configSugestaoVendaSemQtde : ConfigInterno.configSugestaoVendaComQtde;
		configInterno.vlChave = cliente.cdCliente + ":" + flTipoFrequencia;
		configInterno.vlConfigInterno = DateUtil.formatDateYYYYMMDD(DateUtil.getCurrentDate());
		if (SugestaoVenda.FLTIPOFREQUENCIA_SEMANAL.equals(flTipoFrequencia)) {
			configInterno.vlConfigInterno = DateUtil.formatDateYYYYMMDD(DateUtil.getFirstDayOfWeek(DateUtil.getCurrentDate()));
		} else if (SugestaoVenda.FLTIPOFREQUENCIA_MENSAL.equals(flTipoFrequencia)) {
			configInterno.vlConfigInterno = DateUtil.formatDateYYYYMMDD(DateUtil.getFirstUtilDayOfMonth(DateUtil.getCurrentDate()));
		}
		return configInterno;
	}
	
	public boolean isPrimeiroAcessoAoSistema() throws SQLException {
		String vlConfigInterno = getVlConfigInternoGeral(ConfigInterno.dataPrimeiroAcessoAoSistema);
		return ValueUtil.isEmpty(vlConfigInterno) || vlConfigInterno.equals(DateUtil.getCurrentDateYYYYMMDD());
	}
	
	public String getVlChaveLiberacaoTipoPedido(String cdCliente) {
		return cdCliente + "," + SenhaDinamica.LIBERACAO_UNICA_TIPO_PEDIDO;
	}
	
	public String getVlChaveLiberacaoClienteAtrasado(String cdCliente) {
		return cdCliente + "," + SenhaDinamica.LIBERACAO_UNICA_CLIENTE_ATRASADO;
	}
	
	public String getVlChaveLiberacaoLimiteCredito(String cdCliente) {
		return cdCliente + "," + SenhaDinamica.LIBERACAO_UNICA_LIMITE_CREDITO;
	}
	
	public String getVlChaveLiberacaoVisitaForaAgenda(String cdCliente) {
		return cdCliente + "," + SenhaDinamica.LIBERACAO_UNICA_VISITA_FORA_AGENDA;
	}

	public String getVlChaveLiberacaoPedidoBloqueadoSaldoBonificacao(String cdCliente) {
		return cdCliente + "," + SenhaDinamica.LIBERACAO_UNICA_PEDIDO_BLOQUEADO_SALDO_BONIFICACAO;
	}
	
	public String getVlChaveLiberacaoPedidoBonificacaoBloqueadoExtrapolado(String cdCliente) {
		return cdCliente + "," + SenhaDinamica.SENHA_LIBERACAO_SALDO_VERBA_PEDIDO_BONIFICACAO_EXTRAPOLADO;
	}
	
	public boolean isTipoPedidoLiberadoSenha(String cdCliente) throws SQLException {
		if (ValueUtil.isNotEmpty(cdCliente)) {
			String vlConfigInterno = getVlConfigInternoGeral(ConfigInterno.FUNCIONALIDADELIBERADASENHA, getVlChaveLiberacaoTipoPedido(cdCliente));
			return ValueUtil.isNotEmpty(vlConfigInterno) && ValueUtil.valueEquals(DateUtil.getDateValue(vlConfigInterno.substring(0, 10)), DateUtil.getCurrentDate());
		}
		return false;
	}
	
	public boolean isClienteAtrasadoLiberadoSenha(String cdCliente) throws SQLException {
		if (ValueUtil.isNotEmpty(cdCliente)) {
			String vlConfigInterno = getVlConfigInternoGeral(ConfigInterno.FUNCIONALIDADELIBERADASENHA, getVlChaveLiberacaoClienteAtrasado(cdCliente));
			return ValueUtil.isNotEmpty(vlConfigInterno) && ValueUtil.valueEquals(DateUtil.getDateValue(vlConfigInterno.substring(0, 10)), DateUtil.getCurrentDate());
		}
		return false;
	}
	
	public boolean isLimiteCreditoLiberadoSenha(String cdCliente) throws SQLException {
		if (ValueUtil.isNotEmpty(cdCliente)) {
			String vlConfigInterno = getVlConfigInternoGeral(ConfigInterno.FUNCIONALIDADELIBERADASENHA, getVlChaveLiberacaoLimiteCredito(cdCliente));
			return ValueUtil.isNotEmpty(vlConfigInterno) && ValueUtil.valueEquals(DateUtil.getDateValue(vlConfigInterno.substring(0, 10)), DateUtil.getCurrentDate());
		}
		return false;
	}
	
	public boolean isVisitaForaAgendaLiberadoSenha(String cdCliente) throws SQLException {
		if (ValueUtil.isNotEmpty(cdCliente)) {
			String vlConfigInterno = getVlConfigInternoGeral(ConfigInterno.FUNCIONALIDADELIBERADASENHA, getVlChaveLiberacaoVisitaForaAgenda(cdCliente));
			return ValueUtil.isNotEmpty(vlConfigInterno) && ValueUtil.valueEquals(DateUtil.getDateValue(vlConfigInterno.substring(0, 10)), DateUtil.getCurrentDate());
		}
		return false;
	}

	public boolean isPedidoBloqueadoSaldoBonificacaoLiberadoSenha(String cdCliente) throws SQLException {
		if (ValueUtil.isNotEmpty(cdCliente)) {
			String vlConfigInterno = getVlConfigInternoGeral(ConfigInterno.FUNCIONALIDADELIBERADASENHA, getVlChaveLiberacaoPedidoBloqueadoSaldoBonificacao(cdCliente));
			return ValueUtil.isNotEmpty(vlConfigInterno) && ValueUtil.valueEquals(DateUtil.getDateValue(vlConfigInterno.substring(0, 10)), DateUtil.getCurrentDate());
		}
		return false;
	}
	
	public boolean isPedidoBonificacaoBloqueadoExtrapoladoSaldoLiberadoSenha(String cdCliente) throws SQLException {
		if (ValueUtil.isNotEmpty(cdCliente)) {
			String vlConfigInterno = getVlConfigInternoGeral(ConfigInterno.FUNCIONALIDADELIBERADASENHA, getVlChaveLiberacaoPedidoBonificacaoBloqueadoExtrapolado(cdCliente));
			return ValueUtil.isNotEmpty(vlConfigInterno) && ValueUtil.valueEquals(DateUtil.getDateValue(vlConfigInterno.substring(0, 10)), DateUtil.getCurrentDate());
		}
		return false;
	}
	
	public void deleteLiberacoesSenhaAntigas() throws SQLException {
		ConfigInterno configInternoFilter = new ConfigInterno();
		configInternoFilter.cdEmpresa = ConfigInterno.CDEMPRESADEFAULT;
		configInternoFilter.cdConfigInterno = ConfigInterno.FUNCIONALIDADELIBERADASENHA;
		deleteAllByExample(configInternoFilter);
	}
	
	public Vector findAllLiberacoesSenha() throws SQLException {
		ConfigInterno configInternoFilter = new ConfigInterno();
		configInternoFilter.cdEmpresa = ConfigInterno.CDEMPRESADEFAULT;
		configInternoFilter.cdConfigInterno = ConfigInterno.FUNCIONALIDADELIBERADASENHA;
		return findAllByExample(configInternoFilter);
	}
	
	public Vector getLiberacoesByCliente(String cdCliente, Vector liberacoesSenhaList) {
		Vector liberacoesSenhaFinalList = new Vector();
		if (ValueUtil.isNotEmpty(cdCliente) && ValueUtil.isNotEmpty(liberacoesSenhaList)) {
			for (int i = 0; i < liberacoesSenhaList.size(); i++) {
				ConfigInterno configInterno = (ConfigInterno) liberacoesSenhaList.items[i];
				String[] vlChave = configInterno.vlChave.split(",");
				if (cdCliente.equals(vlChave[0])) {
					liberacoesSenhaFinalList.addElement(configInterno);
				}
			}
		}
		return liberacoesSenhaFinalList;
	}
	
	public void removeConfigInterno(int cdConfigInterno, String vlChave) {
		removeConfigInterno(SessionLavenderePda.cdEmpresa, cdConfigInterno, vlChave);
	}
	
	public void removeConfigInternoGeral(int cdConfigInterno, String vlChave) {
		removeConfigInterno(ConfigInterno.CDEMPRESADEFAULT, cdConfigInterno, vlChave);
	}
	
	private void removeConfigInterno(String cdEmpresa, int cdConfigInterno, String vlChave) {
		ConfigInterno configInternoFilter = new ConfigInterno();
    	configInternoFilter.cdEmpresa = cdEmpresa;
    	configInternoFilter.cdConfigInterno = cdConfigInterno;
    	configInternoFilter.vlChave = vlChave;
    	try {
    		delete(configInternoFilter);
		} catch (Throwable e) {
			// nao deletou nada
		}
	}

	public boolean isNaoMostraMensagemTecladoNativo() throws SQLException {
		String config = ConfigInternoService.getInstance().getVlConfigInternoGeral(ConfigInterno.mensagemNovidadeTecladoNativo, "tecladoMensagem");
		if (ValueUtil.isEmpty(config)) {
			return false;
		}
		return Boolean.valueOf(config);
	}

	public boolean usaTecladoWmw() throws SQLException {
		String config = ConfigInternoService.getInstance().getVlConfigInternoGeral(ConfigInterno.configTecladoSelecionado, "teclado");
		if (ValueUtil.isEmpty(config)) {
			return false;
		}
		return Boolean.valueOf(config);
	}
	public String getFusoHorario(String vlChave) throws SQLException {
		return ConfigInternoService.getInstance().getVlConfigInterno(ConfigInterno.FUSOHORARIO, vlChave);
	}
	
	public boolean isListaModoAgrupadorGrade() throws SQLException {
		return ValueUtil.getBooleanValue(getVlConfigInterno(ConfigInterno.ULTIMACONFIGURACAOBOTAOAGRUPADORGRADE));
	}
	
}