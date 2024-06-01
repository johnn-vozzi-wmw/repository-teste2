package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class LogPda extends BaseDomain {

    public static String TABLE_NAME = "TBLVPLOGPDA";

	//Níveis de log
	public static final String LOG_NIVEL_OFF = "OFF";
	public static final String LOG_NIVEL_FATAL = "FATAL";
	public static final String LOG_NIVEL_ERROR = "ERROR";
	public static final String LOG_NIVEL_WARN = "WARN";
	public static final String LOG_NIVEL_INFO = "INFO";
	public static final String LOG_NIVEL_DEBUG = "DEBUG";
	public static final String LOG_NIVEL_TRACE = "TRACE";

	//Categorias de log
	public static final int LOG_CATEGORIA_SESSAO = 1;
	public static final int LOG_CATEGORIA_MEMORIA = 2;
	public static final int LOG_CATEGORIA_RECUPERAR_DADOS_REMOTO = 3;
	public static final int LOG_CATEGORIA_BACKUP = 4;
	public static final int LOG_CATEGORIA_ALTERA_PARAMETRO = 5;
	public static final int LOG_CATEGORIA_SYNC = 6;
	public static final int LOG_CATEGORIA_DATAHORA = 7;
	public static final int LOG_CATEGORIA_PONTUACAO_PEDIDO = 8;
	public static final int LOG_CATEGORIA_STATUS_GPS = 9;
	public static final int LOG_CATEGORIA_ERRO_ENVIO_BACKGROUND = 10;
	public static final int LOG_CATEGORIA_LIBERACAO_SENHA_SQL_EXECUTOR = 12;
	public static final int LOG_CATEGORIA_COMANDO_SQL_EXECUTOR = 13;
	public static final int LOG_CATEGORIA_SYNC_BACKGROUND = 14;
	public static final int LOG_CATEGORIA_ERRO_COMUNICACAO_WGPS = 15;
	public static final int LOG_CATEGORIA_FAKE_GPS = 16;
	public static final int LOG_CATEGORIA_ERRO_GERACAO_NFE = 17;
	public static final int LOG_LIBERACAO_SENHA_FECHAMENTO_DIARIO = 18;
	public static final int LOG_FECHAMENTO_EQUIPAMENTO = 19;
	public static final int LOG_CATEGORIA_DAPLAUDO = 20;
	public static final int LOG_CATEGORIA_LOGIN = 21;
	public static final int LOG_CATEGORIA_MODOFEIRA = 22;

	public static final int TAM_COLUNA_DSLOG = 200;
	
	public String cdLog;
    public String cdNivel;
    public int cdCategoria;
    public String dsLog;
    public Date dtLog;
    public String hrLog;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof LogPda) {
            LogPda logPda = (LogPda) obj;
            return
                ValueUtil.valueEquals(cdLog, logPda.cdLog);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        return  StringUtil.getStringValue(cdLog);
    }

    public int getCdNivelInt() {
    	int cdNivelInt = -1;
    	if (LOG_NIVEL_INFO.equals(cdNivel)) {
    		cdNivelInt = 3;
    	} else if (LOG_NIVEL_DEBUG.equals(cdNivel)) {
    		cdNivelInt = 2;
    	} else if (LOG_NIVEL_TRACE.equals(cdNivel)) {
    		cdNivelInt = 1;
    	} else if (LOG_NIVEL_WARN.equals(cdNivel)) {
    		cdNivelInt = 4;
    	} else if (LOG_NIVEL_ERROR.equals(cdNivel)) {
    		cdNivelInt = 5;
    	} else if (LOG_NIVEL_FATAL.equals(cdNivel)) {
    		cdNivelInt = 6;
    	}
    	return cdNivelInt;
    }

    public static int getCdNivelInt(String cdNivel) {
    	LogPda logPda = new LogPda();
    	logPda.cdNivel = cdNivel;
    	return logPda.getCdNivelInt();
    }
}