package br.com.wmw.lavenderepda.business.domain;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.service.GrupoProduto1Service;
import totalcross.util.Date;

public class VerbaGrupoSaldo extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPVERBAGRUPOSALDO";
	
	public static final int CDVERBAGRUPOPADRAO = 0;
	public static final String NOMECOLUNADTVIGENCIAFINAL = "DTVIGENCIAFINAL";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdGrupoProduto1;
    public String flOrigemSaldo;
    public int cdVerbaGrupo;
    public double vlSaldoInicial;
    public double vlSaldo;
    public double vlPctToleranciaDesc;
    public Date dtVigenciaInicial;
    public Date dtVigenciaFinal;
    public double vlTolerancia;
    public String cdMotivoPendencia;
    public int nuOrdemLiberacao;
    
    //-- Não Persistente
    private GrupoProduto1 grupoProduto1;
	public String[] cdGrupoProduto1Array;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof VerbaGrupoSaldo) {
        	VerbaGrupoSaldo verbaGrupoSaldo = (VerbaGrupoSaldo) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, verbaGrupoSaldo.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, verbaGrupoSaldo.cdRepresentante) &&
                ValueUtil.valueEquals(cdGrupoProduto1, verbaGrupoSaldo.cdGrupoProduto1) &&
                ValueUtil.valueEquals(flOrigemSaldo, verbaGrupoSaldo.flOrigemSaldo) &&
                ValueUtil.valueEquals(cdVerbaGrupo, verbaGrupoSaldo.cdVerbaGrupo);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(cdGrupoProduto1);
        primaryKey.append(";");
        primaryKey.append(flOrigemSaldo);
        primaryKey.append(";");
        primaryKey.append(cdVerbaGrupo);
        return primaryKey.toString();
    }
    
    public boolean isVigente() {
    	return ValueUtil.isEmpty(dtVigenciaFinal) || (DateUtil.getDaysBetween(dtVigenciaFinal, DateUtil.getCurrentDate()) >= 0);
    }

	public GrupoProduto1 getGrupoProduto1() throws SQLException {
		if (grupoProduto1 == null) {
			GrupoProduto1 filter = new GrupoProduto1();
			filter.cdGrupoproduto1 = this.cdGrupoProduto1;
			grupoProduto1 = (GrupoProduto1) GrupoProduto1Service.getInstance().findByPrimaryKey(filter);
		}
		return grupoProduto1;
	}

	public void setGrupoProduto1(GrupoProduto1 grupoProduto1) {
		this.grupoProduto1 = grupoProduto1;
	}

}