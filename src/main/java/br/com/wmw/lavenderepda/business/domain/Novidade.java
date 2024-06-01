package br.com.wmw.lavenderepda.business.domain;

import java.sql.SQLException;

import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.service.AcaoNovidadeService;
import br.com.wmw.lavenderepda.integration.dao.pdbx.AcaoNovidadeDao;
import totalcross.util.Date;

public class Novidade extends LavendereBaseDomain {
	
	public static final String TABLE_NAME = "TBLVPNOVIDADE";

    public int cdSistema;
    public String cdNovidade;
    public String dsNovidade;
    public String dsUrl;
    public Date dtInicial;
    public Date dtFinal;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof Novidade) {
            Novidade novidade = (Novidade) obj;
            return
                ValueUtil.valueEquals(cdSistema, novidade.cdSistema) && 
                ValueUtil.valueEquals(cdNovidade, novidade.cdNovidade);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdSistema);
        primaryKey.append(";");
        primaryKey.append(cdNovidade);
        return primaryKey.toString();
    }

	public String getCdDomain() {
		return cdNovidade;
	}

	public String getDsDomain() {
		return dsNovidade;
	}
	
	public boolean isMostraNovidade(int cdSistemaPda, String cdNovidadePda) throws SQLException {
		AcaoNovidade acaoNovidade = new AcaoNovidade();
		acaoNovidade.cdSistema = cdSistemaPda;
		acaoNovidade.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		acaoNovidade.cdNovidade = cdNovidadePda; 
		acaoNovidade.cdAcaoNovidade = AcaoNovidadeDao.getInstance().findMaxCdAcaoNovidadeByExample(acaoNovidade);
		String tipoAcao = AcaoNovidadeService.getInstance().findColumnByRowKey(acaoNovidade.getRowKey(), "FLACAO");
		return ValueUtil.isEmpty(tipoAcao) || AcaoNovidade.NOVIDADE_IGNORADA.equals(tipoAcao);
	}
}