package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.sync.HttpConnection;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ConexaoPda;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ConexaoPdaPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PedidoPdbxDao;
import br.com.wmw.lavenderepda.presentation.ui.MainLavenderePda;
import br.com.wmw.lavenderepda.sync.SyncManager;
import totalcross.sys.Time;
import totalcross.util.Vector;

public class ConexaoPdaService extends CrudService {

    private static ConexaoPdaService instance;

    private ConexaoPdaService() {
        //--
    }

    public static ConexaoPdaService getInstance() {
        if (instance == null) {
            instance = new ConexaoPdaService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ConexaoPdaPdbxDao.getInstance();
    }

    //@Override
    protected void setDadosAlteracao(BaseDomain domain) {
    	//Não faz nada, quando altera a conexão no Pda
    	//ela não deve ir para o servidor, pois é temporário
    	//e a alteração que ocorre em um único Pda não de ir para todos os outros
    }

    //@Override
    public void insert(BaseDomain domain) throws SQLException {
    	ConexaoPda conexaoPda = (ConexaoPda)domain;
    	conexaoPda.cdConexao = "P" + generateIdGlobal();
    	//--
    	cleanDefaultIfNecessary(conexaoPda);
    	//--
    	super.insert(domain);
    }

    //@Override
    public void update(BaseDomain domain) throws SQLException {
    	cleanDefaultIfNecessary((ConexaoPda)domain);
    	//--
    	super.update(domain);
    }

    protected void cleanDefaultIfNecessary(ConexaoPda conexaoPda) throws SQLException {
     	if (ValueUtil.VALOR_SIM.equals(conexaoPda.flDefault)) {
    		((ConexaoPdaPdbxDao)getCrudDao()).cleanDefault();
    	}
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    	ConexaoPda conexaoPda = (ConexaoPda) domain;
   	 	//flDefault
        if (ValueUtil.VALOR_NAO.equals(conexaoPda.flDefault)) {
	        ConexaoPda conexaoFilter = new ConexaoPda();
	       	conexaoFilter.flDefault = ValueUtil.VALOR_SIM;
	       	Vector conexaoList = findAllByExample(conexaoFilter);
	       	if (conexaoList.size() == 0) {
	            throw new ValidationException(Messages.CONEXAOPDA_DEFAULT_NULL);
	       	} else {
	       		conexaoFilter = (ConexaoPda)conexaoList.items[0];
	       		if ((conexaoList.size() == 1) && conexaoFilter.equals(conexaoPda)) {
	       	        throw new ValidationException(Messages.CONEXAOPDA_DEFAULT_NULL);
	       		}
	       	}
        }

        //dsConexao
        if (ValueUtil.isEmpty(conexaoPda.dsConexao)) {
        	throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CONEXAOPDA_LABEL_DSCONEXAO);
        }

        //dsUrlConexao
        if (ValueUtil.isEmpty(conexaoPda.dsUrlWebService)) {
        	throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CONEXAOPDA_LABEL_DSURLWEBSERVICE);
        }
    }

    public int isEnvioPedidosNecessario() throws SQLException {
    	if (LavenderePdaConfig.obrigaEnvioDePedidosPorTempoDecorrido > 0 || LavenderePdaConfig.obrigaEnvioDePedidosPorQtdPedidosPendentes > 0) {
    		//--
    		if (LavenderePdaConfig.obrigaEnvioDePedidosPorQtdPedidosPendentes > 0) {
    			int pedAbertos = PedidoPdbxDao.getInstance().findCountPedidosAbertos();
    			if (pedAbertos >= LavenderePdaConfig.obrigaEnvioDePedidosPorQtdPedidosPendentes) {
        			return 1;
        		}
    		}
    		if (LavenderePdaConfig.obrigaEnvioDePedidosPorTempoDecorrido > 0) {
		    	String vlConfigInterno = ConfigInternoService.getInstance().getVlConfigInterno(ConfigInterno.tempoUtimoEnvioPedidos);
	    		int segundosNecessarioNovoEnvio = LavenderePdaConfig.obrigaEnvioDePedidosPorTempoDecorrido * 60;
	    		if (vlConfigInterno != null) {
					int nuSegundosUltimoEnvio = TimeUtil.getSecondsRealBetween(TimeUtil.getCurrentTime(), TimeUtil.getTime(vlConfigInterno));
		    		if (nuSegundosUltimoEnvio > segundosNecessarioNovoEnvio) {
		    			return 2;
		    		} else {
		    			return 0;
		    		}
		    	}
    		}
    	}
    	return 0;
	}
    
    public boolean isNecessarioSolicitarEnviosPedidos() throws SQLException {
    	if (LavenderePdaConfig.obrigaEnvioDePedidosPorTempoDecorrido > 0) {
    		String vlConfigInterno = ConfigInternoService.getInstance().getVlConfigInterno(ConfigInterno.tempoUltimaSugestaoEnvioPedidos);
    		int segundosNecessarioNovoEnvio = LavenderePdaConfig.obrigaEnvioDePedidosPorTempoDecorrido * 60;
			int nuSegundosUltimoEnvio = TimeUtil.getSecondsRealBetween(TimeUtil.getCurrentTime(), TimeUtil.getTime(vlConfigInterno));
    		if (vlConfigInterno == null || nuSegundosUltimoEnvio > segundosNecessarioNovoEnvio) {
    			return true;
    		} 
    	}
    	return false;
    }

    public int isRecebimentoDadosNecessario() throws SQLException {
    	return isRecebimentoDadosNecessario(false);
    }
	
    public int isRecebimentoDadosNecessario(boolean validaNovoPedido) throws SQLException {
    	String dataHoraUltRecebimento = ConfigInternoService.getInstance().getVlConfigInternoGeral(ConfigInterno.dataHoraUtimoRecebimentoDados);
    	if (isNecessarioReceberDadosBloqueiaUsoSistema(dataHoraUltRecebimento)) {
    		return 2;
    	}
    	if (isNecessarioReceberDadosBloqueiaNovoPedido(validaNovoPedido, dataHoraUltRecebimento)) {
    		return 1;
    	}
    	return 0;
    }

    protected boolean isNecessarioReceberDadosBloqueiaNovoPedido(boolean validaNovoPedido, String dataHoraUltRecebimento) {
		if (validaNovoPedido && (LavenderePdaConfig.obrigaReceberDadosBloqueiaNovoPedido != null) && !SessionLavenderePda.autorizadoPorSenhaNovoPedidoSemRecebimentoDados) {
			int intervalo = ValueUtil.getIntegerValue(LavenderePdaConfig.obrigaReceberDadosBloqueiaNovoPedido);
			if ((intervalo == 0) && (LavenderePdaConfig.obrigaReceberDadosBloqueiaNovoPedido.indexOf(":") != -1)) {
				String[] dataHora = StringUtil.split(LavenderePdaConfig.obrigaReceberDadosBloqueiaNovoPedido, ':');
				Time timeLimite = TimeUtil.getCurrentTime();
				timeLimite.hour = ValueUtil.getIntegerValue(dataHora[0]);
				timeLimite.minute = ValueUtil.getIntegerValue(dataHora[1]);
				if ((TimeUtil.getTimeAsLong() > timeLimite.getTimeLong()) && (TimeUtil.getSecondsRealBetween(timeLimite, TimeUtil.getTime(dataHoraUltRecebimento)) > 0)) {
					return true;
				}
			} else if (intervalo > 0) {
				return validaIntervalo(TimeUtil.getCurrentTime(), TimeUtil.getTime(dataHoraUltRecebimento), intervalo);
			}
		}
		return false;
	}

	protected boolean isNecessarioReceberDadosBloqueiaUsoSistema(String dataHoraUltRecebimento) {
		if (LavenderePdaConfig.isObrigaReceberDadosBloqueiaUsoSistema() && !SessionLavenderePda.autorizadoPorSenhaSistemaSemRecebimentoDados) {
			int intervalo = ValueUtil.getIntegerValue(LavenderePdaConfig.obrigaReceberDadosBloqueiaUsoSistema);
			if ((intervalo == 0) && (LavenderePdaConfig.obrigaReceberDadosBloqueiaUsoSistema.indexOf(":") != -1)) {
				String[] dataHora = StringUtil.split(LavenderePdaConfig.obrigaReceberDadosBloqueiaUsoSistema, ':');
				Time timeLimite = TimeUtil.getCurrentTime();
				timeLimite.hour = ValueUtil.getIntegerValue(dataHora[0]);
				timeLimite.minute = ValueUtil.getIntegerValue(dataHora[1]);
				if ((TimeUtil.getTimeAsLong() > timeLimite.getTimeLong()) && (TimeUtil.getSecondsRealBetween(timeLimite, TimeUtil.getTime(dataHoraUltRecebimento)) > 0)) {
					return true;
				}
			} else if (intervalo > 0) {
				return validaIntervalo(TimeUtil.getCurrentTime(), TimeUtil.getTime(dataHoraUltRecebimento), intervalo);
			}
		}
		return false;
	}

	protected boolean validaIntervalo(Time dtAtualAsTime, Time dataHoraUltRecebimento, int intervalo) {
		int segundosBloquearSistema = intervalo * 60;
		if (dataHoraUltRecebimento != null) {
			int nuSegundosUltimoRecebimento = TimeUtil.getSecondsRealBetween(dtAtualAsTime, dataHoraUltRecebimento);
			if (nuSegundosUltimoRecebimento >= segundosBloquearSistema) {
				return true;
			}
		}
		return false;
	}

    public boolean validateEnvioRecebimentoDadosObrigatorio() throws SQLException {
		if (!SessionLavenderePda.autorizadoPorSenhaNovoPedidoSemEnvioDados) {
			int resp = ConexaoPdaService.getInstance().isEnvioPedidosNecessario();
			if (resp != 0) {
				if (!MainLavenderePda.getInstance().showEnvioDadosObrigatorio(resp)) {
					return false;
				}
			}
		}
		//--
		int resp = ConexaoPdaService.getInstance().isRecebimentoDadosNecessario(true);
		if (resp != 0) {
			MainLavenderePda.getInstance().showReceberDadosObrigatorio(resp);
			return false;
		}
		return true;
    }

	public Vector findConexaoPdaSorted() throws SQLException {
		return ConexaoPdaPdbxDao.getInstance().findConexaoPdaSorted();
	}
	
	public List<HttpConnection> findConexaoPdaSortedAsList() throws SQLException {
		return ConexaoPdaPdbxDao.getInstance().findConexaoPdaSortedAsList();
	}
    
}
