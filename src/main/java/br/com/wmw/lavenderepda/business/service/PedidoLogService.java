package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PedidoLog;
import br.com.wmw.lavenderepda.business.domain.TipoRegistro;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PedidoLogDbxDao;
import totalcross.sql.Types;
import totalcross.sys.Vm;
import totalcross.util.Vector;

public class PedidoLogService extends CrudService {

    private static PedidoLogService instance;
    
    private PedidoLog pedidoLog;
    
    private PedidoLogService() {
        //--
    }
    
    public static PedidoLogService getInstance() {
        if (instance == null) {
            instance = new PedidoLogService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return PedidoLogDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) {
    	//--
    }
    
    @Override
    public void delete(BaseDomain domain) throws SQLException {
    	PedidoLog pedidoLog = (PedidoLog) domain;
    	ItemPedidoLogService.getInstance().deleteByPedidoLog(pedidoLog);
    	super.delete(domain);
    }
    
    public void deleteAllEnviadosServidor() throws SQLException {
    	Pedido pedidoFilter = new Pedido();
    	pedidoFilter.cdStatusPedido = LavenderePdaConfig.cdStatusPedidoTransmitido;
    	Vector pedidoList = PedidoService.getInstance().findAllByExampleOnlyPda(pedidoFilter);
    	for (int i = 0; i < pedidoList.size(); i++) {
    		Pedido pedido = (Pedido) pedidoList.items[i];
    		PedidoLog pedidoLogFilter = new PedidoLog();
    		pedidoLogFilter.cdEmpresa = pedido.cdEmpresa;
    		pedidoLogFilter.cdRepresentante = pedido.cdRepresentante;
    		pedidoLogFilter.flOrigemPedido = pedido.flOrigemPedido;
    		pedidoLogFilter.nuPedido = pedido.nuPedido;
    		pedidoLogFilter.onlyEnviadosServidor = true;
    		pedidoLogFilter.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_ORIGINAL;
    		deleteAllByExample(pedidoLogFilter);
    		ItemPedidoLogService.getInstance().deleteAllEnviadosServidorByPedidoLog(pedidoLogFilter);
		}
    }
    
    public void deleteByPedido(Pedido pedido) throws SQLException {
    	if (pedido != null) {
    		PedidoLog pedidoLogFilter = new PedidoLog();
    		pedidoLogFilter.cdEmpresa = pedido.cdEmpresa;
    		pedidoLogFilter.cdRepresentante = pedido.cdRepresentante;
    		pedidoLogFilter.flOrigemPedido = pedido.flOrigemPedido;
    		pedidoLogFilter.nuPedido = pedido.nuPedido;
    		Vector pedidoLogList = findAllByExample(pedidoLogFilter);
    		if (ValueUtil.isNotEmpty(pedidoLogList)) {
    			for (int i = 0; i < pedidoLogList.size(); i++) {
    				PedidoLog pedidoLog = (PedidoLog) pedidoLogList.items[i];
    				delete(pedidoLog);
				}
    		}
    	}
    }
    
    public void savePedidoLog(String tipoRegistro, Pedido pedido) throws SQLException {
		PedidoLog newPedidoLog = new PedidoLog.Builder(pedido).completo(pedido).build();
		if (pedidoLog != null && !TipoRegistro.INCLUSAO.equals(tipoRegistro)) {
			PedidoLog pedLog = new PedidoLog.Builder(pedido).build();
			pedLog = (PedidoLog) PedidoLog.comparableChangedProperties(newPedidoLog, pedidoLog, pedLog, tipoRegistro);
			if (pedLog.isAlterado) {
				pedLog.flTipoRegistro = tipoRegistro;
				pedLog.flOrigemAcao = OrigemPedido.FLORIGEMPEDIDO_PDA;
 				insert(pedLog);
				pedidoLog = new PedidoLog.Builder(pedido).completo(pedido).build();
			}
		} else if (TipoRegistro.INCLUSAO.equals(tipoRegistro)) {
			newPedidoLog.flTipoRegistro = TipoRegistro.INCLUSAO;
			insert(newPedidoLog);
			pedidoLog = newPedidoLog;
		}
	}
    
    @Override
    public void insert(BaseDomain domain) throws SQLException {
    	try {
    		super.insert(domain);
    	} catch (ValidationException va) {
    		if (va.getMessage().equals(FrameworkMessages.MSG_VALIDACAO_CHAVE_DUPLICADA)) {
    			Vm.sleep(1000);
    			PedidoLog pedidoLog = (PedidoLog) domain;
    			pedidoLog.hrAcao = TimeUtil.getCurrentTimeHHMMSS();
    			super.insert(pedidoLog);
    		} else {
    			throw va;
    		}
    	}
    }
    
	public void loadPedidoLog(Pedido pedido) {
		if (pedidoLog == null || !pedidoLogPertencePedido(pedido)) {
			pedidoLog = new PedidoLog.Builder(pedido).completo(pedido).build();
		}
	}

	private boolean pedidoLogPertencePedido(Pedido pedido) {
		if (pedido != null && pedidoLog != null) {
			return pedido.cdEmpresa.equals(pedidoLog.cdEmpresa) && pedido.cdRepresentante.equals(pedidoLog.cdRepresentante) && pedido.flOrigemPedido.equals(pedidoLog.flOrigemPedido) && pedido.nuPedido.equals(pedidoLog.nuPedido);
		}
		return false;
	}
	
	@Override
	protected void setDadosAlteracao(BaseDomain domain) {
		PedidoLog pedidoLog = (PedidoLog) domain;
		pedidoLog.cdUsuario = Session.getCdUsuario();
		pedidoLog.cdUsuarioCriacao = pedidoLog.cdUsuario;
	}
	
	public void reabrePedidoLogParaEnvio(Pedido pedido) throws SQLException {
		if (pedido != null) {
			updateFlTipoAlteracaoParaEnvio(pedido, BaseDomain.FLTIPOALTERACAO_ORIGINAL);
			ItemPedidoLogService.getInstance().reabreItemPedidoLogParaEnvio(pedido);
		}
	}
	
	public void fechaPedidoLogParaEnvio(Pedido pedido) throws SQLException {
		if (pedido != null) {
			updateFlTipoAlteracaoParaEnvio(pedido, BaseDomain.FLTIPOALTERACAO_ALTERADO);
			ItemPedidoLogService.getInstance().fechaItemPedidoLogParaEnvio(pedido);
		}
	}
	
	protected void updateCdUsuarioCriacao(Pedido pedido) throws SQLException {
		if (pedido != null) {
			PedidoLog pedidoLogFilter = new PedidoLog();
    		pedidoLogFilter.cdEmpresa = pedido.cdEmpresa;
    		pedidoLogFilter.cdRepresentante = pedido.cdRepresentante;
    		pedidoLogFilter.flOrigemPedido = pedido.flOrigemPedido;
    		pedidoLogFilter.nuPedido = pedido.nuPedido;
    		pedidoLogFilter.cdMotivoCancelamento = pedido.cdMotivoCancelamento;
    		pedidoLogFilter.flTipoRegistro = TipoRegistro.CANCELAMENTO;
    		Vector pedidoLogList = findAllByExample(pedidoLogFilter);
    		for (int i = 0; i < pedidoLogList.size(); i++) {
    			PedidoLog pedidoLog = (PedidoLog) pedidoLogList.items[i];
    			updateColumn(pedidoLog.getRowKey(), "CDUSUARIOCRIACAO", null, Types.NULL);
			}
		}
	}
	
	private void updateFlTipoAlteracaoParaEnvio(Pedido pedido, String flTipoAlteracao) throws SQLException {
		if (pedido != null) {
			PedidoLog pedidoLogFilter = new PedidoLog();
    		pedidoLogFilter.cdEmpresa = pedido.cdEmpresa;
    		pedidoLogFilter.cdRepresentante = pedido.cdRepresentante;
    		pedidoLogFilter.flOrigemPedido = pedido.flOrigemPedido;
    		pedidoLogFilter.nuPedido = pedido.nuPedido;
    		Vector pedidoLogList = findAllByExample(pedidoLogFilter);
    		for (int i = 0; i < pedidoLogList.size(); i++) {
    			PedidoLog pedidoLog = (PedidoLog) pedidoLogList.items[i];
    			updateColumn(pedidoLog.getRowKey(), BaseDomain.NMCAMPOTIPOALTERACAO, flTipoAlteracao, Types.VARCHAR);
			}
		}
	}
    
}
