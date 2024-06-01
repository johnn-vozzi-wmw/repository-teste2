package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class ColetaGps extends BaseDomain {
	
	public static String TABLE_NAME = "TBLVPCOLETAGPS";
	
	public static final String NMCOLUNA_DTCOLETAGPS = "dtColetaGps";
	public static final String NMCOLUNA_HRINICIOCOLETA = "hrInicioColeta";

    public String cdEmpresa;
    public String cdRepresentante;
    public Date dtColetaGps;
    public String hrInicioColeta;
    public String hrFimColeta;
    public String cdMotivoColeta;
    public Date dtEncerramentoAuto;
    public String hrEncerramentoAuto;
    
    //Nao persistentes
    public boolean onlyColetaGpsEmAndamento;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof ColetaGps) {
            ColetaGps coletaGps = (ColetaGps) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, coletaGps.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, coletaGps.cdRepresentante) && 
                ValueUtil.valueEquals(dtColetaGps, coletaGps.dtColetaGps) && 
                ValueUtil.valueEquals(hrInicioColeta, coletaGps.hrInicioColeta);
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
        primaryKey.append(dtColetaGps);
        primaryKey.append(";");
        primaryKey.append(hrInicioColeta);
        return primaryKey.toString();
    }

}