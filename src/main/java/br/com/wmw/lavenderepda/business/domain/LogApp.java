package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import totalcross.util.Date;

public class LogApp extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPLOGAPP";
	
	public static final String CD_PROCESSO_EMISSAO_PEDIDO = "1";
	public static final String DS_DETALHES_EMISSAO_PEDIDO_I = "Criação de novo pedido.";
	public static final String DS_DETALHES_EMISSAO_PEDIDO_E = "Exclusão de pedido. Valor total {0}.";
	public static final String DS_DETALHES_EMISSAO_PEDIDO_A = "Pedido enviado para aprovação. Valor total {0}.";
	public static final String DS_DETALHES_EMISSAO_PEDIDO_PON = "Geração de PDF online para o pedido. Valor total {0}.";
	public static final String DS_DETALHES_EMISSAO_PEDIDO_POFF = "Geração de PDF offline para o pedido. Valor total {0}.";
	public static final String DS_DETALHES_EMISSAO_PEDIDO_SS = "Pedido enviado ao servidor. Valor total {0}.";
	public static final String DS_DETALHES_EMISSAO_PEDIDO_CANCELADO_SS = "Pedido cancelado enviado ao servidor. Valor total {0}.";
	public static final String DS_DETALHES_EMISSAO_ORCAMENTO_SS = "Orçamento enviado ao servidor. Valor total {0}.";
	public static final String DS_DETALHES_EMISSAO_PEDIDO_SA = "Status do pedido alterado. Novo status {0}. Valor total {1}.";
	public static final String DS_DETALHES_EMISSAO_PEDIDO_C = "Pedido cancelado. Valor total {0}.";
	public static final String DS_DETALHES_EMISSAO_PEDIDO_SB = "Envio de pedidos em background.";
	public static final String DS_DETALHES_EMISSAO_PEDIDO_SE = "Erro ao enviar pedido ao servidor. Valor total {0}. Erro: {1}.";
	public static final String DS_DETALHES_EMISSAO_PEDIDO_FE = "Erro ao fechar pedido. Valor total {0}. Erro: {1}.";
	public static final String DS_DETALHES_EMISSAO_PEDIDO_RP = "Pedido reaberto. Valor total {0}.";
	public static final String DS_DETALHES_EMISSAO_PEDIDO_RPE = "Erro ao reabrir pedido. Valor total {0}. Erro: {1}.";
	public static final String FL_TIPO_LOG_INFO = "I";
	public static final String FL_TIPO_LOG_ERRO = "E";
	public static final String VL_CHAVE_EMPTY = "0";

	public static final String CD_PROCESSO_ESTOQUE = "2";


    public String cdEmpresa;
    public String cdRepresentante;
    public String cdLog;
    public Date dtLog;
    public String hrLog;
    public String cdUsuarioLog;
    public String flTipoLog;
    public String vlChave;
    public String cdProcesso;
    public String cdCliente;
    public String dsDetalhes;

    
    public LogApp() {
		super();
	}

	public LogApp(String cdLog, String flTipoLog, String vlChave, String cdProcesso, String dsDetalhes, String cdCliente) {
    	cdEmpresa = SessionLavenderePda.cdEmpresa;
    	cdRepresentante = getCdRepresentante();
    	this.cdCliente = cdCliente != null ? cdCliente : (SessionLavenderePda.getCliente() != null ? SessionLavenderePda.getCliente().cdCliente : null);
    	cdUsuarioLog = Session.getCdUsuario();
    	dtLog = DateUtil.getCurrentDate();
    	hrLog = TimeUtil.getCurrentTimeHHMMSS();
    	this.cdLog = cdLog;
    	this.flTipoLog = flTipoLog;
    	this.vlChave = vlChave;
    	this.cdProcesso = cdProcesso;
    	this.dsDetalhes = dsDetalhes;
    }

	private String getCdRepresentante() {
		return SessionLavenderePda.getRepresentante() != null && SessionLavenderePda.getRepresentante().cdRepresentante != null ? SessionLavenderePda.getRepresentante().cdRepresentante : VL_CHAVE_EMPTY;
	}

	@Override
    public boolean equals(Object obj) {
        if (obj instanceof LogApp) {
            LogApp logApp = (LogApp) obj;
            return
            	ValueUtil.valueEquals(cdEmpresa, logApp.cdEmpresa) &&
            	ValueUtil.valueEquals(cdRepresentante, logApp.cdRepresentante) &&
                ValueUtil.valueEquals(cdLog, logApp.cdLog);
        }
        return false;
    }
	
    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(cdLog);
        return primaryKey.toString();
    }
    
}