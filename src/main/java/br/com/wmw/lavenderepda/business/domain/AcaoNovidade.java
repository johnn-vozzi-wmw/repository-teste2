package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class AcaoNovidade extends BaseDomain {
	
	public static final String TABLE_NAME = "TBLVPACAONOVIDADE";

	public static final String NOVIDADE_IGNORADA = "I";
	public static final String NOVIDADE_LIDA = "L";
	public static final String NOVIDADE_NAO_MOSTRAR_NOVAMENTE = "N";
	
	public int cdSistema;
	public String cdRepresentante;
    public String cdNovidade;
    public int cdAcaoNovidade;
    public String flAcao;
    public Date dtAcao;
    public String hrAcao;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof AcaoNovidade) {
            AcaoNovidade acaoNovidade = (AcaoNovidade) obj;
            return
                ValueUtil.valueEquals(cdSistema, acaoNovidade.cdSistema) && 
                ValueUtil.valueEquals(cdRepresentante, acaoNovidade.cdRepresentante) && 
                ValueUtil.valueEquals(cdNovidade, acaoNovidade.cdNovidade) &&
            	ValueUtil.valueEquals(cdAcaoNovidade, acaoNovidade.cdAcaoNovidade);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdSistema);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(cdNovidade);
        primaryKey.append(";");
        primaryKey.append(cdAcaoNovidade);
        return primaryKey.toString();
    }
}